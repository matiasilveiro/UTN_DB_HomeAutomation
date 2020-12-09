
// Configuracion de server MQTT
const char* mqtt_server = "broker.mqtt-dashboard.com";
const char* mqtt_user = "matias";
const char* mqtt_pswd = "matias";
const int mqtt_port = 1883;

// Variables globales MQTT
WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;
String pubsubAddr = vendor_id + "/" + WiFi.macAddress() + "/";
String databaseAddr = vendor_id + "/" + "db_broadcast" "/" + WiFi.macAddress() + "/";

/**
 * @brief      Initializes MQTT client and sets up streaming callbacks
 */
void MQTT_Init() 
{
    client.setServer(mqtt_server, mqtt_port);
    client.setCallback(callback);
}


/**
 * @brief      TODO
 */
void MQTT_Loop()
{
    // Reconexion del cliente MQTT
    if (!client.connected()) {
        reconnect();
    }
    client.loop();
}



/* ------------------------------------------------------------------*/


/**
 * @brief      Recibe la informacion a transmitir y envia el codigo de hadamard
 *             correspondiente por serie al TDA.
 *
 * @param      data     Vector que tiene la informacion en los 5 bits menos
 *                      significativos de cada elemento.
 * @param[in]  DataLen  Cantidad de elementos que tienen informacion.
 */
void callback(char* topic, byte* payload, unsigned int length) 
{
    payload[length]=0;
    
    String payloadStr = String((char *)payload);
    String topicStr = String(topic);
    
    // Por si tengo que responder a algun topic
    char topicPub[50];
    String topicPubStr;
    
    #ifdef DEBUG
        Serial.println("");
        Serial.print("MQTT Message arrived [");
        Serial.print(topic);
        Serial.print("]: ");
        Serial.print(payloadStr);
        Serial.println();
    #endif
    
    int idx_cfg = topicStr.indexOf("config/");
    if(idx_cfg > -1)
    {
        String configStr = topicStr.substring(idx_cfg+7);
        #ifdef DEBUG
            Serial.print(">> Configuration type: ");
            Serial.println(configStr);
            Serial.print(">> Configuration value: ");
            Serial.println(payloadStr);
        #endif
        
        if(configStr == "status")
        {
            String topicPubStr = databaseAddr + "status";
            topicPubStr.toCharArray(topicPub, 50);
            client.publish(topicPub, "Online");
        }
    } else {
        int idx_set = topicStr.indexOf("set");
        if(idx_set > -1) {
          int idx_node = topicStr.lastIndexOf("/");
          String nodeStr = topicStr.substring(idx_node-1,idx_node);
          
          #ifdef DEBUG
              Serial.print(">> Node address: ");
              Serial.println(nodeStr);
              Serial.print(">> Node value: ");
              Serial.println(payloadStr);
          #endif
          
          //digitalToggle(LED_BUILTIN);
          tdaSerial.print("*" + nodeStr + "+" + payloadStr + "#");
          send_ack(nodeStr, true);
        }
    }
}

void send_ack(String node_addr, boolean state)
{
    char topicPub[50];
    String topicPubStr;
    
    topicPubStr = databaseAddr + node_addr + "/" + "ack";
    topicPubStr.toCharArray(topicPub, 50);
    if(state) {
      client.publish(topicPub, "ack");
    } else {
      client.publish(topicPub, "nack");
    }
}

/* ------------------------------------------------------------------*/

void reconnect() 
{
    char topicSub[50];
    char topicPub[50];
    
    // Loop until we're reconnected
    while (!client.connected()) 
    {
        Serial.print("Attempting MQTT connection... ");
        
        // Create a random client ID
        String clientId = "ESPClient-";
        clientId += String(random(0xffff), HEX);
        
        // Attempt to connect
        if (client.connect(clientId.c_str())) {
            Serial.println("connected!");
            
            // Once connected, publish an announcement...
            String topicPubStr = databaseAddr + "status";
            topicPubStr.toCharArray(topicPub, 50);
            client.publish(topicPub, "Online");
            
            // ... and resubscribe
            String topicSubStr = pubsubAddr + "#";
            topicSubStr.toCharArray(topicSub, 50);
            Serial.print("Subscribing to ");
            Serial.println(topicSub);
            Serial.println();
            client.subscribe(topicSub);
        } else {
            Serial.print("failed, rc=");
            Serial.print(client.state());
            Serial.println(" try again in 5 seconds");
            delay(5000);
        }
    }
}
