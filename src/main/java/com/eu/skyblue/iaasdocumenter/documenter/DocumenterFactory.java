package com.eu.skyblue.iaasdocumenter.documenter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 09:20
 * To change this template use File | Settings | File Templates.
 */
public class DocumenterFactory {

    private DocumenterFactory () {}

    public static IaasDocumenter createIaasDocumenter(Regions region) throws Exception {
        AWSCredentials awsCredentials = null;
        try {
            awsCredentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Unable to load credentials", e);
        }

        AmazonEC2 ec2 = new AmazonEC2Client(awsCredentials);
        ec2.setRegion(Region.getRegion(region));

        AmazonElasticLoadBalancing elb = new AmazonElasticLoadBalancingClient(awsCredentials);
        elb.setRegion(Region.getRegion(region));
        return new AWSVPCDocumenter(new AWSInfrastructureClient(ec2, elb));
    }
}
