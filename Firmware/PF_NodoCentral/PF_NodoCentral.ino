/* ------------------------------------------------------------------ /
  
  Proyecto Final - Nodo central
  
  Autor: Sidorczuk - Silveiro - Trotta - Vattolo
  Ingenieria Electronica - UTN FRBA
  
  Descripcion:
    TODO
      
  Objetivos:
    TODO
    
  Circuito:
    * 
  
*/

/* ------------------------------------------------------------------*/

#include "PF_NodoCentral.h"

/* ------------------------------------------------------------------*/
unsigned char CantidadNodos = 0;
volatile unsigned char NodeActions = 0; // Setear a 1 si hay que enviar una accion a un nodo remoto
volatile unsigned char NodeOfflineRetry = 0; // Setear a 1 si hay que checkear nodos offline
NodeMsj_t NodesMsjBuffer[10];
node_t node_list[MAX_DEVICES];
int statusCounter = 0;

uint32_t e_0 = 0;
uint32_t e_1 = 0;
uint32_t e_2 = 0;
uint32_t e_3 = 0;
uint32_t e_4 = 0;

void setup() 
{
    // Inicializacion del Serial Port para debug
    Serial.begin(BAUD_SERIALPORT);
    printHeader();
    
    // Inicializacion del builtin LED para debug
    //pinMode(LED_BUILTIN, OUTPUT);
    pinMode(PIN_LED1, OUTPUT);
    pinMode(PIN_LED2, OUTPUT);
    //pinMode(PIN_CRUCE_POR_CERO, INPUT);

    // Inicializacion de EEPROM
    if (!EEPROM.begin(EEPROM_SIZE)) 
    {
        Serial.println("Failed to initialise EEPROM");
        Serial.println("Restarting...");
        delay(1000);
        ESP.restart();
    }
    
    // Inicializacion de I/O del TDA5051
    TDA_Init();
    
    // Inicializacion del WiFi
    WiFi_Init();
    
    // Inicializacion del cliente Firebase
    //Firebase_Init();

    // Inicializacion del cliente MQTT
    MQTT_Init();

    // Inicializacion del vector de nodos
    Node_Init();

    //ticker.attach(1, WiFi_Status);
    //ticker1.attach(1, heartbeat);
    //ticker2.attach(7, TDA_Discover);
    //ticker3.attach(10, setRetryFlag);
    ticker3.attach(5, MQTT_SimulateRemoteSensor);
}

void loop() 
{
    // Mensaje entrante por TDA5051
    //TDA_Handle();

    MQTT_Loop();
}

/* ------------------------------------------------------------------*/

void WiFi_Init() 
{
    WiFi.disconnect(true);
    delay(100);

    /*
    WiFiManager wifiManager;
    wifiManager.autoConnect("Automastic Wi-Fi");
    Serial.println("connected...yeey :)");
    */
    
    // We start by connecting to a WiFi network
    Serial.print("Connecting to ");
    Serial.println(SSID_DEFAULT);
    
    WiFi.begin(SSID_DEFAULT, PSWD_DEFAULT);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    
    randomSeed(micros());
    
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
    Serial.println("");
}

void WiFi_Status()
{
    if(WiFi.status() != WL_CONNECTED)
    {
        Serial.println("WiFi disconnected!");
        digitalWrite(PIN_LED1, !digitalRead(PIN_LED1));
    }
}

/* ------------------------------------------------------------------*/

void printHeader()
{
    Serial.println("");
    Serial.println("--------------------------------------------");
    Serial.println("              PF - Nodo central             ");
    Serial.println("--------------------------------------------");
    Serial.println("     2019 - UTN FRBA - Ing. Electronica     ");
    Serial.println("");
}
