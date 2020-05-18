import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BCM)
FLAME=14
LED=15
GPIO.setup(FLAME,GPIO.IN)
GPIO.setup(LED,GPIO.OUT)
try:
    while True:
        flame=GPIO.input(FLAME)
        if flame==0:
            GPIO.output(LED,GPIO.HIGH)
        else:
            GPIO.output(LED,GPIO.LOW)
        time.sleep(0.5)
finally:
    GPIO.cleanup()
