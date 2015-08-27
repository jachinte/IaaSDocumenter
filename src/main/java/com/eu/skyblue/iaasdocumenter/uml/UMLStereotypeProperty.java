package com.eu.skyblue.iaasdocumenter.uml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides constant values representing labels for the stereotype
 * properties defined as part of the IaaSProfile UML profile.
 */
public class UMLStereotypeProperty {
    /**
     * CIDR value for VPC
     */
    public static String VPC_CIDR = "vpcCidr";

    /**
     * Unique identifier
     */
    public static String ID = "id";

    /**
     * Human friendly name
     */
    public static String NAME = "name";

    /**
     * Host name registered in DNS
     */
    public static String DNS_NAME = "dnsName";

    /**
     * Amazon Machine Image ID
     */
    public static String AMI = "ami";

    /**
     * Indicates whether this is a default artefact (VPC, Subnet, Security Group, etc).
     */
    public static String IS_DEFAULT = "isDefault";

    /**
     * CIDR value for subnet
     */
    public static String CIDR_BLOCK = "cidrBlock";

    /**
     * Availability Zone Name
     */
    public static String AVAILABILITY_ZONE = "availabilityZone";

    /**
     * A map of stereotype property names to types
     */
    public static Map<String, String> ATTRIBUTE_TYPE_MAP;

    static {
        Map<String, String> tempMap =  new HashMap<String, String>();
        tempMap.put(VPC_CIDR, UMLPrimitiveType.STRING);
        tempMap.put(ID, UMLPrimitiveType.STRING);
        tempMap.put(NAME, UMLPrimitiveType.STRING);
        tempMap.put(DNS_NAME, UMLPrimitiveType.STRING);
        tempMap.put(AMI, UMLPrimitiveType.STRING);
        tempMap.put(IS_DEFAULT, UMLPrimitiveType.BOOLEAN);
        tempMap.put(CIDR_BLOCK, UMLPrimitiveType.STRING);
        tempMap.put(AVAILABILITY_ZONE, UMLPrimitiveType.STRING);
        ATTRIBUTE_TYPE_MAP = Collections.unmodifiableMap(tempMap);
    }
}
