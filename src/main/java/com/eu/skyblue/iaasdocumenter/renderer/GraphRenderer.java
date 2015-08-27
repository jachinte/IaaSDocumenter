package com.eu.skyblue.iaasdocumenter.renderer;

import org.graphstream.graph.Graph;

/**
 * Specifies the operations a graph renderer should implement
 */
public interface GraphRenderer {

    /**
     * Write out the graph representation to a file
     *
     * @param graph      Graph representing AWS cloud configuration.
     * @param filePath   The filepath for the output.
     */
    public void render(Graph graph, String filePath);
}
