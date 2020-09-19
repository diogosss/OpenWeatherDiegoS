package com.example.openweather.adaptadores;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.openweather.R;

        import java.util.ArrayList;



public class AdaptadorRecyclerView extends RecyclerView.Adapter<AdaptadorRecyclerView.ViewHolder> {

    private static final String TAG ="RecyclerVireAdapter";
    //variables
    private ArrayList<String> mFechas = new ArrayList<>();
    private ArrayList<String> mImagenees = new ArrayList<>();
    private ArrayList<String> mTiempos = new ArrayList<>();
    private Context mContext;

    private final int[] backgroundColors = {
            R.color.list_color1,
            R.color.list_color2,
            R.color.list_color3,
            R.color.list_color4,
            R.color.list_color5,
            R.color.list_color6,
            R.color.list_color7 };

    public AdaptadorRecyclerView(Context context, ArrayList<String> fechas, ArrayList<String> imagenes, ArrayList<String> timepos)
    {
        mFechas = fechas;
        mImagenees = imagenes;
        mTiempos = timepos;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: llamado");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lista_climashistpredict, parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder: llamado");

        //descargar bitmap del URL dado
        Glide.with(mContext).asBitmap().load(mImagenees.get(position)).into(holder.climaID);


        holder.fecha.setText(mFechas.get(position));
        holder.tiempo.setText(mTiempos.get(position));

        int index = position % backgroundColors.length;
        int color = ContextCompat.getColor(mContext, backgroundColors[index]);
        holder.itemView.setBackgroundColor(color);

        holder.climaID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"onCLickListener:");
            }
        });

    }

    @Override
    public int getItemCount() {

        return mImagenees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView climaID;
        TextView fecha, tiempo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            climaID  = itemView.findViewById(R.id.imagen);
            fecha = itemView.findViewById(R.id.fecha);
            tiempo = itemView.findViewById(R.id.temp);

        }
    }
}
