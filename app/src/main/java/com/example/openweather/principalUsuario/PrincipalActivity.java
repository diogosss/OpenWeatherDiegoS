package com.example.openweather.principalUsuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openweather.R;
import com.example.openweather.adaptadores.AdaptadorRecyclerView;
import com.example.openweather.asyncktasks.ObtenerDatos;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * APP DE OPEN WEATHER CREADO POR DIEGO SALAZAR 19/9/2020
 */

public class PrincipalActivity extends AppCompatActivity  {

   // ObtenerDatos asyncTask =new ObtenerDatos();
    //Tag para verificar procesos del main Activity
    private static final String TAG ="ActividadPrincipal";

    //variables para el recyclerView de datos historicos y prediciones
    private ArrayList<String> mFechas = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mTiempos = new ArrayList<>();
    HashMap<String, String> iagenesOpenweather = new HashMap<String, String>();

    //variables para mostrar las ciudades disponibles
    List<String > Ciudades = new ArrayList<>();
    ScrollChoice scrollChoice;

    //variables del layout
    public static TextView FechaActual, TempeActual, ciudadCargada, humedadActual;
    public ImageView IconoActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //inicializar parametros
        inicializar();
        CargarCiudades();

        //verificar conexion a internet
        if (checkInternetConnection()){

            getImages();
            initRecyclerView();

        scrollChoice.addItems(Ciudades, 5); //ciudad default número 5
        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name)
            {
                //Obtener datos de ciudad escogida
                ObtenerDatos proceso  = new ObtenerDatos();
                proceso.execute(name);
                IconoActual.setImageResource(R.drawable.cloud_tres);
            }
        });

        }else {
            Toast.makeText(getApplicationContext(),"No internet connection. Please try again later",Toast.LENGTH_SHORT).show();
        }
    }

    // FUNCIONES
    //ciudades disponibles solo 10
    private void CargarCiudades() {
        Ciudades.add("Manta");
        Ciudades.add("Guayaquil");
        Ciudades.add("Machala");
        Ciudades.add("Ibarra");
        Ciudades.add("Quito");
        Ciudades.add("Cuenca");
        Ciudades.add("Loja");
        Ciudades.add("Tena");
        Ciudades.add("Puyo");
        Ciudades.add("Macas");
    }

    //inicializar parametros
    private void inicializar() {

        scrollChoice = (ScrollChoice)findViewById(R.id.ciudades_opcion);
        iniicalizarArrayImagenes();
        FechaActual = (TextView)findViewById(R.id.fecha_actual);
        TempeActual = (TextView)findViewById(R.id.tempe_actual);
        IconoActual = (ImageView)findViewById(R.id.icono_clima_actual);
        ciudadCargada  =(TextView)findViewById(R.id.ciudadData);
        humedadActual = (TextView)findViewById(R.id.humed_actual);
    }

    //inicializar iconos disponibles en la red
    private void iniicalizarArrayImagenes()
    {

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
    }

    //inicializar Recycler view de datos historicos y predicciones
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_Datos_Hist_Predict);
        recyclerView.setLayoutManager(layoutManager);
        AdaptadorRecyclerView adapter = new AdaptadorRecyclerView(this, mFechas, mImageUrls , mTiempos);
        recyclerView.setAdapter(adapter);
    }

    //funcion para verificar la conexion a red
    private boolean checkInternetConnection()
    {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * MODIFICAR PARA ALMACENAR LOS DATOS OBTENIDOS EN EL ASYNCTASK
     * Y MOSTRAR LOS VALORES DE DIAS PASADOS Y FUTUROS
     *
     * ---------------POR HACER -------------------------------
     *
     */

    //funcion dummy para mostrar los arrays de valores historicos y predicciones
    // hasta solucionar los problemas de lectura de datos desde el asyncktask
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add(cloudy);
        mFechas.add("sept 14");
        mTiempos.add("18°");

        mImageUrls.add(storm);
        mFechas.add("sept 15");
        mTiempos.add("244");

        mImageUrls.add(storm);
        mFechas.add("sept 16");
        mTiempos.add("22°");

        mImageUrls.add(rainy);
        mFechas.add("sept 17");
        mTiempos.add("23°");


        mImageUrls.add(rainy);
        mFechas.add("sept 18");
        mTiempos.add("21°");

        mImageUrls.add(storm);
        mFechas.add("sept 20");
        mTiempos.add("24°");


        mImageUrls.add(sunny);
        mFechas.add("sept 21");
        mTiempos.add("23°");

        mImageUrls.add(rainy);
        mFechas.add("sept 22");
        mTiempos.add("25°");

        mImageUrls.add(rainy);
        mFechas.add("sept 23");
        mTiempos.add("18°");

        mImageUrls.add(cloudy);
        mFechas.add("sept 24");
        mTiempos.add("20°");

        mImageUrls.add(cloudy);
        mFechas.add("sept 25");
        mTiempos.add("25°");

        mImageUrls.add(rainy);
        mFechas.add("sept 26");
        mTiempos.add("26°");

        initRecyclerView();

    }

    String rainy ="https://img.icons8.com/fluent/2x/intense-rain.png";
    String cloudy =   "https://img.icons8.com/fluent/2x/partly-cloudy-rain.png";
    String sunny ="https://img.icons8.com/fluent/2x/summer.png";
    String storm =   "https://img.icons8.com/fluent/2x/storm.png";



}