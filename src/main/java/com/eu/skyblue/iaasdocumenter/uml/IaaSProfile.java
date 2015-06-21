package com.eu.skyblue.iaasdocumenter.uml;

import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.eclipse.uml2.uml.*;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 18/06/15
 * Time: 00:38
 * To change this template use File | Settings | File Templates.
 */
public class IaaSProfile {
    private static String NS_URI = "http://profile/iaas";
    private static String PROFILE_NAME = "IAAS_PROFILE";

    private Stereotype routerStereotype;
    private Stereotype awsNodeStereotype;
    private Stereotype firewallStereotype;
    private Stereotype elasticLBStereotype;
    private Stereotype internetGatewayStereotype;
    private Stereotype ec2InstanceStereotype;
    private Stereotype securityGroupStereotype;
    private Stereotype networkAclStereotype;
    private Stereotype virtualPrivateCloudStereotype;
    private Stereotype subnetStereotype;
    private Stereotype routeTableStereotype;
    private Stereotype networkInterfaceStereotype;
    private Stereotype packetFilteringStereotype;
    private Stereotype osiLayer2LinkStereotype;

    private UMLProfileBuilder umlProfileBuilder;
    private Logger logger;

    private PrimitiveType booleanPrimitiveType;
    private PrimitiveType integerPrimitiveType;
    private PrimitiveType realPrimitiveType;
    private PrimitiveType stringPrimitiveType;
    private PrimitiveType unlimitedPrimitiveType;

    private org.eclipse.uml2.uml.Class propertyMetaclass;
    private org.eclipse.uml2.uml.Class nodeMetaclass;
    private org.eclipse.uml2.uml.Class artefactMetaclass;
    private org.eclipse.uml2.uml.Class associationMetaclass;


    public IaaSProfile(Logger logger) {
        this.logger = logger;
        this.umlProfileBuilder = new UMLProfileBuilder(NS_URI, PROFILE_NAME, logger);

        this.importPrimitiveTypes();
        this.referenceMetaClasses();
        this.createStereotypes();

        this.umlProfileBuilder.defineProfile();
    }

    // import primitive types
    private void importPrimitiveTypes() {
        this.booleanPrimitiveType = umlProfileBuilder.importPrimitiveType(UMLPrimitiveType.BOOLEAN);
        this.integerPrimitiveType = umlProfileBuilder.importPrimitiveType(UMLPrimitiveType.INTEGER);
        this.realPrimitiveType = umlProfileBuilder.importPrimitiveType(UMLPrimitiveType.REAL);
        this.stringPrimitiveType = umlProfileBuilder.importPrimitiveType(UMLPrimitiveType.STRING);
        this.unlimitedPrimitiveType = umlProfileBuilder.importPrimitiveType(UMLPrimitiveType.UMLIMITED_NATURAL);
    }

    // import the metaclasss we'll be using
    private void referenceMetaClasses() {
        this.propertyMetaclass = umlProfileBuilder.referenceMetaclass(UMLPackage.Literals.PROPERTY.getName());
        this.nodeMetaclass = umlProfileBuilder.referenceMetaclass(UMLPackage.Literals.NODE.getName());
        this.artefactMetaclass = umlProfileBuilder.referenceMetaclass(UMLPackage.Literals.ARTIFACT.getName());
        this.associationMetaclass = umlProfileBuilder.referenceMetaclass(UMLPackage.Literals.ASSOCIATION.getName());
    }

    private void createStereotypes() {
        this.createStereotypeRouter();

        this.createStereotypeAWSNode();
        this.createStereotypeElasticLB();
        this.createStereotypeInternetGateway();
        this.createStereotypeEC2Instance();

        this.createStereotypeFirewall();
        this.createStereotypeSecurityGroup();
        this.createStereotypeNetworkACL();

        this.createStereotypeVirtualPrivateCloud();
        this.createStereotypeSubnet();

        this.createStereotypeRouteTable();
        this.createStereotypeNetworkInterface();

        this.createStereotypePacketFiltering();
        this.createStereotypeOSILayer2Link();
    }

    private void createStereotypeRouter() {
        this.routerStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ROUTER, Boolean.FALSE);
        Property vpcCidr = umlProfileBuilder.createAttribute(this.getRouterStereotype(), UMLStereotypeProperty.VPC_CIDR,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getRouterStereotype(), Boolean.FALSE);
    }

    private void createStereotypeAWSNode() {
        this.awsNodeStereotype = umlProfileBuilder.createStereotype(UMLStereotype.AWS_NODE, Boolean.TRUE);
        Property id = umlProfileBuilder.createAttribute(this.getAwsNodeStereotype(), UMLStereotypeProperty.ID,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getAwsNodeStereotype(), Boolean.FALSE);
    }

    private void createStereotypeFirewall() {
        this.firewallStereotype = umlProfileBuilder.createStereotype(UMLStereotype.FIREWALL, Boolean.TRUE);
        Property name = umlProfileBuilder.createAttribute(this.getFirewallStereotype(), UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getFirewallStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getFirewallStereotype(),
                this.getAwsNodeStereotype());
    }

    private void createStereotypeElasticLB() {
        this.elasticLBStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ELASTIC_LB, Boolean.FALSE);
        Property name = umlProfileBuilder.createAttribute(this.getElasticLBStereotype(), UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        Property dnsName = umlProfileBuilder.createAttribute(this.getElasticLBStereotype(), UMLStereotypeProperty.DNS_NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getElasticLBStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getElasticLBStereotype(),
                this.getAwsNodeStereotype());
    }

    private void createStereotypeInternetGateway() {
        this.internetGatewayStereotype = umlProfileBuilder.createStereotype(UMLStereotype.INTERNET_GATEWAY,
                Boolean.FALSE);
        Property name = umlProfileBuilder.createAttribute(this.getInternetGatewayStereotype(), UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getInternetGatewayStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getInternetGatewayStereotype(),
                this.getAwsNodeStereotype());
    }

    private void createStereotypeEC2Instance() {
        this.ec2InstanceStereotype = umlProfileBuilder.createStereotype(UMLStereotype.EC2_INSTANCE, Boolean.FALSE);
        Property ami = umlProfileBuilder.createAttribute(this.getEc2InstanceStereotype(), UMLStereotypeProperty.AMI,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getEc2InstanceStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getEc2InstanceStereotype(),
                this.getAwsNodeStereotype());
    }

    private void createStereotypeSecurityGroup() {
        this.securityGroupStereotype = umlProfileBuilder.createStereotype(UMLStereotype.SECURITY_GROUP, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getSecurityGroupStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getSecurityGroupStereotype(),
                this.getFirewallStereotype());
    }

    private void createStereotypeNetworkACL() {
        this.networkAclStereotype = umlProfileBuilder.createStereotype(UMLStereotype.NETWORK_ACL, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.getNetworkAclStereotype(), Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.getNetworkAclStereotype(),
                this.getFirewallStereotype());
    }

    private void createStereotypeVirtualPrivateCloud() {
        this.virtualPrivateCloudStereotype = umlProfileBuilder.createStereotype(UMLStereotype.VPC, Boolean.FALSE);
        Property id = umlProfileBuilder.createAttribute(this.getVirtualPrivateCloudStereotype(), UMLStereotypeProperty.ID,
                stringPrimitiveType, 0, 1, null);
        Property isDefault = umlProfileBuilder.createAttribute(this.getVirtualPrivateCloudStereotype(),
                UMLStereotypeProperty.IS_DEFAULT, booleanPrimitiveType, 0, 1, null);
        Property cidrBlock = umlProfileBuilder.createAttribute(this.getVirtualPrivateCloudStereotype(),
                UMLStereotypeProperty.CIDR_BLOCK, stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.getVirtualPrivateCloudStereotype(), Boolean.FALSE);
    }

    private void createStereotypeSubnet() {
        this.subnetStereotype = umlProfileBuilder.createStereotype(UMLStereotype.SUBNET, Boolean.FALSE);
        Property isDefault = umlProfileBuilder.createAttribute(this.getSubnetStereotype(),
                UMLStereotypeProperty.IS_DEFAULT, booleanPrimitiveType, 0, 1, null);
        Property cidrBlock = umlProfileBuilder.createAttribute(this.getSubnetStereotype(),
                UMLStereotypeProperty.CIDR_BLOCK, stringPrimitiveType, 0, 1, null);
        Property availabilityZone = umlProfileBuilder.createAttribute(this.getSubnetStereotype(),
                UMLStereotypeProperty.AVAILABILITY_ZONE, stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.getSubnetStereotype(), Boolean.FALSE);
    }

    private void createStereotypeRouteTable() {
        this.routeTableStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ROUTE_TABLE, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.getRouteTableStereotype(), Boolean.FALSE);
    }

    private void createStereotypeNetworkInterface() {
        this.networkInterfaceStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ENI, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.getNetworkInterfaceStereotype(), Boolean.FALSE);
    }

    private void createStereotypePacketFiltering() {
        this.packetFilteringStereotype = umlProfileBuilder.createStereotype(UMLStereotype.PACKET_FILTERING,
                Boolean.FALSE);
        umlProfileBuilder.createExtension(this.associationMetaclass, this.getPacketFilteringStereotype(), Boolean.FALSE);
    }

    private void createStereotypeOSILayer2Link() {
        this.osiLayer2LinkStereotype = umlProfileBuilder.createStereotype(UMLStereotype.OSI_LAYER2_LINK,
                Boolean.FALSE);
        umlProfileBuilder.createExtension(this.associationMetaclass, this.getOsiLayer2LinkStereotype(), Boolean.FALSE);
    }

    public void applyProfile(org.eclipse.uml2.uml.Package model) {
        model.applyProfile(umlProfileBuilder.getProfile());

        logger.out("Profile '%s' applied to package '%s'.", umlProfileBuilder.getProfile().getQualifiedName(),
                model.getQualifiedName());
    }

    public void applyStereotype(NamedElement namedElement, Stereotype stereotype) {
        namedElement.applyStereotype(stereotype);

        logger.out("Stereotype '%s' applied to element '%s'.", stereotype.getQualifiedName(),
                namedElement.getQualifiedName());
    }

    // need to update
    public void setStereotypePropertyValue(NamedElement namedElement, Stereotype stereotype,
                                              Property property, Object value) {
        Object valueToSet = value;

        if ((value instanceof String) && (property.getType() instanceof Enumeration)) {
            // Get the corresponding enumeration literal
            valueToSet = ((Enumeration) property.getType()).getOwnedLiteral((String) value);
        }

        namedElement.setValue(stereotype, property.getName(), valueToSet);

        logger.out("Value of stereotype property '%s' on element '%s' set to %s.", property.getQualifiedName(),
                namedElement.getQualifiedName(), value);
    }

    public Stereotype getRouterStereotype() {
        return routerStereotype;
    }

    public Stereotype getAwsNodeStereotype() {
        return awsNodeStereotype;
    }

    public Stereotype getFirewallStereotype() {
        return firewallStereotype;
    }

    public Stereotype getElasticLBStereotype() {
        return elasticLBStereotype;
    }

    public Stereotype getInternetGatewayStereotype() {
        return internetGatewayStereotype;
    }

    public Stereotype getEc2InstanceStereotype() {
        return ec2InstanceStereotype;
    }

    public Stereotype getSecurityGroupStereotype() {
        return securityGroupStereotype;
    }

    public Stereotype getNetworkAclStereotype() {
        return networkAclStereotype;
    }

    public Stereotype getVirtualPrivateCloudStereotype() {
        return virtualPrivateCloudStereotype;
    }

    public Stereotype getSubnetStereotype() {
        return subnetStereotype;
    }

    public Stereotype getRouteTableStereotype() {
        return routeTableStereotype;
    }

    public Stereotype getNetworkInterfaceStereotype() {
        return networkInterfaceStereotype;
    }

    public Stereotype getPacketFilteringStereotype() {
        return packetFilteringStereotype;
    }

    public Stereotype getOsiLayer2LinkStereotype() {
        return osiLayer2LinkStereotype;
    }

    public Profile getProfile() {
        return umlProfileBuilder.getProfile();
    }
}
