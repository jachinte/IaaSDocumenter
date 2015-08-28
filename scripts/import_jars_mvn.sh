#!/bin/sh
mvn install:install-file -Dfile=org.eclipse.uml2_5.0.0.v20150202-0947.jar -DgroupId=org.eclipse -DartifactId=uml2 -Dpackaging=jar -Dversion=5.0.0.v20150202-0947
mvn install:install-file -DgroupId=org.eclipse.uml2 -Dpackaging=jar -Dversion=5.0.0.v20150202-0947 -DartifactId=uml2 -Dfile=org.eclipse.uml2.uml_5.0.2.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml -Dpackaging=jar -Dversion=5.0.0.v20150202-0947 -DartifactId=validation -Dfile=org.eclipse.uml2.uml.validation_5.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml -Dpackaging=jar -Dversion=5.0.0.v20150202-0947 -DartifactId=edit -Dfile=org.eclipse.uml2.uml.edit_5.0.2.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml.profile -Dpackaging=jar -Dversion=1.0.0.v20150202-0947 -DartifactId=standard -Dfile=org.eclipse.uml2.uml.profile.standard_1.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml -Dpackaging=jar -Dversion=5.0.1.v20150202-0947 -DartifactId=editor -Dfile=org.eclipse.uml2.uml.editor_5.0.1.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml.ecore -Dpackaging=jar -Dversion=3.0.0.v20150202-0947 -DartifactId=importer -Dfile=org.eclipse.uml2.uml.ecore.importer_3.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.uml.ecore -Dpackaging=jar -Dversion=3.0.0.v20150202-0947 -DartifactId=exporter -Dfile=org.eclipse.uml2.uml.ecore.exporter_3.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2 -Dpackaging=jar -Dversion=2.0.0.v20150202-0947 -DartifactId=types -Dfile=org.eclipse.uml2.types_2.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2 -Dpackaging=jar -Dversion=5.0.0.v20150202-0947 -DartifactId=doc -Dfile=org.eclipse.uml2.doc_5.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2 -Dpackaging=jar -Dversion=2.0.1.v20150202-0947 -DartifactId=common -Dfile=org.eclipse.uml2.common_2.0.1.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.common -Dpackaging=jar -Dversion=2.0.0.v20150202-0947 -DartifactId=edit -Dfile=org.eclipse.uml2.common.edit_2.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.codegen -Dpackaging=jar -Dversion=2.0.2.v20150202-0947 -DartifactId=ecore -Dfile=org.eclipse.uml2.codegen.ecore_2.0.2.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2.codegen.ecore -Dpackaging=jar -Dversion=2.0.0.v20150202-0947 -DartifactId=ui -Dfile=org.eclipse.uml2.codegen.ecore.ui_2.0.0.v20150202-0947.jar
mvn install:install-file -DgroupId=org.eclipse.uml2 -Dpackaging=jar -Dversion=1.0.0.v20150202-0947 -DartifactId=ant -Dfile=org.eclipse.uml2.ant_1.0.0.v20150202-0947.jar

