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
    Window window;
    int contador = 10000;
    

    ArrayList<MiniWorldObject> objects = new ArrayList<>();
    ArrayList<MiniWorldObject> miniCreatures = new ArrayList<>();
    ArrayList<Criatura> creatures = new ArrayList<>();
    int selectedCreatureIndex = -1;

    public MiniWorld(int x, int y, int width, int height, World world, WS3DProxy proxy, Window window) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.proxy = proxy;
        this.window = window;
    }


    public void render(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(java.awt.Color.WHITE);
        g2d.drawRect(x, y, width, height);
        for(MiniWorldObject obj : objects) {
            boolean hightlight = false;
            if (obj.type == MiniObjType.CRIATURA) {
                hightlight = (miniCreatures.indexOf(obj) == selectedCreatureIndex);
            }
            obj.render(g2d, hightlight);
        }

        if (this.window.state != Window.EnumState.CRIANDO) {
            if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
                creatures.get(selectedCreatureIndex).render(g2d);
            }
        }
    }

    public void update() {
        contador--;
        if (contador <= 0) {
            contador = 100;
            for (Criatura creature : creatures) {
                if( contador == 1 ){
                    System.out.println("Atualizando criatura...");
                    int idx = creatures.indexOf(creature);
                    if (idx >= 0 && idx < miniCreatures.size()) {
                        MiniWorldObject obj = miniCreatures.get(idx);
                        obj.x = toMiniWorld_X(creature.getX());
                        obj.y = toMiniWorld_Y(creature.getY());
                    }
                }
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
            Criatura c = creatures.get(selectedCreatureIndex);
            try {
                c.moveto(12,my, mx);
            } catch (Exception e) {
                System.out.println("Erro capturado: "+ e);
            }
        }
    }

    public Criatura getSelectedCreature() {
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
            return creatures.get(selectedCreatureIndex);
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

    public float toCopelia_X(int x){
        float mini_world_x = x-this.x;

        return (mini_world_x*this.world.getEnvironmentHeight()/this.width);

    }

    public float toCopelia_Y(int y){
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
            Criatura c = creatures.get(selectedCreatureIndex);
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
                    System.out.println("Criando criatura na posição (" + toCopelia_Y(obj.y) + ", " + toCopelia_X(obj.x) + ")");
                    Creature c = proxy.createCreature(toCopelia_Y(obj.y),toCopelia_X(obj.x),0);
                    System.out.println("Criatura criada");
                    c.start();
                    creatures.add(new Criatura(c, this));
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


    public Object colect() {
        // Faz a criatura selecionada coletar o objeto à sua frente
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
            Criatura c = creatures.get(selectedCreatureIndex);
            try {
                c.acao("get");
                return true;
            } catch (Exception e) {
                System.out.println("Erro ao coletar: " + e);
                return false;
            }
        }
        return false;
    }

    public Object eat() {
        // Faz a criatura selecionada comer o alimento coletado
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
            Criatura c = creatures.get(selectedCreatureIndex);
            try {
                c.acao("eat");
                return true;
            } catch (Exception e) {
                System.out.println("Erro ao comer: " + e);
                return false;
            }
        }
        return false;
    }

    public Object send() {
        // Envia o conteúdo coletado pela criatura selecionada para o depósito
        if (selectedCreatureIndex >= 0 && selectedCreatureIndex < creatures.size()) {
            Criatura c = creatures.get(selectedCreatureIndex);
            try {
                // Primeiro entrega os itens
                c.acao("deposit");
                // Depois completa as missões
                c.completeMission();
                // Incrementa pontos na janela
                window.points += 100; 
                System.out.println("Criatura entregou itens e completou missões! Pontos: " + window.points);
                return true;
            } catch (Exception e) {
                System.out.println("Erro ao depositar: " + e);
                return false;
            }
        }
        return false;
    }
}
