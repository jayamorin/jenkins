ARG BUILD_DATE
ARG VCS_VERSION
ARG APP_VERSION

FROM node:16.15.1-alpine AS node

FROM jenkins/slave:4.9-1-alpine AS base
LABEL maintainer="Jay Amorin <jay.amorin@gmail.com>"
LABEL org.label-schema.build-date=${BUILD_DATE}
LABEL org.label-schema.name="jenkins-nodejs-worker"
LABEL org.label-schema.description="Nodejs worker."
LABEL org.label-schema.usage="/usr/share/doc/jenkins-nodejs-worker/usage.txt"
LABEL org.label-schema.vcs-url="https://github.com/jayamorin/jenkins"
LABEL org.label-schema.vcs-ref=${VCS_VERSION}
LABEL org.label-schema.version=${APP_VERSION}

USER root
COPY --chown=jenkins:jenkins src/usage.txt /usr/share/doc/jenkins-nodejs-worker/usage.txt

COPY --from=node /usr/lib /usr/lib
COPY --from=node /usr/local/share /usr/local/share
COPY --from=node /usr/local/lib /usr/local/lib
COPY --from=node /usr/local/include /usr/local/include
COPY --from=node /usr/local/bin /usr/local/bin

RUN apk add --update --virtual build-dependencies \
    make \
    curl && \
    apk del build-dependencies && \
    rm -rf /tmp/* /var/tmp/* /var/cache/apk/*

USER jenkins