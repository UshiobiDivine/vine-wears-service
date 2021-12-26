FROM adoptopenjdk/openjdk16
EXPOSE 8080
ADD target/vine-wears-service.jar vine-wears-service.jar
ENTRYPOINT ["java", "-jar", "vine-wears-service.jar"]