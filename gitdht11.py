import Adafruit_DHT
import time

sensor=Adafruit_DHT.DHT11
pin=4
humidity,temperature=Adafruit_DHT.read_retry(sensor,pin)

while True:
    if humidity is not None and temperature is not None:
        print('Temp={0:0.1f}*C Humidity={1:0.1f}%'.format(temperature,humidity))
    else:
        print('Failed to get reading')
    time.sleep(0.5)

