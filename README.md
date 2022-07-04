# Jenkins Master

<a href="https://jenkins.io">
  <picture>
    <source width="400" media="(prefers-color-scheme: dark)" srcset="https://www.jenkins.io/images/jenkins-logo-title-dark.svg">
    <img width="400" src="https://www.jenkins.io/images/jenkins-logo-title.svg">
  </picture>
</a>

## About

[![Jenkins Regular Release](https://img.shields.io/endpoint?url=https%3A%2F%2Fwww.jenkins.io%2Fchangelog%2Fbadge.json)](https://www.jenkins.io/changelog)
[![Jenkins LTS Release](https://img.shields.io/endpoint?url=https%3A%2F%2Fwww.jenkins.io%2Fchangelog-stable%2Fbadge.json)](https://www.jenkins.io/changelog-stable)
[![Docker Pulls](https://img.shields.io/docker/pulls/jenkins/jenkins.svg)](https://hub.docker.com/r/jenkins/jenkins/)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3538/badge)](https://bestpractices.coreinfrastructure.org/projects/3538)

In a nutshell, Jenkins is the leading open-source automation server. 
Built with Java, it provides over 1,700 [plugins](https://plugins.jenkins.io/) to support automating virtually anything, 
so that humans can spend their time doing things machines cannot.

## What to Use Jenkins for and When to Use It

Use Jenkins to automate your development workflow, so you can focus on work that matters most. Jenkins is commonly used for:

- Building projects
- Running tests to detect bugs and other issues as soon as they are introduced
- Static code analysis
- Deployment

Execute repetitive tasks, save time, and optimize your development process with Jenkins.


## Installation

```bash
kubectl apply -k overlays/rancher-desktop
```

## Credentials

```bash
kubectl get secret jenkins-operator-credentials-jenkins-master -o 'jsonpath={.data.user}' -n jenkins | base64 -d
kubectl get secret jenkins-operator-credentials-jenkins-master -o 'jsonpath={.data.password}' -n jenkins | base64 -d
```

## Reference

* [Jenkins](https://www.jenkins.io/doc/book/)
* [Jenkins Operator](https://jenkinsci.github.io/kubernetes-operator/docs/getting-started/latest/)