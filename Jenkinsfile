pipeline {
    agent any

    stages {
         stage('Clone Repository') {
               steps {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                        sh 'git clone https://$GITHUB_TOKEN@github.com/duchuyyyy/stock-alert.git'
                        }
                    }
               }
         }

        stage('Down existing container') {
            steps {
                script {
                    sh "docker-compose down"
                }
            }
        }

        stage('Build docker container') {
            steps {
                script {
                    sh "docker-compose up -d"
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}
