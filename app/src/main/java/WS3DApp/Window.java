package WS3DApp;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.Timer;

import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.World;



public class Window extends Frame {
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

    public Window(World world, WS3DProxy proxy) {
        setTitle("Controle da simulação");
        this.world = world;
        this.proxy = proxy;
        int WIDTH = 1200;
        int HEIGHT = 800;
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);

        miniWorld = new MiniWorld(50, 200, 550, 550, world, proxy);

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
        createButtons.add(new CustomButton(800, 120, 100, 50, "Cristal \n branco", () -> this.add_object(MiniObjType.JOIA_WHITE), 20));
        selectButtons.add(new CustomButton(200, 50, 100, 50, "Previous \n Creature", () -> this.miniWorld.selectPreviousCreature(), 20));
        selectButtons.add(new CustomButton(350, 50, 100, 50, "Next \n Creature", () -> this.miniWorld.selectNextCreature(), 20));
        selectButtons.add(new CustomButton(500, 50, 100, 50, "Unselect \n Creature", () -> this.miniWorld.unselectCreature(), 20));
        healthBar = new CustomBar(200, 120, 200, 50, "Fuel", 1000, 1000, 20);
        
        // Adiciona listeners globais
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for (CustomButton b : mainButtons) {
                    b.handleMousePressed(e.getX(), e.getY());
                }

                if (state == EnumState.CRIAR) {
                    for (CustomButton b : createButtons) {
                        b.handleMousePressed(e.getX(), e.getY());
                    }

                }
                
                if (state == EnumState.SELECIONAR) {
                    for (CustomButton b : selectButtons) {
                        b.handleMousePressed(e.getX(), e.getY());
                    }
                    
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        miniWorld.getSelectedCreature().move(4.0, e.getX(), e.getY());

                    }

                }
                if (state == EnumState.CRIANDO) {
                    MiniWorldObject newSelectedObject = miniWorld.handleMousePressed(e.getX(), e.getY(), selectedObject);
    
                    if (selectedObject != newSelectedObject || (newSelectedObject != null && newSelectedObject.type == MiniObjType.BRICK2)) {
                        selectedObject = newSelectedObject;
                        state = EnumState.INICIO;
                    }
                }
                repaint();
            }
        });

        // addMouseListener(new MouseAdapter() {
        //     public void mousePressed(MouseEvent e) {
        //         if (e.getButton() == MouseEvent.BUTTON3) {
        //             selectedObject = null;
        //             state = EnumState.INICIO;
        //             repaint();
        //         }
        //     }
        // });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                for (CustomButton b : mainButtons) {
                    b.handleMouseMoved(e.getX(), e.getY());
                }

                if (state == EnumState.CRIAR) {
                    for (CustomButton b : createButtons) {
                        b.handleMouseMoved(e.getX(), e.getY());
                    }
                }

                if (state == EnumState.SELECIONAR) {
                    for (CustomButton b : selectButtons) {
                        b.handleMouseMoved(e.getX(), e.getY());
                    }

                    
                }

                if(selectedObject != null) {
                    selectedObject.x = e.getX();
                    selectedObject.y = e.getY();
                }
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
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
                    case KeyEvent.VK_J: // Comer o item mais próximo na visão
                        miniWorld.getSelectedCreature().acao("eat");
                        
                        
                        
                    
                }
            }

            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
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
        });

        new Timer(500, e -> window_update()).start();

        setFocusable(true);
    }

    public void window_update() {
        miniWorld.update();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
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

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Pontos: " + points, getWidth() - 150, 50);
    }

    private void add_object(MiniObjType type) {
        state = EnumState.CRIANDO;
        selectedObject = new MiniWorldObject(0, 0, type, null, null);


    }
}