pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: 'main',
                        url: 'git@github.com:duchuyyyy/stock-alert.git',
                        credentialsId: 'github-ssh-key'
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
