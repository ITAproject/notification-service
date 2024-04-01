FROM rsunix/yourkit-openjdk17

WORKDIR /app

COPY target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "--enable-preview", "-jar", "app.jar"]