# Dockerize Jenkins

## Setup and Configuration

1. Install nginx

> ```bash
> apt install nginx
> cp /etc/nginx/sites-available/default /etc/nginx/sites-available/default.`date +%s`
>
> cat > /etc/nginx/sites-available/default <<EOF
> server {
>    listen 80 default_server;
>    listen [::]:80 default_server;
>    server_name jenkins.cloudstart.io;
>
>    location / {
>        proxy_set_header        Host $host:$server_port;
>        proxy_set_header        X-Real-IP $remote_addr;
>        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
>        proxy_set_header        X-Forwarded-Proto $scheme;
>
>        # Fix the "It appears that your reverse proxy set up is broken" error.
>        proxy_pass          http://127.0.0.1:8080;
>        proxy_read_timeout  90;
>
>        proxy_redirect      http://127.0.0.1:8080 http://jenkins.cloudstart.io;
>
>        # Required for new HTTP-based CLI
>        proxy_http_version 1.1;
>        proxy_request_buffering off;
>        # workaround for https://issues.jenkins-ci.org/browse/JENKINS-45651
>        add_header 'X-SSH-Endpoint' 'jenkins.cloudstart.io:50022' always;
>    }
> }
> EOF
> systemctl enable nginx && systemctl restart nginx
> ```

2. Install docker and make sure to uninstall previous version of docker, containerd and runc.

> ```bash
> sudo apt-get update
> sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
> curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
> sudo add-apt-repository    "deb [arch=amd64] https://download.docker.com/linux/ubuntu  $(lsb_release -cs) stable"
> sudo apt-get update
> sudo apt-get install docker-ce docker-ce-cli containerd.io
> sudo docker run hello-world
> sudo usermod -aG docker user
> ```

3. Install docker-compose

> ```bash
> sudo curl -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
> sudo chmod +x /usr/local/bin/docker-compose
> ```

4. Reboot the server
