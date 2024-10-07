pipeline {
    agent any

    stages {
        stage('Clone or Pull Repository') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GITHUB_USERNAME', passwordVariable: 'GITHUB_TOKEN')]) {
                        git url: 'https://$GITHUB_USERNAME:$GITHUB_TOKEN@github.com/duchuyyyy/stock-alert.git'
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

