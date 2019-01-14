#ifdef ESP8266//Se estiver usando ESP8266, automáticamente irá adicionar as bibliotecas do ESP8266.
#include <ESP8266WiFi.h>
#include <WiFiServer.h>
#elif defined ESP32//Se estiver usando ESP32, fara a mesma operaçao.
#include <WiFi.h>
#endif
 
WiFiServer sv(555);//Cria o objeto servidor na porta 555
WiFiClient cl;//Cria o objeto cliente.
int RSSI[8] = {0,0,0,0,0,0,0,0};

void setup()
{
    Serial.begin(9600);//Habilita a comm serial.
 
    WiFi.mode(WIFI_AP);//Define o WiFi como Acess_Point.
    WiFi.softAP("NodeMCU", "");//Cria a rede de Acess_Point.
    sv.begin();//Inicia o servidor TCP na porta declarada no começo.
}
 
void loop(){
    tcp();//Funçao que gerencia os pacotes e clientes TCP.
}

void teste2(){
  int i;
  String req = "";
  bool flag = true;
  while (cl.available() > 0){
    char z = cl.read();
    req += z;
  }
  Serial.println("scan start");
  int n = WiFi.scanNetworks();
  if (n != 0) {
    for (i = 0; i < n; ++i) { 
      if(WiFi.SSID(i) == "AP1"){
        RSSI[0] = WiFi.RSSI(i);
        Serial.println("AP1:"+RSSI[0]);
      }
      if(WiFi.SSID(i) == "AP2"){
        RSSI[1] = WiFi.RSSI(i);
        Serial.println("AP2:"+RSSI[1]);
      }
      if(WiFi.SSID(i) == "AP3"){
        RSSI[2] = WiFi.RSSI(i);  
        Serial.println("AP3:"+RSSI[2]);
      }
      if(WiFi.SSID(i) == "AP4"){
        RSSI[3] = WiFi.RSSI(i);
        Serial.println("AP4:"+RSSI[3]);
      }
      if(WiFi.SSID(i) == "AP5"){
        RSSI[4] = WiFi.RSSI(i);
        Serial.println("AP5:"+RSSI[4]);
      }
      if(WiFi.SSID(i) == "AP6"){
        RSSI[5] = WiFi.RSSI(i);
        Serial.println("AP6:"+RSSI[5]);
      }
      if(WiFi.SSID(i) == "AP7"){
        RSSI[6] = WiFi.RSSI(i);
        Serial.println("AP7:"+RSSI[6]);
      }
      if(WiFi.SSID(i) == "AP8"){
        RSSI[7] = WiFi.RSSI(i);
        Serial.println("AP8:"+RSSI[7]);
      }
    }
    delay(10);
  }
  for (i = 0; i < 8; i = i + 1){
    if (RSSI[i] == 0) flag = false;
  }
  if(flag){
    Serial.println("Tudo diferente de zero");
    for (i = 0; i < 8; i = i + 1){
      cl.write(RSSI[i]);
      RSSI[i] = 0;
    }
  }
}
 
void tcp(){
  if (cl.connected()){
    if (cl.available() > 0){
      teste2();
    }
  }else{
    cl = sv.available();//Disponabiliza o servidor para o cliente se conectar.
    delay(1);
  }
}
