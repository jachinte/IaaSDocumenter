package com.eu.skyblue.iaasdocumenter.documenter;

import org.graphstream.graph.Graph;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 08:52
 * To change this template use File | Settings | File Templates.
 */
public interface IaasDocumenter {
    public void createInventory();
    public List<Graph> getGraphs();
}
