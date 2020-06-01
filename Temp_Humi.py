import Adafruit_DHT
import time

sensor=Adafruit_DHT.DHT11
pin=4

try:
    while True:
        humidity,temperature=Adafruit_DHT.read_retry(sensor,pin)
        if humidity is not None and temperature is not None:
            print"Temp:",temperature,"C Humidity:",humidity,"%"
        time.sleep(0.5)

finally:
    print"Program Exit"
