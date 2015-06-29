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
    private Graphics2D diagram;
    private int xNodeOffset;
    private int yNodeOffset;
    private Font font;
    private int fontSize;
    FontMetrics fontMetrics;

    protected UMLDeploymentDiagram(Graphics2D g) {
        this.diagram = g;
        this.xNodeOffset = 2;
        this.yNodeOffset = 2;
        this.fontSize = 4;
        this.font = new Font("serif", Font.PLAIN, this.fontSize);
        this.diagram.setFont(this.font);
        this.fontMetrics = this.diagram.getFontMetrics();
    }

    protected void drawNode(int x, int y, int width, int height, String stereotype, String elementId, String attributes) {
        this.diagram.drawRect(x, y, width, height);

        this.diagram.drawLine(x,y, x+xNodeOffset, y-yNodeOffset);
        this.diagram.drawLine(x+xNodeOffset, y-yNodeOffset, x+xNodeOffset+width, y-yNodeOffset);
        this.diagram.drawLine(x+xNodeOffset+width, y-yNodeOffset, x+width, y);
        this.diagram.drawLine(x+xNodeOffset+width, y-yNodeOffset, x+xNodeOffset+width, y-yNodeOffset+height);
        this.diagram.drawLine(x+width, y+height, x+xNodeOffset+width, y-yNodeOffset+height);

        renderNodeText(x, y, width, stereotype, elementId, attributes);
    }

    protected void renderNodeText(int x, int y, int width, String stereotype, String elementId, String attributes) {
        List<String> nodeAttributes = Arrays.asList(stereotype, elementId, attributes);
        int stringOffsetY = y;
        for (String attribute: nodeAttributes) {
            int stringWidth = fontMetrics.stringWidth(attribute);
            int stringOffsetX = x + (width/2) - (stringWidth/2);
            stringOffsetY += fontSize + yNodeOffset;
            this.diagram.drawString(attribute, stringOffsetX, stringOffsetY);
        }
    }

    protected void drawArtefact(int x, int y, int width, int height, String stereotype, String elementId,
                             String attributes) {
        this.diagram.drawRect(x, y, width, height);
        renderArtefactText(x, y, width, stereotype, elementId, attributes);
    }

    private void renderArtefactText(int x, int y, int width, String stereotype, String elementId, String attributes) {
        List<String> artefactAttributes = Arrays.asList(stereotype, elementId, attributes);
        int stringOffsetY = y;
        for (String attribute: artefactAttributes) {
            int stringWidth = fontMetrics.stringWidth(attribute);
            int stringOffsetX = x + (width/2) - (stringWidth/2);
            stringOffsetY += fontSize + yNodeOffset;
            this.diagram.drawString(attribute, stringOffsetX, stringOffsetY);
        }
    }
}
