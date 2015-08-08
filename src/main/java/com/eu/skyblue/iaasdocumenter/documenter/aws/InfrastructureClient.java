package com.eu.skyblue.iaasdocumenter.documenter.aws;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:13
 * To change this template use File | Settings | File Templates.
 */
public class InfrastructureClient {
    public static final String VPC_ID = "vpc-id";
    public static final String IGW_ATTACHMENT_VPC_ID = "attachment.vpc-id";
    private AmazonEC2 ec2;
    private AmazonElasticLoadBalancing elb;

    protected InfrastructureClient(AmazonEC2 ec2, AmazonElasticLoadBalancing elb) throws  Exception {
        this.ec2 = ec2;
        this.elb = elb;
    }

    public List<Vpc> getVpcs() {
        return ec2.describeVpcs().getVpcs();
    }

    public List<InternetGateway> getInternetGateways(Vpc vpc) {
        Filter filter = new Filter(IGW_ATTACHMENT_VPC_ID).withValues(vpc.getVpcId());
        DescribeInternetGatewaysResult describeInternetGatewaysResult =
                ec2.describeInternetGateways(new DescribeInternetGatewaysRequest().withFilters(filter));
        return describeInternetGatewaysResult.getInternetGateways();
    }

    public List<RouteTable> getRouteTables(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeRouteTablesResult describeRouteTablesResult =
                ec2.describeRouteTables(new DescribeRouteTablesRequest().withFilters(filter));
        return describeRouteTablesResult.getRouteTables();
    }

    public List<NetworkAcl> getNetworkAcls(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeNetworkAclsResult describeNetworkAclsResult =
                ec2.describeNetworkAcls(new DescribeNetworkAclsRequest().withFilters(filter));
        return describeNetworkAclsResult.getNetworkAcls();
    }

    public List<Subnet> getSubnets(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeSubnetsResult describeSubnetsResult =
                ec2.describeSubnets(new DescribeSubnetsRequest().withFilters(filter));
        return describeSubnetsResult.getSubnets();
    }

    public List<SecurityGroup> getSecurityGroups(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeSecurityGroupsResult describeSecurityGroupsResult =
                ec2.describeSecurityGroups(new DescribeSecurityGroupsRequest().withFilters(filter));
        return describeSecurityGroupsResult.getSecurityGroups();
    }

    public List<NetworkInterface> getNeworkInterfaces(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeNetworkInterfacesResult describeNetworkInterfacesResult =
                ec2.describeNetworkInterfaces(new DescribeNetworkInterfacesRequest().withFilters(filter));
        return describeNetworkInterfacesResult.getNetworkInterfaces();
    }

    public List<Reservation> getInstances(Vpc vpc) {
        Filter filter = new Filter(VPC_ID).withValues(vpc.getVpcId());
        DescribeInstancesResult describeInstancesResult =
                ec2.describeInstances(new DescribeInstancesRequest().withFilters(filter));
        return describeInstancesResult.getReservations();
    }

    public List<LoadBalancerDescription> getElasticLoadBalancers(Vpc vpc) {
        DescribeLoadBalancersResult describeLoadBalancersResult =
                elb.describeLoadBalancers();
        return describeLoadBalancersResult.getLoadBalancerDescriptions();
    }
}
