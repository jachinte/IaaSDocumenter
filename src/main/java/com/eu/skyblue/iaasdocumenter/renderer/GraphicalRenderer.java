package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.uml.UMLStereotype;
import com.eu.skyblue.iaasdocumenter.utils.Logger;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.lang.Math;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 28/06/15
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class GraphicalRenderer implements Algorithm, GraphRenderer{
    private static final String UNATTACHED = "UNATTACHED";
    private static final int ARTEFACT_HEIGHT = 30;
    private static final int ARTEFACT_WIDTH = 65;
    private static final int ROW_GAP = 30;
    private static final int COLUMN_GAP = 65;
    private Graph vpcGraph;
    private Logger logger;
    private Map<String, Integer> artefactTypeMap;

    public GraphicalRenderer(Logger logger) {
        this.logger = logger;
        this.artefactTypeMap = new HashMap<String, Integer>();
    }

    // note: format factory to get correct graphics engine object

    @Override
    public void init(Graph graph) {
        this.vpcGraph = graph;
        artefactTypeMap.put(UMLStereotype.VPC, 0);
        artefactTypeMap.put(UMLStereotype.ROUTER, 0);
        artefactTypeMap.put(UMLStereotype.INTERNET_GATEWAY, 0);
        artefactTypeMap.put(UMLStereotype.NETWORK_ACL, 0);
        artefactTypeMap.put(UMLStereotype.SECURITY_GROUP, 0);
        artefactTypeMap.put(UMLStereotype.EC2_INSTANCE, 0);
        artefactTypeMap.put(UMLStereotype.ELASTIC_LB, 0);
        artefactTypeMap.put(UMLStereotype.ROUTE_TABLE, 0);
        artefactTypeMap.put(UMLStereotype.SUBNET, 0);
        artefactTypeMap.put(UNATTACHED, 0);
    }

    private int getVpcPixelWidthAlloc() {
        int vpcCount =  artefactTypeMap.get(UMLStereotype.VPC);
        return vpcCount * ARTEFACT_WIDTH;
    }

    private int getRouterPixelWidthAlloc() {
        int routerCount = artefactTypeMap.get(UMLStereotype.ROUTER);
        return routerCount * ARTEFACT_WIDTH;
    }

    private int getRouteTablePixelWidthAlloc() {
        int rtCount = artefactTypeMap.get(UMLStereotype.ROUTE_TABLE);
        return rtCount * ARTEFACT_WIDTH;
    }

    private int getInternetGatewayPixelWidthAlloc() {
        int igwCount =  artefactTypeMap.get(UMLStereotype.INTERNET_GATEWAY);
        return igwCount * ARTEFACT_WIDTH;
    }

    private int getNeworkAclPixelWidthAlloc() {
        int networkAclCount = artefactTypeMap.get(UMLStereotype.NETWORK_ACL);
        return (networkAclCount * ARTEFACT_WIDTH) + ((networkAclCount - 1) * COLUMN_GAP);
    }

    private int getSecurityGroupPixelWidthAlloc() {
        int sgCount =  artefactTypeMap.get(UMLStereotype.SECURITY_GROUP);
        int subnetCount = artefactTypeMap.get(UMLStereotype.SUBNET);
        return (sgCount * ARTEFACT_HEIGHT) +
                ((sgCount - 1) * COLUMN_GAP) +
                ((subnetCount - 1) * COLUMN_GAP);
    }

    private int getEc2InstancePixelWidthAlloc() {
        int ec2Count =  artefactTypeMap.get(UMLStereotype.EC2_INSTANCE);
        int subnetCount = artefactTypeMap.get(UMLStereotype.SUBNET);
        return (ec2Count * ARTEFACT_HEIGHT) +
                ((ec2Count - 1) * COLUMN_GAP) +
                ((subnetCount - 1) * COLUMN_GAP);
    }

    private int getElasticLbPixelWidthAlloc() {
        int elbCount =  artefactTypeMap.get(UMLStereotype.ELASTIC_LB);
        int subnetCount = artefactTypeMap.get(UMLStereotype.SUBNET);
        return (elbCount * ARTEFACT_HEIGHT) +
                ((elbCount - 1) * COLUMN_GAP) +
                ((subnetCount - 1) * COLUMN_GAP);
    }

    private int getSubnetPixelWidthAlloc() {
        int subnetCount = artefactTypeMap.get(UMLStereotype.SUBNET);
        int networkAclCount = artefactTypeMap.get(UMLStereotype.NETWORK_ACL);
        return (subnetCount * ARTEFACT_WIDTH) +
                ((subnetCount - 1) * COLUMN_GAP) +
                ((networkAclCount - 1) * COLUMN_GAP);
    }

    private int getUnattachedPixelWidthAlloc() {
        return (artefactTypeMap.get(UNATTACHED) * ARTEFACT_WIDTH) +
                ((artefactTypeMap.get(UNATTACHED) - 1) * COLUMN_GAP);
    }

    // Calculate diagram dimensions
    @Override
    public void compute() {
        // Populate artefactTypeMap with number of artefacts of each type
        for (Node node: vpcGraph.getEachNode()) {
            logger.out("Node: %s -> %s -> %s", node.getId(), node.getAttribute(AttributeName.METACLASS),
                    node.getAttribute(AttributeName.STEREOTYPE));
            if (node.getEdgeSet().size() > 0) {
                int currentCount = artefactTypeMap.get(node.getAttribute(AttributeName.STEREOTYPE));
                artefactTypeMap.put((String)node.getAttribute(AttributeName.STEREOTYPE), new Integer(currentCount + 1));
            } else {
                int currentCount = artefactTypeMap.get(UNATTACHED);
                artefactTypeMap.put((String)node.getAttribute(UNATTACHED), new Integer(currentCount + 1));
            }
        }
    }

    @Override
    public void render(Graph graph, String folderPath) {
        this.init(graph);
        this.compute();
        this.createDiagram(folderPath);

        logger.out("Diagram Height: %s", getDiagramHeight());
        logger.out("Diagram Width: %s", getDiagramWidth());
        dfsTraverse();
    }

    private int getDiagramHeight() {
        int rows = 0;
        int diagramHeight;
        Set s = artefactTypeMap.keySet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            String key = (String)i.next();
            if (artefactTypeMap.get(key) > 0) {
                rows++;
            }
            logger.out("[k] %s -> [v] %s", key, artefactTypeMap.get(key));
        }
        diagramHeight = (rows * ARTEFACT_HEIGHT) + (rows - 1) * ROW_GAP * 2;
        return diagramHeight;
    }

    private int getDiagramWidth() {
        List<Integer> artefactAttributes = Arrays.asList(getVpcPixelWidthAlloc(), getRouterPixelWidthAlloc(),
                getRouteTablePixelWidthAlloc(), getInternetGatewayPixelWidthAlloc(), getNeworkAclPixelWidthAlloc(),
                getSecurityGroupPixelWidthAlloc(), getEc2InstancePixelWidthAlloc(), getElasticLbPixelWidthAlloc(),
                getSubnetPixelWidthAlloc(), getUnattachedPixelWidthAlloc());
        return Collections.max(artefactAttributes);
    }

    private void createDiagram(String filePath) {
        SVGGraphics2D g = new SVGGraphics2D(0.0, 0.0, getDiagramWidth(), getDiagramHeight());
        UMLDeploymentDiagram umlDeploymentDiagram = new UMLDeploymentDiagram(g);
        save(g, filePath);
    }

    private void save(SVGGraphics2D g, String filePath) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(filePath);
            file.write(g.getBytes());
        } catch (IOException e) {
            logger.err("IOException: %s", e.getMessage());
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                logger.err("IOException: (2) %s", e.getMessage());
            }
        }
    }

    private void dfsTraverse() {
        int i = 0;
        //Node rootNode = vpcGraph.getNode(vpcGraph.getId());
        //logger.out("Index: %s, Node: %s", i, rootNode.getId());
        Iterator<Node> iterator = vpcGraph.getNode(vpcGraph.getId()).getDepthFirstIterator();
        while (iterator.hasNext()) {
            Node currentNode = iterator.next();
            logger.out("Index: %s, Node: %s", i, currentNode.getId());
            i++;
        }
    }
}
