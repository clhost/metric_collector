## Описание
Сервис каждую минуту опрашивает Linux ноду по SSH, собирает метрику по 
RAM, CPU и всем имеющимся дискам, сохраняет метрику в бд. Если параметр 
```load``` превышает 90%, сервис выполняет рассылку на email'ы, указанные в 
```receivers.txt```. Пример сообщения:

    RAM is overload: 94.125909204915977.
    Report:
             Total space: 7974
             Free space: 6830
             Used space: 210
             Available space: 7645
    DISK "/dev/sda1" is overload: 92.14.
    Report:
             Size: 118G
             Used: 2.8G
             Available: 110G
Также сервис выполняет рассылку, если любая из метрик в течении 15 минут подряд 
росла и итоговое значение выше или равно 70% (однако я учитываю, что, если 
в течение 15 минут метрика росла, но не достигла 70%, счёт продолжается, пока 
показатель не начнет снижаться или не достигнет 70%).

## Запуск
Запуск - ```./gradlew run```.

## Настройка
Основной конфиг - ```app.properties```. Пример:
     
    ssh.user=root
    ssh.host=localhost
    ssh.port=8080
    ssh.private_key=~/.ssh/some_private_key
    ssh.passphrase=clhost
    
    mail.username=devopstask@gmail.com
    mail.password=devopstask8
    mail.smtp.host=smtp.gmail.com
    mail.smtp.port=587
    mail.smtp.auth=true
    mail.smtp.ssl.trust=smtp.gmail.com
    mail.smtp.starttls.enable=true
    
В качестве отправителя почты был создан аккаунт:
    
    username: devopstask@gmail.com
    password: devopstask8
    
Привязан к моему телефону.

Второй конфиг - ```hibernate.properties```. Пример:

    hibernate.connection.driver_class=com.mysql.jdbc.Driver
    hibernate.connection.url=jdbc:mysql://localhost:3306/report_table
    hibernate.connection.username=root
    hibernate.connection.password=root
    
    hibernate.c3p0.min_size=5
    hibernate.c3p0.max_size=200
    hibernate.c3p0.timeout=1800
    hibernate.c3p0.max_statements=50
    
    hibernate.dialect=io.clhost.devopstask.database.config.UTFMySQLSupportDialect
    hibernate.current_session_context_class=thread
    
    hibernate.connection.CharSet=utf8
    hibernate.connection.characterEncoding=utf8
    hibernate.connection.useUnicode=true
    
    hibernate.hbm2ddl.auto=update

Использую ```Hibernate 5.2``` в качестве ORM и базу ```MySQL 5.7```. Предварительно требуется создание
```report_table``` базы (почему table - прост).