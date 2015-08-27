package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.MetaClass;
import com.eu.skyblue.iaasdocumenter.renderer.algo.AWSInfrastructureDeploymentDiagramLayoutAlgorithm;
import com.eu.skyblue.iaasdocumenter.uml.UMLPrimitiveType;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Graph renderer. Provides functionality inherited by concrete implementations
 * of GraphRenderer.
 */
public abstract class AbstractGraphicalFormatRenderer {
    private Logger logger;
    private Map<String, String> selectedAttributeNames;

    private AWSInfrastructureDeploymentDiagramLayoutAlgorithm layoutAlgorithm;

    /**
     * Constructs a new <code>AbstractGraphicalFormatRenderer</code> object.
     *
     * @param logger         Logger
     */
    public AbstractGraphicalFormatRenderer(Logger logger) {
        this.logger = logger;
        this.layoutAlgorithm = new AWSInfrastructureDeploymentDiagramLayoutAlgorithm(logger);
        this.selectedAttributeNames = new HashMap<String, String>();
        selectedAttributeNames.put(AttributeName.VPC, UMLPrimitiveType.STRING);
        selectedAttributeNames.put(AttributeName.CIDR_BLOCK, UMLPrimitiveType.STRING);
        selectedAttributeNames.put(AttributeName.NAME, UMLPrimitiveType.STRING);
    }

    /**
     * Creates a new UML deployment diagram. Override in subclass.
     *
     * @return Deployment diagram.
     */
    public UMLDeploymentDiagram createDiagram() { return null; };


    /**
     * Write out a representation of the graph. Override in subclass.
     *
     * @param graph      Graph representing AWS cloud configuration.
     * @param filePath   Filepath for representation.
     */
    public void render(Graph graph, String filePath)  {};

    /**
     * Position the artefacts from the specified graph on the specified deployment diagram.
     *
     * @param vpcGraph               Graph representing AWS cloud configuration.
     * @param umlDeploymentDiagram   Deployment diagrram.
     */
    protected void placeArtefacts(Graph vpcGraph, UMLDeploymentDiagram umlDeploymentDiagram) {

        logger.out("Drawing nodes on diagram", "");
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

        logger.out("Drawing associations on diagram", "");
        for (Edge edge: vpcGraph.getEachEdge()) {
            Coordinate upperLineCoordinate = getLineUpperCoordinate(edge.getNode0(), edge.getNode1());
            Coordinate lowerLineCoordinate = getLineLowerCoordinate(edge.getNode0(), edge.getNode1());
            umlDeploymentDiagram.drawAssociation(upperLineCoordinate.getX(), upperLineCoordinate.getY(),
                    lowerLineCoordinate.getX(), lowerLineCoordinate.getY(),
                    (String)edge.getAttribute(AttributeName.STEREOTYPE));

            logger.out("Draw assoc Node %s (%s,%s) - Node %s (%s,%s)", edge.getNode0().getId(),
                    upperLineCoordinate.getX(),  upperLineCoordinate.getY(), edge.getNode1().getId(),
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
        return attributeValues.toString();
    }

    private Node getUpperNode(Node node0, Node node1) {
        if ((Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE) <
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE)) {

            return node0;
        }

        return node1;
    }

    private Node getLowerNode(Node node0, Node node1) {
        if ((Integer)node0.getAttribute(UMLDeploymentDiagram.Y_COORDINATE) >
                (Integer)node1.getAttribute(UMLDeploymentDiagram.Y_COORDINATE)) {

            return node0;
        }

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

    /**
     * Returns the layout algorithm.
     *
     * @return Layout algorithm
     */
    public AWSInfrastructureDeploymentDiagramLayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }
}
