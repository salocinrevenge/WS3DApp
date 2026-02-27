package WS3DApp;

import java.awt.Graphics2D;

import org.checkerframework.checker.units.qual.min;

public class MiniWorldObject {
    int x;
    int x2=-1;
    int y;
    int y2=-1;
    
    private MiniWorld world;
    MiniObjType type;
    MiniWorldObject extra;

    public MiniWorldObject(int x, int y, MiniObjType type, MiniWorld world, MiniWorldObject extra) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.world = world;
        this.extra = extra;
    }

    public void render(Graphics2D g2d) {

        switch (this.type) {
            case CRIATURA:
                // corpo cinza preenchido
                g2d.setColor(java.awt.Color.GRAY);
                g2d.fillOval(x, y, 20, 20);
                g2d.fillRect(x, y, 20, 20);
                // olhos azuis preenchidos
                g2d.setColor(java.awt.Color.BLUE);
                g2d.fillOval(x + 4, y + 4, 4, 4);
                g2d.fillOval(x + 12, y + 4, 4, 4);
                // boca amarela arredondada preenchida
                g2d.setColor(java.awt.Color.YELLOW);
                g2d.fillRoundRect(x + 4, y + 12, 12, 4, 4, 4);
                // antena cinza também
                g2d.setColor(java.awt.Color.GRAY);
                g2d.drawLine(x + 10, y, x + 10, y - 6);
                // voltar para cor padrão
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case MACA:
                g2d.drawOval(x, y, 15, 15);
                g2d.setColor(java.awt.Color.RED);
                g2d.fillOval(x, y, 15, 15);
                // folha verde no topo
                g2d.setColor(java.awt.Color.GREEN);
                g2d.fillOval(x + 6, y - 5, 5, 5);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case NOZ:
                g2d.setColor(new java.awt.Color(255,100,19));
                g2d.fillOval(x, y, 12, 16);
                g2d.setColor(new java.awt.Color(139,69,19)); // marrom
                g2d.fillOval(x, y, 12, 8);
                g2d.setColor(java.awt.Color.BLACK);
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawLine(x+8, y, x + 6, y + 4);
                g2d.setStroke(new java.awt.BasicStroke(1));
                break;
            case BRICK:
                if (x2 != -1 && y2 != -1) {
                    g2d.setColor(java.awt.Color.YELLOW);
                    g2d.fillRect(x, y, x2 - x, y2 - y);
                } else {
                    g2d.setColor(java.awt.Color.YELLOW);
                    g2d.drawRect(x, y, 20, 20);
                }
                break;
            case JOIA_RED:
                g2d.setColor(java.awt.Color.RED);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_GREEN:
                g2d.setColor(java.awt.Color.GREEN);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_BLUE:
                g2d.setColor(java.awt.Color.BLUE);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_YELLOW:
                g2d.setColor(java.awt.Color.YELLOW);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_MAGENTA:
                g2d.setColor(java.awt.Color.MAGENTA);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_WHITE:
                g2d.setColor(java.awt.Color.WHITE);
                g2d.fillPolygon(
                        new int[]{x + 5, x + 10, x + 5, x},
                        new int[]{y, y + 5, y + 10, y + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case BRICK2:
                g2d.drawRect(x, y, 20, 20);
                break;
        }

    }

    public void setWorld(MiniWorld world) {
        this.world = world;
    }
    
}
