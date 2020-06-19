import RPi.GPIO as GPIO
from COgas import COlevel
from COgas import LPGlevel
import time
#from Temp_Humi import temperature
from flame import flame_value
#from example import gas_co
#from example import gas_lpg
#from example import gas_smoke

co=COlevel
lpg=LPGlevel
smoke=0
sensor={co,lpg,smoke}
fire=flame_value
temp=0
print(COlevel)
print(LPGlevel)
#print(gas_smoke)
#print(temperature)
print(flame_value)
sum=0
for i in range(3):
    if co>=74 or (lpg <=2.1 or lpg>=10) or (smoke<15 and smoke>5):
        sum=sum+1

if sum>0:
    if sum>2 and (fire>1000 or temp>60):
        print("fire!")
    elif sum==1:
        print("Danger!")
else:
    print("normal")
