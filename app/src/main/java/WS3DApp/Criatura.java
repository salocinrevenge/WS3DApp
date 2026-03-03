package WS3DApp;
import ws3dproxy.model.Creature;

public class Criatura {

    Bag bag;
    Vision vision;
    Missions missions;
    Creature proxyCreature;
    MiniWorld miniWorld;


    public Criatura(Creature proxyCreature, MiniWorld miniWorld) {
        this.proxyCreature = proxyCreature;
        this.miniWorld = miniWorld;
        bag = new Bag(700, 220, 300, 550);
        vision = new Vision(450, 120, 300, 50);
        missions = new Missions();
        
    }

    public void updateState() {
        vision.update_creature(proxyCreature);
        missions.update(proxyCreature);
        // Update bag state based on proxyCreature's inventory
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


    public void acao(String action) {
        switch (action) {
            case "eat":
                try {
                    proxyCreature.eatIt(vision.getFirstThingInVision().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // Implement other actions like move, pick up, etc.
        }

    }
    public void move(double vel, double x, double y) {
        try {
            proxyCreature.moveto(vel,miniWorld.toCopelia_Y((int)y), miniWorld.toCopelia_X((int)x));
        } catch (Exception e) {
            System.out.println("Erro capturado: "+ e);
        }
    }


}
