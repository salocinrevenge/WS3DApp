package WS3DApp;

import java.awt.Graphics2D;

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
        int x_to_draw = x;
        int y_to_draw = y;

        switch (this.type) {
            case CRIATURA:
                // corpo cinza preenchido
                g2d.setColor(java.awt.Color.GRAY);
                g2d.fillOval(x_to_draw, y_to_draw, 20, 20);
                g2d.fillRect(x_to_draw, y_to_draw, 20, 20);
                // olhos azuis preenchidos
                g2d.setColor(java.awt.Color.BLUE);
                g2d.fillOval(x_to_draw + 4, y_to_draw + 4, 4, 4);
                g2d.fillOval(x_to_draw + 12, y_to_draw + 4, 4, 4);
                // boca amarela arredondada preenchida
                g2d.setColor(java.awt.Color.YELLOW);
                g2d.fillRoundRect(x_to_draw + 4, y_to_draw + 12, 12, 4, 4, 4);
                // antena cinza também
                g2d.setColor(java.awt.Color.GRAY);
                g2d.drawLine(x_to_draw + 10, y_to_draw, x_to_draw + 10, y_to_draw - 6);
                // voltar para cor padrão
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case MACA:
                g2d.drawOval(x_to_draw, y_to_draw, 15, 15);
                g2d.setColor(java.awt.Color.RED);
                g2d.fillOval(x_to_draw, y_to_draw, 15, 15);
                // folha verde no topo
                g2d.setColor(java.awt.Color.GREEN);
                g2d.fillOval(x_to_draw + 6, y_to_draw - 5, 5, 5);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case NOZ:
                g2d.setColor(new java.awt.Color(255,100,19));
                g2d.fillOval(x_to_draw, y_to_draw, 12, 16);
                g2d.setColor(new java.awt.Color(139,69,19)); // marrom
                g2d.fillOval(x_to_draw, y_to_draw, 12, 8);
                g2d.setColor(java.awt.Color.BLACK);
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawLine(x_to_draw+8, y_to_draw, x_to_draw + 6, y_to_draw + 4);
                g2d.setStroke(new java.awt.BasicStroke(1));
                break;
            case BRICK:
                if (x2 != -1 && y2 != -1) {
                    g2d.setStroke(new java.awt.BasicStroke(3)); // torna a linha mais larga
                    g2d.drawLine(x_to_draw, y_to_draw, x2, y2);
                } else {
                    g2d.drawRect(x_to_draw, y_to_draw, 20, 20);
                }
                break;
            case JOIA_RED:
                g2d.setColor(java.awt.Color.RED);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_GREEN:
                g2d.setColor(java.awt.Color.GREEN);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_BLUE:
                g2d.setColor(java.awt.Color.BLUE);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_YELLOW:
                g2d.setColor(java.awt.Color.YELLOW);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_MAGENTA:
                g2d.setColor(java.awt.Color.MAGENTA);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case JOIA_WHITE:
                g2d.setColor(java.awt.Color.WHITE);
                g2d.fillPolygon(
                        new int[]{x_to_draw + 5, x_to_draw + 10, x_to_draw + 5, x_to_draw},
                        new int[]{y_to_draw, y_to_draw + 5, y_to_draw + 10, y_to_draw + 5},
                        4);
                g2d.setColor(java.awt.Color.BLACK);
                break;
            case BRICK2:
                g2d.drawRect(x_to_draw, y_to_draw, 20, 20);
                break;
        }

    }

    public void setWorld(MiniWorld world) {
        this.world = world;
    }
    
}
