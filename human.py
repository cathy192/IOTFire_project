import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
PIR=17

GPIO.setup(PIR,GPIO.IN)
try:
    while True:
        if GPIO.input(PIR):
            t=time.localtime()
            print" %d:%d:%d motion detected"%(t.tm_hour,t.tm_min,t.tm_sec)
        else:
            print"No motion"

        time.sleep(2)
except keyboardInterrupt:
    print"quit"
    GPIO.cleanup()
