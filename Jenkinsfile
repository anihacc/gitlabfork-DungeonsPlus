pipeline {
  options {
    buildDiscarder(logRotator(artifactNumToKeepStr: '10'))
  }
  agent {
    docker {
      args '-v gradle-cache:/home/gradle/.gradle'
      image 'gradle:jdk17'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'gradle build'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts 'build/libs/*.jar'
      }
    }
  }
}
