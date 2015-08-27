package com.eu.skyblue.iaasdocumenter.uml;

import com.eu.skyblue.iaasdocumenter.utils.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.*;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import java.util.Map;

/**
 * Provides the functionality for programmatically creating an
 * instance of a UML profile
 */
public class UMLProfileBuilder {
    private com.eu.skyblue.iaasdocumenter.utils.Logger logger;
    private ResourceSet resourceSet;
    private Profile profile;

    /**
     * Constructs a new <code>UMLProfileBuilder</code> object.
     *
     * @param nsUri          URI String for the profile to be created
     * @param profileName    Name of the profile to be created
     * @param logger         Logger
     */
    protected UMLProfileBuilder(String nsUri, String profileName, Logger logger) {
        // Create a resource-set to contain the resource(s) that we load and save
        resourceSet = new ResourceSetImpl();
        Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
        extensionToFactoryMap.put(XMI212UMLResource.PROFILE_FILE_EXTENSION, new UMLResourceFactoryImpl());
        UMLPackage.eINSTANCE.setNsURI(XMI242UMLResource.UML_METAMODEL_2_4_1_NS_URI);

        // Initialize registrations of resource factories, library models,
        // profiles, Ecore metadata, and other dependencies required for
        // serializing and working with UML resources.
        UMLResourcesUtil.init(resourceSet);

        this.profile = UMLFactory.eINSTANCE.createProfile();
        this.profile.setName(profileName);
        this.profile.setURI(nsUri);

        this.logger = logger;
    }

    /**
     * Returns an instance of the UML profile
     * @return UML profile
     */
    protected Profile getProfile() {
        return this.profile;
    }

    /**
     * Imports the specified primitive type
     *
     * @param name  The type to import.
     *
     * @return      The imported type.
     *
     */
    protected PrimitiveType importPrimitiveType(String name) {
        org.eclipse.uml2.uml.Package umlLibrary =
            (org.eclipse.uml2.uml.Package)load(URI.createURI(XMI2UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_2_4_1_URI));
        PrimitiveType PrimitiveType = (PrimitiveType) umlLibrary.getOwnedType(name);
        this.profile.createElementImport(PrimitiveType);

        logger.out("PrimitiveType type '%s' imported.", PrimitiveType.getQualifiedName());

        return PrimitiveType;
    }

    /**
     * References the specified metaclass
     *
     * @param name  The metaclass to reference
     *
     * @return      The referenced metaclass
     */
    protected org.eclipse.uml2.uml.Class referenceMetaclass(String name) {
        org.eclipse.uml2.uml.Package _package = load(URI.createURI(UMLResource.UML_METAMODEL_URI));
        Model umlMetamodel = (Model) _package;
        _package.eResource().setURI(URI.createURI(XMI2UMLResource.UML_METAMODEL_2_4_1_URI));
        org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType(name);
        this.profile.createMetaclassReference(metaclass);

        logger.out("Metaclass '%s' referenced.", metaclass.getQualifiedName());

        return metaclass;
    }

    /**
     * Creates a new stereotype
     *
     * @param name  The name of the stereotype to create
     *
     * @return      The new stereotype
     */
    protected Stereotype createStereotype(String name, boolean isAbstract) {
        Stereotype stereotype = this.profile.createOwnedStereotype(name, isAbstract);

        logger.out("Stereotype '%s' created.", stereotype.getQualifiedName());

        return stereotype;
    }


    /**
     * Creates a new stereotype generalization
     *
     * @param specificClassifier  The specific classifier
     * @param generalClassifier   The general classifier
     *
     * @return                    The new stereotype generalization
     */
    protected Generalization createGeneralization(Classifier specificClassifier, Classifier generalClassifier) {
        Generalization generalization = specificClassifier.createGeneralization(generalClassifier);

        logger.out("Generalization %s -> %s created.", specificClassifier.getQualifiedName(), generalClassifier.getQualifiedName());

        return generalization;
    }

    /**
     * Creates a new attribute
     *
     * @param class_         The class in which the attribute is to be created
     * @param name           The name of the attribute
     * @param type           The type of attribute
     * @param lowerBound     Lower bound for attribute
     * @param upperBound     Upper bound for attribute
     * @param defaultValue   Default value for attribute
     *
     * @return               The new attribute
     */
    protected Property createAttribute(org.eclipse.uml2.uml.Class class_, String name, Type type,
                                       int lowerBound, int upperBound, Object defaultValue) {
        Property attribute = class_.createOwnedAttribute(name, type, lowerBound, upperBound);

        if (defaultValue instanceof Boolean) {
            LiteralBoolean literal = (LiteralBoolean)attribute.createDefaultValue(null,
                    null, UMLPackage.Literals.LITERAL_BOOLEAN);
            literal.setValue(((Boolean) defaultValue).booleanValue());
        } else if (defaultValue instanceof String) {
            if (type instanceof Enumeration) {
                InstanceValue value = (InstanceValue) attribute.createDefaultValue(null,
                        null, UMLPackage.Literals.INSTANCE_VALUE);
                value.setInstance(((Enumeration) type).getOwnedLiteral((String) defaultValue));
            } else {
                LiteralString literal = (LiteralString) attribute.createDefaultValue(null,
                        null, UMLPackage.Literals.LITERAL_STRING);
                literal.setValue((String) defaultValue);
            }
        }

        logger.out("Attribute '%s' : %s [%s..%s]%s created.", //
                attribute.getQualifiedName(), // attribute name
                type.getQualifiedName(), // type name
                lowerBound, // no special case for multiplicity lower bound
                (upperBound == LiteralUnlimitedNatural.UNLIMITED)
                        ? "*" // special case for unlimited bound
                        : upperBound, // finite upper bound
                (defaultValue == null)
                        ? "" // no default value (use type's intrinsic default)
                        : String.format(" = %s", defaultValue));

        return attribute;
    }

    /**
     * Creates a new extension
     *
     * @param metaclass      The base class
     * @param stereotype     The stereotype
     * @param required       Whether mandatory or not
     *
     * @return               The new extension
     */
    protected Extension createExtension(org.eclipse.uml2.uml.Class metaclass, Stereotype stereotype,
                                        boolean required) {
        Extension extension = stereotype.createExtension(metaclass, required);

        logger.out("%sxtension '%s' created.", //
                required
                        ? "Required e" // it's a required extension
                        : "E", // an optional extension
                extension.getQualifiedName());

        return extension;
    }

    /**
     * Defines the profile
     */
    protected void defineProfile() {
        this.profile.define();

        logger.out("Profile '%s' defined.", profile.getQualifiedName());
    }

    /**
     * Loads a package from a resource with the specified URI.
     *
     * @param uri      Resource URI
     *
     * @return         The loaded package.
     */
    protected org.eclipse.uml2.uml.Package load(URI uri) {
        org.eclipse.uml2.uml.Package package_ = null;

        try {
            // Load the requested resource
            Resource resource = resourceSet.getResource(uri, true);

            EList<EObject> e = resource.getContents ();

            // Get the first (should be only) package from it
            package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
        } catch (WrappedException we) {
            logger.err(we.getMessage());
            System.exit(1);
        }

        return package_;
    }
}
