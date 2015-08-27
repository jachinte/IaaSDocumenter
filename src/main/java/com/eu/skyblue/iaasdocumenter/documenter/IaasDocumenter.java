package com.eu.skyblue.iaasdocumenter.documenter;

import org.graphstream.graph.Graph;

import java.util.List;

/**
 * Specifies the operations an IaaSDocumenter should implement
 */
public interface IaasDocumenter {
    /**
     * Create an inventory of the provisioned IaaS artefacts
     */
    public void createInventory();

    /**
     * Return a list of private cloud containers
     * @return List of private cloud containers
     */
    public List<Graph> getGraphs();
}
