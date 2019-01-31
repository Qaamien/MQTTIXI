import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.iota.ict.ixi.Ixi;
import org.iota.ict.ixi.IxiModule;
import org.iota.ict.model.TransactionBuilder;
import org.iota.ict.network.event.GossipEvent;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipListener;


public class MQTTIXI extends IxiModule {


    //           This is example IXI module which receives MQTT messages from subscribed topics.
    //  You can add specific topic and if it receives specific message then it sends that same message to
    //                         ICT network in to address which you have picked.
    // Module also listen that same address from ICT network and if it receives transaction to that address
    //            it will send that transactions message to MQTT topic which you have chosen.

    //                     You can use this as template to make your own IXI modules.


    public MQTTIXI(Ixi ixi) {
        super(ixi);

    }



    private final static Logger LOGGER = LogManager.getLogger(MQTTIXI.class);

    String broker 	    = "tcp://IP:PORT" ;   // write LAN IP and port of your local MQTT broker

    String pubTopic 	= "Pub/Example";      // Topic where you publish data, add more to use multiple topics
    String subTopic	    = "Sub/Example";      // Topic where you subscribe to receive data, add more to use multiple topics

    String clientID     = "ICT NODE ICT-0";

    // Password and username of your broker, Change your password same way like example below and write your username
    char[] password = new char[]{'y','o','u','r','p','a','s','s','w','o','r','d'};
    String userName = "yourBrokerUsername";

    MqttClient client;
    MqttConnectOptions connOpt;



    @Override
    public void run() {

        LOGGER.info("MQTT IXI module started!");

        try {

            client = new MqttClient(broker, clientID, new MemoryPersistence());

            connOpt = new MqttConnectOptions();

            connOpt.setCleanSession(true);       // Starts clean session
            connOpt.setKeepAliveInterval(30);    // Sends "im alive" message to broker to keep connection alive
            connOpt.setUserName(userName);
            connOpt.setPassword(password);

            client.connect(connOpt);             // Connect with ConnectOptions

            // Topics which you want this IXI module to subscribe:

            client.subscribe(subTopic);              // Topic in String which was declared before
            client.subscribe("jorma");     // You can also add topics this way, listen topic: jorma


        } catch (MqttException e) {
            e.printStackTrace();
        }

        // Set callback to broker and start listen topics and receiving messages which will be send to those topics

        client.setCallback(new MqttCallback() {


            public void messageArrived(String topic, MqttMessage message) throws Exception {

                String ReceivedMSG = new String(message.getPayload());

                // Shows topic and message

                LOGGER.info("\nMQTT IXI MODULE RECEIVED A MESSAGE:" +

                        "\nTOPIC:   " + topic +
                        "\nMESSAGE: " + ReceivedMSG);


                // Add here what you wanna do if your chosen topic receive specific message
                // In this example if message is "Microhash" it send same message to ICT network address: MQTTIXI9999...

                if (topic.equals(subTopic) && ReceivedMSG.equals("Microhash")) {

                    final TransactionBuilder t = new TransactionBuilder();
                    t.address = "MQTTIXI99999999999999999999999999999999999999999999999999999999999999999999999999";
                    t.asciiMessage(ReceivedMSG);
                    ixi.submit(t.buildWhileUpdatingTimestamp());

                    LOGGER.info("MESSAGE SENDED to ICT NETWORK");

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

            public void connectionLost(Throwable cause) {

                LOGGER.info("MQTT IXI LOST CONNECTION TO MQTT BROKER!" + cause.getMessage());
            }

        });


        // Makes this IXI MODULE to listen gossip ICT network

        this.ixi.addGossipListener(new GossipListener() {

            private final GossipFilter filter = new GossipFilter();

            @Override
            public void onGossipEvent(GossipEvent gossipEvent) {

                // Filter, which make program to only listen ict network address MQTTIXI999999999..
                filter.watchAddress("MQTTIXI99999999999999999999999999999999999999999999999999999999999999999999999999");

                // Ignores other address transactions, only let watched address transactions pass
                if (!filter.passes(gossipEvent.getTransaction())) return;

                else {
                try{

                    MqttMessage message = new MqttMessage();
                    message.setPayload(gossipEvent.getTransaction().decodedSignatureFragments.getBytes());

                    client.publish(pubTopic, message); // Publish Message from ICT network to topic

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }}
        });

    }
}
