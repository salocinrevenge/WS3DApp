package WS3DApp;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class CustomButton {
    int x, y, width, height;
    String text;
    Runnable action;
    boolean hovered = false;
    int fontSize;

    public CustomButton(int x, int y, int width, int height, String text, Runnable action, int fontSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
        this.fontSize = fontSize;
    }

    public boolean isInside(int mx, int my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void render(Graphics2D g2d) {
        float alpha = hovered ? 0.8f : 0.3f;
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(x, y, width, height);
        g2d.setComposite(old);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(x, y, width, height);

        Font oldFont = g2d.getFont();
        g2d.setFont(new Font(oldFont.getName(), oldFont.getStyle(), fontSize));
        FontMetrics fm = g2d.getFontMetrics();
        // quebra em linhas caso haja '\n'
        String[] lines = text.split("\n", -1);
        int totalHeight = fm.getHeight() * lines.length;
        int startY = y + (height - totalHeight) / 2 + fm.getAscent();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int textX = x + (width - fm.stringWidth(line)) / 2;
            int textY = startY + i * fm.getHeight();
            g2d.drawString(line, textX, textY);
        }
        g2d.setFont(oldFont);
    }

    public void handleMousePressed(int mx, int my) {
        if (isInside(mx, my)) {
            action.run();
        }
    }

    public void handleMouseMoved(int mx, int my) {
        hovered = isInside(mx, my);
    }
}
