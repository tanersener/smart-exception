# fluentd/Dockerfile

FROM fluent/fluentd:v1.6-debian-1
USER root
RUN ["gem", "uninstall", "elasticsearch"]
RUN ["gem", "install", "elasticsearch", "-v", "7.12"]
RUN ["gem", "install", "fluent-plugin-elasticsearch", "--no-document", "--version", "3.5.3"]
USER fluent
