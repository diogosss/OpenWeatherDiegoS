# OpenWeatherDiegoS
# Aplicacíon 
## Diseño 
* Para la realización del diseño del **layout** se tomó como base los colores propios que usa la página openweather.com **tomate y gris**
* No es necesario abrir otra actividad para escoger la ciudad
* Al tener solo 10 ciudades se optó por implementar un **scrollChoice** desde el cual el usuario podrá escoger la ciudad que desee ver.
* Al escoger una ciudad automáticamente se actualizan los campos de temperatura, humedad, fecha, icono
* En los datos del JSON se encuentra la información del icono, por lo que se usrán los mismo iconos que la pagina openweather.com facilita.

Diseño
---------------
<br> 
 <img height=700 src=https://raw.githubusercontent.com/diogosss/OpenWeatherDiegoS/master/imagenesGithub/Screenshot_20200919-075718%5B1%5D.png />
</br>

Detalles de la programación
---------------
* ## Realizado
  * Obtener los datos JSON de http://api.openweathermap.org/data/2.5/weather?q=CIUDAD&units=metric&APPID=ecded2f66715cb8891165ccaf0945421
    * datos recuperados: lat, lon, temp, icon
  * Obtener los datos históricos JSON de http://api.openweathermap.org/data/2.5/onecall/timemachine?lat=-0.23&lon=-78.52&dt=1599941567&appid=ecded2f66715cb8891165ccaf0945421
    * relizar 5 llamadas para recibir 5 JSON con datos de cada día consultado (necesario según openWeather)
    * datos recuperados: temperatura, icon
  * Obtener los datos históricos JSON de https://api.openweathermap.org/data/2.5/onecall?lat=-1.47&lon=-77.98&units=metric&exclude=hourly,current,minutely&appid=ecded2f66715cb8891165ccaf0945421
    * Necesario ara obtener los valores de ls predicciones de los próximos 7 dias a partir de la fecha actual
    * datos recuperados: temperatura, icon
  * Mostrar un scroll de opciones para escoger la ciudad de 10 posibles, cada opción automáticamente actualiza los campos del Layout principal 
  * Mostrar la ciudad, latitud, longitud, humedad y temperatura de la página OpenWeather 
  * Mostrar un RecyclerView horizontal donde se colocarán los valores de temperatura y fecha además del Icono correspondiente
* ## Por hacer
  * Los datos obtenidos se encuentran en la Asynck Task pero no se envian al MainActivity para ser mostrados
  * Layout cuando la orientación del teléfono cambia
  * Con los valores de **icon** de cada fecha cambiar el icono del Layout 
  * Actualizar la actividad en caso de que no exista conexión a la Internet y el usuario se conecte
  
  
Instrucciones para correr el programa
---------------

* Para la instalación de la aplicación solo se necesita dar permisos a Android para instalar un paquete .apk de origen desconocido.
* Para el funcionamiento hay que dar click en el icono de OpenWeather, donde aparecerá una actividad Splash con una caratula, 
donde posteriormente se podrá observar la actividad principal.
* En la actividad Principal, se mostrará el **ScrollChoice** para escoger la ciudad. Automaticamente se actualizaran los campos donde mostrará la ciudad, latitud, longitu, fecha, temperatura, 
humedad.


* Es necesario los permisos de Internet en la aplicación, por lo que en **AndroidManifest.xml** debe estar:
  * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
  * `<uses-permission android:name="android.permission.INTERNET"/>`
  
*Para el uso del **ScrollChoice** es necesario la siguiente libreria en build.gradle
    * compile 'com.webianks.library:scroll-choice:1.0.1'
*Para la vista del **RecyclerView** con los datos pasados y predicciones del tiempo, es necesario la siguiente libreria en build.gradle
    * `compile 'androidx.cardview:cardview:1.0.0'`
    * `compile 'androidx.recyclerview:recyclerview:1.0.0'`
    * `compile 'com.github.bumptech.glide:glide:4.4.0'`
    * `compile 'com.github.bumptech.glide:compiler:4.4.0'`
