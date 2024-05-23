FROM openjdk:11

RUN apt-get update \
    && apt-get install -y maven git \
    && rm -rf /var/lib/apt/lists/*

# POZOR! vrstva se ulozi do cache a preskoci se i kdyz je v repu neco noveho!
# workaround: docker build --no-cache ... ale tim se pripravime o zasadni vyhodu
# ciste reseni: ---> nedelat git clone v Dockerfile <---
RUN git clone https://github.com/amoravek/k8s.git \
    && cd k8s/k8s-sample-app \
    && mvn clean package

WORKDIR k8s/k8s-sample-app/target

RUN touch xxx

ENTRYPOINT ["java", "-jar", "k8s-sample-app.jar"]
CMD ["server"]
