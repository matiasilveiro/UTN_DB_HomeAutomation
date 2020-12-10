
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

typedef struct {
  int actuatorAddr;
  int sensorAddr;
  int refValue;
  String condition;
  int actionTrue;
  int actionFalse;
} Control;

Control controlList[4];
int controlQty = 0;

DynamicJsonDocument doc(1024);

/**
 * @brief      Initializes MQTT client and sets up streaming callbacks
 */
void MQTT_Init() 
{
    client.setServer(mqtt_server, mqtt_port);
    client.setCallback(callback);

    pinMode(4, OUTPUT);
    pinMode(5, OUTPUT);
    pinMode(19,OUTPUT);
    pinMode(22,OUTPUT);
}


/**
 * @brief      Loops over MQTT client connection, and reconnects in case of failure
 */
void MQTT_Loop()
{
    // Reconexion del cliente MQTT
    if (!client.connected()) {
        MQTT_Reconnect();
    }
    client.loop();
}



/* ------------------------------------------------------------------*/


/**
 * @brief      MQTT subscription callback.
 *
 * @param      topic    The topic of the received message
 *             payload  The incoming message
 *             length   Length of the payload
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
            MQTT_sendAck(nodeStr, true);
            MQTT_setDebugLED(nodeStr, payloadStr);
        } else {
            int idx_set = topicStr.indexOf("controls");
            if(idx_set > -1) {
                int idx_action = topicStr.lastIndexOf("/");
                String action = topicStr.substring(idx_action+1);
                deserializeJson(doc, payloadStr);
                String controlName = doc["Name"];
                
                #ifdef DEBUG
                    Serial.print(">> Control query: ");
                    Serial.println(action);
                    Serial.print(">> Control name: ");
                    Serial.println(controlName);
                #endif
            }
        }
    }
}


/**
 * @brief      Sends a message acknowledged to the server.
 *
 * @param      node_addr  The address of the remote node
 *             state      Sends ack if true, nack if false
 */
void MQTT_sendAck(String node_addr, boolean state)
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


void MQTT_fetchControls() 
{
    char topicPub[50];
    String topicPubStr;
    
    topicPubStr = databaseAddr + "get";
    topicPubStr.toCharArray(topicPub, 50);
    client.publish(topicPub, "controls");
}


/**
 * @brief      Updates the status of the remote node in the server.
 *
 * @param      node_addr  The address of the remote node
 *             state      Sends Online if true, Offline if false
 */
void MQTT_updateRemoteStatus(String node_addr, boolean state)
{
    char topicPub[50];
    String topicPubStr;
    
    topicPubStr = databaseAddr + node_addr + "/" + "status";
    topicPubStr.toCharArray(topicPub, 50);
    if(state) {
      client.publish(topicPub, "Online");
    } else {
      client.publish(topicPub, "Offline");
    }
}


void MQTT_SimulateRemoteSensor(void)
{
    char topicPub[50];
    char messagePub[10];
    String topicPubStr;
    long measure = random(20,40);
    
    topicPubStr = databaseAddr + "0" + "/" + "value";
    topicPubStr.toCharArray(topicPub, 50);
    String(measure).toCharArray(messagePub, 10);
    client.publish(topicPub, messagePub);
}

/* ------------------------------------------------------------------*/


/**
 * @brief      Connects to MQTT broker, and subscribes to the node topic
 */
void MQTT_Reconnect() 
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

            MQTT_fetchControls();
        } else {
            Serial.print("failed, rc=");
            Serial.print(client.state());
            Serial.println(" try again in 5 seconds");
            delay(5000);
        }
    }
}


void MQTT_setDebugLED(String led, String state)
{
  uint8_t pinState = LOW;
  if(state == "1") {
    pinState = HIGH;
  }

  if(led == "0") digitalWrite(4, pinState);
  if(led == "1") digitalWrite(5, pinState);
  if(led == "2") digitalWrite(19,pinState);
  if(led == "3") digitalWrite(22,pinState);
}
