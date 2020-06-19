import RPi.GPIO as GPIO
import time
import Adafruit_DHT
from mq import *
import sys
import os

sensor=Adafruit_DHT.DHt11
t_pin=4
DO_pin = 21
AO_pin = 2#fire flame pin
#flame sensor AO connected to ADC chanannel 0
# change these as desired - they're the pins connected from the
# SPI port on the ADC to the Cobbler
SPICLK = 11
SPIMISO = 9
SPIMOSI = 10
SPICS = 8
flame_value=0
#port init
def init():
          GPIO.setwarnings(False)
          GPIO.cleanup()
          GPIO.setmode(GPIO.BCM)
          #GPIO.output(buzzer,GPIO.HIGH)
          GPIO.setup(AO_pin,GPIO.IN)
          GPIO.setup(DO_pin,GPIO.IN,pull_up_down=GPIO.PUD_UP)
          # set up the SPI interface pins
          GPIO.setup(SPIMOSI, GPIO.OUT)
          GPIO.setup(SPIMISO, GPIO.IN)
          GPIO.setup(SPICLK, GPIO.OUT)
          GPIO.setup(SPICS, GPIO.OUT)
          pass

#read SPI data from MCP3008(or MCP3204) chip,8 possible adc's (0 thru 7)
def readadc(adcnum, clockpin, mosipin, misopin, cspin):
        if ((adcnum > 7) or (adcnum < 0)):
                return -1
        GPIO.output(cspin, True)  

        GPIO.output(clockpin, False)  # start clock low
        GPIO.output(cspin, False)     # bring CS low

        commandout = adcnum
        commandout |= 0x18  # start bit + single-ended bit
        commandout <<= 3    # we only need to send 5 bits here
        for i in range(5):
                if (commandout & 0x80):
                        GPIO.output(mosipin, True)
                else:
                        GPIO.output(mosipin, False)
                commandout <<= 1
                GPIO.output(clockpin, True)
                GPIO.output(clockpin, False)

        adcout = 0
        # read in one empty bit, one null bit and 10 ADC bits
        for i in range(12):
                GPIO.output(clockpin, True)
                GPIO.output(clockpin, False)
                adcout <<= 1
                if (GPIO.input(misopin)):
                        adcout |= 0x1

        GPIO.output(cspin, True)
        
        adcout >>= 1       # first bit is 'null' so drop it
        return adcout

def main():
         init()
         time.sleep(2)
         print"will detect sonud and light signal"
         sum=0
         os.system("sh streaming.sh")
         while True:
                  flame_value = readadc(AO_pin, SPICLK, SPIMOSI, SPIMISO, SPICS)
                  humidity,temperature=Adafruit_DHt.read_retry(sensor,t_pin)
                  perc=mq.MQPercentage()
                  lpg=perc["GAS_LPG"]
                  co=perc["CO"]
                  smoke=perc["SMOKE"]
                  lng=0
                  gas_sensor={co,lpg,smoke,lng}
                  html=urllib.urlopen("https://api.thingspeak.com/update?api_key=17LJP4VT3R02XCAQ&field1="+str(temperature)+"&field2="+str(humidity)+"&field3="+str(co)+"&field4="+str(lpg)+"&field5="+str(smoke)+"&field6="+str(flame_value))
                  if gas_sensor[0]>=74:
                      sum=sum+1
                      gaslist.append(gas_sensor[0])
                  elif gas_sensor[1]<=2.1 or gas_sensor[1]>=10:
                      sum=sum+1
                      gaslist.append(gas_sensor[1])
                  elif gas_sensor[2]>=5 and gas_sensor[2]<=15:
                      sum=sum+1
                      gaslist.append(gas_sensor[2])
                  elif gas_sensor[3]<=5 or gas_sensor[3]>=15:
                      sum=sum+1
                      gaslist.append(gas_sensor[3])

                  if sum>0:
                      if fire>=150 or temp>=60:
                          print("fire!")
                          print(gaslist)
                      else:
                          print("danger!")
                          print(gaslist)
                  else:
                      print("Normal")

                  #if flame_value!=1023:
                   #         print"***********"
                    #        print"* Fire *"
                     #       print"fire AD is:"+str(1023-flame_value)
                      #      print' '
                       #     time.sleep(0.5)
                 # else:
                  #          print"***********"
                   #         print"* No Fire*"
                            #print"fire AD is:"+str(flame_value)
                           # print "fire ADC value is: " + str("%.1f"%((1024-flame_value)/1024.*3.3))+"V"
                    #        print"***********"
                     #       print' '  
                      #      time.sleep(0.5)
                            
if __name__ =='__main__':
         try:
                  main()
         except KeyboardInterrupt:
                  pass
 
          
          
