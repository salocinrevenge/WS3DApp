package WS3DApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import ws3dproxy.model.Thing;
import ws3dproxy.model.Bag;

public class BagMine {
    Bag bag;
    int x, y, width, height;
    Criatura criatura;

    public BagMine(int x, int y, int width, int height, Criatura criatura) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.criatura = criatura;
        this.update();
    }

    public void update() {
        try {
            this.bag = criatura.proxyCreature.updateBag();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar bag: " + e.getMessage());
        }
    }

    public void render(Graphics2D g2d) {
        if (bag == null) {
            return;
        }
        String mochila = bag.printBag();

        g2d.setColor(Color.WHITE);
        String[] linhas = mochila.split("\n");
        int linhaAltura = g2d.getFontMetrics().getHeight();
        for (int i = 0; i < linhas.length; i++) {
            g2d.drawString(linhas[i], x + 10, y + 20 + i * linhaAltura);
        }
        
        g2d.drawRect(x, y, width, height);
    }
}
