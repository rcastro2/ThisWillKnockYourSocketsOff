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
        // if(this instanceof Animation){
        //     width = ((Animation)this).frame_width;
        //     height = ((Animation)this).frame_height;
        // }
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
          i = new ImageIcon(url).getImage();
          this.width = i.getWidth(null);
          this.height = i.getHeight(null);
          System.out.println(this.width);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
        
    }
    public Sprite(String fn, int x, int y) {
        this.x = x;
        this.y = y;
        URL url = getClass().getClassLoader().getResource(fn);
        if (url != null) {
          i = new ImageIcon(url).getImage();
          this.width = i.getWidth(null);
          this.height = i.getHeight(null);
        } else {
          System.err.println("Failed to load image: " + fn);
        }
    }

    public void draw(){
        if(this.visible){
            Game.canvas.drawImage(i,(int)x,(int)y,null);
        }
        updateRect();
        drawBoundaries();
    }

    public void resizeTo(int w, int h){
        i = i.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH);
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
        //System.out.println(this.frame_per_cols + " " + this.frame_per_rows);
    }

    public void draw(){
        int x = (int)(this.x);
        int y = (int)(this.y);
        int sourceStartX = (current_frame % this.frame_per_cols) * frame_width;
        int sourceStartY = (current_frame / this.frame_per_cols) * frame_height;
        if(this.visible){
            Game.canvas.drawImage(i,x - frame_width / 2,y - frame_height / 2,x + frame_width, y + frame_height, sourceStartX, sourceStartY, sourceStartX + frame_width, sourceStartY + frame_height,null);
        }

        this.frame_count += this.frame_rate;
        if(this.frame_count > 1){
            this.frame_count = 0;
            current_frame++;
        }
        current_frame = current_frame % frames;

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
