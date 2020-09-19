package com.example.openweather.asyncktasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.openweather.principalUsuario.PrincipalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ObtenerDatos extends AsyncTask<String, Void, Void>
{


    //variables globales
    String ciudadp = "";
    private int diasPasadoscont;

    private ArrayList<String> diasMesesScroll = new ArrayList<>();
    private ArrayList<String> iconosScroll = new ArrayList<>();
    private ArrayList<String> imagenesScroll = new ArrayList<>();
    private ArrayList<String> temperaturaScroll = new ArrayList<>();

    private ArrayList<String> JsonFuturoData = new ArrayList<>();

    String ciudadHoy = "",fechaHoy="",iconoHoy="",humedadHoy="",temperaturaHoy="",longi="",lati="", FechaPasar="";

    @Override
    protected Void doInBackground(String... params)
    {
        ciudadp=params[0];

        Log.i("Ciudad: ",ciudadp);

        try {
            datosdeldiaActual();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * Lectura de datos de la pagina openweatehr con valores de la fecha actual
     * @throws IOException
     * @throws JSONException
     */
    private void datosdeldiaActual() throws IOException, JSONException {
        String dataActual= "";

        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+ciudadp+"&units=metric&APPID=ecded2f66715cb8891165ccaf0945421");
        HttpURLConnection httURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httURLConnection.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        while(line != null)
        {
            line = bufferReader.readLine();
            dataActual = dataActual + line;
        }

        Log.i("datosrecibidos",dataActual);

        //sacar el array Json de weather
        JSONObject ja = new JSONObject(dataActual);
        String weaherData = ja.getString("weather");
        Log.i("jasonObject",weaherData);
        JSONArray array = new JSONArray(weaherData);

        for(int i =0; i<array.length();i++)
        {
            JSONObject weatherPart = array.getJSONObject(i);
            iconoHoy=weatherPart.getString("icon");
        }

        Log.i("icono: ",iconoHoy);

        //objeto nested
        String mainTemperature = ja.getString("main");
        JSONObject mainPart = new JSONObject(mainTemperature);
        temperaturaHoy = mainPart.getString("temp");
        humedadHoy = mainPart.getString("humidity");
        Log.i("temperatura: ",temperaturaHoy);
        Log.i("humedad: ",humedadHoy);

        String fechaUnix = ja.getString("dt");
        fechaHoy = String.valueOf((Long.parseLong(fechaUnix)*1000L -(5*1000 * 60 * 60 ))/1000L);
        Log.i("fechaUnix: ",fechaUnix);
        FechaPasar = sumarRestarDias(fechaHoy,0);

        String ciudadCrudo = ja.getString("name");
        ciudadHoy = ciudadCrudo;
        Log.i("ciudad consultada: ",ciudadHoy);

        String coord = ja.getString("coord");
        JSONObject coordPart = new JSONObject(coord);
        longi = coordPart.getString("lon");
        lati = coordPart.getString("lat");
        Log.i("latitud: ",longi);
        Log.i("longitud: ",lati);

        Log.i("----------------: ",lati);
        diasPasadoscont=-5;
        String diaspasados="";
        String fechBuscar="";
        for(int i=0;i<5;i++)
        {
            diaspasados = String.valueOf(sumarRestarDias(fechaHoy,diasPasadoscont));
            diasMesesScroll.add(diaspasados);
            fechBuscar = String.valueOf((Long.parseLong(fechaHoy)*1000L + diasPasadoscont*(1000 * 60 * 60 * 24))/1000L);
            Log.i("fechaBuscar***",fechBuscar);
            iconosScroll.add(diasHistoricos(longi,lati,fechBuscar).get(0)); //iconos
            temperaturaScroll.add(diasHistoricos(longi,lati,fechBuscar).get(1)); //temperatura
            diasPasadoscont+=1;
        }


        JsonFuturoData = (diasPosteriores(longi,lati));

        for(int i = 2; i<=20 ; i+=3)
        {
            imagenesScroll.add(getImages(JsonFuturoData.get(i)));
        }

        for(int i = 1; i<=19 ; i+=3)
        {
            temperaturaScroll.add(JsonFuturoData.get(i));
        }


        for(int i = 0; i<=18 ; i+=3)
        {
            diasMesesScroll.add(sumarRestarDias(JsonFuturoData.get(i),0));
        }

        Log.i("fin","dsPst");

    }

    /**
     * Leer valores de los dias posteriores al actual,
     * @param longiP longitud de la ciudad requerida
     * @param latiP  latitud de la ciudad requerida
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private ArrayList<String> diasPosteriores(String longiP, String latiP) throws IOException, JSONException {
        String dataFuturo = "";
        ArrayList<String> arFuturo = new ArrayList<String>();
        URL urlforecast = new URL("https://api.openweathermap.org/data/2.5/onecall?lat="+latiP+"&lon="+longiP+"&units=metric&exclude=hourly,current,minutely&appid=ecded2f66715cb8891165ccaf0945421");
        HttpURLConnection httURLConnection = (HttpURLConnection) urlforecast.openConnection();

        InputStream inputStream = httURLConnection.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        while(line != null)
        {
            line = bufferReader.readLine();
            dataFuturo = dataFuturo + line;
        }

        //sacar el array Json de weather
        JSONObject jaF = new JSONObject(dataFuturo);
        String futuroData = jaF.getString("daily");
        Log.i("jasonObject",futuroData);
        JSONArray arrayF = new JSONArray(futuroData);

        for(int i = 1 ; i < arrayF.length(); i++) {
            String dia = arrayF.getJSONObject(i).getString("dt");
            Log.i("nested dia",dia);

            String objetoTemp = arrayF.getJSONObject(i).getString("temp");   //para sacar temperatura
            JSONObject partTempo = new JSONObject(objetoTemp);
            String diatemp = partTempo.getString("day");
            Log.i("nested temp",diatemp);

            String arrayWeather = arrayF.getJSONObject(i).getString("weather"); //para sacar icono
            JSONArray arraywF = new JSONArray(arrayWeather);
            String iconoFW ="";
            for(int j = 0 ; j < arraywF.length(); j++)
            {
                JSONObject weatherPart = arraywF.getJSONObject(j);
                iconoFW=weatherPart.getString("icon");
            }
            Log.i("nested icon",iconoFW);

            arFuturo.add(dia);
            arFuturo.add(diatemp);
            arFuturo.add(iconoFW);
        }

        return arFuturo;

    }

    /**
     * Para leer los datos historicos se necesita enviar cada fecha para cada llamado en este caso
     * se realizan 5 llamados desde la fecha actual a las fechas requeridas
     * @param longiA  longitud de la ciudad requerida
     * @param latiA     latitud de la ciudad requerida
     * @param fechaHy   fecha previamente calculada
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private ArrayList<String> diasHistoricos(String longiA, String latiA, String fechaHy) throws IOException, JSONException {
        String dataHistorico = "";
        ArrayList<String> ar = new ArrayList<String>();
        String iconoPid = "", tiempoPast = "";
        URL urlhistorical = new URL("http://api.openweathermap.org/data/2.5/onecall/timemachine?lat="+longiA+"&lon="+latiA+"&dt="+fechaHy+"&units=metric&appid=ecded2f66715cb8891165ccaf0945421");

        HttpURLConnection httURLConnection = (HttpURLConnection) urlhistorical.openConnection();

        InputStream inputStream = httURLConnection.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        while(line != null)
        {
            line = bufferReader.readLine();
            dataHistorico = dataHistorico + line;
        }

        Log.i("datos_historicos1dia",dataHistorico);

        //sacar el array Json de weathe
        //sacar el valor de temperatura main en objeto nested
        JSONObject ja = new JSONObject(dataHistorico);
        String mainTemperature = ja.getString("current");
        JSONObject mainPart = new JSONObject(mainTemperature);
        tiempoPast = mainPart.getString("temp");
        Log.i("tiempo",tiempoPast);


        String weatherData = mainPart.getString("weather");
        Log.i("jasonObject",weatherData);
        JSONArray array = new JSONArray(weatherData);

        for(int i =0; i<array.length();i++)
        {
            JSONObject weatherPart = array.getJSONObject(i);
            iconoPid=weatherPart.getString("icon");
        }
        Log.i("icono",iconoPid);

        ar.add(iconoPid);
        ar.add(tiempoPast);

        return ar ;

    }

    /**
     *  CAlculo de la fecha a partir de la fecha Timestamp UNIX que entraga la pgina web openweather
     *
     * @param fechaUNIX   fecha UNIX para ser procesada
     * @param dia          dias que se aumentaran o restaran de la fecha entregada
     * @return
     */
    private static String sumarRestarDias(String fechaUNIX, int dia) {


        String resultado="";
        String MESString = "";
        //sumar o restar dias
        Date fechasumada = new Date(Long.parseLong(fechaUNIX)*1000L + dia*(1000 * 60 * 60 * 24) );
        //Log.i("Fechasumada", String.valueOf(fechasumada));

        //parte para transformar UNIX a mes dia
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(fechasumada);
        Log.i("Fechasumada", formattedDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechasumada);
        int mes = calendar.get(Calendar.MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);
        MESString = new DateFormatSymbols().getMonths()[mes];

        Log.i("mesSumado", MESString);
        Log.i("diaSumado", String.valueOf(dayOfWeek));

        resultado =  MESString+" "+dayOfWeek;

        return resultado;
    }

    /**
     * Set de imagenes con sus links web para pasar a la Main Principal para ser mostrada
     * en el scroll vire y en el icono de tiempo principal
     * @param consultImagen
     * @return
     */
    private String getImages(String consultImagen)
    {
        HashMap<String, String> iagenesOpenweather = new HashMap<String, String>();
        iagenesOpenweather.put("01d", "http://openweathermap.org/img/wn/01d@2x.png");
        iagenesOpenweather.put("01n", "http://openweathermap.org/img/wn/01n@2x.png");
        iagenesOpenweather.put("02d", "http://openweathermap.org/img/wn/02d@2x.png");
        iagenesOpenweather.put("02n", "http://openweathermap.org/img/wn/02n@2x.png");
        iagenesOpenweather.put("03d", "http://openweathermap.org/img/wn/03d@2x.png");
        iagenesOpenweather.put("03n", "http://openweathermap.org/img/wn/03n@2x.png");
        iagenesOpenweather.put("04d", "http://openweathermap.org/img/wn/04d@2x.png");
        iagenesOpenweather.put("04n", "http://openweathermap.org/img/wn/04n@2x.png");
        iagenesOpenweather.put("09d", "http://openweathermap.org/img/wn/09d@2x.png");
        iagenesOpenweather.put("09n", "http://openweathermap.org/img/wn/09n@2x.png");
        iagenesOpenweather.put("10d", "http://openweathermap.org/img/wn/10d@2x.png");
        iagenesOpenweather.put("10n", "http://openweathermap.org/img/wn/10n@2x.png");
        iagenesOpenweather.put("11d", "http://openweathermap.org/img/wn/11d@2x.png");
        iagenesOpenweather.put("11n", "http://openweathermap.org/img/wn/11n@2x.png");
        iagenesOpenweather.put("13d", "http://openweathermap.org/img/wn/13d@2x.png");
        iagenesOpenweather.put("13n", "http://openweathermap.org/img/wn/13n@2x.png");
        iagenesOpenweather.put("50d", "http://openweathermap.org/img/wn/50d@2x.png");
        iagenesOpenweather.put("50n", "http://openweathermap.org/img/wn/50n@2x.png");

        return iagenesOpenweather.get(consultImagen);
    }

    @Override
    protected void onPostExecute(Void avoid) {
        //delegate.processFinish(result);

        //  PrincipalActivity.FechaActual.setText(this.);
        PrincipalActivity.TempeActual.setText(this.temperaturaHoy + "°");
        PrincipalActivity.FechaActual.setText(this.FechaPasar);
        PrincipalActivity.ciudadCargada.setText(this.ciudadp + " EC " + "lat= " + lati + " Lon= " + longi);
        PrincipalActivity.humedadActual.setText("Humedad: " + this.humedadHoy + " % ");
    }
}
