На текущий момент не все что хотелось реализовать успелось. Я оставил TODO'шки в коде к которым планирую вернуться позже.
Также справедливо будет отметить что тестирование приложения и его отладка заняли весьма значительное количество времени. Возможно, некоторые пункты, вероятно, даже могут выходить за рамки базовых требований к реализации задачи (т.к. основной целью являлось, на мой взгляд, все таки демонстрация навыков разработки), но тем не менее их хотелось бы довести до ума, ибо в текущем состоянии приложение еще не готово. К тому же нельзя не отметить что абстракция от вендора фактически являет собой многочисленные профили сборок каждый из которых ориентирован под конкретного вендора, поэтому полная абстракция в рамках вендора весьма условная.

####Описание

Приложение разрабатывалось для просмотра отношений родитель - ребенок на глубине 1 уровня (что следует из контекста поставленной задачи). Для более высоких уровней необходим принципиально другой интерфейс пользователя.
Приложение не отправляет лишних запросов на сервер (кроме ситуации когда COUNT(id) в таблице совпадает с настройками отображения), все данные кэшируются. Общение с сервером происходит по событиям :
- выполняется поиск (клик по кнопке "Искать"). Все закешированные данные удаляются, происходит новый поиск по установленным критериям, учитывается ограничение по количеству выводимых результатов;
- выполняется загрузка фото (при клике на строке таблицы).
- выполняется подзагрузка данных (при переходе на следующую "новую" страницу, при изменении количества отображаемых строк в большую сторону). Подзагрузка данных выполняет наполнение текущего отображения данных недостающими.
Верстка адаптировалась под разрешение 1280х800
Необходимая версия JRE 8

####Сборка приложения
```Осуществляется через maven :
mvn clean install
```
####Настройка приложения
```
PostgreSQL
1). Создать базу bpc на сервере;
2). Выполнить скрипт ddl.sql
3). Выполнить скрипт migration.sql предварительно актуализировав путь к файлу photo.png
(Фотография добавится лишь к одному пользователю)

```
####Запуск приложения
```Выполняется с помощью файла запуска bpc-test-run.sh, в котором необходимо указать слудующие настройки :
spring.datasource.url - URL подключения к СУБД (на текущий момент есть только поддержка PostgreSQL 9.5)
spring.datasource.username - имя пользователя
spring.datasource.passwor - пароль

Актуализировать путь до файла bpc-test-1.0.jar
При необходимотси выполнить chmod 774 bpc-test-run.sh для установки прав на запуск скрипта
Запущенное приложение доступно по адресу http://localhost:8080
```

####Из нереализованного :

- Улучшение выдачи результатов SQL
- Абстрагироваться от вендора БД / JPA реализации / Сервлет контейнера
- Написание миграции больших данных и тестироване на них
- Добавить SQLite в качестве БД по умолчанию
- Настроить пул коннекшенов к БД
- Подкрутить логгирование
- Улучшения верстки
- Инструкция по установке / настройке

Я планирую выполнять эти пункты по мере возможности в ближайшее время и обновлять код приложения на гитхабе.


####Известные проблемы :

1). Есть сложности с отображением детей в интерфейсе при частичной подгрузке данных, так как они резольвятся динамически на сервере либо на клиенте, то могут возникнуть ситуации когда их нельзя отобразить, так как не удается зарезольвить родственную связь. Решать нужно комплексно;
2). Не реализован поиск по имени детей;
3). В некоторых ситуациях при применении фильтрации в интерфейсе можно увидеть [Присутствует] в колонаках Отец / Мать. Это связано с тем что была реализована упрощенная модель json объектов в интерфейсе (они имеют только id, а имена резольвятся в процессе). Решается переходом на другую модель.
4). При клике по кнопке следующей страницы в UI (с еще не загруженными данными) данные подгружаются с сервера, кэшируются, но не происходит фактического перехода на следующую страницу (нужно еще раз кликнуть по кнопке следующей страницы для того чтобы данные отобразились из кэша). Баг связан с тем, что при работе с данными отдаваемыми st-table используется динамическая заглушка для отображения кнопки следующей страницы. В момент перед очередной загрузкой данных она временно удаляется и это временное удаление способствует событию отрисовки таблицы по сокращенному массиву данных (без заглушки), из-за этой отрисовки и происходит ситуация описанная выше. Предполагаемый фикс - прямое управление состоянием st-table в данной ситуации.
5). При выполнении поиска находясь на странице != 1 не происходит редирект на первую страницу. Баг связан, опять же, с динамической заглушкой.
6). При переключении между страницами не сбрасывается выделение выбранной строки.

Если вы обнаружите баги / либо будут предложения по улучшению, просьба дать обратную связь.

sergey_ustinov@bk.ru