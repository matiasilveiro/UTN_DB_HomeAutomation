
// Variables globales Firebase
String pathNodes = "/Nodes";
String pathConfig = "/Config/Firmware";
String jsonData = "";
FirebaseData fbDataNodes;
FirebaseData fbDataConfig;

/**
 * @brief      Initializes Firebase database and sets up streaming callbacks
 */
void Firebase_Init()
{
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    Firebase.reconnectWiFi(true);

    
    if (!Firebase.beginStream(fbDataConfig, pathConfig))
    {
        Serial.println("Could not begin stream");
        Serial.println("REASON: " + fbDataConfig.errorReason());
        Serial.println();
    }    
    Firebase.setStreamCallback(fbDataConfig, streamConfigCallback, streamTimeoutCallback);
    Firebase.setInt(fbDataConfig, pathConfig+"/refresh",0);


    if (!Firebase.beginStream(fbDataNodes, pathNodes))
    {
        Serial.println("Could not begin stream");
        Serial.println("REASON: " + fbDataNodes.errorReason());
        Serial.println();
    }    
    Firebase.setStreamCallback(fbDataNodes, streamNodesCallback, streamTimeoutCallback);
}

/* ------------------------------------------------------------------*/

/**
 * @brief      'Nodes' FB node streaming callback
 *
 * @param[in]  firebaseData  The modified node data
 */
void streamNodesCallback(StreamData firebaseData)
{
    String path = firebaseData.dataPath();
    String type = firebaseData.dataType();
    String node;
    String field;
    uint8_t NodeType;

    int idx = path.lastIndexOf("/");
    if(idx > 0)
    {
        node = path.substring(1,idx);
        field = path.substring(idx+1);
        Serial.print(">> Node address: ");
        Serial.println(node);
        Serial.print(">> Modified field: ");
        Serial.println(field);

        if(field == "action")
        {
            Firebase.getString(fbDataNodes, pathNodes+"/"+node+"/action");
            String payloadStr = fbDataNodes.stringData();

            Firebase.getString(fbDataNodes, pathNodes+"/"+node+"/type");
            String type = fbDataNodes.stringData();

            if(type == "LUZ")
            {
                NodesMsjBuffer[NodeActions].address = (uint8_t)node.toInt();
                if(payloadStr == "on")
                    NodesMsjBuffer[NodeActions].command = CMD_ON;
                else
                    NodesMsjBuffer[NodeActions].command = CMD_OFF;
                NodesMsjBuffer[NodeActions].value = 4;
            }
            else if(type == "PLUG")
            {
                NodesMsjBuffer[NodeActions].address = (uint8_t)node.toInt();
                if(payloadStr == "on")
                    NodesMsjBuffer[NodeActions].command = CMD_ON;
                else
                    NodesMsjBuffer[NodeActions].command = CMD_OFF;
                NodesMsjBuffer[NodeActions].value = 4;
            }
            else if(type == "DIMMER")
            {
                String dimmValue = payloadStr.substring(0,payloadStr.length()-1);
                Serial.println(dimmValue);
                uint8_t dimValue = map(dimmValue.toInt(),0,100,5,10);
                NodesMsjBuffer[NodeActions].address = (uint8_t)node.toInt();
                NodesMsjBuffer[NodeActions].command = CMD_DIM;
                NodesMsjBuffer[NodeActions].value = dimValue;
            }
            NodeActions++;
        }
        else if(field == "status")
        {
            Firebase.getString(fbDataNodes, pathNodes+"/"+node+"/status");
            String payloadStr = fbDataNodes.stringData();
            int idx = 0;

            while(node_list[idx].address != node.toInt())
              idx++;
            if(payloadStr == "online")
            {
                node_list[idx].online = NODE_ONLINE;
                CantidadNodos++;
            }
            else if(payloadStr == "offline")
                node_list[idx].online = NODE_OFFLINE;
            else if(payloadStr == "invalid")
                node_list[idx].online = NODE_INVALID;
        }
    }
    else {
        //Serial.println(">> Not yet implemented");
        ;
    }
}

/* ------------------------------------------------------------------*/

/**
 * @brief      'Configuration' FB node streaming callback
 *
 * @param[in]  firebaseData  The modified node data
 */
void streamConfigCallback(StreamData firebaseData)
{
    String field = firebaseData.dataPath().substring(1);
    
    if(field == "new_ssid")
    {
        String ssid = firebaseData.stringData();
        Serial.println(">> New SSID: " + ssid);
    }
    else if(field == "new_pswd")
    {
        String pswd = firebaseData.stringData();
        Serial.println(">> New PSWD: " + pswd);
    }
    else if(field == "refresh")
    {
        Serial.println(">> Refreshing...");
        Firebase.setInt(fbDataConfig, pathConfig+"/refresh",0);
        NodeOfflineRetry = 1;
    }
    else {
        //Serial.println(">> Not yet implemented");
        ;
    }
}

/* ------------------------------------------------------------------*/

/**
 * @brief      Timeout streaming callback
 *
 * @param[in]  timeout  Timeout occurred if true
 */
void streamTimeoutCallback(bool timeout)
{
    if (timeout)
    {
        Serial.println();
        Serial.println("Stream timeout, resume streaming...");
        Serial.println();
    }
}

/* ------------------------------------------------------------------*/

/**
 * @brief      Serial prints the data from the node, regarding data type
 *
 * @param[in]  firebaseData  The firebase data to print
 */
void printFirebaseData(FirebaseData firebaseData)
{
  
    Serial.println("PATH: " + firebaseData.dataPath());
    Serial.println("TYPE: " + firebaseData.dataType());
    Serial.print("VALUE: ");
    if (firebaseData.dataType() == "int")
        Serial.println(firebaseData.intData());
    else if (firebaseData.dataType() == "float")
        Serial.println(firebaseData.floatData(), 5);
    else if (firebaseData.dataType() == "double")
        printf("%.9lf\n", firebaseData.doubleData());
    else if (firebaseData.dataType() == "boolean")
        Serial.println(firebaseData.boolData() == 1 ? "true" : "false");
    else if (firebaseData.dataType() == "string")
        Serial.println(firebaseData.stringData());
   /* else if (firebaseData.dataType() == "json")
        Serial.println(firebaseData.jsonData());*/
    Serial.println("------------------------------------");
    Serial.println();
}
