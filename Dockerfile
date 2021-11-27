FROM gradle:7-jdk17-alpine
COPY . /src/moment_backend
WORKDIR /src/moment_backend

RUN gradle --no-daemon build



CMD gradle --no-daemon bootRun
