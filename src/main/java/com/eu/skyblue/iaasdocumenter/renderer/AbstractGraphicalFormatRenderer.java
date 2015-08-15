package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.aws.MetaClass;
import com.eu.skyblue.iaasdocumenter.renderer.algo.AWSInfrastructureDeploymentDiagramLayoutAlgorithm;
import com.eu.skyblue.iaasdocumenter.uml.UMLPrimitiveType;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

import de.erichseifert.vectorgraphics2d.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 04/07/15
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGraphicalFormatRenderer {
    private Logger logger;
    private Map<String, String> selectedAttributeNames;

    private AWSInfrastructureDeploymentDiagramLayoutAlgorithm layoutAlgorithm;

    public AbstractGraphicalFormatRenderer(Logger logger) {
        this.logger = logger;
        this.layoutAlgorithm = new AWSInfrastructureDeploymentDiagramLayoutAlgorithm(logger);
        this.selectedAttributeNames = new HashMap<String, String>();
        selectedAttributeNames.put(AttributeName.VPC, UMLPrimitiveType.STRING);
        selectedAttributeNames.put(AttributeName.CIDR_BLOCK, UMLPrimitiveType.STRING);
        selectedAttributeNames.put(AttributeName.NAME, UMLPrimitiveType.STRING);
    }

    // Override in sub class
    public void save(String filePath, VectorGraphics2D document) {
    }

    // Override in subclass
    public UMLDeploymentDiagram createDiagram() {
        return  null;
    }

    // Override in subclass
    public void render(Graph graph, String filePath) {
    }

    protected void placeArtefacts(Graph vpcGraph, UMLDeploymentDiagram umlDeploymentDiagram) {
        // Place nodes on diagram
        for(Node node: vpcGraph.getEachNode() ) {
            if (node.getAttribute(UMLDeploymentDiagram.X_COORDINATE) == null) {
                logger.out("Excluding graph node %s from diagram (not linked to anything)", node.getId());
            } else {
                logger.out("Addding %s («%s») to diagram", node.getAttribute(AttributeName.METACLASS), node.getId(),
                        node.getAttribute(AttributeName.STEREOTYPE));
                if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.NODE)) {
                    umlDeploymentDiagram.drawNode((Integer)node.getAttribute(UMLDeploymentDiagram.X_COORDINATE),
                            (Integer)node.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                            UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS,
                            UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS,
                            (String)node.getAttribute(AttributeName.STEREOTYPE),
                            node.getId(), getSelectedAttributes(node));

                } else if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.ARTEFACT)) {
                    umlDeploymentDiagram.drawArtefact((Integer) node.getAttribute(UMLDeploymentDiagram.X_COORDINATE),
                            (Integer) node.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                            UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS,
                            UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS,
                            (String) node.getAttribute(AttributeName.STEREOTYPE),
                            node.getId(), getSelectedAttributes(node));

                } else {
                    logger.err("ERROR: Skipped node %s - Invalid metaclass (%s)",
                            node.getId(), (String)node.getAttribute(AttributeName.METACLASS));
                }
            }
        }

        for (Edge edge: vpcGraph.getEachEdge()) {
            logger.out("Node 0: %s, Node 1: %s", edge.getNode0().getId(), edge.getNode1().getId());
            Coordinate upperLineCoordinate = getLineUpperCoordinate(edge.getNode0(), edge.getNode1());
            Coordinate lowerLineCoordinate = getLineLowerCoordinate(edge.getNode0(), edge.getNode1());
            umlDeploymentDiagram.drawAssociation(upperLineCoordinate.getX(), upperLineCoordinate.getY(),
                    lowerLineCoordinate.getX(), lowerLineCoordinate.getY(),
                    (String)edge.getAttribute(AttributeName.STEREOTYPE));

            logger.out("%s,%s - %s,%s", upperLineCoordinate.getX(), upperLineCoordinate.getY(),
                    lowerLineCoordinate.getX(), lowerLineCoordinate.getY());
        }
    }

    private String getSelectedAttributes(Node node) {
        StringBuffer attributeValues = new StringBuffer("{");
        for (String attributeName: node.getEachAttributeKey()) {
            if (selectedAttributeNames.containsKey(attributeName)) {
                String attributeType = (String)selectedAttributeNames.get(attributeName);
                if (attributeType.equalsIgnoreCase(UMLPrimitiveType.STRING)) {
                    attributeValues.append(attributeName + ": " + (String)node.getAttribute(attributeName));
                } else if (attributeType.equalsIgnoreCase(UMLPrimitiveType.BOOLEAN)) {
                    Boolean booleanValue = (Boolean)node.getAttribute(attributeName);
                    attributeValues.append(attributeName + ": " + booleanValue.toString());
                }
                attributeValues.append(",");
            }
        }
        attributeValues.deleteCharAt(attributeValues.length() - 1);
        if (attributeValues.length() > 0) {
            attributeValues.append("}");
        }
        //attributeValues.append("}");
        return attributeValues.toString();
    }

    private Node getUpperNode(Node node0, Node node1) {
        if ((Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE) <
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE)) {
            logger.out("Upper node: %s, (%s,%s)", node0.getId(),
                    (Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                    (Integer)node0.getAttribute(UMLDeploymentDiagram.X_COORDINATE));
            return node0;
        }

        logger.out("Upper node: %s, (%s,%s)", node1.getId(),
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                (Integer)node1.getAttribute(UMLDeploymentDiagram.X_COORDINATE));
        return node1;
    }

    private Node getLowerNode(Node node0, Node node1) {
        if ((Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE) >
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE)) {
            logger.out("Lower node: %s, (%s,%s)", node0.getId(),
                    (Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                    (Integer)node0.getAttribute(UMLDeploymentDiagram.X_COORDINATE));
            return node0;
        }

        logger.out("Lower node: %s, (%s,%s)", node1.getId(),
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                (Integer)node1.getAttribute(UMLDeploymentDiagram.X_COORDINATE));
        return node1;
    }

    private Coordinate getLineUpperCoordinate(Node node0, Node node1) {
        Node upperNode = getUpperNode(node0, node1);

        int xCoordinate = (Integer)upperNode.getAttribute(UMLDeploymentDiagram.X_COORDINATE) +
                (UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS / 2);

        int yCoordinate = (Integer)upperNode.getAttribute(UMLDeploymentDiagram.Y_COORDINATE) +
                UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS;
        return new Coordinate(xCoordinate, yCoordinate);
    }

    private Coordinate getLineLowerCoordinate(Node node0, Node node1) {
        Node lowerNode = getLowerNode(node0, node1);

        int xCoordinate = (Integer)lowerNode.getAttribute(UMLDeploymentDiagram.X_COORDINATE) +
                (UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS / 2);

        int yCoordinate = (Integer)lowerNode.getAttribute(UMLDeploymentDiagram.Y_COORDINATE);
        if (((String)lowerNode.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.NODE)) {
            yCoordinate -= UMLDeploymentDiagram.Y_NODE_OFFSET;
        }
        return new Coordinate(xCoordinate, yCoordinate);
    }

    public AWSInfrastructureDeploymentDiagramLayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }
}
