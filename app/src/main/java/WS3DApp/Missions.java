package WS3DApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

public class Missions {
    int x, y;
    public Missions() {
        x=1100;
        y=100;
        
    }

    List<Leaflet> currentMissions;

    public List<Leaflet> update(Creature creature) {
        
        currentMissions = creature.getLeaflets();
        return currentMissions;
    }

    public void render(Graphics2D g2d) {
        // Implement rendering logic for missions
        g2d.setColor(Color.WHITE);
        g2d.drawString("Missions:", x, y);
        if (currentMissions != null) {
            int y = this.y + 20;
            int maxLen = 50;
            for (Leaflet leaflet : currentMissions) {
                String text = "- " + leaflet.toString();
                while (text.length() > 0) {
                    int len = Math.min(maxLen, text.length());
                    String line = text.substring(0, len);
                    g2d.drawString(line, x, y);
                    y += 20;
                    text = text.substring(len);
                }
            }
        }
    }

    public void update() {
        // Implement update logic for missions
    }



}
