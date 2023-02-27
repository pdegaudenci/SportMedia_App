# SportMediaApp [Java + Android]

Prototipo de red social que permite conectar a personas que deseen realizar una actividad deportiva grupal en un lugar cercano a su ubicación.
La arquitectura de los componentes de la aplicacion es la siguiente:

* 3 Actividades : HomeActivity,LoginActivity y RegisterActivity. 
* 11 fragments que conforman las principales vistas de la aplicación. Los fragment están contenidos dentro de la Actividad principal HomeActivity y, 
a través del adminitrador de fragmentos se producen las transiciones
* Clases auxiliares : Clases entidades del modelo de Datos, clase adapter, controladores y clase que implementa patron DAO.
* Ficheros de diseño xml con la declaracion de las UI de las vistas y ficheros de recursos.
* Servicios externos : Uso de firebase Realtime database para la persistencia de los datos
* API de google: Maps y Places.

## Views de la App

![Screenshot_20230226-215846_SportsMedia](https://user-images.githubusercontent.com/73450522/221664119-352aa058-2e90-4a37-8959-df47515af5f8.jpg)![Screenshot_20230226-201055_SportsMedia](https://user-images.githubusercontent.com/73450522/221664235-3131e298-5baa-4153-b811-a5a2e35f4d20.jpg)


### Pre-requisitos 📋

_La aplicación fue desarrollada para plataforma Android segund los siguientes requisitos no funcionales:_

```
Version de android: 12
Compilado con Nivel de API 32
Nivel de API mínimo: 26
IDE utilizado : Android Studio
```

## Construido con 🛠️

_Se utilizaron las siguientes herramientas _

* Java
* [Android] Librerias para el diseño y definición de las Interfaces Gráficas de Usuario en formato xml.
* [Material Design] Lenguaje de diseño de google
* [APIs de Google Cloud] Maps (visualización) y Places (Autocompletado de direcciones y obtención de datos geograficos) . Google Services
* [Firebase] BBDD NoSQL basado en colecciones con documentos en formato JSON.
* [Recycler view + cardview] Visualizacion optimizada de elementos en vistas de tipo lista.
* [Maven] Gestor de dependencias

