FROM gcr.io/distroless/java11

COPY ./target/k8s-sample-app.jar /app/

# shell-formu nemuzeme pouzit, protoze v distroless image chybi shell
# ENTRYPOINT java -jar /app/k8s-sample-app.jar

# ...proto pouzijeme exec-formu
ENTRYPOINT ["java", "-jar", "/app/k8s-sample-app.jar"]

CMD ["server"]
