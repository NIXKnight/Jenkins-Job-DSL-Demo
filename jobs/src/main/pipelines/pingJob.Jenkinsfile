pipeline {

  agent {
    kubernetes {
      defaultContainer "jnlp"
    }
  }

  stages {

    stage("Install Ping Package"){
      steps {
        sh """
        sudo apt-get update && sudo apt-get install iputils-ping
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
