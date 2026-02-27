package WS3DApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Bag {
    ArrayList<MiniWorldObject> items = new ArrayList<>();
    int x, y, width, height;

    public Bag(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addItem(MiniWorldObject item) {
        items.add(item);
    }

    public void removeItem(MiniWorldObject item) {
        items.remove(item);
    }

    public void render(Graphics2D g2d) {
        for (MiniWorldObject item : items) {
            item.render(g2d, false);
        }
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
    }
}
