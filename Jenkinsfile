pipeline {
    agent any

        stage("Staging"){
            steps{
                sh 'docker-compose up -d --build'
            }
        }
    }
}