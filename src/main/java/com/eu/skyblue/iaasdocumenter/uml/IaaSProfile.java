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
        Property vpcCidr = umlProfileBuilder.createAttribute(this.routerStereotype, UMLStereotypeProperty.VPC_CIDR,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.routerStereotype, Boolean.FALSE);
    }

    private void createStereotypeAWSNode() {
        this.awsNodeStereotype = umlProfileBuilder.createStereotype(UMLStereotype.AWS_NODE, Boolean.TRUE);
        Property id = umlProfileBuilder.createAttribute(this.awsNodeStereotype, UMLStereotypeProperty.ID,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.awsNodeStereotype, Boolean.FALSE);
    }

    private void createStereotypeFirewall() {
        this.firewallStereotype = umlProfileBuilder.createStereotype(UMLStereotype.FIREWALL, Boolean.TRUE);
        Property name = umlProfileBuilder.createAttribute(this.firewallStereotype, UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.firewallStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.firewallStereotype,
                this.awsNodeStereotype);
    }

    private void createStereotypeElasticLB() {
        this.elasticLBStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ELASTIC_LB, Boolean.FALSE);
        Property name = umlProfileBuilder.createAttribute(this.elasticLBStereotype, UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        Property dnsName = umlProfileBuilder.createAttribute(this.elasticLBStereotype, UMLStereotypeProperty.DNS_NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.elasticLBStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.elasticLBStereotype,
                this.awsNodeStereotype);
    }

    private void createStereotypeInternetGateway() {
        this.internetGatewayStereotype = umlProfileBuilder.createStereotype(UMLStereotype.INTERNET_GATEWAY,
                Boolean.FALSE);
        Property name = umlProfileBuilder.createAttribute(this.internetGatewayStereotype, UMLStereotypeProperty.NAME,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.internetGatewayStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.internetGatewayStereotype,
                this.awsNodeStereotype);
    }

    private void createStereotypeEC2Instance() {
        this.ec2InstanceStereotype = umlProfileBuilder.createStereotype(UMLStereotype.EC2_INSTANCE, Boolean.FALSE);
        Property ami = umlProfileBuilder.createAttribute(this.ec2InstanceStereotype, UMLStereotypeProperty.AMI,
                stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.ec2InstanceStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.ec2InstanceStereotype,
                this.awsNodeStereotype);
    }

    private void createStereotypeSecurityGroup() {
        this.securityGroupStereotype = umlProfileBuilder.createStereotype(UMLStereotype.SECURITY_GROUP, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.securityGroupStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.securityGroupStereotype,
                this.firewallStereotype);
    }

    private void createStereotypeNetworkACL() {
        this.networkAclStereotype = umlProfileBuilder.createStereotype(UMLStereotype.NETWORK_ACL, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.nodeMetaclass, this.networkAclStereotype, Boolean.FALSE);
        Generalization generalization = umlProfileBuilder.createGeneralization(this.networkAclStereotype,
                this.firewallStereotype);
    }

    private void createStereotypeVirtualPrivateCloud() {
        this.virtualPrivateCloudStereotype = umlProfileBuilder.createStereotype(UMLStereotype.VPC, Boolean.FALSE);
        Property id = umlProfileBuilder.createAttribute(this.virtualPrivateCloudStereotype, UMLStereotypeProperty.ID,
                stringPrimitiveType, 0, 1, null);
        Property isDefault = umlProfileBuilder.createAttribute(this.virtualPrivateCloudStereotype,
                UMLStereotypeProperty.IS_DEFAULT, stringPrimitiveType, 0, 1, null);
        Property cidrBlock = umlProfileBuilder.createAttribute(this.virtualPrivateCloudStereotype,
                UMLStereotypeProperty.CIDR_BLOCK, stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.virtualPrivateCloudStereotype, Boolean.FALSE);
    }

    private void createStereotypeSubnet() {
        this.subnetStereotype = umlProfileBuilder.createStereotype(UMLStereotype.SUBNET, Boolean.FALSE);
        Property isDefault = umlProfileBuilder.createAttribute(this.subnetStereotype,
                UMLStereotypeProperty.IS_DEFAULT, stringPrimitiveType, 0, 1, null);
        Property cidrBlock = umlProfileBuilder.createAttribute(this.subnetStereotype,
                UMLStereotypeProperty.CIDR_BLOCK, stringPrimitiveType, 0, 1, null);
        Property availabilityZone = umlProfileBuilder.createAttribute(this.subnetStereotype,
                UMLStereotypeProperty.AVAILABILITY_ZONE, stringPrimitiveType, 0, 1, null);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.subnetStereotype, Boolean.FALSE);
    }

    private void createStereotypeRouteTable() {
        this.routeTableStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ROUTE_TABLE, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.routeTableStereotype, Boolean.FALSE);
    }

    private void createStereotypeNetworkInterface() {
        this.networkInterfaceStereotype = umlProfileBuilder.createStereotype(UMLStereotype.ENI, Boolean.FALSE);
        umlProfileBuilder.createExtension(this.artefactMetaclass, this.networkInterfaceStereotype, Boolean.FALSE);
    }

    private void createStereotypePacketFiltering() {
        this.packetFilteringStereotype = umlProfileBuilder.createStereotype(UMLStereotype.PACKET_FILTERING,
                Boolean.FALSE);
        umlProfileBuilder.createExtension(this.associationMetaclass, this.packetFilteringStereotype, Boolean.FALSE);
    }

    private void createStereotypeOSILayer2Link() {
        this.osiLayer2LinkStereotype = umlProfileBuilder.createStereotype(UMLStereotype.OSI_LAYER2_LINK,
                Boolean.FALSE);
        umlProfileBuilder.createExtension(this.associationMetaclass, this.osiLayer2LinkStereotype, Boolean.FALSE);
    }

    public void applyProfile(org.eclipse.uml2.uml.Package model) {
        model.applyProfile(umlProfileBuilder.getProfile());

        logger.out("Profile '%s' applied to package '%s'.", umlProfileBuilder.getProfile().getQualifiedName(),
                model.getQualifiedName());
    }

    // code smell?
    public void applyNodeStereotype(NamedElement namedElement, String stereotype) {
        Stereotype stereotypeToApply;
        switch (stereotype) {
            case UMLStereotype.ROUTER:
                stereotypeToApply = routerStereotype;
                break;
            case UMLStereotype.AWS_NODE:
                stereotypeToApply = awsNodeStereotype;
                break;
            case UMLStereotype.FIREWALL:
                stereotypeToApply = firewallStereotype;
                break;
            case UMLStereotype.ELASTIC_LB:
                stereotypeToApply = elasticLBStereotype;
                break;
            case UMLStereotype.INTERNET_GATEWAY:
                stereotypeToApply = internetGatewayStereotype;
                break;
            case UMLStereotype.EC2_INSTANCE:
                stereotypeToApply = ec2InstanceStereotype;
                break;
            case UMLStereotype.SECURITY_GROUP:
                stereotypeToApply = securityGroupStereotype;
                break;
            case UMLStereotype.NETWORK_ACL:
                stereotypeToApply = networkAclStereotype;
                break;
            case UMLStereotype.VPC:
                stereotypeToApply = virtualPrivateCloudStereotype;
                break;
            case UMLStereotype.SUBNET:
                stereotypeToApply = subnetStereotype;
                break;
            case UMLStereotype.ROUTE_TABLE:
                stereotypeToApply = routeTableStereotype;
                break;
            case UMLStereotype.ENI:
                stereotypeToApply = networkInterfaceStereotype;
                break;
            case UMLStereotype.PACKET_FILTERING:
                stereotypeToApply = packetFilteringStereotype;
                break;
            case UMLStereotype.OSI_LAYER2_LINK:
                stereotypeToApply = osiLayer2LinkStereotype;
                break;
            default:
                throw new IllegalArgumentException("Invalid stereotype");
        }
        namedElement.applyStereotype(stereotypeToApply);

        logger.out("Stereotype '%s' applied to element '%s'.", routerStereotype.getQualifiedName(),
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


}
