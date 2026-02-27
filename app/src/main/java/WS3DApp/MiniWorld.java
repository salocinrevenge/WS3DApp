package WS3DApp;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;

import ws3dproxy.model.Creature;
import ws3dproxy.model.World;
import ws3dproxy.WS3DProxy;

public class MiniWorld {

    int x;
    int y;
    int width;
    int height;
    World world;
    WS3DProxy proxy;
    

    ArrayList<MiniWorldObject> objects = new ArrayList<>();
    ArrayList<Creature> creatures = new ArrayList<>();

    public MiniWorld(int x, int y, int width, int height, World world, WS3DProxy proxy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.proxy = proxy;
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
                        int temp1, temp2;
                        temp1 = extraObj.x;
                        temp2 = extraObj.x2;
                        extraObj.x = Math.min(temp1, temp2);
                        extraObj.x2 = Math.max(temp1, temp2);
                        temp1 = extraObj.y;
                        temp2 = extraObj.y2;
                        extraObj.y = Math.min(temp1, temp2);
                        extraObj.y2 = Math.max(temp1, temp2);
                        selectedObject = extraObj;
                    }
                }

                if (!addOnWorld(selectedObject))
                {
                    objects.remove(selectedObject);
                }
                return null;
            }
        }
        return selectedObject;
    }

    private float toCopelia_X(int x){
        float mini_world_x = x-this.x;

        return (mini_world_x*this.world.getEnvironmentHeight()/this.width);

    }

    private float toCopelia_Y(int y){
        float mini_world_y = y-this.y;

        return (mini_world_y*this.world.getEnvironmentWidth()/this.height);

    }


    public boolean addOnWorld(MiniWorldObject obj)
    {
        try {
            switch (obj.type) {
                case MiniObjType.MACA:
                    World.createFood(0, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;
                case MiniObjType.NOZ:
                    World.createFood(1, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;

                case MiniObjType.JOIA_RED:
                    World.createJewel(0, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;

                case MiniObjType.JOIA_GREEN:
                    World.createJewel(1, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;

                case MiniObjType.JOIA_BLUE:
                    World.createJewel(2, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;
                case MiniObjType.JOIA_YELLOW:
                    World.createJewel(3, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;

                case MiniObjType.JOIA_MAGENTA:
                    World.createJewel(4, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;

                case MiniObjType.JOIA_WHITE:
                    World.createJewel(5, toCopelia_Y(obj.y), toCopelia_X(obj.x));
                    break;
                case MiniObjType.BRICK:
                    World.createBrick(3, toCopelia_Y(obj.y), toCopelia_X(obj.x), toCopelia_Y(obj.y2), toCopelia_X(obj.x2));
                    break;
                case MiniObjType.CRIATURA:
                    Creature c = proxy.createCreature(toCopelia_Y(obj.y),toCopelia_X(obj.x),0);
                    c.start();
                    creatures.add(c);
                    System.out.println("Criatura criada na posição (" + toCopelia_Y(obj.y) + ", " + toCopelia_X(obj.x) + ")");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro capturado: "+ e);
            return false;
        }
        return true;
    }
}
