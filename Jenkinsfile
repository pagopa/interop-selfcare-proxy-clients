void sbtAction(String task) {
    sh '''
        echo "
        realm=Sonatype Nexus Repository Manager
        host=${NEXUS}
        user=${NEXUS_CREDENTIALS_USR}
        password=${NEXUS_CREDENTIALS_PSW}" > ~/.sbt/.credentials
        '''
    sh "sbt -Dsbt.log.noformat=true ${task}"
} 

void updateGithubCommit(String status) {
  def token = '${GITHUB_PAT_PSW}'
  sh """
    curl --silent --show-error \
      "https://api.github.com/repos/pagopa/${REPO_NAME}/statuses/${GIT_COMMIT}" \
      --header "Content-Type: application/json" \
      --header "Authorization: token ${token}" \
      --request POST \
      --data "{\\"state\\": \\"${status}\\",\\"context\\": \\"Jenkins Continuous Integration\\", \\"description\\": \\"Build ${BUILD_DISPLAY_NAME}\\"}" &> /dev/null
  """
}


pipeline {
  agent { label 'sbt-template' }
    environment {
    NEXUS = "${env.NEXUS}"
    MAVEN_REPO = "${env.MAVEN_REPO}"
    GITHUB_PAT = credentials('github-pat')
    NEXUS_CREDENTIALS = credentials('pdnd-nexus')
    REPO_NAME="""${sh(returnStdout:true, script: 'echo ${GIT_URL} | sed "s_https://github.com/pagopa/\\(.*\\)\\.git_\\1_g"')}""".trim()
  }
  stages {
    stage('Test') {
      steps {
        container('sbt-container') {
          updateGithubCommit 'pending'
          sbtAction 'test'
        }
      }
    }
    stage('Publish Client on Nexus') {
      when {
        anyOf {
          branch pattern: "[0-9]+\\.[0-9]+\\.x", comparator: "REGEXP"
          buildingTag()
        }
      }
      steps {
        container('sbt-container') {
          script {
            sbtAction 'clean compile scalafmt publish'
          }
        }
      }
    }
  }
  post {
    success { 
      updateGithubCommit 'success'
    }
    failure { 
      updateGithubCommit 'failure'
    }
    aborted { 
      updateGithubCommit 'error'
    }
  }
}
