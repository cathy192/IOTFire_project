import socket

#HOST='192.168.137.137'
HOST=''
PORT=12346
s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
print 'Socket created'

try:
    s.bind((HOST,PORT))
except socket.error:
    print 'Bind failed'

s.listen(5)
print 'Socket awaiting messages'
(conn,addr)=s.accept()
print 'Connected'

while True:
    data = conn.recv(1024)
    print 'I send '+data
    reply=''

    if data=='Hello':
        reply = 'Hi,back!'
    elif data=='This is important':
        reply = 'Ok, I have done the important thing you have asked me!'
    elif data=='quit':
        conn.send('Terminating')
        break
    else:
        reply = 'Unknown command'

    conn.send(reply)

conn.close()
