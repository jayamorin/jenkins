FROM jenkins/ssh-slave:latest 
USER root
RUN apt-get update && \
    apt-get -y install curl
RUN curl -fsSL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get update && \
    apt-get install -y nodejs \
    gcc \
    g++ \
    make
RUN curl -fsSL https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - && \
    echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list && \
    apt-get update && \
    apt-get install -y yarn
RUN groupadd -g 999 docker
RUN usermod -aG docker jenkins
USER jenkins
