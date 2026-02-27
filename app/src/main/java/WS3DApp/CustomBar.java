package WS3DApp;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class CustomBar {
    int x, y, width, height;
    String text;
    Runnable action;
    boolean hovered = false;
    int fontSize;
    float max_value;
    float current_value;

    public CustomBar(int x, int y, int width, int height, String text, float max_value, float current_value, int fontSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.max_value = max_value;
        this.current_value = current_value;
        this.fontSize = fontSize;
    }

    public void updateValue(double new_value) {
        
        this.current_value = (float) new_value;
    }

    public void render(Graphics2D g2d) {
        // Desenha o fundo da barra
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(x, y, width, height);
        // Desenha a parte preenchida da barra
        int filledWidth = (int) ((current_value / max_value) * width);
        g2d.setColor(new Color(100, 100, 255));
        g2d.fillRect(x, y, filledWidth, height);
        // Desenha a borda da barra
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(x, y, width, height);
        // Desenha o texto centralizado
        Font oldFont = g2d.getFont();
        g2d.setFont(new Font(oldFont.getName(), oldFont.getStyle(), fontSize));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, textX, textY);
        g2d.setFont(oldFont);
    }

}