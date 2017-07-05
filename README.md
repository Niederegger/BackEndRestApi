# BackEnd
Before Compilation, insert Files from dist folder of Angular FrontEnd into resource/static
-> this way you get the benefits of FrontEnd developed Angular Application with Rest Api Spring Boot

inside resource/application.properties server Port is handled like followed:
8080 as Developement Port
80 as deployment Port

you can adjust this as you wish

compile:
```
mvn clean package
```

start:
```
java -jar target\AjaxDemo-0.0.1-SNAPSHOT.jar
```

Required:
Tomcat Server (used 8.5)


also:
adjust Server Varialbes inside 
/AjaxDemo/src/main/java/de/vv/web/db/DBCon.java
/AjaxDemo/src/main/resources/application.properties/application.properties
