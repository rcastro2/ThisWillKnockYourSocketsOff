package com.example;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

class Keys {
    public static boolean[] pressed = new boolean[200];
    public static int LEFT = 37, RIGHT = 39, UP = 38, DOWN = 40, A = 65, 	B = 66, 	C = 67, 	D = 68, 	E = 69, 	F = 70, 	G = 71, 	H = 72, 	I = 73, 	J = 74, 	K = 75, 	L = 76, 	M = 77, 	N = 78, 	O = 79, 	P = 80, 	Q = 81, 	R = 82, 	S = 83, 	T = 84, 	U = 85, 	V = 86, 	W = 87, 	X = 88, 	Y = 89, 	Z = 90, SPACE = 32, ENTER = 10, D0 = 48, D1 = 49, D2 = 50, D3 = 51, D4 = 52, D5 = 53, D6 = 54, D7 = 55, D8 = 56, D9 = 57, PLUS = 43, MINUS = 44, SHIFT = 16, CTRL = 17, ALT = 18;
}

class Mouse {
    public static int x;
    public static int y;
    public static boolean leftClick;
    public static boolean rightClick;
    public static boolean leftPressed;
    public static boolean rightPressed;
}

class Font {
    public java.awt.Font font;
    public Color color;
    public Color shadow;

    public Font(String family, int size) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = Color.BLACK;
        this.shadow = null;
    }
    public Font(String family, int size, Color color) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = color;
        this.shadow = null;
    }
    public Font(String family, int size, Color color, Color shadow) {
        this.font = new java.awt.Font(family, java.awt.Font.BOLD, size);
        this.color = color;
        this.shadow = shadow;
    }
}

class Coordinate{
    public int x;
    public int y;
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class Sound {  
    private Clip clip;
    
    public Sound(String soundFileName) {
       try {
          URL url = this.getClass().getClassLoader().getResource(soundFileName);
          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
          clip = AudioSystem.getClip();
          clip.open(audioInputStream);
          setVolume(0);
          clip.start();
       } catch (UnsupportedAudioFileException e) {
          e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       } catch (LineUnavailableException e) {
          e.printStackTrace();
       }
    }
    public void play() {
        clip.loop(1);
    }
    public void setVolume(double level) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (volume != null) {
            volume.setValue((int)(level / 100));     
        }
    } 
}

class Game{
    public static Graphics2D canvas;
    public static int width;
    public static int height;
    public static Sprite background;
    public static Coordinate[][] backgroundXY = new Coordinate[3][3];

    public static void drawText(String msg, int x, int y){
        canvas.drawString(msg,x,y);
    }
    public static void drawText(String msg, int x, int y, Font f){
        canvas.setFont(f.font);
        if(f.shadow != null){
            canvas.setColor(f.shadow);
            canvas.drawString(msg,x + 2,y + 2);
        }
        canvas.setColor(f.color);
        canvas.drawString(msg,x,y);
    }

    public static int mod(int a, int b) {
        //Mimic how Python treats modulus of negative numbers
        int c = a % b;
        return (c < 0) ? c + b : c;
    }

    public static void setBackground(Sprite bkGraphics){
        background = bkGraphics;
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
              backgroundXY[r][c] = new Coordinate(background.width * (c-1) + background.width / 2,background.height * (r-1) + background.height / 2);
            }
        }
    }
    public static void scrollBackground(String direction, int amt){
        int width = background.width;
        int height = background.height;

        if(direction.contains("left")){
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(backgroundXY[r][c].x + width  / 2 <= 0){
                        backgroundXY[r][c].x = backgroundXY[r][mod((c+2),3)].x + width;
                    }  
                    backgroundXY[r][c].x -= amt;
                }
            }
        }
        if(direction.contains("right")){
            for(int r = 0; r < 3; r++){
                for(int c = 2; c >= 0; c--){
                    if(backgroundXY[r][c].x - width  / 2 >= Game.width){
                        backgroundXY[r][c].x = backgroundXY[r][mod((c-2),3)].x - width;
                    }  
                    backgroundXY[r][c].x += amt;
                }
            }
        }
        if(direction.contains("up")){
            for(int c = 0; c < 3; c++){
                for(int r = 0; r < 3; r++){
                    if(backgroundXY[r][c].y + height  / 2 <= 0){
                        backgroundXY[r][c].y = backgroundXY[mod((r+2),3)][c].y + height;
                    }  
                    backgroundXY[r][c].y -= amt;
                }
            }
        }
        if(direction.contains("down")){
            for(int c = 0; c < 3; c++){
                for(int r = 2; r >= 0; r--){
                    if(backgroundXY[r][c].y - height  / 2 >= Game.height){
                        backgroundXY[r][c].y = backgroundXY[mod((r-2),3)][c].y - height;
                    }  
                    backgroundXY[r][c].y += amt;
                }
            }
        }
        for(int r = 0; r < 3; r++)
            for(int c = 0; c < 3; c++){
                background.moveTo(backgroundXY[r][c].x,backgroundXY[r][c].y);
            }           
    }

}

abstract class GameObject{
    public int x, y;
    public int width, height;
    public int left,right,top,bottom;
    public boolean visible;
    public String borderType;

    public GameObject(){
        this.x = (int)(Game.width / 2);
        this.y = (int)(Game.height / 2);
        this.visible = true;
        this.borderType = null;
    }

    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
        draw();
    }

    abstract public void draw();
    
    public boolean collidedWith(GameObject obj, String shape){
        boolean collision = false;
        if(obj.visible){
            if(shape.equals("circle")){
                double dx = this.x - obj.x;
                double dy = this.y - obj.y;
                double d = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                collision = d < (int)((this.width/2+this.height/2)/2) + (int)((obj.width/2+obj.height/2)/2);
            }else if(shape.equals("rect")){
                collision = !(obj.left > this.right || obj.right < this.left || obj.top > this.bottom || obj.bottom < this.top);
            }
        }
        return collision;
    }

    protected void drawBoundaries(){
        if(borderType != null && borderType.equals("circle")){
            Game.canvas.drawOval(this.x - this.width/2,this.y - this.height/2,this.width,this.height);
        }else if (borderType != null && borderType.equals("rect")){
            Game.canvas.drawRect(this.x - this.width/2,this.y - this.height/2,this.width,this.height);
        }
    }
    public void updateRect(){
        int width = this.width;
        int height = this.height;
  
        this.left = this.x - width/2;
        this.top = this.y - height/2;
        this.right = this.x + width/2;
        this.bottom = this.y + height/2;
    }
}

class Sprite extends GameObject{
    protected Image i;

    public Sprite(String fn) {
        URL url = getClass().getClassLoader().getResource(fn);
        //System.out.println(url);
        if (url != null) {
          this.i = new ImageIcon(url).getImage();
          this.width = this.i.getWidth(null);
          this.height = this.i.getHeight(null);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
        
    }
    public Sprite(String fn, int x, int y) {
        this.x = x;
        this.y = y;
        URL url = getClass().getClassLoader().getResource(fn);
        if (url != null) {
          this.i = new ImageIcon(url).getImage();
          this.width = this.i.getWidth(null);
          this.height = this.i.getHeight(null);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
    }

    public void draw(){
        if(this.visible){
            Game.canvas.drawImage(i,(int)(this.x - this.width/2),(int)(this.y - this.height/2),null);
        }
        updateRect();
        drawBoundaries();
    }

    public void resizeTo(int w, int h){
        this.i = this.i.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH);
    }

}

class Animation extends Sprite{
    public int frame_width,frame_height, current_frame, frames, frame_per_rows, frame_per_cols;
    private double frame_rate, frame_count;

    public Animation(String fn, int frames, int frame_width, int frame_height, double frame_rate) {
        super(fn);
        this.frames = frames;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.frame_per_cols = i.getWidth(null) / frame_width;
        this.frame_per_rows = i.getHeight(null) / frame_height;
        this.frame_rate = frame_rate;
        this.frame_count = 0;
        this.current_frame = 0;
        this.width = frame_width;
        this.height = frame_height;
    }

    public void draw(){
        int sourceStartX = (this.current_frame % this.frame_per_cols) * this.frame_width;
        int sourceStartY = (this.current_frame / this.frame_per_cols) * this.frame_height;
        if(this.visible){
            Game.canvas.drawImage(i,this.x - this.frame_width / 2,this.y - this.frame_height / 2,this.x + this.frame_width, this.y + this.frame_height, sourceStartX, sourceStartY, sourceStartX + this.frame_width, sourceStartY + this.frame_height,null);
        }

        this.frame_count += this.frame_rate;
        if(this.frame_count > 1){
            this.frame_count = 0;
            this.current_frame++;
        }
        this.current_frame = this.current_frame % this.frames;

        updateRect();
        drawBoundaries();     
    }
}

class Shape extends GameObject{
    public String shape;
    public int side, size;
    public Color color;

    public Shape(String shape,int arg1,int arg2,Color color){
        this.shape = shape;
        if(shape.equals("ellipse")){
            this.width = arg1;
            this.height = arg2;
            this.color = color;
        }
    }
    public void draw(){
        Game.canvas.setPaint(this.color);
        if(this.visible){
            if(shape.equals("ellipse")){
                Game.canvas.fillOval(this.x - this.width/2,this.y - this.height/2,this.width,this.height);
            }
        }
    }
}

