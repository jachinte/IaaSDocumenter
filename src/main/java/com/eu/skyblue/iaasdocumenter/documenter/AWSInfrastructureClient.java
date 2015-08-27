package com.eu.skyblue.iaasdocumenter.documenter;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;

import java.util.List;

/**
 * Provides functionality for querying AWS.
 */
public class AWSInfrastructureClient {
    public static final String VPC_ID = "vpc-id";
    public static final String IGW_ATTACHMENT_VPC_ID = "attachment.vpc-id";
    private AmazonEC2 ec2;
    private AmazonElasticLoadBalancing elb;

    /**
     * Constructs a new <code>AWSInfrastructureClient</code> object.
     *
     * @param ec2  EC2 querying client
     * @param elb  Elastic Load Balancing querying client
     */
    protected AWSInfrastructureClient(AmazonEC2 ec2, AmazonElasticLoadBalancing elb) throws  Exception {
        this.ec2 = ec2;
        this.elb = elb;
    }

    /**
     * Returns the list of provisioned VPCs
     *
     * @return List of provisioned VPCs
     */
    public List<Vpc> getVpcs() {
        return ec2.describeVpcs().getVpcs();
    }

    /**
     * Returns the list of provisioned Internet Gateways for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Internet Gateways
     */
    public List<InternetGateway> getInternetGateways(Vpc vpc) {
        Filter filter = new Filter(IGW_ATTACHMENT_VPC_ID).withValues(vpc.getVpcId());
        DescribeInternetGatewaysResult describeInternetGatewaysResult =
                ec2.describeInternetGateways(new DescribeInternetGatewaysRequest().withFilters(filter));
        return describeInternetGatewaysResult.getInternetGateways();
    }

    /**
     * Returns the list of provisioned Route Tables for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Route Tables
     */
    public List<RouteTable> getRouteTables(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeRouteTablesResult describeRouteTablesResult =
                ec2.describeRouteTables(new DescribeRouteTablesRequest().withFilters(filter));
        return describeRouteTablesResult.getRouteTables();
    }

    /**
     * Returns the list of provisioned Network ACLs for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Network ACLs
     */
    public List<NetworkAcl> getNetworkAcls(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeNetworkAclsResult describeNetworkAclsResult =
                ec2.describeNetworkAcls(new DescribeNetworkAclsRequest().withFilters(filter));
        return describeNetworkAclsResult.getNetworkAcls();
    }

    /**
     * Returns the list of provisioned Subnets for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Subnets
     */
    public List<Subnet> getSubnets(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeSubnetsResult describeSubnetsResult =
                ec2.describeSubnets(new DescribeSubnetsRequest().withFilters(filter));
        return describeSubnetsResult.getSubnets();
    }

    /**
     * Returns the list of provisioned Security Groups for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Security Groups
     */
    public List<SecurityGroup> getSecurityGroups(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeSecurityGroupsResult describeSecurityGroupsResult =
                ec2.describeSecurityGroups(new DescribeSecurityGroupsRequest().withFilters(filter));
        return describeSecurityGroupsResult.getSecurityGroups();
    }

    /**
     * Returns the list of provisioned Network Interfaces for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Network Interfaces
     */
    public List<NetworkInterface> getNeworkInterfaces(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeNetworkInterfacesResult describeNetworkInterfacesResult =
                ec2.describeNetworkInterfaces(new DescribeNetworkInterfacesRequest().withFilters(filter));
        return describeNetworkInterfacesResult.getNetworkInterfaces();
    }

    /**
     * Returns the list of provisioned EC2 Instances for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned EC2 Instances
     */
    public List<Reservation> getInstances(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeInstancesResult describeInstancesResult =
                ec2.describeInstances(new DescribeInstancesRequest().withFilters(filter));
        return describeInstancesResult.getReservations();
    }

    /**
     * Returns the list of provisioned Elastic Load Balancers for the specified VPC
     *
     * @param vpc VPC to query
     *
     * @return List of provisioned Elastic Load Balancers
     */
    public List<LoadBalancerDescription> getElasticLoadBalancers(Vpc vpc) {
        DescribeLoadBalancersResult describeLoadBalancersResult =
                elb.describeLoadBalancers();
        return describeLoadBalancersResult.getLoadBalancerDescriptions();
    }
}
