package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.aws.MetaClass;
import com.eu.skyblue.iaasdocumenter.uml.IaaSProfile;
import com.eu.skyblue.iaasdocumenter.uml.UMLPrimitiveType;
import com.eu.skyblue.iaasdocumenter.uml.UMLStereotype;
import com.eu.skyblue.iaasdocumenter.uml.UMLStereotypeProperty;
import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.XMI212UMLResource;
import org.eclipse.uml2.uml.resource.XMI242UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: raye
 * Date: 21/06/15
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class XMIRenderer implements Algorithm, GraphRenderer {
    private Graph vpcGraph;
    private IaaSProfile iaaSProfile;
    private Logger logger;
    private Map<String, PackageableElement> vpcArtefacts;

    private Model model;
    private ResourceSet resourceSet;
    private org.eclipse.uml2.uml.Package deploymentView;

    private URI uri;
    private Resource resource;

    public XMIRenderer(IaaSProfile iaaSProfile, Logger logger) {
        this.iaaSProfile = iaaSProfile;
        this.logger = logger;

        this.model = UMLFactory.eINSTANCE.createModel();
        model.setName("Root");
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(XMI212UMLResource.FILE_EXTENSION,
                new UMLResourceFactoryImpl());
        UMLPackage.eINSTANCE.setNsURI(XMI242UMLResource.UML_METAMODEL_2_4_1_NS_URI);
        UMLResourcesUtil.init(resourceSet);

        this.vpcArtefacts = new HashMap<String, PackageableElement>();

        this.iaaSProfile.applyProfile(model);

        this.deploymentView = createNestedPackage("Deployment View");
    }

    @Override
    public void init(Graph graph) {
        this.vpcGraph = graph;
    }

    @Override
    public void compute() {
        processNodes();

        processEdges();
    }

    private void processEdges() {
        for (Edge edge: vpcGraph.getEachEdge()) {
            logger.out("[e] %s: '%s' «%s».", edge.getAttribute(AttributeName.METACLASS), edge.getId(),
                    edge.getAttribute(AttributeName.STEREOTYPE));

            if (((String)edge.getAttribute(AttributeName.STEREOTYPE)).equalsIgnoreCase(UMLStereotype.DEPLOYMENT)) {
                createDeploymentAssociation(edge);
                logger.out("[create deployment]: %s, %s", edge.getId(), (String)edge.getAttribute(AttributeName.STEREOTYPE));
            } else {
                PackageableElement packageableElement =
                        deploymentView.createPackagedElement(edge.getId(), UMLPackage.eINSTANCE.getAssociation());
                vpcArtefacts.put(edge.getId(), packageableElement);

                ((Association) packageableElement).createOwnedEnd(edge.getNode0().getId(),
                        (Type) this.vpcArtefacts.get(edge.getNode1().getId()));

                ((Association) packageableElement).createOwnedEnd(edge.getNode1().getId(),
                        (Type) this.vpcArtefacts.get(edge.getNode0().getId()));

                applyStereotype(packageableElement, (String)edge.getAttribute(AttributeName.STEREOTYPE));
                logger.out("[create assoc]: %s, %s", edge.getId(), (String)edge.getAttribute(AttributeName.STEREOTYPE));
            }
        }
    }

    private void createDeploymentAssociation(Edge edge) {
        Node node0 = edge.getNode0();
        Node node1 = edge.getNode1();
        Deployment d;

        if (((String)node0.getAttribute(AttributeName.STEREOTYPE)).equalsIgnoreCase(UMLStereotype.ROUTER)) {
            d = ((DeploymentTarget)this.vpcArtefacts.get(node0.getId())).createDeployment(edge.getId());
            EList<NamedElement> e = d.getSuppliers();
            e.add((Artifact)this.vpcArtefacts.get(node1.getId()));
        } else {
            d = ((DeploymentTarget)this.vpcArtefacts.get(node1.getId())).createDeployment(edge.getId());
            EList<NamedElement> e = d.getSuppliers();
            e.add((Artifact)this.vpcArtefacts.get(node0.getId()));
        }
        vpcArtefacts.put(edge.getId(), d);
    }

    private void applyStereotype(PackageableElement packageableElement, String stereotypeAttribute) {
        iaaSProfile.applyStereotype(packageableElement, getStereotype(stereotypeAttribute));
    }

    private Stereotype getStereotype(String stereotype) {
        Stereotype umlStereotype = null;
        if (stereotype.equalsIgnoreCase(UMLStereotype.EC2_INSTANCE)) {
            umlStereotype = iaaSProfile.getEc2InstanceStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.INTERNET_GATEWAY)) {
            umlStereotype = iaaSProfile.getInternetGatewayStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ROUTER)) {
            umlStereotype = iaaSProfile.getRouterStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ELASTIC_LB)) {
            umlStereotype = iaaSProfile.getElasticLBStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.SECURITY_GROUP)) {
            umlStereotype = iaaSProfile.getSecurityGroupStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.NETWORK_ACL)) {
            umlStereotype = iaaSProfile.getNetworkAclStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.SUBNET)) {
            umlStereotype = iaaSProfile.getSubnetStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.ROUTE_TABLE)) {
            umlStereotype = iaaSProfile.getRouteTableStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.PACKET_FILTERING)) {
            umlStereotype = iaaSProfile.getPacketFilteringStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.VPC)) {
            umlStereotype = iaaSProfile.getVirtualPrivateCloudStereotype();
        } else if (stereotype.equalsIgnoreCase(UMLStereotype.OSI_LAYER2_LINK)) {
            umlStereotype = iaaSProfile.getOsiLayer2LinkStereotype();
        }
        return umlStereotype;
    }

    private void processNodes() {
        for(Node node : vpcGraph.getEachNode() ) {
            logger.out("[n] %s: '%s' «%s».", node.getAttribute(AttributeName.METACLASS), node.getId(),
                    node.getAttribute(AttributeName.STEREOTYPE));
            try {
                PackageableElement packageableElement =
                        deploymentView.createPackagedElement(node.getId(), getEClass(node));
                vpcArtefacts.put(node.getId(), packageableElement);
                applyStereotype(packageableElement, (String)node.getAttribute(AttributeName.STEREOTYPE));

                addProperties(packageableElement, node);
            } catch (Exception e) {
                logger.err("Error while adding %s %s: '%s'", node.getAttribute(AttributeName.METACLASS),
                        node.getId(), e.getMessage());
            }
        }
    }

    private void addProperties(PackageableElement packageableElement, Node node) {
        EList<Stereotype> stereotypes = packageableElement.getAppliedStereotypes();
        Iterator<Stereotype> stereotypeIterator = stereotypes.iterator();
        while (stereotypeIterator.hasNext()) {
            Stereotype stereotype = stereotypeIterator.next();
            System.out.println("AP STEREOTYPE: " + stereotype.getName());

            EList<Property> properties = stereotype.getAllAttributes();
            List<String> stereotypeProperties = new ArrayList<String>();
            Iterator<Property> propertyIterator = properties.iterator();
            while(propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                stereotypeProperties.add(property.getName());
                System.out.println(stereotype.getName() + " -> " + property.getName());
            }

            for (String attributeName: node.getEachAttributeKey()) {
                String attributeType = UMLStereotypeProperty.ATTRIBUTE_TYPE_MAP.get(attributeName);
                if (stereotypeProperties.contains(attributeName)) {
                    System.out.println(" found: " + attributeName);
                    if (attributeType.equalsIgnoreCase(UMLPrimitiveType.STRING)) {
                        packageableElement.setValue(stereotype, attributeName, node.getAttribute(attributeName));
                    } else if (attributeType.equalsIgnoreCase(UMLPrimitiveType.BOOLEAN)) {
                        packageableElement.setValue(stereotype, attributeName,
                                ((Boolean)node.getAttribute(attributeName)).toString());
                    }
                }
            }
            if (stereotypeProperties.contains(UMLStereotypeProperty.ID)) {
                packageableElement.setValue(stereotype, UMLStereotypeProperty.ID, node.getId());
            }


        }
    }

    private EClass getEClass(Node node) throws Exception {
        EClass eClass;

        if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.ARTEFACT)) {
            eClass =  UMLPackage.eINSTANCE.getArtifact();
        } else if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.ASSOCIATION)) {
            eClass =   UMLPackage.eINSTANCE.getAssociation();
        } else if (((String)node.getAttribute(AttributeName.METACLASS)).equalsIgnoreCase(MetaClass.NODE)) {
            eClass =   UMLPackage.eINSTANCE.getNode();
        } else {
            throw new Exception("Unknown metaclass");
        }
        return eClass;
    }

    private org.eclipse.uml2.uml.Package createNestedPackage(String name) {
        return this.model.createNestedPackage(name);
    }

    @Override
    public void render(Graph graph, String filePath) {
        this.init(graph);

        // Required here instead of within the save method due to undocumented behaviour with Eclipse UML2
        uri = URI.createFileURI(filePath).appendFileExtension(XMI212UMLResource.FILE_EXTENSION);
        resource = resourceSet.createResource(uri);
        resource.getContents().add(this.model);
        resource.getContents().add(iaaSProfile.getProfile());

        this.compute();
        this.save();
    }

    //private void save(String filePath) {
    private void save() {

        try {
            ((XMIResourceImpl)resource).setXMIVersion(UML212UMLResource.VERSION_2_1_VALUE);
            resource.save(null);
        } catch (IOException e) {
            System.err.println("Error saving XMI: " + e.getMessage());
        }
    }
}
