package WS3DApp;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.World;



public class Window extends Canvas {
    ArrayList<CustomButton> mainButtons = new ArrayList<>();
    ArrayList<CustomButton> createButtons = new ArrayList<>();
    ArrayList<CustomButton> selectButtons = new ArrayList<>();
    CustomBar healthBar;
    EnumState state 
    = EnumState.INICIO;

    public enum EnumState {
        INICIO,
        CRIAR,
        SELECIONAR,
        CRIANDO
    }

    MiniWorldObject selectedObject = null;

    MiniWorld miniWorld;
    World world;
    WS3DProxy proxy;
    int points = 0;

    public Window(World world, WS3DProxy proxy, App app) {
        JFrame frame = new JFrame("Controle da simulação");
        this.world = world;
        this.proxy = proxy;
        int WIDTH = 1400;
        int HEIGHT = 800;
        frame.setSize(WIDTH, HEIGHT);
        frame.setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        
        // Adicionar o App ao frame
        app.setSize(WIDTH, HEIGHT);
        frame.add(app);
        frame.setVisible(true);
        setVisible(true);

        miniWorld = new MiniWorld(50, 200, 550, 550, world, proxy, this);

        // Cria botões
        mainButtons.add(new CustomButton(50, 50, 100, 50, "Criar", () -> this.state=EnumState.CRIAR, 20));
        mainButtons.add(new CustomButton(50, 120, 100, 50, "Selecionar", () -> this.state=EnumState.SELECIONAR, 20));
        mainButtons.add(new CustomButton(900, 750, 100, 50, "🔙", () -> this.state=EnumState.INICIO, 60));
        createButtons.add(new CustomButton(200, 50, 100, 50, "Criatura", () -> this.add_object(MiniObjType.CRIATURA), 20));
        createButtons.add(new CustomButton(200, 120, 100, 50, "Maçã", () -> this.add_object(MiniObjType.MACA), 20));
        createButtons.add(new CustomButton(350, 50, 100, 50, "Noz", () -> this.add_object(MiniObjType.NOZ), 20));
        createButtons.add(new CustomButton(350, 120, 100, 50, "Brick", () -> this.add_object(MiniObjType.BRICK), 20));
        createButtons.add(new CustomButton(500, 50, 100, 50, "Cristal \n vermelho", () -> this.add_object(MiniObjType.JOIA_RED), 20));
        createButtons.add(new CustomButton(500, 120, 100, 50, "Cristal \n verde", () -> this.add_object(MiniObjType.JOIA_GREEN), 20));
        createButtons.add(new CustomButton(650, 50, 100, 50, "Cristal \n azul", () -> this.add_object(MiniObjType.JOIA_BLUE), 20));
        createButtons.add(new CustomButton(650, 120, 100, 50, "Cristal \n amarelo", () -> this.add_object(MiniObjType.JOIA_YELLOW), 20));
        createButtons.add(new CustomButton(800, 50, 100, 50, "Cristal \n magenta", () -> this.add_object(MiniObjType.JOIA_MAGENTA), 20));
        createButtons.add(new CustomButton(800, 120, 100, 50, "Cristal \\n branco", () -> this.add_object(MiniObjType.JOIA_WHITE), 20));
        createButtons.add(new CustomButton(950, 50, 100, 50, "Delivery \\n Spot", () -> this.createDeliverySpot(), 20));
        selectButtons.add(new CustomButton(200, 50, 100, 50, "Previous \n Creature", () -> this.miniWorld.selectPreviousCreature(), 20));
        selectButtons.add(new CustomButton(350, 50, 100, 50, "Next \n Creature", () -> this.miniWorld.selectNextCreature(), 20));
        selectButtons.add(new CustomButton(500, 50, 100, 50, "Unselect \n Creature", () -> this.miniWorld.unselectCreature(), 20));
        selectButtons.add(new CustomButton(650, 50, 100, 50, "Coletar", () -> this.miniWorld.colect(), 20));
        selectButtons.add(new CustomButton(800, 50, 100, 50, "Comer ", () -> this.miniWorld.eat(), 20));
        selectButtons.add(new CustomButton(950, 50, 100, 50, "Entregar ", () -> this.miniWorld.send(), 20));
        selectButtons.add(new CustomButton(1100, 50, 100, 50, "Novo \n Leaflet", () -> this.generateNewLeaflet(), 20));
        healthBar = new CustomBar(200, 120, 200, 50, "Fuel", 1000, 1000, 20);
        

        // Adiciona listeners simples ao App para capturar input
        app.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                app.setMouseInput(e.getX(), e.getY(), e.getButton(), true);
            }
        });

        app.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                app.setMousePosition(e.getX(), e.getY());
            }
        });

        app.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                app.setKeyInput(e.getKeyCode(), true);
            }

            public void keyReleased(KeyEvent e) {
                app.setKeyInput(e.getKeyCode(), false);
            }
        });
        
        
        app.setFocusable(true);
        app.requestFocus();
        app.start();
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (CustomButton b : mainButtons) {
            b.render(g2d);
        }
        if (state == EnumState.CRIAR) {
            for (CustomButton b : createButtons) {
                b.render(g2d);
            }
        }
        if (state == EnumState.SELECIONAR) {
            for (CustomButton b : selectButtons) {
                b.render(g2d);
            }
            healthBar.render(g2d);
        }
        miniWorld.render(g2d);
        if(this.selectedObject != null) {
            this.selectedObject.render(g2d, false);
        }
        
        healthBar.updateValue(miniWorld.getSelectedCreatureFuel());

        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Pontos: " + points, getWidth() - 150, 50);



        super.paint(g);
    }

    public void update()
    {
        miniWorld.update();
        // Atualizar pontos com base nas missões completadas
        Criatura selected = miniWorld.getSelectedCreature();
        if (selected != null) {
            selected.updateMissions();
        }
    }

    private void add_object(MiniObjType type) {
        state = EnumState.CRIANDO;
        selectedObject = new MiniWorldObject(0, 0, type, null, null);
    }
    
    public void handleMouseInput(int x, int y, int button) {
        for (CustomButton b : mainButtons) {
            b.handleMousePressed(x, y);
        }

        if (state == EnumState.CRIAR) {
            for (CustomButton b : createButtons) {
                b.handleMousePressed(x, y);
            }
        }
        
        if (state == EnumState.SELECIONAR) {
            for (CustomButton b : selectButtons) {
                b.handleMousePressed(x, y);
            }
            
            if (button == MouseEvent.BUTTON3) {
                System.out.println("HEY");
                Criatura selected = miniWorld.getSelectedCreature();
                if (selected != null) {
                    selected.moveto(4, x, y);
                }
            }
        }
        
        if (state == EnumState.CRIANDO) {
            MiniWorldObject newSelectedObject = miniWorld.handleMousePressed(x, y, selectedObject);

            if (selectedObject != newSelectedObject || (newSelectedObject != null && newSelectedObject.type == MiniObjType.BRICK2)) {
                selectedObject = newSelectedObject;
                state = EnumState.CRIANDO;
            }
        }
    }
    
    public void handleMouseMove(int x, int y) {
        for (CustomButton b : mainButtons) {
            b.handleMouseMoved(x, y);
        }

        if (state == EnumState.CRIAR) {
            for (CustomButton b : createButtons) {
                b.handleMouseMoved(x, y);
            }
        }

        if (state == EnumState.SELECIONAR) {
            for (CustomButton b : selectButtons) {
                b.handleMouseMoved(x, y);
            }
        }

        if(selectedObject != null) {
            selectedObject.x = x;
            selectedObject.y = y;
        }
    }
    
    public void handleKeyInput(int keyCode, boolean pressed) {
        if (pressed) {
            Criatura selected;
            switch(keyCode) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    miniWorld.act("A");
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    miniWorld.act("D");
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    miniWorld.act("W");
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    miniWorld.act("S");
                    break;
                case KeyEvent.VK_J:
                    selected = miniWorld.getSelectedCreature();
                    if (selected != null) selected.acao("get");
                    break;
                case KeyEvent.VK_K:
                    selected = miniWorld.getSelectedCreature();
                    if (selected != null) selected.acao("eat");
                    break;
            }
        } else {
            switch(keyCode) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    miniWorld.act("a");
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    miniWorld.act("d");
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    miniWorld.act("w");
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    miniWorld.act("s");
                    break;
            }
        }
    }
    
    private void createDeliverySpot() {
        try {
            // Cria um delivery spot no centro do mundo
            int centerX = world.getEnvironmentWidth() / 2;
            int centerY = world.getEnvironmentHeight() / 2;
            World.createDeliverySpot(centerX, centerY);
            System.out.println("Delivery spot criado em: (" + centerX + ", " + centerY + ")");
        } catch (Exception e) {
            System.out.println("Erro ao criar delivery spot: " + e.getMessage());
        }
    }
    
    private void generateNewLeaflet() {
        Criatura selected = miniWorld.getSelectedCreature();
        if (selected != null) {
            try {
                // Gera um novo leaflet usando o WS3DProxy
                selected.proxyCreature.genLeaflet();
                System.out.println("Novo leaflet gerado!");
            } catch (Exception e) {
                System.out.println("Erro ao gerar leaflet: " + e.getMessage());
            }
        }
    }
}