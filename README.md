# ShoppingList

Приложение для  синхронного создания списков покупок и добавления/удаления товаров.
В приложении есть возможность создавать и редактировать списки одновременно на нескольких устройствах, данные будут обновляться мгновенно.
Приложение состоит из двех экранов.<br>
<br>**Экран Списки покупок**
<br>На экране отображаются созданные списки, также есть кнопка создания нового списка. При отсутсвии списков, будет заглушка. Отображение списков реализовано с помощью recyclerView. При пролистывании списков Титул страницы будет скрываться с анимацией. При длительном нажатии на любой из списков
 появится меню (BottomSheet) с деталями о списке и возможностью его удалить. Для защиты от случайного удаления есть диалог-предупреждение, также можно восстановить удаленный список в течение  5 секунд - при удалении появится snackbar с соответсвующей опцией.
 По клику на список мы перейдем на экран содержимого списка. Все данные о списках и их содержимом хранятся на сервере. Синхронное обновление всех списков происходит с помощью pooling запросов к серверу.<br>
<br>**Экран Содержимое списка**
<br> Экран аналогичен первому, за исключением некоторых изменений в дизайне(изменена кнопка добавления товара и то, как проиходит удаление товара). Список отображается с помощью LinearLayout, а не GridLayout. Меню удаления реализовано с помощью BottomSheet<br>


**Техническая часть**
+ Проект выполенен как single activity с фрагментами. Навигация с помощью Navigation component и SafeArgs.
+ Проект реализован на чистой архитектуре, использованым слои data, domain, presentation. Слои не связаны, модели в разных слоях используются разные.
+ В presentation слое проекта используется паттерн MVVM и LiveData.
+ DI реализован с помощью Koin.
+ Для получения данных с API используется Retrofit и Courutines.
+ На всех экранах реализована модель LCE(Loadin, Content, Error) с помощью sealed interfaces. Т.е. при загрузке данных мы видем прогресс бар, при удачном получени видим контент, при ошибке - экран ошибки.
+ Добавлен splashScreen.

## Используемый стек

+ Kotlin
+ Clean Architecture
+ Koin
+ Retrofit
+ SOLID
+ MVVM (ViewModel, LiveData)  
+ RecyclerView & DiffUtil  
+ ViewBinding  
+ Navigation Component & SafeArgs 
+ Glide
+ Coroutines
+ SplashScreen
  

## Gif flow приложения
<br>**Экран Списки покупок**
<img src="https://github.com/alexxk2/ShoppingList/blob/dev/app/src/main/res/drawable/lists_screen.gif" width="340" height="699" />  <br>
<br>**Экран Содержимое списка**
<img src="https://github.com/alexxk2/ShoppingList/blob/dev/app/src/main/res/drawable/product_screen.gif" width="340" height="699" />  <br>
<br>**Синхронное работа со списками**
<img src="https://github.com/alexxk2/ShoppingList/blob/dev/app/src/main/res/drawable/remote_creating.gif" />  <br>

