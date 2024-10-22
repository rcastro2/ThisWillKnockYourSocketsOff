package com.example;

import io.socket.emitter.Emitter;
import org.json.JSONObject;
import java.awt.*;
import java.lang.reflect.Field; 

//mvn exec:java -Dexec.mainClass="com.example.App"
public class CircleWorld implements GameLogic{
    Animation bk;
    JSONObject players = new JSONObject();
    int speed = 2;
    Coordinate you = new Coordinate(0, 0);

    public CircleWorld() {
        bk = new Animation("images/field_5.png",5,1000,1000,0.05);
        Game.setBackground(bk);

        App.socket.on("players", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args[0] instanceof JSONObject){
                    players = (JSONObject)args[0];
                }                
             }
        });
    }

    public void gameLoop(){
        String direction = "";
        if(Keys.pressed[Keys.UP]){
            you.y -= speed;  
            direction += "down";
        }
        if(Keys.pressed[Keys.DOWN]){
            you.y += speed;
            direction += "up";
        }
        if(Keys.pressed[Keys.LEFT]){
            you.x -= speed;  
            direction += "right";
        }
        if(Keys.pressed[Keys.RIGHT]){
            you.x += speed;
            direction += "left";
        }
        JSONObject info = new JSONObject();
        info.put("id",App.id);
        info.put("x",you.x);
        info.put("y",you.y);
        info.put("color",App.color);

        if(!direction.equals("")){
            App.socket.emit("players", info);
        }
        Game.scrollBackground(direction,2);

        for (String key : players.keySet()) {
            JSONObject player = (JSONObject)players.get(key);
            int screenX = player.getInt("x") - you.x + Game.width / 2;
            int screenY = player.getInt("y") - you.y + Game.height / 2;
            if(player.getString("id").equals(App.id)){
                screenX = Game.width / 2;
                screenY = Game.height / 2;
            }
            Color c = Color.WHITE;
            try {
                Field field = Color.class.getField(player.getString("color"));
                c = (Color)field.get(null);
            } catch (Exception e) {
                c = Color.WHITE; 
            }
            
            Font f = new Font("Arial",18,c);
            Game.canvas.setPaint(c);
            Game.canvas.fillOval(screenX, screenY,25,25);
            String id = player.getString("id");
            String loc = String.format("(%d, %d)",player.getInt("x"), player.getInt("y"));
            Game.drawText(player.getString("id"),screenX - (int)(id.length()*3.1),screenY - 20,f);
            Game.drawText(loc,screenX - (int)(loc.length()*2.5),screenY + 50,f);
        }
    }

    public void print(Object obj){
        System.out.println(obj.toString());
    }
}
