package WS3DApp;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import ws3dproxy.model.World;


public class Window extends Frame {
    ArrayList<CustomButton> mainButtons = new ArrayList<>();
    ArrayList<CustomButton> createButtons = new ArrayList<>();
    EnumState state 
    = EnumState.SELECIONANDO;

    public enum EnumState {
        INICIO,
        CRIAR,
        SELECIONANDO,
        CRIANDO
    }

    MiniWorldObject selectedObject = null;

    MiniWorld miniWorld;
    World world;

    public Window(World world) {
        setTitle("Controle da simulação");
        this.world = world;
        int WIDTH = 1000;
        int HEIGHT = 800;
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);

        // Cria botões
        mainButtons.add(new CustomButton(50, 50, 100, 50, "Criar", () -> this.state=EnumState.CRIAR, 20));
        mainButtons.add(new CustomButton(50, 120, 100, 50, "Selecionar", () -> this.state=EnumState.SELECIONANDO, 20));
        mainButtons.add(new CustomButton(900, 750, 100, 50, "🔙", () -> this.state=EnumState.SELECIONANDO, 60));
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
        
        miniWorld = new MiniWorld(50, 200, 550, 550, world);


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
                selectedObject = miniWorld.handleMousePressed(e.getX(), e.getY(), selectedObject);

                repaint();
            }
        });
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

                if(selectedObject != null) {
                    selectedObject.x = e.getX();
                    selectedObject.y = e.getY();
                }
                repaint();
            }
        });
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
        miniWorld.render(g2d);
        if(this.selectedObject != null) {
            this.selectedObject.render(g2d);
        }
    }

    private void add_object(MiniObjType type) {
        state = EnumState.CRIANDO;
        selectedObject = new MiniWorldObject(0, 0, type, null, null);


    }
}