#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v5.3.4').inside('-u root -e MAVEN_OPTS="-Duser.home=./"') {
        stage('check java') {
            sh "java -version"
        }

        stage('clean') {
            sh "chmod +x mvnw"
            sh "./mvnw clean"
        }

        stage('install tools') {
            sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v8.11.4 -DnpmVersion=6.4.1"
        }

        stage('npm install') {
            sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm"
        }

        stage('backend tests') {
            try {
                sh "./mvnw test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }

        stage('frontend tests') {
            try {
                sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='test -- -u'"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/test-results/jest/TESTS-*.xml'
            }
        }

        stage('package and deploy') {
            sh "./mvnw com.heroku.sdk:heroku-maven-plugin:2.0.5:deploy -DskipTests -Pprod -Dheroku.appName=twitster"
            archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        }
        stage('quality analysis') {
            withSonarQubeEnv('localhost') {
                sh "./mvnw sonar:sonar"
            }
        }
    }

    def dockerImage
    stage('build docker') {
        sh "cp -R src/main/docker target/"
        sh "cp target/*.war target/docker/"
        dockerImage = docker.build('jvandusen/twitster', 'target/docker')
    }

    stage('publish docker') {
        docker.withRegistry('https://registry.hub.docker.com', 'DOCKER_HUB_CREDENTIALS') {
            dockerImage.push 'latest'
        }
    }
}
