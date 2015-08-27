package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.utils.Logger;

import de.erichseifert.vectorgraphics2d.*;

import org.graphstream.graph.Graph;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Renders the infrastructure graph in SVG format.
 */
public class SVGFormatRenderer extends AbstractGraphicalFormatRenderer implements GraphRenderer {
    private static final String SVG_FILE_EXTENSION = ".svg";
    private Logger logger;

    /**
     * Constructs a new <code>SVGFormatRenderer</code> object.
     *
     * @param logger         Logger
     */
    public SVGFormatRenderer(Logger logger) {
        super(logger);
        this.logger = logger;
    }

    /**
     * Creates a new UML deployment diagram. Override in subclass.
     *
     * @return Deployment diagram.
     */
    public UMLDeploymentDiagram createDiagram() {
        SVGGraphics2D document = new SVGGraphics2D(0.0, 0.0, getLayoutAlgorithm().getDiagramWidth(),
                getLayoutAlgorithm().getDiagramHeght());
        document.setColor(Color.darkGray);
        document.setStroke(new BasicStroke(0.4F));
        return new UMLDeploymentDiagram(document);
    }

    /**
     * Write out the SVG representation of the graph.
     *
     * @param graph      Graph representing AWS cloud configuration.
     * @param filePath   The filepath for the SVG file.
     */
    @Override
    public void render(Graph graph, String filePath) {
        super.render(graph, filePath);
        getLayoutAlgorithm().init(graph);
        getLayoutAlgorithm().compute();

        UMLDeploymentDiagram umlDeploymentDiagram = this.createDiagram();

        placeArtefacts(graph, umlDeploymentDiagram);
        save(filePath, (SVGGraphics2D)umlDeploymentDiagram.getDocument());
    }

    private void save(String filePath, SVGGraphics2D document) {
        filePath = filePath + SVG_FILE_EXTENSION;
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(filePath);
            file.write(document.getBytes());
        } catch (IOException e) {
            logger.err("IOException: %s, while writing file", e.getMessage(), filePath);
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                logger.err("IOException: %s, while closing file: %s", e.getMessage(), filePath);
            }
        }
    }
}
