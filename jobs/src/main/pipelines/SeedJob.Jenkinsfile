pipeline {
  agent { node { label 'built-in' } }
  stages {
    stage('Compile') {
      steps {
        sh './gradlew clean build'
      }
    }
    stage('Seed the Jobs') {
      steps {
        jobDsl targets: 'jobs/src/main/groovy/seed/SeedJob.groovy',
              additionalClasspath: 'jobs/build/libs/*.jar',
              removedJobAction: 'DELETE',
              removedViewAction: 'DELETE'
      }
    }
  }
  post {
    always {
      cleanWs()
    }
  }
}
