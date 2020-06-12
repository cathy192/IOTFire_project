import RPi.GPIO as GPIO
import flame.py 
import COgas.py
import time

sensor[2]={COlevel,LPGlevel};
#fire=flamelevel
sum=0
while True:
    for i in sensor:
            if (COlevel>=74)or((LPGlevel<=2.1)or(LPGlevel>=10)):
                sum+=sensor[i]
            
    if sum>0:
        if sum>2 and (fire>1000 or temp>60):
            print"fire"
        elif sum==1:
            print"danger"
    elif sum==0:
        print"normal"

     
