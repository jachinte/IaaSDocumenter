package com.eu.skyblue.iaasdocumenter.renderer.algo;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.renderer.UMLDeploymentDiagram;
import com.eu.skyblue.iaasdocumenter.uml.UMLStereotype;
import com.eu.skyblue.iaasdocumenter.utils.Coordinate;
import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 02/07/15
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public class OrthogonalLayoutAlgorithm implements Algorithm {
    private static int ROW_VPC = 2;
    private static int ROW_IGW = 1;
    private static int ROW_ROUTE_TABLE = 3;
    private static int ROW_VPC_ROUTER = 4;
    private static int ROW_ACL = 5;
    private static int ROW_ELB = 6;
    private static int ROW_SUBNET = 7;
    private static int ROW_EC2_INSTANCE = 8;
    private static int ROW_SECURITY_GROUP = 9;

    private static String D_ROW = "d-row";
    private static String D_COL = "d-col";

    private Graph vpcGraph;
    private Logger logger;

    private Set<Coordinate> coordinates;
    private int maxCol;
    private int diagramWidth;
    private int diagramHeight;
    private int lastRow;

    public OrthogonalLayoutAlgorithm(Logger logger) {
        this.logger = logger;
        this.coordinates =  new HashSet<Coordinate>();
        this.maxCol = 0;
        this.diagramHeight = 0;
        this.diagramWidth = 0;
        this.lastRow = ROW_SECURITY_GROUP;
    }

    @Override
    public void init(Graph graph) {
        this.vpcGraph = graph;
    }

    @Override
    public void compute() {
        dfsTraverse();
        calculateDiagramHeight();
        calculateDiagramWidth();

        logger.out("Height: %s, Width: %s", getDiagramHeght(), getDiagramWidth());
    }

    public int getDiagramHeght() {
        return this.diagramHeight;
    }

    public int getDiagramWidth() {
        return this.diagramWidth;
    }

    private int getRow(Node node) {
        int row = 0;
        String stereotype = node.getAttribute(AttributeName.STEREOTYPE);
        if (stereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) {
            row = ROW_EC2_INSTANCE;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.INTERNET_GATEWAY)) {
            row = ROW_IGW;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ROUTER)) {
            row = ROW_VPC_ROUTER;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ELASTIC_LB)) {
            row = ROW_ELB;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) {
            row = ROW_SECURITY_GROUP;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.NETWORK_ACL)) {
            row = ROW_ACL;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.SUBNET)) {
            row = ROW_SUBNET;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE)) {
            row = ROW_ROUTE_TABLE;
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.VPC)) {
            row = ROW_VPC;
        }
        return row;
    }

    // needs refactoring
    private boolean sameColumn(Node previousNode, Node currentNode) {
        String currentNodeStereotype = currentNode.getAttribute(AttributeName.STEREOTYPE);
        String previousNodeStereotype = previousNode.getAttribute(AttributeName.STEREOTYPE);

        if ((previousNodeStereotype.equalsIgnoreCase(UMLStereotype.VPC) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.INTERNET_GATEWAY)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.INTERNET_GATEWAY) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTER)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.ELASTIC_LB) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.NETWORK_ACL) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET))    ) {
            return true;
        }
        return false;
    }

    private boolean firstColumn(Node previousNode, Node currentNode) {
        // avoids null pointer exception
        if (previousNode == null) {
            return true;
        }
        String previousNodeStereotype = previousNode.getAttribute(AttributeName.STEREOTYPE);
        String currentNodeStereotype = currentNode.getAttribute(AttributeName.STEREOTYPE);

        // check for null first to avoid exception
        if (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE) &&
                (!currentNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE))) {
            return true;
        }
        return false;
    }

    private boolean nextColumn(Node previousNode, Node currentNode) {
        String previousNodeStereotype = previousNode.getAttribute(AttributeName.STEREOTYPE);
        String currentNodeStereotype = currentNode.getAttribute(AttributeName.STEREOTYPE);

        if ((previousNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTER) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) ||

            (previousNodeStereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP) &&
                currentNodeStereotype.equalsIgnoreCase(UMLStereotype.SUBNET)) ||

            currentNodeStereotype.equalsIgnoreCase(UMLStereotype.ELASTIC_LB) ||
            currentNodeStereotype.equalsIgnoreCase(UMLStereotype.NETWORK_ACL)) {
            return true;
        }

        return false;
    }


    // Code smell. Major need for refactoring!
    private int getCol(Node previousNode, Node currentNode) {
        int column = 0;

        if (firstColumn(previousNode, currentNode)) {
            //column = Integer.parseInt((String)previousNode.getAttribute(D_COL)) - 1;
            column = 1;
        } else if (sameColumn(previousNode, currentNode)) {
            //column = Integer.parseInt((String)previousNode.getAttribute(D_COL));
            column = (Integer)previousNode.getAttribute(D_COL);
        } else if (nextColumn(previousNode, currentNode)) {
            //column = Integer.parseInt((String)previousNode.getAttribute(D_COL)) + 1;
            column = (Integer)previousNode.getAttribute(D_COL) + 1;
        } else {
            // default if we don't know, so attempt to place it under the previous node
            column = (Integer)previousNode.getAttribute(D_COL);
        }

        return column;
    }

    private int rightShift(int row, int column) {
        int newColumn = column;
        while (coordinates.contains(new Coordinate(row, newColumn))) {
            newColumn++;
        }
        return newColumn;
    }

    private void dfsTraverse() {
        int i = 0;
        Node previousNode= null;
        String previousNodeId = "_notSet_";
        Iterator<Node> iterator = vpcGraph.getNode(vpcGraph.getId()).getDepthFirstIterator();
        while (iterator.hasNext()) {
            Node currentNode = iterator.next();

            int row = getRow(currentNode);
            currentNode.setAttribute(D_ROW, row);
            currentNode.setAttribute(UMLDeploymentDiagram.Y_COORDINATE, getYCoordinate(row));


            int column = rightShift(getRow(currentNode), getCol(previousNode, currentNode));
            currentNode.setAttribute(D_COL, column);
            currentNode.setAttribute(UMLDeploymentDiagram.X_COORDINATE, getXCoordinate(column));

            coordinates.add(new Coordinate(getRow(currentNode), column));

            logger.out("Index: %s, Previous Node: %s, Current Node: %s, Row: %s (%s), Column: %s (%s)",
                    i, previousNodeId, currentNode.getId(), getRow(currentNode), getYCoordinate(row),
                    column, getXCoordinate(column));

            i++;
            previousNode = currentNode;
            previousNodeId = currentNode.getId();
            this.maxCol = Math.max(this.maxCol, column);
        }
    }

    private int getYCoordinate(int row) {
        return ((row - 1) * UMLDeploymentDiagram.ROW_GAP_PIXELS) +
                ((row - 1) * UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS) +
                UMLDeploymentDiagram.VERTICAL_OFFSET;
    }

    private int getXCoordinate(int col) {
        return ((col - 1) * UMLDeploymentDiagram.COLUMN_GAP_PIXELS) +
                ((col - 1) * UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS) +
                UMLDeploymentDiagram.HORIZONTAL_OFFSET;
    }

    private void calculateDiagramHeight() {
        this.diagramHeight =  (this.lastRow * UMLDeploymentDiagram.ROW_GAP_PIXELS) +
                ((this.lastRow) * UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS);
    }

    private void calculateDiagramWidth() {
        this.diagramWidth = (this.maxCol * UMLDeploymentDiagram.COLUMN_GAP_PIXELS) +
                ((this.maxCol) * UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS);
    }
}
