/**
 * @brief      Initializes nodes list array with default values
 */
void Node_Init()
{
    uint8_t cin;
    cin = EEPROM.read(EEPROM_CIN);
    if(!cin)
    {
        EEPROM.write(EEPROM_CIN,1);
        EEPROM.commit();
    }
    else
    {
        //CantidadNodos = EEPROM.read(EEPROM_NODES_Q);
        for(int i=0; i<MAX_DEVICES; i++)
        {
            //Firebase.getString(fbDataNodes, pathNodes+"/"+String(FIRST_ADDRESS+i));
            //node_list[i].address = (uint8_t)(fbDataNodes.stringData()).toInt();
            node_list[i].address = FIRST_ADDRESS + i;
            Firebase.getString(fbDataNodes, pathNodes+"/"+String(FIRST_ADDRESS+i)+"/status");
            if(fbDataNodes.stringData() == "online")
            {
                CantidadNodos++;
                node_list[i].online = NODE_ONLINE;
            }
            else if(fbDataNodes.stringData() == "offline")
                node_list[i].online = NODE_OFFLINE;
            else if(fbDataNodes.stringData() == "invalid")
                node_list[i].online = NODE_INVALID;

            Firebase.getString(fbDataNodes, pathNodes+"/"+String(FIRST_ADDRESS+i)+"/type");
            if(fbDataNodes.stringData() == "LUZ")
                node_list[i].type = NODE_TYPE_ONOFF;
            else if(fbDataNodes.stringData() == "DIMMER")
                node_list[i].type = NODE_TYPE_DIMMER;
            else if(fbDataNodes.stringData() == "PLUG")
                node_list[i].type = NODE_TYPE_PLUG;
        }        
    }

    /*for(int i=0; i<MAX_DEVICES; i++) //Solo para debug
    {
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(node_list[i].address)+"/action","off");
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(node_list[i].address)+"/name","Luzita");
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(node_list[i].address)+"/status","online");
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(node_list[i].address)+"/type","LUZ");
    }*/
}

/* ------------------------------------------------------------------*/

/**
 * @brief      Sets the current node online (connected)
 *
 * @param[in]  i     The address of the node to set
 *
 * @return     1 if OK, -1 if error
 */
void Node_setOnline(int addr)
{
    int idx = 0;

    while(node_list[idx].address != addr)
      idx++;
    node_list[idx].online = NODE_ONLINE;
    Serial.print("Address a setear Online: ");
    Serial.println(addr);
    if(addr < FIRST_ADDRESS + MAX_DEVICES)
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/status","online");
    else
        Serial.println("ERROR K10");
}

void Node_setType(int addr, uint8_t type)
{
    int idx = 0;
    while(node_list[idx].address != addr)
      idx++;
    node_list[idx].type = type;
    
    Serial.print("Type a setear del nuevo nodo online: ");
    Serial.println(type);
    if(type == NODE_TYPE_ONOFF)
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/type","LUZ");
    else if(type == NODE_TYPE_DIMMER)
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/type","DIMMER");
    else if(type == NODE_TYPE_PLUG)
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/type","PLUG");
}


/**
 * @brief      Sets the current node offline (disconnected)
 *
 * @param[in]  i     The address of the node to set
 *
 * @return     1 if OK, -1 if error
 */
void Node_setOffline(int addr)
{
    int idx = 0;

    while(node_list[idx].address != addr)
      idx++;
    
    node_list[idx].online = NODE_OFFLINE;
    //node_list[FIRST_ADDRESS+addr].online = NODE_OFFLINE;
    if(addr < FIRST_ADDRESS + MAX_DEVICES)
        Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/status","offline");
    else
        Serial.println("ERROR K10");
}

void Node_setInvalid(int addr)
{
    int idx = 0;

    while(node_list[idx].address != addr)
      idx++;
    
    node_list[idx].online = NODE_INVALID;
    //node_list[FIRST_ADDRESS+addr].online = NODE_OFFLINE;
    
    Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/status","invalid");
}

/*
FirebaseJson Node_getJSON(int i)
{
    FirebaseJson json;

    json.addString("action",node_list[i].action);
    if(node_list[i].online)
        json.addString("status","online");
    else
        json.addString("status","offline");
        
    if(node_list[i].type == NODE_TYPE_ONOFF)
        json.addString("type","on_off");
    else if(node_list[i].type == NODE_TYPE_DIMMER)
        json.addString("type","dimmer");

    return json;
}
*/

/* ------------------------------------------------------------------*/

/**
 * @brief      Serial prints the information of a node
 *
 * @param[in]  i     The address of the node
 */
void Node_printInfo(int i)
{
    if(i > 0)
    {
        Serial.print("Node number: ");
        Serial.println(i);
        
        Serial.print(">> Node type: ");
        if(node_list[i-1].type == NODE_TYPE_ONOFF)
            Serial.println("on_off");
        else if(node_list[i-1].type == NODE_TYPE_DIMMER)
            Serial.println("dimmer");
            
        Serial.print(">> Online: ");
        Serial.println(node_list[i-1].online);
        
        Serial.print(">> Action: ");
        Serial.println(node_list[i-1].action);
    }
}
