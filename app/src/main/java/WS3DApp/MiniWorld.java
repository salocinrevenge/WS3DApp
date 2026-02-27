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
    ArrayList<MiniWorldObject> miniCreatures = new ArrayList<>();
    ArrayList<Creature> creatures = new ArrayList<>();
    int selectedCreatureIndex = -1;

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
            boolean hightlight = false;
            if (obj.type == MiniObjType.CRIATURA) {
                hightlight = (miniCreatures.indexOf(obj) == selectedCreatureIndex);
            }
            obj.render(g2d, hightlight);
        }
    }

    public void update() {
        for (Creature creature : creatures) {
            creature.updateState();
            int idx = creatures.indexOf(creature);
            if (idx >= 0 && idx < miniCreatures.size()) {
                MiniWorldObject obj = miniCreatures.get(idx);
                obj.x = toMiniWorld_X(creature.s.comY);
                obj.y = toMiniWorld_Y(creature.s.comX);
            }
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

    public void walk_to(int mx, int my) {
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < miniCreatures.size()) {
            Creature c = creatures.get(selectedCreatureIndex);
            try {
                c.moveto(12,toCopelia_Y(my), toCopelia_X(mx));
            } catch (Exception e) {
                System.out.println("Erro capturado: "+ e);
            }
        }
    }

    public MiniWorldObject getSelectedCreature() {
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < miniCreatures.size()) {
            return miniCreatures.get(selectedCreatureIndex);
        }
        return null;
    }

    public void selectNextCreature() {
        if (miniCreatures.size() > 0) {
            selectedCreatureIndex = (selectedCreatureIndex + 1) % miniCreatures.size();
        }
    }

    public void selectPreviousCreature() {
        if (miniCreatures.size() > 0) {
            selectedCreatureIndex = (selectedCreatureIndex - 1 + miniCreatures.size()) % miniCreatures.size();
        }
    }

    public void unselectCreature() {
        selectedCreatureIndex = -1;
    }

    private float toCopelia_X(int x){
        float mini_world_x = x-this.x;

        return (mini_world_x*this.world.getEnvironmentHeight()/this.width);

    }

    private float toCopelia_Y(int y){
        float mini_world_y = y-this.y;

        return (mini_world_y*this.world.getEnvironmentWidth()/this.height);

    }

    private int toMiniWorld_X(double x){
        double copelia_x = x;

        return (int)(copelia_x*this.width/this.world.getEnvironmentHeight())+this.x;

    }

    private int toMiniWorld_Y(double comY){
        double copelia_y = comY;

        return (int)(copelia_y*this.height/this.world.getEnvironmentWidth())+this.y;

    }

    public double getSelectedCreatureFuel() {
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < miniCreatures.size()) {
            // creatures.get(selectedCreatureIndex).updateState();
            return creatures.get(selectedCreatureIndex).getFuel();
        }
        return 0;
    }

    public void act(String action) {
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
            Creature c = creatures.get(selectedCreatureIndex);
            try {
                switch(action) {
                    case "W":
                        c.move(4.0,4.0,0.0);
                        break;
                    case "S":
                        c.move(-4.0,-4.0,0.0);
                        break;
                    case "A":
                        c.move(4.0,-4.0,0.0);
                        break;
                    case "D":
                        c.move(-4.0,4.0,0.0);
                        break;
                    case "w":
                    case "s":
                    case "a":
                    case "d":
                        c.move(0.0,0.0,0.0);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro capturado: "+ e);
            }
        }
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
                    miniCreatures.add(obj);
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
