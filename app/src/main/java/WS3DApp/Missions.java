package WS3DApp;

import java.awt.Graphics2D;
import java.util.List;

import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

public class Missions {
    public Missions() {
        
    }

    List<Leaflet> currentMissions;

    public void update(Creature creature) {
        
        currentMissions = creature.getLeaflets();
    }

    public void render(Graphics2D g2d) {
        // Implement rendering logic for missions
    }

    public void update() {
        // Implement update logic for missions
    }



}
