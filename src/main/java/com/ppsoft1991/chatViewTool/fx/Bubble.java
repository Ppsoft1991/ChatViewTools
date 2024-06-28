package com.ppsoft1991.chatViewTool.fx;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Bubble extends Group {

    private static final int p = 10; // Padding around the text
    private static final int s = 2;  // Side space around text
    private static final int ps2 = 2*(p+s);
    private static final int p2 = 2*p;
    private static final int pm = 10; // Padding around meta text
    private static final int pm2 = 2*pm;
    private static final int sm = 2;  // Side space around meta text
    private static final Font textFont = Font.font("Arial", 14);
    private static final Paint textColor = Color.WHITE;
    private static final Font metaFont = Font.font("Arial", 10);
    private static final Paint metaColor = Color.LIGHTGRAY;

    private Rectangle r;

    private Paint bubbleColor = Color.rgb(0, 126, 229);

    private int edgeRadius = 30;

    private double maxWidth = 260; // Default max width for the bubble

    public Bubble(String text) {
        super();
        init(0, 0, text, "");
    }

    public Bubble(String text, String meta, boolean quadratic) {
        super();
        init(0, 0, text, meta);
        if (quadratic) {
            r.setHeight(r.getWidth());
        }
    }

    public Bubble(String text, boolean quadratic) {
        this(text, "", quadratic);
    }

    public Bubble(String text, String meta) {
        super();
        init(0, 0, text, meta);
    }

    public Bubble(int x, int y, String text, String meta) {
        super();
        init(x, y, text, meta);
    }

    private void init(int x, int y, String text, String meta) {
        Text temp = new Text(text);
        temp.setFont(textFont);
        temp.setWrappingWidth(maxWidth - ps2); // Set wrapping width for measurement

        Label l = new Label(text);
        l.setFont(textFont);
        l.setTextFill(textColor);
        l.setWrapText(true); // Enable text wrapping
        l.setMaxWidth(maxWidth - ps2); // Set maximum width for text
        l.setTranslateX(x + p + s);
        l.setTranslateY(y + p);

        Text tmp = new Text(meta);
        tmp.setFont(metaFont);
        Label m = new Label(meta);
        m.setFont(metaFont);
        m.setTextFill(metaColor);
        m.setTranslateX(x + p + s);
        m.setTranslateY(y + temp.getLayoutBounds().getHeight() + pm2);

        int w = (int) maxWidth;
        double wTmp = tmp.getLayoutBounds().getWidth();
        if (wTmp > w) {
            w = (int) wTmp + pm2;
        }
        int h = (int) (temp.getLayoutBounds().getHeight() + tmp.getLayoutBounds().getHeight() + p2 + pm2);

        r = new Rectangle(x, y, w, h);
        r.setArcHeight(edgeRadius);
        r.setArcWidth(edgeRadius);
        r.setFill(bubbleColor);

        getChildren().addAll(r, l, m);
    }




    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
        // Adjust layout based on new width if necessary
    }

    public double getMaxWidth() {
        return this.maxWidth;
    }

    public void setBubbleColor(Color color) {
        bubbleColor = color;
    }

    // Other getters and setters remain the same
}
