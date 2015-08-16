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
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:17
 * To change this template use File | Settings | File Templates.
 */
public class AWSVPCDocumenter implements IaasDocumenter {
    private AWSInfrastructureClient awsInfrastructureClient;
    private List<Graph> graphList;

    protected AWSVPCDocumenter(AWSInfrastructureClient awsInfrastructureClient) throws Exception {
        this.awsInfrastructureClient = awsInfrastructureClient;
        this.graphList = new ArrayList<Graph>();
    }

    // Get list of VPCs and create graphs for them
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

    public List<Graph> getGraphs() {
        return this.graphList;
    }
}
