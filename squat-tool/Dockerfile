#from https://runnable.com/docker/java/dockerize-your-java-application
FROM  phusion/baseimage:0.9.17


RUN echo "deb http://archive.ubuntu.com/ubuntu trusty main universe" > /etc/apt/sources.list

RUN apt-get -y update
#install java
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y -q python-software-properties software-properties-common

ENV JAVA_VER 8
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN echo 'deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list && \
    echo 'deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys C2518248EEA14886 && \
    apt-get update && \
    echo oracle-java${JAVA_VER}-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections && \
    apt-get install -y --force-yes --no-install-recommends oracle-java${JAVA_VER}-installer oracle-java${JAVA_VER}-set-default && \
    apt-get clean && \
    rm -rf /var/cache/oracle-jdk${JAVA_VER}-installer

RUN update-java-alternatives -s java-8-oracle

RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >> ~/.bashrc

RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*


#copy files to docker
ADD pcm /home/pcm
ADD src /home/src
ADD lib /home/lib
ADD pom.xml /home/pom.xml


# Install maven
ENV MAVEN_VERSION 3.3.9

RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

WORKDIR /home

RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/fastpan-v0.2.1.jar   -DgroupId=de.fakeller.performance   -DartifactId=fastpan   -Dversion=0.2.1   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/guava-19.0.jar   -DgroupId=com.google.guava   -DartifactId=guava   -Dversion=19.0   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/log4j-over-slf4j-1.7.21.jar   -DgroupId=org.slf4j   -DartifactId=log4j-over-slf4j   -Dversion=1.7.21   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/log4j-2.5.jar   -DgroupId=org.apache.logging.log4j   -DartifactId=log4j   -Dversion=2.5   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/hpo.jar   -DgroupId=hpo   -DartifactId=hpo   -Dversion=1.0.0   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.apache.commons.logging_1.1.1.v201101211721.jar   -DgroupId=commons-logging   -DartifactId=commons-logging   -Dversion=1.1.1   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.emf.cdo.common_4.3.0.v20140309-0644.jar   -DgroupId=org.eclipse.emf   -DartifactId=org.eclipse.emf.cdo.common   -Dversion=4.3.0   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.emf.cdo_4.3.0.v20140520-1823.jar   -DgroupId=org.eclipse.emf   -DartifactId=org.eclipse.emf.cdo   -Dversion=4.3.0   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.emf.common_2.10.1.v20150123-0348.jar   -DgroupId=org.eclipse.emf   -DartifactId=org.eclipse.emf.common   -Dversion=2.10.1   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.emf.ecore.xmi_2.10.2.v20150123-0348.jar   -DgroupId=org.eclipse.emf   -DartifactId=org.eclipse.emf.ecore.xmi   -Dversion=2.10.2   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.emf.ecore_2.10.2.v20150123-0348.jar   -DgroupId=org.eclipse.emf   -DartifactId=eclipse.emf.ecore   -Dversion=2.10.2   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.ocl.ecore_3.3.100.v20140610-0641.jar   -DgroupId=org.eclipse.ocl   -DartifactId=org.eclipse.ocl.ecore   -Dversion=3.3.100   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/org.eclipse.ocl_3.4.2.v20140725-2242.jar   -DgroupId=org.eclipse.ocl   -DartifactId=org.eclipse.ocl   -Dversion=3.4.2   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/palladio-analysis-0.6.3-SNAPSHOT.jar   -DgroupId=de.fakeller.palladio-headless   -DartifactId=palladio-analysis   -Dversion=0.6.3   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/palladio-builder-0.6.3-SNAPSHOT.jar   -DgroupId=de.fakeller.palladio-headless   -DartifactId=palladio-builder   -Dversion=0.6.3   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/palladio-environment-0.6.3-SNAPSHOT-jar-with-dependencies.jar  -DgroupId=de.fakeller.palladio-headless   -DartifactId=palladio-environment   -Dversion=0.6.3   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]
RUN ["mvn", "org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=/home/lib/slf4j-api-1.7.21.jar   -DgroupId=org.sl4j   -DartifactId=slf4j-api   -Dversion=1.7.21   -Dpackaging=jar   -DlocalRepositoryPath=localrep"]

# Prepare by downloading dependencies
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

RUN ["mvn", "package"]

# the jar is not copied anymore but created by maven
#ADD squat-tool-performance-bot.jar /home/squat-tool-performance-bot.jar

ENTRYPOINT java -jar /home/squat-tool-performance-bot.jar