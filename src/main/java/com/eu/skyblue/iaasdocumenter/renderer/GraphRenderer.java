package com.eu.skyblue.iaasdocumenter.renderer;

import org.graphstream.graph.Graph;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public interface GraphRenderer {
    public void render(Graph graph, String filePath);
}
