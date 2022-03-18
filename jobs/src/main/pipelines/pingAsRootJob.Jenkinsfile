pipeline {

  agent {
    kubernetes {
      defaultContainer "super-container"
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          serviceAccountName: jenkins
          securityContext:
            runAsUser: 0
            fsGroup: 0
            runAsNonRoot: false
          containers:
          - name: super-container
            imagePullPolicy: Always
            image: debian:bullseye
            command:
            - sleep
            args:
            - 99d
      '''
    }
  }

  stages {

    stage("Install Ping Package"){
      steps {
        sh """
        apt-get update && apt-get install iputils-ping -y
        """
      }
    }

    stage("Ping Server"){
      steps {
        sh "chmod +x ${env.WORKSPACE}/resources/pingjob.sh"
        sh "${env.WORKSPACE}/resources/pingjob.sh"
      }
    }

  }

  post {
    always {
      cleanWs()
    }
  }

}
