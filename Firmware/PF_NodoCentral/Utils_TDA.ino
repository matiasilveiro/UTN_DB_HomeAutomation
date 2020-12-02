
void TDA_Init()
{
    // Inicializacion de pines de Tx/Rx
    #ifdef ESP8266
        tdaSerial.begin(BAUD_TDA5051);
    #endif
    #ifdef ESP32
        tdaSerial.begin(BAUD_TDA5051,SERIAL_8N1,16,17); // Baud, config, Rx, Tx
    #endif
    //tdaSerial.write((static_cast<uint8_t>(0)));
    
    // Inicializacion de pines de clock y power-down
    pinMode(PIN_TDA_CLKOUT, INPUT);
    pinMode(PIN_TDA_PD, OUTPUT);
    
    // Reinicio de TDA5051
    digitalWrite(PIN_TDA_PD, HIGH);
    delay(100);
    digitalWrite(PIN_TDA_PD, LOW);
    delay(100);
    Serial.println("Se configuro TDA");
}

/* ------------------------------------------------------------------*/

/**
 * @brief      Maquina de Estados principal del programa. ALterna normalmente
 *             entre el proceso de enumeracion y control de estado de los nodos
 *             remotos. Para comunicarse con un nodo remoto, mediante la
 *             interrupcion de firebase se altera la secuencia y una vez
 *             concluida la accion actual se procede a transmitir una accion a
 *             un determinado nodo remoto.
 */
void TDA_Handle(void)
{
    static unsigned char estadoTDA = 1;
    NodeMsj_t MsjENUM = {.address = ADD_ENUM, .command = CMD_ADD, .value = VAL_ENUM};
    //unsigned char MsjENUM[3] = {ADD_ENUM,CMD_ADD,VAL_ENUM};
    unsigned char Rx_CMD = 0;
    unsigned char AddNodoNuevo = 5;
    //unsigned char Mensaje[3] = {0,0,0};
    NodeMsj_t msjDEBUG;
    NodeMsj_t Mensaje;
    int i = 0;
    uint8_t actionRetry =3;
    
    switch(estadoTDA)
    {
        case 0: //Ejecutar acciones sobre nodos remotos
            Serial.println();
            Serial.println("ACCION");
            do
            {
                TDA_Tx(NodesMsjBuffer[NodeActions-1]);
                Rx_CMD = TDA_Rx(&Mensaje);
                if(Rx_CMD == CMD_ACK && Mensaje.address == NodesMsjBuffer[NodeActions-1].address)
                {
                    NodeActions--;
                    actionRetry = 3;
                    ChangeNodeStatus(NodesMsjBuffer[NodeActions-1],NODE_ACK);
                }
                else
                {
                    actionRetry--;
                    if(!actionRetry)
                    {
                        NodeActions--;
                        actionRetry = 3;
                        ChangeNodeStatus(NodesMsjBuffer[NodeActions-1],NODE_NOANS);   
                    }
                }
            }
            while(NodeActions);
            estadoTDA = 1; //Resumo operacion normal
            break;
      
        case 1: // Enumeracion
            Serial.println();
            Serial.println("ENUMERACION");
            
            TDA_Tx(MsjENUM);
            Rx_CMD = TDA_Rx(&Mensaje);
            switch(Rx_CMD)
            {
                case CMD_ADD:
                    Serial.println("Hay un nuevo nodo a enumerar");
                    MsjENUM.command = Mensaje.value;
                    AddNodoNuevo = GetNewAdd();
                    if(AddNodoNuevo != ADD_ENUM)
                    {
                        MsjENUM.value = AddNodoNuevo;
                        TDA_Tx(MsjENUM);
                        Rx_CMD = TDA_Rx(&Mensaje);
                        if(Mensaje.command == CMD_ACK && Mensaje.address == AddNodoNuevo)
                            ChangeNodeStatus(Mensaje,NODE_ONLINE);
                    }
                    else
                        Serial.println("No hay direcciones disponibles");
                    break;

                default:
                    Serial.println("No hubo respuesta a la busqueda de nodos a enumerar");
                    break;
            }
            if(NodeActions)
                estadoTDA = 0;
            else if(NodeOfflineRetry)
                estadoTDA = 3;
            else
                estadoTDA = 2;
            break;

        case 2: // Control periodico de nodos remotos
            statusCounter++;
            if(statusCounter == 30)
            {
                Serial.println();
                Serial.println("STATUS");
                UpdateNodesStatus();
                if(NodeActions)
                    estadoTDA = 0;
                else if(NodeOfflineRetry)
                    estadoTDA = 3;
                else
                    estadoTDA = 1;
                statusCounter = 0;
            }
            break;

        case 3:
            Serial.println("RETRY OFFLINE");
            RetryMissingNodes();
            Serial.println("Termine Retry Offline");
            NodeOfflineRetry = 0;
            if(NodeActions)
                estadoTDA = 0;
            else
                estadoTDA = 1;
            break;

        default:
            Serial.println("Default de FSM Principal");
            break;
    }
}

/**
 * @brief      Realiza un control de los nodos remotos enumerados. Para cada uno
 *             envia el comando status para saber si se mantiene la conexion con
 *             dicho nodo. En caso de falta de respuesta reiterada del nodo, se
 *             procede a darlo como offline y se avisa a la base de datos. Si el
 *             nodo responde no se realiza accion.
 */
void UpdateNodesStatus(void)
{
    unsigned char CMD_Rx;
    static uint8_t i = 0, j = 0;
    int RetryCount = 0;
    unsigned char Msj[3]={0};
    NodeMsj_t MsjSTATUS = {.address = 0, .command = CMD_STATUS, .value = 5}; //value es irrelevante
    NodeMsj_t MsjRx = {.address = 0, .command = 0, .value = 0}; //value es irrelevante

    Serial.print("CantidadNodos: ");
    Serial.println(CantidadNodos);

    for(; (node_list[j].online != NODE_ONLINE && j < MAX_DEVICES) ; j++);
    //Serial.print("Node online: ");
    //Serial.println(node_list[j].online);
    if(j < MAX_DEVICES)
    {
        MsjSTATUS.address = FIRST_ADDRESS + j; // Cargar primera direccion de asignacion para nodos remotos. Asumo direciones consecutivas
        j++;
        
        RetryCount=5;
        while(RetryCount > 0)
        {
            Serial.print("Node address: " + String(MsjSTATUS.address) + "\t\t");
            Serial.print("RetryCount: ");
            Serial.println(RetryCount);
            TDA_Tx(MsjSTATUS);
            CMD_Rx = TDA_Rx(&MsjRx);
            //Serial.print("Comando Recibido: ");
            //Serial.println(CMD_Rx);
            if(CMD_Rx == CMD_ACK && MsjRx.address == MsjSTATUS.address)
                RetryCount = -1;
            else
                RetryCount--;
            
            if(NodeActions)
                break;
        }
        if(!RetryCount)
        {
            ChangeNodeStatus(MsjSTATUS,NODE_OFFLINE);
        }
        else if(RetryCount == -1)
            ChangeNodeStatus(MsjRx,NODE_STATUS);
    }
    i++;
    if(i >= CantidadNodos || j >= MAX_DEVICES)
    {
        i = 0;
        j = 0;    
    }
    Serial.println("Termine Status");
}


/**
 * @brief      Realiza un control de los nodos remotos posibielemte desconectados.
 *             Para cada uno envia el comando status para saber si se mantiene la
 *             conexion con dicho nodo. En caso de falta de respuesta reiterada
 *             del nodo, se procede a darlo como offline y se avisa a la base de
 *             datos. Si el nodo responde no se realiza accion.
 */
void RetryMissingNodes(void)
{
    unsigned char CMD_Rx;
    static uint8_t cont = 0;
    int RetryCount = 0;
    unsigned char Msj[3]={0};
    NodeMsj_t MsjSTATUS = {.address = 0, .command = CMD_STATUS, .value = 5}; //value es irrelevante
    NodeMsj_t MsjRx = {.address = 0, .command = 0, .value = 0}; //value es irrelevante

    if(cont == MAX_DEVICES)
        cont = 0;
    while( cont<MAX_DEVICES )
    {
        if( node_list[cont].online == NODE_OFFLINE )
        {
            MsjSTATUS.address = FIRST_ADDRESS + cont;
            RetryCount=2;
            while(RetryCount > 0)
            {
                Serial.print("Node address: " + String(MsjSTATUS.address) + "\t");
                Serial.print("RetryCount: ");
                Serial.println(RetryCount);
                TDA_Tx(MsjSTATUS);
                CMD_Rx = TDA_Rx(&MsjRx);
                if(CMD_Rx == CMD_ACK && MsjRx.address == MsjSTATUS.address)
                    RetryCount = -1;
                else
                    RetryCount--;
            
                if(NodeActions)
                    RetryCount = 0;
            }
            if(RetryCount == -1)
            {
                MsjSTATUS.command = CMD_RETRY;
                ChangeNodeStatus(MsjSTATUS,NODE_ONLINE);
                MsjSTATUS.command = CMD_STATUS;
            }
            cont++;
            return;
        }
        cont++;
    }
}


void setRetryFlag(void)
{
    Serial.println();
    Serial.println("Automatic retry triggered");
    NodeOfflineRetry = 1;

    Serial.println("Estadistica de errores:");
    Serial.println("0 errores: "+String(e_0));
    Serial.println("1 errores: "+String(e_1));
    Serial.println("2 errores: "+String(e_2));
    Serial.println("3 errores: "+String(e_3));
    Serial.println("4 errores: "+String(e_4));
}

/**
 * @brief      Cambia un parametro del estado de un nodo. Puede ser producto del
 *             proceso de enumeracion, del control de estado, o frente a una
 *             accion realizada sobre un nodo remoto.
 *
 * @param[in]  NodeInfo    Estructura de mensaje de nodo que tiene la direccion
 *                         del nodo e informacion en el campo value.
 * @param[in]  NodeStatus  Indica el cambio a realizar del nodo.
 */
void ChangeNodeStatus(NodeMsj_t NodeInfo, unsigned char NodeStatus)
{
    int idx = 0;
    if(NodeInfo.address)
    {
        switch(NodeStatus)
        {
            case NODE_ACK:
            Serial.println("Estoy en NODE ACK");
                Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/ack","ack");
                break;

            case NODE_STATUS:
                //Firebase.getString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/status");
                //delay(100);
                Serial.println("Estoy en NODE STATUS Address: " + String( (int)NodeInfo.address ));
                while(node_list[idx].address != NodeInfo.address)
                  idx++;
                if(node_list[idx].online == NODE_OFFLINE)
                {
                    Serial.println("Pongo online al nodo: "+String( (int)NodeInfo.address ));
                    Node_setOnline((int)NodeInfo.address);
                }
                
                //Serial.println("Estoy en NODE STATUS");
                //Serial.print("El adress del nodo es: ");
                //Serial.println(NodeInfo.address);
                /*
                Serial.println(fbDataNodes.stringData());
            
                Firebase.getString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/type");
                
                Serial.println(fbDataNodes.stringData());
                
                if(fbDataNodes.stringData() == "DIMMER")
                {
                    Serial.println("Estoy en DIMMER");
                    Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/action",String((int)NodeInfo.value)+"%");
                }
                else
                {
                    Serial.println("Estoy en ON/OFF");
                    if(NodeInfo.value == VAL_OFF)
                        Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/action","off");
                    else if(NodeInfo.value == VAL_ON)
                        Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/action","on");
                }
                */
                break;
  
            case NODE_OFFLINE:
                Serial.println("Estoy en NODE OFFLINE");
                Node_setOffline((int)NodeInfo.address);
                CantidadNodos--;
                EEPROM.write(EEPROM_NODES_Q, CantidadNodos);
                EEPROM.commit();
                break;
  
            case NODE_ONLINE:
                Node_setOnline((int)NodeInfo.address);
                if(NodeInfo.command == CMD_ACK)
                {
                    Serial.println("Estoy en NODE ONLINE. Seteando action a off");
                    Node_setType((int)NodeInfo.address, NodeInfo.value);
                    Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/action","off");
                    delay(100);
                    Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/ack","noans");
                }
                CantidadNodos++;
                EEPROM.write(EEPROM_NODES_Q, CantidadNodos);
                EEPROM.commit();
                break;
  
            case NODE_NOANS:
                //Firebase.setString(fbDataNodes, pathNodes+"/"+String(addr)+"/ack","online");
                Firebase.setString(fbDataNodes, pathNodes+"/"+String((int)NodeInfo.address)+"/ack","noans");
                break;

            case NODE_INVALID:
                Node_setInvalid((int)NodeInfo.address);
                break;

            default:  
                Serial.println("Default de ChangeStatus");
                break;
        }
    }
}

uint8_t GetNewAdd(void)
{
    uint8_t i = 0;

    while(i < MAX_DEVICES)
    {
        if(node_list[i].online == NODE_INVALID)
            return node_list[i].address;
        else
            i++;
    }
    return ADD_ENUM; //Devuelvo la direccion de enumeracion si me quede sin direcciones.
}

/**
 * @brief      Recibe un mensaje enviado por un nodo remoto. Si llega un mensaje
 *             y es valido se interpreta el mensaje recibido.
 *
 * @param      Mensaje  Estructura de mensaje de nodo donde se guarda el mensaje
 *                      recibido.
 *
 * @return     Devuelve el comando recibido si el msj fue valido. En caso
 *             contrario avisa que no hubo msj con la macro No Command.
 */
unsigned char TDA_Rx(NodeMsj_t* Mensaje)
{
    int error_H = Hadamard_Rx(Mensaje);

    if(error_H > 0)
    {
        if( Mensaje->address != ADD_ENUM)
        {
            switch(Mensaje->command)
            {
                case CMD_ACK:
                    //digitalWrite(LED_BUILTIN,HIGH);
                    Serial.println(">> ACK recibido");
                    return CMD_ACK;
                    break;

                default:
                    Serial.println(">> Comando invalido o reservado");
                    return CMD_NOC;
                    break;
            }
        }
        else
            return CMD_ADD;
    }
    return CMD_NOC; //Command No-Command
}

void TDA_Tx(NodeMsj_t TxMsj) 
{
    //int sensor = analogRead(PIN_CRUCE_POR_CERO);
    static int estado=0;
    unsigned char Msj[3];
    Msj[0] = TxMsj.address;
    Msj[1] = TxMsj.command;
    Msj[2] = TxMsj.value;

    /*
    Serial.print("El mensaje a enviar es: ");
    Serial.print(Msj[0]);
    Serial.print(" ");
    Serial.print(Msj[1]);
    Serial.print(" ");
    Serial.println(Msj[2]);
    */
    while(1)
    {
      //if(sensor > 250)
      //{        
        //Serial.print("Sensor linea: ");
        //Serial.println(sensor);
        Hadamard_Tx(Msj,3);
        return;
      //}
    }
}
