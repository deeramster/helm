pipeline {
    agent any // Выбираем Jenkins агента, на котором будет происходить сборка: нам нужен любой

    environment {
        TELEGRAM_BOT_TOKEN = credentials('telegram-bot-token') // ID credentials
        TELEGRAM_CHAT_ID = credentials('telegram-chat-id')
    }

    triggers {
        pollSCM('H/5 * * * *') // Запускать будем автоматически по крону примерно раз в 5 минут
    }

    tools {
        maven 'maven-3.8.1' // Для сборки бэкенда нужен Maven
        // jdk 'jdk16' // И Java Developer Kit нужной версии
        nodejs 'node-16' // NodeJS нужен для фронта
    }

    stages {
        stage('Build & Test backend') {
            steps {
                dir("backend") { // Переходим в папку backend
                    sh 'mvn package' // Собираем мавеном бэкенд
                }
            }

            post {
                success {
                    junit 'backend/target/surefire-reports/**/*.xml' // Передадим результаты тестов в Jenkins
                }
            }
        }

        stage('Build frontend') {
            steps {
                dir("frontend") {
                    sh 'npm install' // Для фронта сначала загрузим все сторонние зависимости
                    sh 'npm run build' // Запустим сборку
                }
            }
        }

        stage('Save artifacts') {
            steps {
                archiveArtifacts(artifacts: 'backend/target/sausage-store-0.0.1-SNAPSHOT.jar')
                archiveArtifacts(artifacts: 'frontend/dist/frontend/*')
            }
        }
    }
    post {
        success {
            sh """curl -X POST -H 'Content-type: application/json' \
                --data '{"chat_id": "${TELEGRAM_CHAT_ID}", "text": "Build successful! 🎉"}' \
                https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage"""
        }
    }
}
