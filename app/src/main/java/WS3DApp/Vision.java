package WS3DApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;


public class Vision {
    private List<Thing> detectedObjects;
    int x, y, width, height;
    private Creature creature;
    public void see() {
        // Implement object detection logic
        detectedObjects = detectObjects();
    }

    public void update_creature(Creature creature_new) {
        this.creature = creature_new;
        see();
    }

    public Thing getFirstThingInVision() {
        if (detectedObjects != null && !detectedObjects.isEmpty()) {
            return detectedObjects.get(0);
        }
        return null;
    }

    private List<Thing> detectObjects() {
        // Your detection logic here
        for (Thing t : this.creature.getThingsInVision()) {
                    System.out.print(" "+t.getName());
                }
        return null;
    }
    
    public Vision(int x, int y, int width, int height) {
        // Initialize vision system
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }



    public void render(Graphics2D g2d) {
        // Implement rendering logic for vision
        // see();
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
    }
}
