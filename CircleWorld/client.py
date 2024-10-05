from gamelib import *
import socketio

server_url = "http://127.0.0.1:5000"  
sio = socketio.Client()

@sio.event
def players(info):
    global members
    members = info

colors = {"black":black,"white":white,"red":red,"green":green,"blue":blue,"yellow":yellow,"magenta":magenta,"cyan":cyan,"orange":orange,"pink":pink,"brown":brown,"gray":gray}

game = Game(800,600,"Delta Fighter")
bk = Animation("field_5.png",5,game,1000,1000)
game.setBackground(bk)
ball = Shape("ellipse",game,50,50,orange)

you = {"id":0,"color":"white","x":0,"y":0}
members = {}
direction = ""
def player_controls():
    global direction
    direction = ""
    speed = 2
    if keys.Pressed[K_LEFT]:
        you["x"] -=speed
        direction += "right"
    if keys.Pressed[K_RIGHT]:
        you["x"] +=speed
        direction += "left"
    if keys.Pressed[K_UP]:
        you["y"] -=speed 
        direction += "down"
    if keys.Pressed[K_DOWN]:
        you["y"] +=speed
        direction += "up"
    info = {"id":you["id"], 
            "x":you["x"],
            "y":you["y"],
            "color":you["color"]
    }
    if direction != "":
        sio.emit('players', info)
    

def display_members():
    global members
    for key in members:
        player = members[key]
        screenX = player["x"] - you["x"] + game.width / 2
        screenY = player["y"] - you["y"] + game.height / 2
        if player["id"] == you["id"]:
            screenX = game.width / 2
            screenY = game.height / 2
        width,height = 25,25
        left, top  = screenX-width/2,screenY-height/2
        rect = pygame.Rect(left,top,width,height)

        color = player["color"] if player["color"] in colors else white
        pygame.draw.ellipse(game.screen,color,rect)
        offset = len(player["id"])*4.5
        game.drawText(player["id"],screenX - offset,screenY - 40, Font(color))
        offset = len(f"({player['x']},{player['y']})")*3.2
        game.drawText(f"({player['x']},{player['y']})",screenX-offset,screenY + 30,Font(color))
          
def game_screen():
    global direction
    while not game.over:
        game.processInput()
        game.scrollBackground(direction)
        player_controls()
        display_members()
        game.update(60)
    game.quit()

if __name__ == "__main__":
    you["id"] = input("Enter Name: ")
    color = input("Enter Color: ")
    you["color"] = color if color in colors else "white"
    if color != you["color"]: print(f"{color} replaced with {you['color']}")
    sio.connect(server_url+f"?id={you['id']}&color='{you['color']}'")
    game_screen() 
    sio.wait()  