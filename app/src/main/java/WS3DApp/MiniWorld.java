package WS3DApp;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MiniWorld {

    int x;
    int y;
    int width;
    int height;

    ArrayList<MiniWorldObject> objects = new ArrayList<>();

    public MiniWorld(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(x, y, width, height);
        for(MiniWorldObject obj : objects) {
            obj.render(g2d);
        }
    }

    public MiniWorldObject handleMousePressed(int mx, int my, MiniWorldObject selectedObject) {
        // Verifica se o clique foi dentro do mini mundo
        if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
            if(selectedObject != null) {
                // Atualiza a posição do objeto selecionado
                selectedObject.x = mx;
                selectedObject.y = my;
                // Adiciona o objeto à lista de objetos do mini mundo, se ainda não estiver lá
                if (!objects.contains(selectedObject)) {
                    objects.add(selectedObject);
                }
                selectedObject.setWorld(this);

                if (selectedObject.type == MiniObjType.BRICK) {
                    return new MiniWorldObject(mx, my, MiniObjType.BRICK2, null, selectedObject);
                }
                if (selectedObject.type == MiniObjType.BRICK2) {
                    objects.remove(selectedObject);
                    int idx = objects.indexOf(selectedObject.extra);
                    if (idx != -1) {
                        MiniWorldObject extraObj = objects.get(idx);
                        extraObj.x2 = mx;
                        extraObj.y2 = my;
                    }
                }
                return null;
            }
        }
        return selectedObject;
    }
}
