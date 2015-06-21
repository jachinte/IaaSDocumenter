package com.eu.skyblue.iaasdocumenter.renderer;

import com.eu.skyblue.iaasdocumenter.generator.aws.AttributeName;
import com.eu.skyblue.iaasdocumenter.generator.aws.MetaClass;
import com.eu.skyblue.iaasdocumenter.uml.IaaSProfile;
import com.eu.skyblue.iaasdocumenter.utils.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
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


            PackageableElement packageableElement =
                    deploymentView.createPackagedElement(edge.getId(), UMLPackage.eINSTANCE.getAssociation());
            vpcArtefacts.put(edge.getId(), packageableElement);

            ((Association)packageableElement).createOwnedEnd(edge.getNode0().getId(),
                    (Type)this.vpcArtefacts.get(edge.getNode1().getId()));

            ((Association)packageableElement).createOwnedEnd(edge.getNode1().getId(),
                    (Type)this.vpcArtefacts.get(edge.getNode0().getId()));

            // Create stereotype
        }
    }

    private void processNodes() {
        for(Node node : vpcGraph.getEachNode() ) {
            logger.out("[n] %s: '%s' «%s».", node.getAttribute(AttributeName.METACLASS), node.getId(),
                    node.getAttribute(AttributeName.STEREOTYPE));
            try {
                PackageableElement packageableElement =
                        deploymentView.createPackagedElement(node.getId(), getEClass(node));
                vpcArtefacts.put(node.getId(), packageableElement);
            } catch (Exception e) {
                logger.err("Error while adding %s %s: '%s'", node.getAttribute(AttributeName.METACLASS),
                        node.getId(), e.getMessage());
            }

            // Add stereotype here
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
        this.compute();
        this.save(filePath);
    }

    private void save(String filePath) {
        URI uri = URI.createFileURI(filePath).appendFileExtension(XMI212UMLResource.FILE_EXTENSION);
        Resource resource = resourceSet.createResource(uri);
        resource.getContents().add(iaaSProfile.getProfile());
        resource.getContents().add(this.model);

        try {
            ((XMIResourceImpl)resource).setXMIVersion(UML212UMLResource.VERSION_2_1_VALUE);
            resource.save(null);
        } catch (IOException e) {
            System.err.println("Error saving XMI: " + e.getMessage());
        }
    }
}
