
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checkout'
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvnw.cmd clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t order-management:${env.BUILD_NUMBER} ."
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat 'set "KUBECONFIG=C:/ProgramData/Jenkins/.kube/config" && kubectl apply -f deployment.yaml'
                
                bat "kubectl set image deployment/order-management order-management=order-management:${env.BUILD_NUMBER}"

                bat 'kubectl rollout status deployment/order-management --timeout=120s'
            }
        }

        stage('Verify Deployment') {
            steps {
                bat 'kubectl get pods'
                bat 'kubectl get service order-management-service'
            }
        }
    }

    post {
        success {
            echo 'CI/CD successful!'
        }
        failure {
            echo 'Pipeline failed. Check the Jenkins Console Output.'
        }
    }
}
