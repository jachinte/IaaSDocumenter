package com.eu.skyblue.iaasdocumenter.generator;

import com.amazonaws.services.ec2.model.Vpc;
import com.eu.skyblue.iaasdocumenter.documenter.AWSInfrastructureClient;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:53
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorFactory {
    private GeneratorFactory() {}

    public static Generator createVPCGenerator(AWSInfrastructureClient awsInfrastructureClient, Vpc vpc, Graph vpcGraph) {
        return new VPCGraphGenerator(awsInfrastructureClient, vpc, vpcGraph);
    }
}
