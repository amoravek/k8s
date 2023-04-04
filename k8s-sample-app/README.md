# K8sSampleApp

How to start the K8sSampleApp application
---

1. Run `mvn clean package` to build your application
1. Start application with `java -jar target/k8s-sample-app.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
