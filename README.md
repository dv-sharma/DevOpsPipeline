# CI/CD Pipeline: Deploying Containerized Java SpringBoot Application on Kubernetes Cluster hosted on AWS

### DESCRIPTION

#### PROJECT IDEA 

This project is a continuation of [Project1](https://github.com/dv-sharma/DevOps_Project-1), where we containerized a Java application using Docker & deployed it on a local minikube **Kubernetes** cluster.

All this was done manually, We will be implementing DevOps in this project by automating these processes and optimizing the flow.

#### IMPLEMENTATION

Project implementation:

- CI/CD Pipeline using Jenkins which pulls the application code from Github.

- Containerize the application using Docker & publish the image on DockerHub.

- Deploy the containerized application on a Kubernetes cluster hosted on AWS.

#### TOOLS & TECHNOLOGIES USED

- Framework/Language: Java Spring Boot
- Source Version Control: Github
- Build Automation tool: Maven
- CI/CD: Jenkins
- Containerization platform: Docker, DockerHub
- Container orchestration: Kubernetes
- Virtual Cloud Servers: AWS

### PROJECT LEARNINGS

1. Understanding of Application Development, Server Administration & Deployment on Cloud.

2. Design implement Continuous Integration and Continuous Deployment pipelines & Pipeline As Code.

3. Experience with containerization tools such as Docker & Kubernetes.

4. Understanding of Virtualization and Container architectures.

5. Lots & Lots of Troubleshooting experience with failed builds, jobs, networks and creating everything from scratch.

#### SETUP

We need 3 Servers (virtual machines) that are hosted on AWS ec2 instances:


1.  Jenkins server (Amazon Linux) 

2. Kubernetes Master (Ubuntu) // Use t2.medium for Master node.

3. Kubernetes Worker (Ubuntu)

NOTE: I have provisioned this Kubernetes cluster using **kubeadm**



![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1643390063430/4b5T_DE3f.png)

> Explanation for 

Application development

Dockerfile 

Kubernetes yamls 

here-https://github.com/dv-sharma/DevOps_Project-1

#### JenkinsFile

You can find the full pipeline in **Jenkinsfile**

**STAGE 1- Cloning the project**

Clone the source code of GitHub repo inside Jenkins.

We can take the help of a pipeline syntax generator for this.

The 'GIT_HUB_CREDENTIALS' can be set up in credentials -> global configuration.


```
stage("Git Clone"){
        cleanWs()
        git branch: 'main', credentialsId: 'GIT_HUB_CREDENTIALS', url: 'https://github.com/dv-sharma/DevOps_Project-1.git'
    }
```

**STAGE 2- Building with Maven**

Building the Spring Boot Application Using Maven

Running simple mvn clean install command

```
stage('Maven Build') {

     sh '''mvn clean install'''
  }
```

**STAGE 3- Building the Docker image with version **

This command outputs the docker version, builds the image based on Dockerfile present in the directory, then lists the images and tags the image.


```
stage("Docker build"){

        sh 'docker version'
        sh 'docker build -t pipeline-demo .'
        sh 'docker image list'
        sh 'docker tag pipeline-demo 05061120/pipelinedemo:v1'
    }
```

**STAGE 4- Login to DockerHub**

Store the docker credentials the same way we did above for Github
We are using the withCredentials plugin for passing the credentials as variables to login to our DockerHub command.


```
stage("Docker Login"){
        withCredentials([string(credentialsId: 'DOCKER_PASSWORD', variable: 'PASSWORD')]) {
            sh 'docker login -u 05061120 -p $PASSWORD'
        }
    }
```

**STAGE 5-Pushing image to DockerHub**

Pushing our image to Dockerhub

```.
stage("Pushing Image to Docker Hub"){
        sh 'docker push  05061120/pipelinedemo:v1'
    }
```

**STAGE 6-Deploying on Kubernetes Cluster**

Here,We are SSHing into the master with **sshagent ** plugin.

This is how I have stored the credentials. This uses the .PEM key we use while spinning up the AWS machine.

SCP (Secure Copy Protocol) is a network protocol used to securely copy files/folders between Linux (Unix) systems on a network.
we are using this to copy the deployment file- firstdep.yaml & service file- samplesvc.yaml to the kubernetes node.

Next step is to run the deployments & service object yamls , for that we have created a script. If there are no files created it will catch the error & create objects , otherwise apply the changes we have done to the yaml files.

![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1643396402323/kmKBmL_Es.png)

```
stage ("SSH kubernetes master & Deploy"){
        sshagent(['k9-master']) {
            sh "scp -o StrictHostKeyChecking=no firstdep.yaml samplesvc.yaml ubuntu@172.31.15.234:/home/ubuntu/"
            script{
                try{
                    
                    sh "ssh ubuntu@172.31.15.234 kubectl apply -f ."
                    
                }
                catch(error){
                    sh "ssh ubuntu@172.31.15.234 kubectl create -f . "
                }                
            }
            }
            }
    }
```

That's it, trigger the build from jenkins and your application is deployed. There are lot's of other ways to trigger the pipeline , for e.g. based on Git commit, based on SCM polling. 

![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1643397256931/tg4673kTW.png)


![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1643397490959/4yi6vXnXF.png)

The application is available at port 32000 of our node because of NodePort service


![image.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1643446777403/MubycHgKW.png)


##  FUTURE SCOPE

I am keeping this Project OPEN-SOURCE & will keep on updating this with any improvements in the Pipeline, such as better way of Infrastructure setup , version, more automation.

Also I will update more details regarding setting of infrastructure & other details about the files.

**THANKS**





