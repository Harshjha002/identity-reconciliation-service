FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build -x test

RUN ls build/libs/

RUN cp $(ls build/libs/*.jar | grep -v plain) ./app.jar

CMD ["java", "-jar", "./app.jar"]