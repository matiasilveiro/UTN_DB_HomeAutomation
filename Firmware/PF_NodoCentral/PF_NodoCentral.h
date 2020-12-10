#define USE_ESP32
//#define USE_ESP8266

// Librerias para ESP32
#ifdef USE_ESP32
    #include <WiFi.h>
    #include <WiFiClient.h>
    #include <FirebaseESP32.h>
    #include <PubSubClient.h>
    #include <ArduinoJson.h>
    #include <HardwareSerial.h>
    #include <Ticker.h>
    #include "EEPROM.h"
    #include <DNSServer.h>
    #include <ESP8266WebServer.h>
    #include <WiFiManager.h>
#endif

// Librerias para ESP8266
#ifdef USE_ESP8266
    #include <ESP8266WiFi.h>
    #include <WiFiClient.h>
    #include <FirebaseESP8266.h>
    #include <SoftwareSerial.h>
    #include <Ticker.h>
#endif

// Definiciones y macros
#define PIN_TDA_RX      5   // TDA -> Node
#define PIN_TDA_TX      6   // Node -> TDA
#define PIN_TDA_PD      2   // Power down
#define PIN_TDA_CLKOUT  7   // Clock out
#define PIN_SWITCH      4   // Soft reset

#define PIN_LED1        27
#define PIN_LED2        26

#define PIN_CRUCE_POR_CERO 34

#define NODE_TYPE_ONOFF   0x02
#define NODE_TYPE_DIMMER  0x06
#define NODE_TYPE_PLUG    0x04

#define EEPROM_SSID_IDX   0x00
#define EEPROM_PSWD_IDX   0x20
#define EEPROM_CIN        0x30
#define EEPROM_NODES_Q    0x31
#define EEPROM_SIZE       64
#define BAUD_SERIALPORT   115200
#define BAUD_TDA5051      1200

#define FIRST_ADDRESS   5
#define LAST_ADDRESS   10
#define MAX_

#define ADD_ENUM 2 // CAMBIAR. Esta direccion se reserva como direccion por defecto cuando un
                  // nodo remoto no tiene direccion propia asignada por el nodo central

/* Lista de comandos validos */
#define CMD_NOC     0
#define CMD_ADD     11 // Indica que el nodo central encuesta por nodos remotos nuevos
#define CMD_ACK     13 // Indica que se recibio un mensaje valido.
#define CMD_NACK    14 // Indica que no se realizo lo pedido.
#define CMD_ON      15 // Señal de encendido para la placa de actuacion de un nodo remoto.
#define CMD_OFF     16 // Señal de apagado para la placa de actuacion de un nodo remoto.
#define CMD_STATUS  17 // Estado del nodo. Para ver que el nodo siga respondiendo 
#define CMD_DIM     18 // Usado para indicar que en el campo Valor viene un nuevo dimerizado.
#define CMD_RETRY   19

/* Lista de estados del TDA */
#define STATE_ACTION    0
#define STATE_ENUM      1
#define STATE_STATUS    2
#define STATE_RETRY     3
/*----------------------------*/

typedef struct {
    uint8_t address;
    uint8_t type;
    uint8_t online;
    String action;
} node_t;

typedef struct NodeMsj
{
    uint8_t address;
    uint8_t command;
    uint8_t value;   
}NodeMsj_t;

#define VAL_ENUM    16 // CAMBIAR. Valor especial que solo se compara durante el proceso de enumeracion.
#define VAL_ON      13
#define VAL_OFF     14

#define MAX_DEVICES       5

#define NODE_OFFLINE    0
#define NODE_ONLINE     1
#define NODE_ACK        2
#define NODE_NACK       3
#define NODE_NOANS      4
#define NODE_INVALID    5
#define NODE_STATUS     6

#define DEBUG           // Habilita ciertos serial prints
#define WIFI_OVERRIDE   // Guarda en la EEPROM la SSID y PSWD defaults

// Credenciales de red
#define SSID_DEFAULT "Moto One Action"
#define PSWD_DEFAULT "198d8de7df5f"
//#define SSID_DEFAULT "Fibertel WiFi011 2.4GHz"
//#define PSWD_DEFAULT "mardama242829"


// Credenciales de Firebase
#define FIREBASE_HOST "automastic-db-ba00c.firebaseio.com"
#define FIREBASE_AUTH "BKKCYTtrx89xdBYCJ4lN0ouwUHWFgdkXIDrJFrSw"



const String vendor_id = "utn_pf";
const String factory_id = "1234abcd";


// Prototipos de funcion (incompletos!!)
void TDA_Init();
void WiFi_Init();
void printHeader();
void Firebase_Init();
void callback(char* topic, byte* payload, unsigned int length);
#define digitalToggle(pin) digitalWrite(pin, !digitalRead(pin))


// Variables globales
Ticker ticker;      // WiFi status
Ticker ticker1;     // Heartbeat
Ticker ticker2;     // Discover
Ticker ticker3;     // Refresh
#ifdef USE_ESP8266
    SoftwareSerial tdaSerial(D5, D6, false, 95, 10); // Rx, Tx
#endif
#ifdef USE_ESP32
    HardwareSerial tdaSerial(1);
#endif
