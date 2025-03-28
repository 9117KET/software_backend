@echo off
set SPRING_DATASOURCE_USERNAME=postgres
set SPRING_DATASOURCE_PASSWORD=PzvtlSLIhZUZdSNzJsPvfxzlYqCPzbqu
set SPRING_DATASOURCE_URL=jdbc:postgresql://maglev.proxy.rlwy.net:44972/railway
set SPRING_MAIL_HOST=smtp.gmail.com
set SPRING_MAIL_PORT=587
set SPRING_MAIL_USERNAME=dashpress1000@gmail.com
set SPRING_MAIL_PASSWORD=hifkcdnflwecfpvj
set JWT_SECRET=b8iuBAt4rXDi4MI863UWRFKR0q4KBT7I6jU1maMuyz8G

call mvnw.cmd spring-boot:run 