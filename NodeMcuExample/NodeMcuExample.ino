
/*-------------------------------------------------------------------------

       Example code how make NodeMcu interact with MQTT broker and
                 through that with IOTA's ICT network.
                 
-------------------------------------------------------------------------*/

// If you run MQTT IXI module as it is, this will send "Microhash" message to MQTTIXI999999.. address on ICT network

#include <PubSubClient.h>
#include <ESP8266WiFi.h>

//MQTT & WiFi
// Change your username and password to match your MQTT broker ones
// Define your topics

#define MQTT_USERNAME ""
#define MQTT_PASSWORD ""
#define MQTT_PUBLISH "Pub/Example"
#define MQTT_SUBSCRIBE "Sub/Example"

// Define your brokers IP and set your WiFi router SSID and Password 

const char* MQTT_BROKER_IP = "";
const char* ssid = "";
const char* password = "";

WiFiClient wifiClient;
PubSubClient client(wifiClient);


/*--------------------------------------------------------------------*/


void setup_wifi(){

  Serial.println( ssid );
// Connect your WiFi router

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid,password);
  Serial.print("Connecting");


  while( WiFi.status() != WL_CONNECTED ){
  delay(500);
  Serial.print(".");        

  }

  Serial.println();
  Serial.println("Connected!");
  Serial.print("Your NodeMcu IP Address is: ");
  Serial.println(WiFi.localIP() );
  WiFi.hostname("NodeMcu");

}

/*-------------------------------------------------------------------------------*/

void reconnect() {

  
    while (!client.connected()) {
    Serial.print("Connectiong to MQTT broker...");


// Create a random client ID

    String clientId = "NodeMcu";
       clientId += String(random(0xffff), HEX);
    
// Connect to MQTT

    if (client.connect(clientId.c_str(),MQTT_USERNAME,MQTT_PASSWORD)) {
    Serial.println("connected");
    }
    
// If failed

    else {
    Serial.print("failed, rc=");
    Serial.print(client.state());
    Serial.println(" trying again");
    delay(200);

   }
  }
 }
  
/*-------------------------------------------------------------------------------*/

void setup() {

// Starting..
  
  Serial.begin(115200);
  setup_wifi ();
  delay(500);
  
//MQTT IP:PORT (Change if your brokers port s not 1883)

  client.setServer(MQTT_BROKER_IP, 1883);
  reconnect();

// If you run MQTT IXI module as it is this will send once "Microhash" message to MQTTIXI999999.. address on ICT network

String message = "Microhash";
message += String("");
client.publish(MQTT_SUBSCRIBE,message.c_str());
}

/*-------------------------------------------------------------------------------*/

void loop (){



// Code your project with anything you want!
// and make it communicate through MQTT to ICT network.



    
}


