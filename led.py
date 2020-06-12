import bluetooth
import socket
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setup(3,GPIO.OUT)
GPIO.setup(2,GPIO.OUT)
GPIO.setup(4,GPIO.OUT)
GPIO.setup(17,GPIO.OUT)
GPIO.setup(27,GPIO.OUT)
server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_socket.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)
port=1
#server_socket.close()
server_socket.bind(('127.0.0.1',port))
server_socket.listen(1)
#server_socket.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)
cnt1=0
cnt2=0
cnt3=0
cnt4=0
cnt5=0
client_socket,address=server_socket.accept()
print("Accepted connection from",address)

while 1:
    data=client_socket.recv(1024)
    print("Received:%s"%data)
    if(data=="0"):
        GPIO.output(3,False)
        GPIO.output(2,False)
        GPIO.output(4,False)
        GPIO.output(17,False)
        GPIO.output(27,False)
        cnt1=0
        cnt2=0
        cnt3=0
        cnt4=0
        cnt5=0
    elif(data=="1"):
        if(cnt1==0):
            GPIO.output(3,True)
            cnt1=cnt1+1
        elif(cnt1==1):
            GPIO.output(3,False)
            cnt1=0
    elif(data=="2"):
        if(cnt2==0):
            GPIO.output(2,True)
            cnt2=cnt2+1
        elif(cnt2==1):
            GPIO.output(2,False)
            cnt2=0
    elif(data=="3"):
        if(cnt3==0):
            GPIO.output(4,True)
            cnt3=cnt3+1
        elif(cnt3==1):
            GPIO.output(4,False)
            cnt3=0
    elif(data=="4"):
        if(cnt4==0):
            GPIO.output(17,True)
            cnt4=cnt4+1
        elif(cnt4==1):
            GPIO.output(17,False)
            cnt4=0
    elif(data=="5"):
        if(cnt5==0):
            GPIO.output(27,True)
            cnt5=cnt5+1
        elif(cnt5==1):
            GPIO.output(27,False)
            cnt5=0
    elif(data=="q"):
        print("Quit")
        break
client_socket.close()
server_socket.close()


