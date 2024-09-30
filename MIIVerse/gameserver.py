from flask import Flask, request
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, cors_allowed_origins="http://localhost:8000")

players = {}

@socketio.on('connect')
def connect_handler():
    socket_id = request.sid  # Access socket ID here
    pid = request.args["player_id"]
    pcolor = request.args["player_color"]
    players[pid] = {"id":pid,
                    "x":0,
                    "z":0,
                    "angleY":0,
                    "angleX":0,
                    "color":pcolor}
    print(players)
    emit('players', players, broadcast=True )
        
    print(f"Client {pid} connected with socket ID: {socket_id}")

@socketio.on('players')
def players_handler(info):
    players[info["id"]] = {"id":info["id"],
                           "x":info["x"],
                           "z":info["z"],
                           "angleY":info["angleY"],
                           "angleX":info["angleX"],
                           "color":info["color"]}
    
    emit('players', players, broadcast=True )

if __name__ == '__main__':
    socketio.run(app, port=5000, log_output=False, debug=False)
