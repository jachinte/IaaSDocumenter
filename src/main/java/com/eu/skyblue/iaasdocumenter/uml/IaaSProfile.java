package com.eu.skyblue.iaasdocumenter.uml;

import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLPackage;

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
}
