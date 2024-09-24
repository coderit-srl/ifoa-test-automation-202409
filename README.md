Installazione Allure:
 - https://allurereport.org/docs/install-for-windows/

Per eseguire i test con Allure, eseguire da linea di comando:
"mvn clean test surefire-report:report-only"

Per avviare il server per visualizzare i report, eseguire da linea di comando:
"allure serve target/allure-results"