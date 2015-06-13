package com.eu.skyblue.iaasdocumenter;

import com.amazonaws.regions.Regions;
import com.eu.skyblue.iaasdocumenter.documenter.IaasDocumenter;
import com.eu.skyblue.iaasdocumenter.documenter.aws.DocumenterFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.view.Viewer;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static IaasDocumenter iaasDocumenter;
    public static void main(String[] args) {
        System.out.println("*** IaaSDocumenter! ***");

        try {
            iaasDocumenter = DocumenterFactory.createIaasDocumenter(Regions.US_WEST_2);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        iaasDocumenter.createInventory();

        // Debug stuff - should be moved into a renderer
        List<Graph> g = iaasDocumenter.getGraphs();
        for (Graph graph : iaasDocumenter.getGraphs()) {
            Viewer v = graph.display(false);
            HierarchicalLayout hl = new HierarchicalLayout();
            v.enableAutoLayout(hl);

            Iterator<Node> nodeIterator = graph.iterator();
            while (nodeIterator.hasNext()) {
                System.out.println(nodeIterator.next().getId());
            }
            System.out.println("--------------------------------------------");
        }

    }
}
