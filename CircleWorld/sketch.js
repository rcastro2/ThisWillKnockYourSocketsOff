let players = {},you = {id:0,color:"white",x:0,y:0},speed = 2, ready = false;

function connectSockets(){
  you["id"] = prompt("Enter Name")
  you["color"] = prompt("Enter a color")
  socket = io.connect(`http://127.0.0.1:5000?id=${you["id"]}&color='${you["color"]}'`);
  socket.on('players', function(info){
    //console.log(info);
    players = info;
    ready = true;
  });
}
function preload(){
  connectSockets();
  starfield = loadImage("field_5.png")
}

function setup() {
  createCanvas(windowWidth, windowHeight);
  imageMode("center");
  rectMode("center"); 
  textAlign(CENTER);
  textSize(14);
  bk = new Background(new Animation(starfield,5,1000,1000,1));
}
function windowResized() {
  resizeCanvas(windowWidth, windowHeight);
}

function draw() {
  if(!ready) return;
  let direction = "";
  if (keyIsDown(UP_ARROW)) {
    you.y-=speed; 
    direction += "down";
  }
  if (keyIsDown(DOWN_ARROW)) {
    you.y+=speed;
    direction += "up";
  }
  if (keyIsDown(LEFT_ARROW)) {
    you.x-=speed;
    direction += "right";
  }
  if (keyIsDown(RIGHT_ARROW)) {
    you.x+=speed;
    direction += "left";
  }
  info = {id:you.id, 
    x:you.x,
    y:you.y,
    color:you.color
  };
  if(direction != ""){
    socket.emit('players', info);
  }
  bk.scroll(direction);
  for(let key in players){
    //Draw each player relative to you
    let screenX = players[key].x - you.x + width / 2;
    let screenY = players[key].y - you.y + height / 2;
    if(key == you.id){
      screenX = width / 2;
      screenY = height / 2;
    }
    fill(players[key].color)
    circle(screenX, screenY, 25);
    text(players[key].id, screenX, screenY - 15);
    text(`(${players[key].x},${players[key].y})`, screenX, screenY + 30);
  }
}

class Background{
  constructor(image){
    this.image = image;
    this.width = this.image.width;
    this.height = this.image.height;
    this.backgroundXY = []
    //Tile the image on a 3 x 3 grid
    for(var i = 0; i < 9; i++){
      this.backgroundXY.push({"x":i % 3 * this.width,"y":Math.floor(i / 3) * this.height });
    }
  }
  scroll(direction){
    for(var i = 0; i < 9; i++){
      if(direction.includes("left")){
        this.backgroundXY[i].x-=speed;
        if(this.backgroundXY[i].x + this.width / 2 < 0){
          this.backgroundXY[i].x = this.backgroundXY[(i+2)%3].x + this.width ;
        }
      }
      if(direction.includes("right")){
        this.backgroundXY[i].x+=speed;
        if(this.backgroundXY[i].x - this.width / 2 > width){
          this.backgroundXY[i].x = this.backgroundXY[(i+2)%3].x - this.width;
        }
      }
      if(direction.includes("up")){
        this.backgroundXY[i].y-=speed;
        if(this.backgroundXY[i].y + this.height / 2 < 0){
          this.backgroundXY[i].y = this.backgroundXY[(i+2)%3].y + this.height;
        }
      }
      if(direction.includes("down")){
        this.backgroundXY[i].y+=speed;
        if(this.backgroundXY[i].y - this.height / 2 > height){
          this.backgroundXY[i].y = this.backgroundXY[(i+2)%3].y - this.height;
        }
      }
      this.image.x = this.backgroundXY[i].x
      this.image.y = this.backgroundXY[i].y
      this.image.draw();
    }
  } 
}




