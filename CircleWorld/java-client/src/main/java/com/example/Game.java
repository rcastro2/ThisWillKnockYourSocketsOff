package com.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Game extends JPanel implements KeyListener, MouseMotionListener, MouseListener {
    public static Graphics2D canvas;
    public static int width;
    public static int height;
    public static Sprite background;
    public static Coordinate[][] backgroundXY = new Coordinate[3][3];

    GameLogic game;

    public Game(int width, int height) {
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        setFocusable(true);
        Game.width = width;
        Game.height = height;

        //Replace with Class which implements GameLogic.  Essentially the "game" 
        game = new CircleWorld();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Game.canvas = (Graphics2D)g;
        Game.canvas.setStroke(new BasicStroke(4));
        Game.canvas.setPaint(Color.RED);
        game.gameLoop();
        Game.width = this.getWidth();
        Game.height = this.getHeight();
        update(g);
    }
    public void update(Graphics g){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    public static void drawText(String msg, int x, int y){
        Game.canvas.drawString(msg,x,y);
    }
    public static void drawText(String msg, int x, int y, Font f){
        Game.canvas.setFont(f.font);
        if(f.shadow != null){
            Game.canvas.setColor(f.shadow);
            Game.canvas.drawString(msg,x + 2,y + 2);
        }
        Game.canvas.setColor(f.color);
        Game.canvas.drawString(msg,x,y);
    }

    public static int mod(int a, int b) {
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
        System.out.println(background instanceof Animation);

    }
    public static void scrollBackground(String direction, int amt){
        int width = (int)background.width;
        int height = (int)background.height;
        if(background instanceof Animation){
            width = ((Animation)background).frame_width;
            height = ((Animation)background).frame_height;
        }
        if(direction.contains("left")){
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(backgroundXY[r][c].x + width  / 2 <= 0){
                        backgroundXY[r][c].x = (int)(backgroundXY[r][mod((c+2),3)].x + width);
                    }  
                    backgroundXY[r][c].x -= amt;
                }
            }
        }
        if(direction.contains("right")){
            for(int r = 0; r < 3; r++){
                for(int c = 2; c >= 0; c--){
                    if(backgroundXY[r][c].x - width  / 2 >= Game.width){
                        backgroundXY[r][c].x = (int)(backgroundXY[r][mod((c-2),3)].x - width);
                    }  
                    backgroundXY[r][c].x += amt;
                }
            }
        }
        if(direction.contains("up")){
            for(int c = 0; c < 3; c++){
                for(int r = 0; r < 3; r++){
                    if(backgroundXY[r][c].y + height  / 2 <= 0){
                        backgroundXY[r][c].y = (int)(backgroundXY[mod((r+2),3)][c].y + height);
                    }  
                    backgroundXY[r][c].y -= amt;
                }
            }
        }
        if(direction.contains("down")){
            for(int c = 0; c < 3; c++){
                for(int r = 2; r >= 0; r--){
                    if(backgroundXY[r][c].y - height  / 2 >= Game.height){
                        backgroundXY[r][c].y = (int)(backgroundXY[mod((r-2),3)][c].y - height);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keys.pressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keys.pressed[e.getKeyCode()] = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Mouse.x = e.getX();
        Mouse.y = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Mouse.leftClick = e.getButton() == 1;
        Mouse.rightClick = e.getButton() == 3;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            Mouse.leftPressed = true;
        }
        if(e.getButton() == 3){
            Mouse.rightPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1){
            Mouse.leftPressed = false;
        }
        if(e.getButton() == 3){
            Mouse.rightPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}