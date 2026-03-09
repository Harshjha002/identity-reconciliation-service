FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build -x test

RUN mv build/libs/*.jar ./app.jar

CMD ["java", "-jar", "./app.jar"]