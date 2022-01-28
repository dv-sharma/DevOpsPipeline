node {
    stage("Git Clone"){

        git branch: 'main', credentialsId: 'GIT_HUB_CREDENTIALS', url: 'https://github.com/dv-sharma/DevOps_Project-1.git'
    }
    stage('Maven Build') {

     sh '''mvn clean install
'''
  }

    stage("Docker build"){

        sh 'docker version'
        sh 'docker build -t pipeline-demo .'
        sh 'docker image list'
        sh 'docker tag pipeline-demo 05061120/pipelinedemo:v1'
    }
    stage("Docker Login"){
        withCredentials([string(credentialsId: 'DOCKER_PASSWORD', variable: 'PASSWORD')]) {
            sh 'docker login -u 05061120 -p $PASSWORD'
        }
    }
    stage("Pushing Image to Docker Hub"){
        sh 'docker push  05061120/pipelinedemo:v1'
    }
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
