import logging
from flask import Flask, request
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'

socketio = SocketIO(app, cors_allowed_origins="http://localhost:8000")
questions = [
    {"question":"What is the capital of NY?",
     "choices":["Manhattan","Buffalo","Albany","Syracuse"]},
    {"question":"How many sides to a dodecahedron?",
     "choices":["12","10","8","16"]},
    {"question":"Where is your tibia located?",
     "choices":["Chest","Arm","Hip","Leg"],
    }
]
answers = [3,1,4]
players = {"sid":[],"info":{}}
game = {"current": 0,"value": 4, "responses":0, "responded":[]}

@socketio.on("connect")
def connect_handler():
    socket_id = request.sid  
    name = request.args["name"]
    if name != "null":
        if len(players["info"]) < 3:
            players["info"][socket_id] = {"name":name,"points":0}
            players["sid"].append(socket_id)
            print(len(players["info"]))
            print(players)
            current_players = [ player for player in players["info"].values() ]
            data = {"players":current_players}
            if len(players["info"]) == 3:
                data["question"] = questions[game["current"]]

            emit('game',data, to=players["sid"])
        else:
            emit('status',"Game closed",to=socket_id)

@socketio.on('game')
def game_handler(response):
    if answers[game["current"]] == int(response):
        players["info"][request.sid]["points"] += game["value"]
        game["value"] -= 1
    game["responses"] += 1
    game["responded"].append(request.sid)
    current_players = [ player for player in players["info"].values() ]
    data = {"players":current_players}
    if game["responses"] == 3 and game["current"] < len(questions)-1:
        game["current"] += 1
        game["responses"] = 0
        game["value"] = 4
        game["responded"] = []
        data["question"] = questions[game["current"]]
        emit('game',data, broadcast=True)
    elif game["responses"] == 3 and game["current"] == len(questions)-1:
        data["over"] = True
        emit('game',data, to=game["responded"])
    else:
        emit('game',data, to=game["responded"])

if __name__ == '__main__':
    log = logging.getLogger('werkzeug')
    log.setLevel(logging.ERROR)  
    app.logger.setLevel(logging.ERROR)  
    socketio.run(app, port=5000, host="0.0.0.0", log_output=False, debug=True)
