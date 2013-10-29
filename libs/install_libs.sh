#!/bin/bash
mvn install:install-file -Dfile=saxon9he-9.0.2j.jar -DgroupId=net.sf.saxon -DartifactId=saxon9he -Dversion=9.0.2j -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=jmxri-1.2.1.jar -DgroupId=com.sun.jmx -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=jmxtools-1.2.1.jar -DgroupId=com.sun.jdmk -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true

