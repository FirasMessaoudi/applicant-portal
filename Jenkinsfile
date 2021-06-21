pipeline {
    agent any
    
    triggers {
        cron('H */4 * * 1-5')
    }

    stages {
    	stage('Clone sources') {
    		steps {
    			echo 'Cloning source code...'
	        	git credentialsId: 'dcc-jenkins-git', url: 'http://dcc.elm.com.sa:9992/dcc/dcc-foundation.git'
        	}
	    }
        stage('Build') {
            steps {
                echo 'Building...'
                 withMaven(maven: 'maven-3.5.2') {
			      sh "mvn -f dcc-foundation-quickstarts/dcc-quickstarts-aash/pom.xml clean package"
			    }
            }
        }
        /*stage('Maven deploy') {
            steps {
                echo 'Deploying to nexus...'
                 withMaven(maven: 'maven-3.5.2') {
			      sh "mvn -f dcc-foundation-quickstarts/dcc-quickstarts-aash/pom.xml deploy -Dmaven.test.skip -Ddistribution.repo.url=http://dcc.elm.com.sa:8081"
			    }
            }
        }*/
        stage('Sonar') {
        	
        	when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
                echo 'Sonar metrics for build#${env.BUILD_ID}'
                 withMaven(maven: 'maven-3.5.2') {
			      sh "mvn -f dcc-foundation-quickstarts/dcc-quickstarts-aash/pom.xml sonar:sonar"
			    }
            }
        }
        stage('Deploy') {
        	when {
              expression {
                currentBuild.result == null || currentBuild.result == 'SUCCESS' 
              }
            }
            steps {
            	echo "Deploying build#${env.BUILD_ID}"
                sh 'cd dcc-foundation-quickstarts/dcc-quickstarts-aash; export DOCKER_HOST=127.0.0.1:2375; docker-compose kill; docker-compose up -d'
            }
        }
    }
}