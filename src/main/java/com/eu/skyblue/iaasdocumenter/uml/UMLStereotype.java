package com.eu.skyblue.iaasdocumenter.uml;

/**
 * Provides constant values representing labels of various UML stereotypes,
 * including the ones defined for the IaaSProfile UML profile.
 */
public class UMLStereotype {
    /**
     * AWS Node. Superclass for all AWS artefacts with a processing
     * capacity, other than Router.
     */
    public static final String AWS_NODE = "AWSNode";

    /**
     * Subclass of AWSNode and serves as a superclass for AWS firewall types.
     */
    public static final String FIREWALL = "Firewall";

    /**
     * Represents the Internet Gateway component.
     */
    public static final String INTERNET_GATEWAY = "InternetGateway";

    /**
     * Represents the Implicit Router component.
     */
    public static final String ROUTER = "Router";

    /**
     * Represents the Elastic Load Balancer component.
     */
    public static final String ELASTIC_LB = "ElasticLB";

    /**
     * Represents the Security Group (firewall) component.
     */
    public static final String SECURITY_GROUP = "SecurityGroup";

    /**
     * Represents the Network ACL (firewall) component.
     */
    public static final String NETWORK_ACL = "NetworkACL";

    /**
     * Represents the Subnet component.
     */
    public static final String SUBNET = "Subnet";

    /**
     * Represents the Route Table component.
     */
    public static final String ROUTE_TABLE = "RouteTable";

    /**
     * Represents the PacketFiltering relationship.
     */
    public static final String PACKET_FILTERING = "PacketFiltering";

    /**
     * Represents the OSILayer2Link relationship
     */
    public static final String OSI_LAYER2_LINK = "OSILayer2Link";

    /**
     * Represents the EC2 component.
     */
    public static final String EC2_INSTANCE = "EC2Instance";

    /**
     * Represents the ENI component.
     */
    public static final String ENI = "ElasticNetworkInterface";

    /**
     * Represents the depends relationship.
     */
    public static final String DEPENDENCY = "Dependency";

    /**
     * Represents the deploy relationship.
     */
    public static final String DEPLOYMENT = "deploy";

    /**
     * Represents the note (comment) artefact.
     */
    public static final String NOTE = "Note";

    /**
     * Represents the VPC component.
     */
    public static final String VPC = "VirtualPrivateCloud";
}
