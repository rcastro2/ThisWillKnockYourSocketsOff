import socket               

s = socket.socket() 
host = "192.168.1.170"
port = 12345             

s.connect((host, port))
msg = ""
while msg != "q": 
    msg = input(">")
    s.send(bytes(msg, encoding="ascii"))
    print (s.recv(1024).decode('ascii'))
s.close()
