#!/usr/bin/env python
# -*- coding: utf-8 -*-

# http://www.steves-internet-guide.com/into-mqtt-python-client/

import paho.mqtt.client as mqtt
import time
import atexit
import central_node

#NODE_ADDR ='1234-5678-9014'
NODE_ADDR ='1234-5678-9013'

BROKER = 'broker.mqtt-dashboard.com'
PORT = 1883
QoS = 2
SUBS_ADDR = 'utn_pf/{}/#'.format(NODE_ADDR)
PUBS_ADDR = 'utn_pf/db_broadcast/{}/'.format(NODE_ADDR)


########################################################################

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    if rc==0:
        print("Connected OK Returned code =",rc)
        # Subscribing in on_connect() means that if we lose the connection and
        # reconnect then subscriptions will be renewed. There are other methods to achieve this.
        print(">>> Subscribing to topic",SUBS_ADDR)
        client.subscribe(SUBS_ADDR)

        topic = PUBS_ADDR + 'status'
        print(">>> Publishing 'Online' to topic",topic)
        client.publish(topic=topic, payload='Online')
    else:
        if rc==1:
            print("Connection refused – incorrect protocol version")
        elif rc==2:
            print("Connection refused – invalid client identifier")
        elif rc==3:
            print("Connection refused – server unavailable")
        elif rc==4:
            print("Connection refused – bad username or password")
        elif rc==5:
            print("Connection refused – not authorised")

############

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, message):
    #print("     >>> message received= " ,str(message.payload.decode("utf-8")))
    #print("     >>> message topic=",message.topic)
    #print("     >>> message qos=",message.qos)
    #print("     >>> message retain flag=",message.retain)
    decodeMqttMessage(message.topic, str(message.payload.decode("utf-8")))


def decodeMqttMessage(topic: str, msg: str):
    topic = topic.split('/')
    field = topic[2]
    #print('DecodeMQTT: {}: {}'.format(field,msg))

    if(field == 'config'):
        config = topic[3]
        if(config == 'status'):
            topic = PUBS_ADDR + "status"
            client.publish(topic= topic, payload= 'Online')
    elif(field.isdigit()):
        remote_node = topic[2]
        print('Remote addr: {}, value: {}'.format(remote_node, msg))


############

# The callback for when the script is closed.
@atexit.register
def on_close():
    print("Cerrando...")
    client.loop_stop()  # Stop the loop

########################################################################


def print_header():
    print("--------------------------------------------")
    print("              PF - Nodo central             ")
    print("--------------------------------------------")
    print("     2020 - UTN FRBA - Ing. Electronica     ")
    print("\n")

def central_node_emulator():
    print("\n")
    print("1) Listar nodos remotos")
    print("2) Agregar un nodo remoto")
    print("3) Conectar nodo remoto")
    print("4) Desconectar nodo remoto")
    print("5) Cerrar")
    option = input(">> Elige la opcion: ")

    if(option == "1"):
        central_node.list_remote_nodes()
    elif(option == "2"):
        node = central_node.add_remote_node()
        topic = PUBS_ADDR + "new"
        client.publish(topic= topic, payload= node)
    elif(option == "3"):
        address = input(">> Ingrese la direccion: ")
        central_node.set_remote_node_status(int(address),'Online')
        topic = PUBS_ADDR + str(address) + "/status"
        client.publish(topic= topic, payload= 'Online')
    elif(option == "4"):
        address = input(">> Ingrese la direccion: ")
        central_node.set_remote_node_status(int(address),'Offline')
        topic = PUBS_ADDR + str(address) + "/status"
        client.publish(topic= topic, payload= 'Offline')
    elif(option == "5"):
        exit(0)
    else:
        print("Opcion incorrecta")


########################################################################

if __name__ == '__main__':
    print_header()

    broker_address = BROKER

    print(">>> Creating new instance...")
    client = mqtt.Client("P1")          # Create new instance
    client.on_message = on_message      # Attach function to callback
    client.on_connect = on_connect      # Attach function to callback

    print(">>> Connecting to broker")
    try:
        client.connect(broker_address)  # Connect to broker
    except:
        print("Connection failed, exiting...")
        exit()

    client.loop_start() # Start the loop
    time.sleep(2)       # Wait

    while(True):
        central_node_emulator()

