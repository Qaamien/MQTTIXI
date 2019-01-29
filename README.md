# MQTT IXI module


## Ixi module example for IOTA ICT network.

To make IXI modules based on this example you will need to set up:

IOTA ICT node and install Mosquitto MQTT broker on your Raspberry Pi.


https://github.com/iotaledger/ict


https://www.mosquitto.org   

-Install instruction on terminal in MosquittoBroker dir. in this repo-

# Uses:

PAHO MQTT LIBRARY: https://github.com/eclipse/paho.mqtt.java


## Introduction


Module subscribe to MQTT topic which you decide, and you can set specific message which will trigger
module to send that message to ICT network in address which you have chosen.

Module also listen that same address from ICT network and when that address receives transaction
it will send that transactions message to MQTT topic which you have chosen.

# DEVICE -> MQTT -> IXI MODULE -> ICT NETWOTK -> IXIMODULE -> MQTT -> DEVICE 

So you can send message and listen address to make sure it has received your message.


I will add some examples what you can do soon..


MQTT makes possible to send data to ICT network from mcus and programs 

With MQTT you can send sensor data from NodeMcu, Arduino etc to IXI module which will
then send that data to ICT network and anyone who listens that address can read that data
from network.

# Make your own based on this module



If you want to test this IXI module clone it and modify MQTTIXI.java file configurations to match your
own, and make it do what ever you want


 Compile with: gradle ixi


then copy *.jar file to: ict/modules
and restart ICT

 

##                                ***USE AT YOUR OWN RISK!***

   I am not responsible for any damage caused by running this software. Please use it at your own risk!
                    If you don't understand what you are doing, do not use it.
   Use this only as template and make your own IXI module if you want to communicate with ICT and MQTT

