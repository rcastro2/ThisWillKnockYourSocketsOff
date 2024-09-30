import socketio

server_url = "http://127.0.0.1:5000"  
sio = socketio.Client()

@sio.event
def message(data):
    if data['sender'] != name:
      print(f"\t\t\t\t\t\t{data['sender']}: {data['info']}")

if __name__ == "__main__":
  name = input("Enter Name:")
  sio.connect(server_url+f"?name={name}")
  msg = ""
  while msg != "q":
    msg = input("")
    sio.emit("message", msg)  
  sio.wait()  
