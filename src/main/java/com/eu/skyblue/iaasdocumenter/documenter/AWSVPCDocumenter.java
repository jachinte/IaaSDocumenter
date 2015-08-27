package com.eu.skyblue.iaasdocumenter.documenter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.Vpc;
import com.eu.skyblue.iaasdocumenter.generator.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.GeneratorFactory;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a graph of the provisioned VPCs.
 */
public class AWSVPCDocumenter implements IaasDocumenter {
    private AWSInfrastructureClient awsInfrastructureClient;
    private List<Graph> graphList;

    /**
     * Constructs a new <code>AWSVPCDocumenter</code> object.
     *
     * @param awsInfrastructureClient  AWS client
     */
    protected AWSVPCDocumenter(AWSInfrastructureClient awsInfrastructureClient) throws Exception {
        this.awsInfrastructureClient = awsInfrastructureClient;
        this.graphList = new ArrayList<Graph>();
    }

    /**
     * Creates an inventory of the provisioned artefacts for each VPC
     */
    public void createInventory() throws AmazonServiceException {
        for (Vpc vpc: awsInfrastructureClient.getVpcs()) {
            Graph vpcGraph = new MultiGraph(vpc.getVpcId());
            vpcGraph.addAttribute(AttributeName.CIDR_BLOCK, vpc.getCidrBlock());

            Generator vpcGraphGenerator = GeneratorFactory.createVPCGenerator(this.awsInfrastructureClient, vpc, vpcGraph);

            vpcGraphGenerator.addSink(vpcGraph);
            vpcGraphGenerator.begin();
            vpcGraphGenerator.nextEvents();
            vpcGraphGenerator.end();

            graphList.add(vpcGraph);
        }
    }

    /**
     * Returns the list of graphs for provisioned VPCs.
     *
     * @return list of VPC graphs
     */
    public List<Graph> getGraphs() {
        return this.graphList;
    }
}
