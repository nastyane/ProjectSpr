# Features of modern Java
Бизнес-логика для системы управления проектами. Система позволяет группе разработчиков управлять разработкой программных проектов. В ней определены следующие объекты:
* Проект. У каждого проекта есть определенная команда разработчиков, тестировщиков и один менеджер. Также к проекту может быть привязан тимлидер. У проекта определены различные майлстоуны. К каждому проекту могут быть привязаны сообщения об ошибках.
* Майлстоун. Одна из итераций цикла разработки проекта. Привязан к определенным датам. К майлстоунам привязаны определенные тикеты (задания). Майлстоун имеет определенный статус: открыт, активен или закрыт. Майлстоун может быть закрыт только когда все его тикеты выполнены. В каждый момент времени у проекта может быть только один майлстоун.
* Тикет. Определенное задание для разработчиков. Может быть выдано определенной группе разработчиков. Привязан к определенному проекту и майлстоуну. Имеет статус: новый, принятый, в процессе выполнения, выполнен.
* Сообщение об ошибке. Отчет о найденной ошибке в проекте. Привязан к определенному проекту. Имеет статус: новый, исправленный, протестированный, закрытый.

В системе определены следующие роли для пользователей:
* менеджер;
* тимлидер;
* разработчик;
* тестировщик.
  Для каждого проекта у пользователя определена своя роль (если он участвует в разработке данного проекта).

У всех пользователей системы есть возможность:
* зарегистрироваться;
* просмотреть все проекты в которых они участвуют;
* посмотреть список заданий, который был им выдан;
* посмотреть список отчетов об ошибках, которые ему надо исправить;
* создать новый проект.

Функции менеджера проекта:
* Управление пользователями:
1. назначение тимлидера
2. добавление разработчика к проекту
3. добавление тестировщика к проекту

* Управление майлстоунами:
1. создание нового майлстоуна
2. изменение статуса майлстоуна

* Управление тикетами
1. создание нового тикета
2. привязка разработчика к тикету
3. проверка выполнения тикета

Функции тимлидера:
* Управление тикетами
1. создание нового тикета
2. привязка разработчика к тикету
3. проверка выполнения тикета

* Выполнение тикетов

Функции разработчика:
* Выполнение тикетов
* Создание сообщений об ошибках
* Исправление сообщений об ошибках

Функции тестировщика:
* Тестирование проекта
* Создание сообщений об ошибках
* Проверка исправления сообщений об ошибках# ProjectSpr
