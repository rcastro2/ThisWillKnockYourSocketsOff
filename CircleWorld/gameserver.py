import logging
from flask import Flask, request
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, cors_allowed_origins="http://localhost:8000")

players = {}

@socketio.on('connect')
def connect_handler():
    id = request.args["id"]
    color = request.args["color"]
    if id != "null":
        players[id] = {"id":id,
                        "color":color,
                        "x":0,
                        "y":0
                        }  
        emit('players', players, broadcast=True )

        socket_id = request.sid  
        print(f"Client (${id}) connected with socket ID: ${socket_id}")

@socketio.on('players')
def players_handler(info):
    players[info["id"]] = {"id":info["id"],
                           "x":info["x"],
                           "y":info["y"],
                           "color":info["color"]}  
    #print(players)
    emit('players', players, broadcast=True )

if __name__ == '__main__':
    #Suppress Flask messages
    log = logging.getLogger('werkzeug')
    log.setLevel(logging.ERROR)  
    app.logger.setLevel(logging.ERROR)
    socketio.run(app, port=5000, log_output=False, debug=False)
