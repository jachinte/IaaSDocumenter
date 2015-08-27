package com.eu.skyblue.iaasdocumenter.generator;

import com.amazonaws.services.ec2.model.Vpc;

import com.eu.skyblue.iaasdocumenter.documenter.AWSInfrastructureClient;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;

/**
 * Creates Generator objects.
 */
public class GeneratorFactory {
    private GeneratorFactory() {}

    /**
     * Constructs a new <code>Generator</code> object.
     *
     * @param awsInfrastructureClient    AWS client object (provides EC2 and ELB clients)
     * @param vpc                        AWS VPC to be queried.
     * @param vpcGraph                   Graph to be populated by the Generator
     */
    public static Generator createVPCGenerator(AWSInfrastructureClient awsInfrastructureClient, Vpc vpc, Graph vpcGraph) {
        return new VPCGraphGenerator(awsInfrastructureClient, vpc, vpcGraph);
    }
}
