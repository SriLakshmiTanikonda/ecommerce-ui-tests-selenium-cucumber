// Declarative Jenkins pipeline. Expects JDK and Maven tools configured in Jenkins,
// and Chrome/Firefox on the agent, or a Selenium Grid URL.
pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser to run on')
        string(name: 'TAGS', defaultValue: '@smoke', description: 'Cucumber tag expression, e.g. @smoke or "@checkout and not @wip"')
        string(name: 'THREADS', defaultValue: '3', description: 'Scenarios to run in parallel')
        string(name: 'GRID_URL', defaultValue: '', description: 'Selenium Grid URL; leave empty to run browsers on the agent')
        booleanParam(name: 'RERUN_FAILED', defaultValue: true, description: 'Retry failed scenarios once before failing')
    }

    options {
        timeout(time: 45, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh """
                    mvn -B clean test \
                      -Dbrowser=${params.BROWSER} \
                      -Dheadless=true \
                      -Dparallel.threads=${params.THREADS} \
                      -Drerun=${params.RERUN_FAILED} \
                      -Dgrid.url=${params.GRID_URL} \
                      "-Dcucumber.filter.tags=${params.TAGS}"
                """
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/cucumber-reports/cucumber.xml'
            // Needs the Jenkins Allure plugin; remove this line if it isn't installed.
            allure includeProperties: false, results: [[path: 'target/allure-results']]
            archiveArtifacts artifacts: 'target/cucumber-reports/**, target/logs/**', allowEmptyArchive: true
        }
    }
}
