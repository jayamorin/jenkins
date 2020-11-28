ARG BUILD_DATE
ARG BUILD_VERSION
ARG APP_VERSION
FROM jenkins/jenkins:${APP_VERSION} AS default
LABEL maintaner="Jay Amorin <jay.amorin@gmail.com>"
LABEL org.label-schema.build-date=${BUILD_DATE}
LABEL org.label-schema.name="jenkins-master"
LABEL org.label-schema.description="Jenkins master running on docker"
LABEL org.label-schema.usage="/usr/share/doc/jenkins-master/usage.txt"
LABEL org.label-schema.url="https://jenkins.jayamorin.com"
LABEL org.label-schema.vcs-url="https://github.com/jayamorin/jenkins-docker"
LABEL org.label-schema.vcs-ref=$BUILD_VERSION
LABEL org.label-schema.version="1.0"
LABEL org.label-schema.vendor="jayamorin.com"
LABEL org.label-schema.schema-version="1.0"
LABEL org.label-schema.docker.cmd="docker run -d -p 8080:8080 -p 50000:50000 \
    -v jenkins_data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock \
    -v /home/user/.ssh:/var/jenkins_home/.ssh -e JAVA_OPTS=-Xmx4096m -Djava.awt.headless=true \
    -Djenkins.install.runSetupWizard=false -Dhudson.footerURL=http://jenkins.jayamorin.com \
    -Djava.util.logging.config.file=/var/jenkins_home/log.properties \
    -e TRY_UPGRADE_IF_NO_MARKER:=true jenkins-master:latest"
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY log.properties /var/jenkins_home/log.properties
COPY plugins.txt /usr/share/jenkins/plugins.txt
COPY usage.txt /usr/share/doc/jenkins-master/usage.txt
RUN /usr/local/bin/install-plugins.sh $(cat /usr/share/jenkins/plugins.txt | tr '\n' ' ')
USER root
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get -y install apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common && \
    curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
    add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
    $(lsb_release -cs) \
    stable" && \
    apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get -y install docker-ce && \
    rm -rf /var/lib/apt/lists/*
RUN groupmod -g 998 docker
RUN usermod -a -G docker jenkins
USER jenkins
