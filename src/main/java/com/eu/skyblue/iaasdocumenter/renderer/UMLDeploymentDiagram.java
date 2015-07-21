package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.uml.UMLStereotype;

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

    public static final int DOG_EARED_SHEET_WIDTH = 5;
    public static final int DOG_EARED_SHEET_VERTICAL_FOLD_HEIGHT = 5;
    public static final int DOG_EARED_SHEET_HORIZONTAL_FOLD_WIDTH = 3;
    public static final int DOG_EARED_SHEET_HEIGHT = 7;

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
        Stroke originalStroke = document.getStroke();
        if (stereotype.equalsIgnoreCase(UMLStereotype.DEPLOYMENT)) {
            Stroke dashed = new BasicStroke(0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.f, new float[]{2.5f, 1.5f}, 0);
            document.setStroke(dashed);
        }
        document.drawLine(x1, y1, x2, y2);
        document.setStroke(originalStroke);
        if (stereotype.equalsIgnoreCase(UMLStereotype.DEPLOYMENT)) {
            this.drawArrowhead2(x1, y1, x2, y2);
        }
        renderAssociationStereotypeText(x1, y1, x2, y2, addGuillemets(stereotype));
    }

//    private void drawArrowhead2(int x1, int y1, int x2, int y2) {
//        int dx = x1 - x2;
//        int dy = y1 - y2;
//
//        double dist = Math.sqrt(dx * dx + dy * dy);
//        int offset = 8;
//
//        double nX = dx / dist;
//        double nY = dy / dist;
//
//        double xP = offset * nX;
//        double yP = offset * nY;
//
//        int d2x = dx + (int)yP;
//        int d2y = dy - (int)xP;
//
//        int d3x = dx - (int)yP;
//        int d3y = dy + (int)xP;
//
//        this.document.drawLine(x2,y2, d3x,d3y);
//
//    }

    // http://stackoverflow.com/questions/1800138/given-a-start-and-end-point-and-a-distance-calculate-a-point-along-a-line
    private void drawArrowhead2(int x1, int y1, int x2, int y2) {
        double vx = x2 - x1;
        double vy = y2 - y1;

        double distance = 10;

        double len = Math.sqrt(vx * vx + vy * vy);

        vx = vx / len;
        vy = vy / len;

        int px = (int) Math.abs(((double) x2 + vx * (len + (double)distance)));
        int py = (int) Math.abs(((double) y2 + vy * (len + (double)distance)));
        System.out.println("********** X = " + px + ", Y=" + py);
        System.out.println("********** X1 = " + x1 + ", Y1=" + y1);
        System.out.println("********** X2 = " + x2 + ", Y2=" + y2);
        this.document.drawLine(px,py, 0,0);
    }

    protected void drawArtefact(int x, int y, int width, int height, String stereotype, String elementId,
                             String attributes) {
        this.document.drawRect(x, y, width, height);
        drawDogearedSheetForArtefact(x, y);
        renderArtefactText(x, y, width, addGuillemets(stereotype), elementId, attributes);
    }

    private void drawDogearedSheetForArtefact(int x, int y) {
        int leftmostXCoord = x + ARTEFACT_WIDTH_PIXELS - DOG_EARED_SHEET_WIDTH - X_NODE_OFFSET;
        int rightmostXCoord = x + ARTEFACT_WIDTH_PIXELS - X_NODE_OFFSET;

        int topmostYCoord = y + Y_NODE_OFFSET;
        int bottomYCoord = y + Y_NODE_OFFSET + DOG_EARED_SHEET_HEIGHT;

        int foldPointY = bottomYCoord - DOG_EARED_SHEET_VERTICAL_FOLD_HEIGHT;
        int foldPointX = rightmostXCoord - DOG_EARED_SHEET_HORIZONTAL_FOLD_WIDTH;

        this.document.drawLine(leftmostXCoord, topmostYCoord, leftmostXCoord, bottomYCoord);
        this.document.drawLine(leftmostXCoord, bottomYCoord, rightmostXCoord, bottomYCoord);

        this.document.drawLine(rightmostXCoord, bottomYCoord, rightmostXCoord, foldPointY);
        this.document.drawLine(leftmostXCoord, topmostYCoord, foldPointX, topmostYCoord);

        this.document.drawLine(foldPointX, topmostYCoord, rightmostXCoord, foldPointY);

        this.document.drawLine(foldPointX, topmostYCoord, foldPointX, foldPointY);
        this.document.drawLine(foldPointX, foldPointY, rightmostXCoord, foldPointY);
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

    private void renderAssociationStereotypeText(int x1, int y1, int x2, int y2, String stereotype) {
        int stringOffsetY = getMidpoint(y1,y2);
        int stringOffsetX = getMidpoint(x1,x2);
        int stringWidth = fontMetrics.stringWidth(stereotype);
        stringOffsetX = stringOffsetX - (stringWidth/2);
        this.document.drawString(stereotype, stringOffsetX, stringOffsetY);
    }

    private int getMidpoint(int p1, int p2) {
        return Math.max(p1, p2) - ((Math.max(p1,p2) - Math.min(p1,p2))/2);
    }

    public Graphics2D getDocument() {
        return document;
    }
}
