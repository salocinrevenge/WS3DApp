package WS3DApp;
import ws3dproxy.CommandExecException;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

import java.awt.Graphics2D;
import java.util.List;

public class Criatura {

    BagMine bag;
    Vision vision;
    Missions missions;
    Creature proxyCreature;
    MiniWorld miniWorld;


    public Criatura(Creature proxyCreature, MiniWorld miniWorld) {
        this.proxyCreature = proxyCreature;
        this.miniWorld = miniWorld;
        bag = new BagMine(700, 220, 300, 550, this);
        vision = new Vision(450, 120, 300, 50);
        missions = new Missions();
        
    }

    public void updateStates() {
        try {
            vision.update_creature(proxyCreature);
            missions.update(proxyCreature);
            proxyCreature.updateState();
            bag.update(); // Atualizar a bag também
            System.out.println("Estados da criatura atualizados");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar estados: " + e.getMessage());
        }
    }

    public Missions updateMissions() {
        this.missions.update(proxyCreature);
        return this.missions;
    }


    public double getX() {
        return proxyCreature.s.comY;
    }
    public double getY() {
        return proxyCreature.s.comX;
    }

    public void moveto(int vel, int x, int y) {
        try {
            proxyCreature.moveto(vel,miniWorld.toCopelia_Y(y), miniWorld.toCopelia_X(x));
        } catch (Exception e) {
            System.out.println("Erro capturado: "+ e);
        }
    }

    public double getFuel() {
        return proxyCreature.getFuel();
    }


    public void completeMission()
    {
        int number_missions = proxyCreature.getLeaflets().size();
        int pontos = 0;

        for (int i = 0; i < number_missions; i++) {
            Long ID_leaflet = proxyCreature.getLeaflets().get(i).getID();
            String ID_leaftlet_string = ID_leaflet.toString();
            try {
                proxyCreature.deliverLeaflet(ID_leaftlet_string);
            } catch (CommandExecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (proxyCreature.getLeaflets().size() < number_missions - 1) {
                break; // Se a lista de missões for atualizada, saia do loop
            }
        }

    }

    public void acao(String action) {
        System.out.println("Executando ação: " + action);
        vision.update_creature(proxyCreature);
        Thing thing;
        switch (action) {
            case "eat":
                try {
                    thing = vision.getFirstThingInVision();
                    if (thing != null) {
                        proxyCreature.eatIt(thing.getName());
                        System.out.println("Criatura comeu: " + thing.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "get":
                try {
                    thing = vision.getFirstThingInVision();
                    if (thing != null) {
                        proxyCreature.putInSack(thing.getName());
                        System.out.println("Criatura coletou: " + thing.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "deposit":
                try {
                    // Tenta entregar itens no delivery spot mais próximo
                    proxyCreature.deliverLeaflet("AAAA");
                    System.out.println("Criatura entregou itens no delivery spot");
                } catch (Exception e) {
                    System.out.println("Erro ao entregar itens: " + e.getMessage());
                }
                break;
        }
    }
    public void move(double vel, double x, double y) {
        try {
            proxyCreature.moveto(vel,miniWorld.toCopelia_Y((int)y), miniWorld.toCopelia_X((int)x));
        } catch (Exception e) {
            System.out.println("Erro capturado: "+ e);
        }
    }

    public void update() {
        try {
            updateStates();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar criatura: " + e.getMessage()); 
        }
    }

    public void render(Graphics2D g2d) {
        // Implement rendering logic for the creature
        vision.render(g2d);
        missions.render(g2d);
        bag.render(g2d);
    }


}
