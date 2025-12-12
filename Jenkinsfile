pipeline {
    agent any

    tools {
        // "maven" must match the name configured in Jenkins Global Tools
        maven 'maven'
        jdk 'jdk17'
    }

    stages {
        stage('Build & Test') {
            steps {
                // This runs the tests and generates the JaCoCo report
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // This looks for a SonarQube server configured in Jenkins
                // we name the server "sonar" in Jenkins settings
                withSonarQubeEnv('sonar') {
                    //sh 'mvn sonar:sonar'
					sh 'mvn sonar:sonar -Dsonar.host.url=http://sonarqube:9000'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                // This waits for SonarQube to say "Pass" or "Fail"
                // If it fails (Red), the pipeline stops.
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
