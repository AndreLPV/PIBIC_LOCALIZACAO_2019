#include <ESP8266WiFi.h>

WiFiServer server(8090);
WiFiClient cliente;
IPAddress ip(192, 168, 0, 117);
IPAddress gateway(192, 168, 0, 1);
IPAddress subnet(255, 255, 255, 0);

int RSSI[8] = {0,0,0,0,0,0,0,0};

void setup() {
  Serial.begin(9600);
  WiFi.config(ip, gateway, subnet);
  WiFi.begin("CTL_WIFI", "");
  while (WiFi.status() != WL_CONNECTED) {
    delay(100);
  }
  server.begin();
  Serial.println("Connected");
  Serial.println(WiFi.localIP());
}

void atualiza(){
  int i;
  String req = "";
  while (cliente.available() > 0){
    char z = cliente.read();
    req += z;
  }
  Serial.println("scan start");
  int n = WiFi.scanNetworks();
  if (n != 0) {
    for (i = 0; i < n; ++i) { 
      if(WiFi.SSID(i) == "AP1") RSSI[0] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP2") RSSI[1] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP3") RSSI[2] = WiFi.RSSI(i);      
      if(WiFi.SSID(i) == "AP4") RSSI[3] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP5") RSSI[4] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP6") RSSI[5] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP7") RSSI[6] = WiFi.RSSI(i);
      if(WiFi.SSID(i) == "AP8") RSSI[7] = WiFi.RSSI(i);  
    }
    delay(10);
  }
  
}

void manda(){
  int i;
  bool flag = true;
  for (i = 0; i < 8; i = i + 1){
    if (RSSI[i] == 0) flag = false;
  }
  if(flag){
    for (i = 0; i < 8; i = i + 1){
      cliente.write(RSSI[i]);
      Serial.println(RSSI[i]);      
    }
  }
}

void loop() {
  cliente = server.available();
  atualiza();
  if (!cliente) {
    return;
  }
  Serial.println("Conectado");
  manda();
  
  delay(100);
  cliente.flush();
}
