uint16_t HadamardMatrix[16]={0xFFFF,0xAAAA,0xCCCC,0x9999,0xF0F0,0xA5A5,0xC3C3,0x9696,0xFF00,0xAA55,0xCC33,0x9966,0xF00F,0xA55A,0xC33C,0x9669};


/* ------------------------- Tx ---------------------------*/

/**
 * @brief      Codifica los 5 bits mas bajos de data con una palabra de la
 *             matriz de hadamard
 *
 * @param[in]  data  buffer con la informacion a codificar
 *
 * @return     Devuelve el codigo de 16 bits a transmitir
 */
uint16_t Hadamard_Generator(unsigned char data)
{
    unsigned char indice = 0,inversion = 0;
    uint16_t aux = 0, codeword;

    indice = data & 0x0F;
    inversion = (data >> 4) & 1;

    codeword = HadamardMatrix[indice];
    if(!inversion)
        return codeword;
    else
        return ~codeword;
}

/**
 * @brief      Recibe la informacion a transmitir y envia el codigo de hadamard
 *             correspondiente por serie al TDA.
 *
 * @param      data     Vector que tiene la informacion en los 5 bits menos
 *                      significativos de cada elemento.
 * @param[in]  DataLen  Cantidad de elementos que tienen informacion.
 */
void Hadamard_Tx(unsigned char* data, unsigned char DataLen)
{
    char i = 0;
    uint16_t codeword;

    for(i=0;i<DataLen;i++)
    {
        codeword = Hadamard_Generator(data[i]);
        tdaSerial.write( (unsigned char)(codeword & 0xFF));
        tdaSerial.write( (unsigned char)((codeword >> 8) & 0xFF));
    }
    tdaSerial.flush();
}

uint8_t Hadamard_CRC(uint8_t* Trama)
{
    uint8_t CRC;
    CRC = Trama[0] + Trama[1] + Trama[2];
    CRC = (~CRC) + 1;
    CRC %= MAX_DEVICES;
    return CRC;
}

uint8_t CRC_Check(uint8_t* Trama)
{
    return (Hadamard_CRC(Trama) == Trama[3]) ? 1 : 0;
}

/* ------------------------- Rx ---------------------------*/

/**
 * @brief      Recupera a partir de una palabra de hadamard los 5 bits de
 *             informacion que la generaron. Ademas corrige hasta 3 errores en
 *             la trama, o avisa de un cuarto error.
 *
 * @param[in]  codeword  Entero de 16 bits que codifica 5 bits de informacion
 * @param      errores   Se informa con esta variable la cantidad de errores que
 *                       tuvo codeword.
 *
 * @return     Devuelve los 5 bits de informacion corregidos si hubo menos de 3
 *             errores. En caso de 4 errores su valor no tiene significado.
 */
unsigned char Hadamard_Regenerator(uint16_t codeword,unsigned char* errores)
{
    unsigned char i=0,j=0,cont=0;
    uint16_t aux = 0;
    uint8_t Hbit = 0, k = 0;

    while(1)
    {
        if(i == 16)
        {
            Hbit = 0x10;
            i = 0;
            codeword = ~codeword;
        }
        cont = 0;
        j = 0;
        aux = HadamardMatrix[i] ^ codeword;
        while(cont < 5 && j < 16)
        {
            if((aux >> j) & 1)
                cont++;
            j++;
        }
        *errores = cont;
        if(cont <= 4)
            return i|Hbit;
        i++;

        k++;
        if(k == 32)
        {
            Serial.print("ERROR K32");
            *errores = 4;
            return i|Hbit;
        }
    }
}

void MostrarHad(unsigned char Dato,unsigned char errores)
{
    if(Dato != 0)
    {
        if(errores < 4) // menos de 4 errores=> trama correcta.
        {
            Serial.print("Trama correcta: ");
            Serial.print(Dato);
            Serial.print("\t");
            Serial.print("Errores: ");
            Serial.print(errores);
            Serial.println("\t");
        }
        else
        {
            Serial.print("Trama incorrecta: ");
            Serial.print(Dato);
            Serial.print("\t");
            Serial.print("Errores: ");
            Serial.print(errores);
            Serial.println("\t");
        }
        switch(errores)
        {
            case 0: e_0++; break;
            case 1: e_1++; break;
            case 2: e_2++; break;
            case 3: e_3++; break;
            case 4: e_4++; break;
            default: break;
        }
    }
}

/**
 * @brief      Recibe del TDA por puerto serie informacion codificada en
 *             palabras de hadamard y recupera la informacion corrigiendo hasta
 *             3 errores. En caso de 4 errores descarta la trama y solo detecta.
 *
 * @param      Mensaje  Buffer de 3 bytes donde se carga los 3 datos recibidos:
 *                      ADD, CMD, Valor. Son numeros de 5 bits.
 *
 * @return     Devuelve 1 en caso de recibir y recuperar una trama de 3 palabras
 *             correctas. En caso de error o paso del timeout devuelve -1 y el
 *             contenido del buffer Mensaje no tiene validez.
 */
 #define    HADAMARD_TIMEOUT    0x1FFFFF  // Con este anda piola, pero lento
 //#define    HADAMARD_TIMEOUT    0x1F0FFF
int Hadamard_Rx(NodeMsj_t* RxMsj)
{
    unsigned char i = 0;
    unsigned char Rx_Buf[2] = {0,0};
    uint16_t trama;
    unsigned char Dato = 0, errores = 0;
    unsigned char EstadoTrama = 0, IndiceTrama = 0;
    uint32_t TimeoutTest = HADAMARD_TIMEOUT;
    unsigned char Mensaje[3];

    while(1)
    {
        switch(EstadoTrama)
        {
            case 0:
                if(tdaSerial.available() > 0)
                {
                    Rx_Buf[i] = tdaSerial.read();
                    i++;
                }
                if(i == 2)
                {
                    trama = Rx_Buf[0] | (Rx_Buf[1] << 8);
                    Dato = Hadamard_Regenerator(trama,&errores);
                    if(Dato == 0)
                    {
                        Rx_Buf[0] = Rx_Buf[1];
                        i = 1;
                    }
                    else
                    {
                        MostrarHad(Dato,errores); //Para debug solamente. Imprime por Serial.
                        i = 0;
                        if(errores <= 3)
                            EstadoTrama = 1;
                        else // Si hubo 4 errores tengo una trama invalida y devuelvo -1
                            return -1;
                            //Mandar NACK ¿?          
                    }
                }
                TimeoutTest--;
                if(!TimeoutTest) // Si pasa el tiempo descartar trama.
                {
                    Serial.println("Salio por timeout Hadamard");
                    return -1;
                }
                    
                break;

            case 1:
                Mensaje[IndiceTrama] = Dato;
                TimeoutTest = HADAMARD_TIMEOUT; // Si Logre una trama valida, reseteo el timeout.
                
                IndiceTrama++;
                if(IndiceTrama == 3) // Si llegaron 3 tramas validas consecutivas las cargo en
                {                   // el buffer "Mensaje" y devuelvo 1.
                    RxMsj->address = Mensaje[0];
                    RxMsj->command = Mensaje[1];
                    RxMsj->value = Mensaje[2];
                    return 1;
                }
                
                EstadoTrama = 0;
                break;

            case 2: // Analisis de CRC
                //En caso de agregar CRC
                break;
        }
    }
}
