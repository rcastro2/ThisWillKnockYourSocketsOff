This Will Knock Your Sockets Off
================================
<h2>Overview</h2>
<p>
Projects explores socket programming as part of a workshop delivered through Math for America. Sockets create bi-directional communication channels between clients and a server for real time communication.  All examples use Python to create a socket server and corresponding technology to create client applications. The Python socket server program must be running first before clients can connect.  Start each client program using the appropriate environment.  Web based clients must be started using a web server.  You may use python -m http.server to start up a web server in the folder that contains the web based client. Web and socket servers are different. For Java clients, <a href="https://www.tutorialspoint.com/maven/index.htm" target="_blank">Maven</a> was used to build the application.  You may run the Java clients using Maven or Java directly on the .jar file produced.

Below are a few resources used to research socket programming.
</p>
<a target="_blank" href="https://socket.io/">socket.io</a><br>
<a target="_blank" href="https://python-socketio.readthedocs.io/en/stable/index.html">
python-socketio</a><br>
<a target="_blank" href="https://flask-socketio.readthedocs.io/en/latest/index.html">
flask-socketio</a><br>

<hr>
<h2>Chatroom Basic</h2>
<p>
This project explores the most fundamental application of socket programming using the socket library included with the standard installation of Python.
The project demonstrates a turn based chatroom where messages are passed between users at the server and client.  There are several limitations with this project and its demonstration of socket communication.  SocketIO library which is demonstrated in the succeeding projects improves on these limitations. 
</p>
<hr>
<h2>Chatroom Improved</h2>
<p>
This project improves on the Chatroom Basic project by providing an asynchronous approach to the chatroom communication where one client does not have to wait for a response before sending another message.  Project provides the ability to connect multiple clients together through the Python server.  Both the server and the clients use an additional library, SocketIO to accomplish this improvement. Unlike the Chatroom Basic project, the server merely broadcasts the message from one client to all other clients.  This project demonstrates the ability for clients that use different programming languages to communicate with each other. One client was programmed using Python, another client was programmed using Java and last client was programmed using web based technologies. 
</p>
<hr>
<h2>Circle World</h2>
<p>
This project demonstrates a multiplayer visual application using p5js, pygame and socket programming. p5js and pygame are visual libraries for rendering graphics using web based technologies and Python respectively. In this project, each player is a circle in the world and the position of each player is synchronized with all other players through the server.  There are two client applications, one client application uses Python and Pygame while the other client uses web based technologies and p5js.  There are also two additional libraries included, gamelib.py and gamelib_p5.js to assist with the rendering of the graphics in the project.  The Python and web based clients can "play" together!
</p>
<hr>
<h2> MIIVerse </h2>
<p>
This project demonstrates a multiplayer visual application using socket programming and AFrame.io, a web based framework for building 3D, virtual and augmented reality experiences in the browser.  Each player is a 3D character in the world with the ability to move around and view other players.  From the Python server point of view, this project is practically identical to Circle World where the purpose of the server is merely to synchronized the positions all players. Players can also "look" up and down which tilts the head of their character.  The tilting of each player's head is also communicated to all other players through the server.
</p>
<hr>
<h2>Kahoot</h2>
<p>
This project highlights the ability for the server to take a more "active" role in the communication with clients.  Kahoot is a quiz based game where players earned points by how quickly they are able to answer the questions correctly.  The Python server takes on the role of a "gameshow" host where the server presents the question to everyone and awards points accordingly based on the order that correct responses were submitted by players.  Unlike the previous projects, clients do not communicate with each other.
</p>