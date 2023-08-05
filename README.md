# TOILET MAP BACK-END

- Change profile in `/src/main/resources/profiles/default/application.yml` folder

    - In line 29, change to `active: default,profileName` (ex: active: default,tiendev)

- Run this command to run project local

  `mvn spring-boot:run -D"spring-boot.run.profiles"=profileName`

  Example:

  `mvn spring-boot:run -D"spring-boot.run.profiles"=tiendev`

  `mvn spring-boot:run -D"spring-boot.run.profiles"=phuongdev`

  `mvn spring-boot:run -D"spring-boot.run.profiles"=quandev`

- Swagger UI

  `http://localhost:8081/swagger-ui/index.html`


- Tien's Note: `mvn clean install -DskipTests -Ptiendev `

    