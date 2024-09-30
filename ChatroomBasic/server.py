import socket               

s = socket.socket()         
host = "192.168.1.170"
port = 12345                

s.bind((host, port))        
s.listen(5)
c, addr = s.accept()

print ('Got connection from', addr)

msg = ""
while msg != "q":
   print (c.recv(1024).decode('ascii')) 
   msg = input(">")
   c.send(bytes(msg, encoding="ascii"))
c.close()      
