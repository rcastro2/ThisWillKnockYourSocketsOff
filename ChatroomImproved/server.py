from flask import Flask, request
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'

socketio = SocketIO(app, cors_allowed_origins="http://localhost:8000")

clients = {}

@socketio.on('connect')
def connect_handler(info):
    print(info)
    socket_id = request.sid  # Access socket ID here
    name = request.args["name"]
    clients[socket_id] = name
    print(clients)

@socketio.on('message')
def message_handler(info):
    data = {"info":info,"sender":clients[request.sid]}
    emit('message', data, broadcast=True )

if __name__ == '__main__':
    socketio.run(app, port=5000, host="0.0.0.0", log_output=False, debug=True)
