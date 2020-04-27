
#include <DHT11.h>
int DHTPIN=11;
DHT11 dht11(DHTPIN);

void setup(){
Serial.begin(9600);
}
void loop(){
 float humi,temp;
 int i;
 if((i=dht11.read(humi,temp))==0){
  Serial.print("humidity:");
  Serial.print(humi);
  Serial.print("temperature:");
  Serial.println(temp);
 }
 else{
  Serial.print("Error");
 }
 delay(1000);
}
