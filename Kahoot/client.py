import socketio
import os

name = input("Enter name:")
server_url = f"http://127.0.0.1:5000?name={name}"  
sio = socketio.Client()

@sio.event
def status(data):
    print(data)

@sio.event
def game(data):
    os.system("cls")
    displayPlayers(data["players"])
    if "question" in data:
      displayQuestion(data["question"])
      choice = input()
      sio.emit("game", choice)
    elif "over" in data:
      print("Game Over") 
      sio.disconnect()
    else:
      print("Waiting....")
    
def displayPlayers(data):
    print("_"*10+"Players"+"_"*10)
    for player in data:
       print(f"{player['points']}\t{player['name']}")
    print("_"*27)

def displayQuestion(data):
   print(data["question"] + "\n" + "="*20)
   label = 1
   for choice in data["choices"]:
      print(f"{label}) {choice}")
      label += 1

if __name__ == "__main__":
  sio.connect(server_url)

  sio.wait()  
