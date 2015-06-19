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
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.*;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 13/06/15
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 */
public class UMLProfileBuilder {
    private com.eu.skyblue.iaasdocumenter.utils.Logger logger;
    private ResourceSet resourceSet;
    private Profile profile;


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

    protected Profile getProfile() {
        return this.profile;
    }

    // Import primitive types
    protected PrimitiveType importPrimitiveType(String name) {
        org.eclipse.uml2.uml.Package umlLibrary =
            (org.eclipse.uml2.uml.Package)load(URI.createURI(XMI2UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_2_4_1_URI));
        PrimitiveType PrimitiveType = (PrimitiveType) umlLibrary.getOwnedType(name);
        this.profile.createElementImport(PrimitiveType);

        logger.out("PrimitiveType type '%s' imported.", PrimitiveType.getQualifiedName());

        return PrimitiveType;
    }

    protected org.eclipse.uml2.uml.Class referenceMetaclass(String name) {
        org.eclipse.uml2.uml.Package _package = load(URI.createURI(UMLResource.UML_METAMODEL_URI));
        Model umlMetamodel = (Model) _package;
        _package.eResource().setURI(URI.createURI(XMI2UMLResource.UML_METAMODEL_2_4_1_URI));
        org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType(name);
        this.profile.createMetaclassReference(metaclass);

        logger.out("Metaclass '%s' referenced.", metaclass.getQualifiedName());

        return metaclass;
    }

    // Create stereotype
    protected Stereotype createStereotype(String name, boolean isAbstract) {
        Stereotype stereotype = this.profile.createOwnedStereotype(name, isAbstract);

        logger.out("Stereotype '%s' created.", stereotype.getQualifiedName());

        return stereotype;
    }


    // Create stereotype generalization
    protected Generalization createGeneralization(Classifier specificClassifier, Classifier generalClassifier) {
        Generalization generalization = specificClassifier.createGeneralization(generalClassifier);

        logger.out("Generalization %s --|> %s created.", specificClassifier.getQualifiedName(), generalClassifier.getQualifiedName());

        return generalization;
    }

    // Stereotype property (create)
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

    // Create extension
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

    // Define profile
    protected void defineProfile() {
        this.profile.define();

        logger.out("Profile '%s' defined.", profile.getQualifiedName());
    }

    // Create enumeration
    protected Enumeration createEnumeration(org.eclipse.uml2.uml.Package package_, String name) {
        Enumeration enumeration = package_.createOwnedEnumeration(name);

        logger.out("Enumeration '%s' created.", enumeration.getQualifiedName());

        return enumeration;
    }

    // Create enumeration literal
    protected EnumerationLiteral createEnumerationLiteral(Enumeration enumeration, String name) {
        EnumerationLiteral enumerationLiteral = enumeration.createOwnedLiteral(name);

        logger.out("Enumeration literal '%s' created.", enumerationLiteral.getQualifiedName());

        return enumerationLiteral;
    }

    protected void save(org.eclipse.uml2.uml.Package package_, URI uri) {
        // Create the resource to be saved and add the package to it
        Resource resource = resourceSet.createResource(uri);
        resource.getContents().add(package_);

        // And save.
        try {
            ((XMIResourceImpl)resource).setXMIVersion(UML212UMLResource.VERSION_2_1_VALUE);
            resource.save(null);
            logger.out("Done.");
        } catch (IOException ioe) {
            logger.err(ioe.getMessage());
        }
    }

    // Load
    protected org.eclipse.uml2.uml.Package load(URI uri) {
        org.eclipse.uml2.uml.Package package_ = null;

        try {
            // Load the requested resource
            Resource resource = resourceSet.getResource(uri, true);

            EList<EObject> e = resource.getContents ();

            // Get the first (should be only) package from it
            package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
            //System.out.println(">>>> UMLPackage.Literals.PACKAGE: " + UMLPackage.Literals.PACKAGE);
        } catch (WrappedException we) {
            logger.err(we.getMessage());
            System.exit(1);
        }

        return package_;
    }

    // Apply profile
    protected void applyProfile(org.eclipse.uml2.uml.Package package_) {
        package_.applyProfile(this.profile);

        logger.out("Profile '%s' applied to package '%s'.", profile.getQualifiedName(), package_.getQualifiedName());
    }

    // Apply stereotype
    protected void applyStereotype(NamedElement namedElement, Stereotype stereotype) {
        namedElement.applyStereotype(stereotype);

        logger.out("Stereotype '%s' applied to element '%s'.", stereotype.getQualifiedName(),
                namedElement.getQualifiedName());
    }

    // Acess stereotype property values
    protected Object getStereotypePropertyValue(NamedElement namedElement, Stereotype stereotype, Property property) {
        Object value = namedElement.getValue(stereotype, property.getName());

        logger.out("Value of stereotype property '%s' on element '%s' is %s.", property.getQualifiedName(),
                namedElement.getQualifiedName(), value);

        return value;
    }
    protected void setStereotypePropertyValue(NamedElement namedElement, Stereotype stereotype,
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
