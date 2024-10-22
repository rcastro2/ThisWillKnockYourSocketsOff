package com.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window extends JPanel implements KeyListener, MouseMotionListener, MouseListener {

    GameLogic game;

    public Window(int width, int height) {
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