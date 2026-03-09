FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

CMD ["java", "-jar", "build/libs/*.jar"]