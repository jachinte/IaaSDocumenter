package com.eu.skyblue.iaasdocumenter.uml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 19/06/15
 * Time: 00:02
 * To change this template use File | Settings | File Templates.
 */
public class UMLStereotypeProperty {
    public static String VPC_CIDR = "vpcCidr";
    public static String ID = "id";
    public static String NAME = "name";
    public static String DNS_NAME = "dnsName";
    public static String AMI = "ami";
    public static String IS_DEFAULT = "isDefault";
    public static String CIDR_BLOCK = "cidrBlock";
    public static String AVAILABILITY_ZONE = "availabilityZone";

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
