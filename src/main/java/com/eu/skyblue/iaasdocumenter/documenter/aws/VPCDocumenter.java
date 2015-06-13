package com.eu.skyblue.iaasdocumenter.documenter.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.model.Vpc;
import com.eu.skyblue.iaasdocumenter.documenter.IaasDocumenter;
import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.aws.GeneratorFactory;
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
public class VPCDocumenter implements IaasDocumenter {
    private InfrastructureClient infrastructureClient;
    private List<Graph> graphList;

    protected VPCDocumenter(InfrastructureClient infrastructureClient) throws Exception {
        this.infrastructureClient = infrastructureClient;
        this.graphList = new ArrayList<Graph>();
    }

    // Get list of VPCs and create graphs for them
    public void createInventory() throws AmazonServiceException {
        for (Vpc vpc: infrastructureClient.getVpcs()) {
            Graph vpcGraph = new MultiGraph(vpc.getVpcId());
            vpcGraph.addAttribute(AttributeName.CIDR_BLOCK, vpc.getCidrBlock());

            Generator vpcGraphGenerator = GeneratorFactory.createVPCGenerator(this.infrastructureClient, vpc, vpcGraph);

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
