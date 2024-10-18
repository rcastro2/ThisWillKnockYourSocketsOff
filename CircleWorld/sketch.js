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
  bk.scroll(direction,speed);
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
  constructor(bkGraphics){
    this.background = bkGraphics;
    this.backgroundXY = [[],[],[]]
    for(let r = 0; r < 3; r++){
      for(let c = 0; c < 3; c++){
        this.backgroundXY[r].push({"x":this.background.width * (c-1) + this.background.width / 2,"y":this.background.height * (r-1) + this.background.height / 2})
      }
    }
  }
  scroll(direction,amt){
    if(direction.includes("left"))
      for(let r = 0; r < 3; r++)
        for(let c = 0; c < 3; c++){
          if(this.backgroundXY[r][c]["x"] + this.background.width  / 2 <= 0){
            this.backgroundXY[r][c]["x"] = this.backgroundXY[r][mod((c+2),3)]["x"] + this.background.width 
          }  
          this.backgroundXY[r][c]["x"] -= amt
        }
    if(direction.includes("right"))
      for(let r = 0; r < 3; r++)
        for(let c = 2; c >= 0; c--){
          if(this.backgroundXY[r][c]["x"] - this.background.width  / 2 >= width){
            this.backgroundXY[r][c]["x"] = this.backgroundXY[r][mod((c-2),3)]["x"] - this.background.width 
          }  
          this.backgroundXY[r][c]["x"] += amt
        }
    if(direction.includes("up"))
      for(let c = 0; c < 3; c++)
        for(let r = 0; r < 3; r++){
          if(this.backgroundXY[r][c]["y"] + this.background.height  / 2 <= 0){
            this.backgroundXY[r][c]["y"] = this.backgroundXY[mod((r+2),3)][c]["y"] + this.background.height 
          }  
          this.backgroundXY[r][c]["y"] -= amt
        }
    if(direction.includes("down"))
      for(let c = 0; c < 3; c++)
        for(let r = 2; r >= 0; r--){
          if(this.backgroundXY[r][c]["y"] - this.background.height  / 2 >= height){
            this.backgroundXY[r][c]["y"] = this.backgroundXY[mod((r-2),3)][c]["y"] - this.background.height 
          }  
          this.backgroundXY[r][c]["y"] += amt
        }
    for(let r = 0; r < 3; r++)
      for(let c = 0; c < 3; c++)   
        this.background.moveTo(this.backgroundXY[r][c]["x"],this.backgroundXY[r][c]["y"])
  } 
}

function mod(a, b) {
  c = a % b
  return (c < 0) ? c + b : c
}




