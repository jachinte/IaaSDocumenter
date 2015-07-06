package com.eu.skyblue.iaasdocumenter.renderer;

import java.util.Arrays;
import java.util.List;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 30/06/15
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */
public class UMLDeploymentDiagram {
    public static final int ARTEFACT_HEIGHT_PIXELS = 25; // 30
    public static final int ARTEFACT_WIDTH_PIXELS = 65;

    public static String X_COORDINATE = "xCoordinate";
    public static String Y_COORDINATE = "yCoordinate";

    public static final int ROW_GAP_PIXELS = 20; //30
    public static final int COLUMN_GAP_PIXELS = 40; //65

    public static final int HORIZONTAL_OFFSET = 10;
    public static final int VERTICAL_OFFSET = 10;


    public static final int X_NODE_OFFSET = 2;
    public static final int Y_NODE_OFFSET = 2;

    public static final int FONT_SIZE = 4;

    private Font font;
    FontMetrics fontMetrics;
    private Graphics2D document;

    protected UMLDeploymentDiagram(Graphics2D document) {
        this.document = document;
        this.font = new Font("serif", Font.PLAIN, FONT_SIZE);
        this.document.setFont(this.font);
        this.fontMetrics = this.document.getFontMetrics();
    }

    private String addGuillemets(String string) {
        return "«" + string + "»";
    }
    protected void drawNode(int x, int y, int width, int height, String stereotype, String elementId, String attributes) {
        this.document.drawRect(x, y, width, height);

        this.document.drawLine(x, y, x + X_NODE_OFFSET, y - Y_NODE_OFFSET);
        this.document.drawLine(x + X_NODE_OFFSET, y- Y_NODE_OFFSET, x+ X_NODE_OFFSET +width, y- Y_NODE_OFFSET);
        this.document.drawLine(x + X_NODE_OFFSET + width, y - Y_NODE_OFFSET, x + width, y);
        this.document.drawLine(x + X_NODE_OFFSET + width, y - Y_NODE_OFFSET, x + X_NODE_OFFSET +width,
                y - Y_NODE_OFFSET + height);
        this.document.drawLine(x + width, y + height, x + X_NODE_OFFSET + width, y - Y_NODE_OFFSET + height);

        renderNodeText(x, y, width, addGuillemets(stereotype), elementId, attributes);
    }

    protected void renderNodeText(int x, int y, int width, String stereotype, String elementId, String attributes) {
        List<String> nodeAttributes = Arrays.asList(stereotype, elementId, attributes);
        int stringOffsetY = y;
        for (String attribute: nodeAttributes) {
            int stringWidth = fontMetrics.stringWidth(attribute);
            int stringOffsetX = x + (width/2) - (stringWidth/2);
            stringOffsetY += FONT_SIZE + Y_NODE_OFFSET;
            this.document.drawString(attribute, stringOffsetX, stringOffsetY);
        }
    }

    protected void drawAssociation(int x1, int y1, int x2, int y2, String stereotype) {
        this.document.drawLine(x1, y1, x2, y2);
    }

    protected void drawArtefact(int x, int y, int width, int height, String stereotype, String elementId,
                             String attributes) {
        this.document.drawRect(x, y, width, height);
        renderArtefactText(x, y, width, addGuillemets(stereotype), elementId, attributes);
    }

    private void renderArtefactText(int x, int y, int width, String stereotype, String elementId, String attributes) {
        List<String> artefactAttributes = Arrays.asList(stereotype, elementId, attributes);
        int stringOffsetY = y;
        for (String attribute: artefactAttributes) {
            int stringWidth = fontMetrics.stringWidth(attribute);
            int stringOffsetX = x + (width/2) - (stringWidth/2);
            stringOffsetY += FONT_SIZE + Y_NODE_OFFSET;
            this.document.drawString(attribute, stringOffsetX, stringOffsetY);
        }
    }

    public Graphics2D getDocument() {
        return document;
    }
}
