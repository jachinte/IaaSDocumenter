package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.aws.MetaClass;
import com.eu.skyblue.iaasdocumenter.renderer.algo.OrthogonalLayoutAlgorithm;
import com.eu.skyblue.iaasdocumenter.utils.Logger;

import de.erichseifert.vectorgraphics2d.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 04/07/15
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGraphicalFormatRenderer {
    private Logger logger;
    //private VectorGraphics2D document;
    //private UMLDeploymentDiagram umlDeploymentDiagram;

    private OrthogonalLayoutAlgorithm layoutAlgorithm;

    public AbstractGraphicalFormatRenderer(Logger logger) {
        this.logger = logger;
        this.layoutAlgorithm = new OrthogonalLayoutAlgorithm(logger);
    }

    // Override in sub class
    public void save(String filePath, VectorGraphics2D document) {

        logger.out("In superclass");
    }

    // Override in subclass
    public UMLDeploymentDiagram createDiagram() {
        return  null;
    }

    // Override in subclass
    public void render(Graph graph, String filePath) {
//        this.layoutAlgorithm.init(graph);
//        this.layoutAlgorithm.compute();
//
//        UMLDeploymentDiagram umlDeploymentDiagram = this.createDiagram();
//
//        placeArtefacts(graph, umlDeploymentDiagram);
//        save(filePath, (VectorGraphics2D)umlDeploymentDiagram.getDocument());
    }

    protected void placeArtefacts(Graph vpcGraph, UMLDeploymentDiagram umlDeploymentDiagram) {
        // Place nodes on diagram
        for(Node node : vpcGraph.getEachNode() ) {
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
                            node.getId(), "{}");

                } else if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.ARTEFACT)) {
                    umlDeploymentDiagram.drawArtefact((Integer) node.getAttribute(UMLDeploymentDiagram.X_COORDINATE),
                            (Integer) node.getAttribute(UMLDeploymentDiagram.Y_COORDINATE),
                            UMLDeploymentDiagram.ARTEFACT_WIDTH_PIXELS,
                            UMLDeploymentDiagram.ARTEFACT_HEIGHT_PIXELS,
                            (String) node.getAttribute(AttributeName.STEREOTYPE),
                            node.getId(), "{}");

                } else {
                    logger.err("ERROR: Skipped node %s - Invalid metaclass (%s)", node.getId(), (String)node.getAttribute(AttributeName.METACLASS));
                }
            }
        }

        // Place edges on diagrams (TBC)
    }

    public OrthogonalLayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }
}
