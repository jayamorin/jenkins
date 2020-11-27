FROM jenkins/ssh-slave:latest AS default
ARG BUILD_DATE
ARG BUILD_VERSION
LABEL maintaner="Jay Amorin <jay.amorin@gmail.com>"
LABEL org.label-schema.build-date=${BUILD_DATE}
LABEL org.label-schema.name="jenkins-slave-python"
LABEL org.label-schema.description="Jenkins slave running python on docker"
LABEL org.label-schema.usage="/usr/share/doc/jenkins-slave-python/usage.txt"
LABEL org.label-schema.vcs-url="https://github.com/jayamorin/jenkins-docker"
LABEL org.label-schema.vcs-ref=$BUILD_VERSION
LABEL org.label-schema.version="1.0"
LABEL org.label-schema.vendor="jayamorin.com"
LABEL org.label-schema.schema-version="1.0"

FROM default AS python3.9
LABEL org.label-schema.docker.python-version="3.9"
LABEL org.label-schema.docker.cmd="docker run -d jenkins-slave-python:3.9"
USER root
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get -y install python3.9 \
    python3.9-dev \
    python3.9-venv \
    python3-pip && \
    rm -rf /var/lib/apt/lists/*
COPY usage.txt /usr/share/doc/jenkins-slave-python/usage.txt
RUN groupadd -g 999 docker
RUN usermod -aG docker jenkins
USER jenkins
