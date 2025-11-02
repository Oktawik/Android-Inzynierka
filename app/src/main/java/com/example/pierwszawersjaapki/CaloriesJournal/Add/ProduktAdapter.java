package com.example.pierwszawersjaapki.CaloriesJournal.Add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;
import java.util.Locale;

public class ProduktAdapter extends RecyclerView.Adapter<ProduktViewHolder> {
    Context context;
    List<ProduktItem> produktyLista;
    onBtnProduktListener listener;
    onProduktClickListener produktClickListener;

    public ProduktAdapter(Context context, List<ProduktItem> produktyLista, onBtnProduktListener listener, onProduktClickListener produktClickListener) {
        this.context = context;
        this.produktyLista = produktyLista;
        this.listener = listener;
        this.produktClickListener = produktClickListener;
    }

    @NonNull
    @Override
    public ProduktViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProduktViewHolder(LayoutInflater.from(context).inflate(R.layout.dziennik_dodaj_produkt, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProduktViewHolder holder, int position) {
        ProduktItem item = produktyLista.get(position);

        holder.tv_produkt_nazwa.setText(produktyLista.get(position).getNazwa());
        holder.tv_produkt_porcja.setText(String.format(Locale.US, "%d g", item.getJedna_porcja()));
        holder.tv_produkt_kalorie.setText(String.format(Locale.US, "%d kcal", item.getIlosc_kalorii()));
        holder.tv_bialko.setText(String.format(Locale.US, "%d g", item.getBialko()));
        holder.tv_weglowodany.setText(String.format(Locale.US, "%d g", item.getWeglowodany()));
        holder.tv_tluszcze.setText(String.format(Locale.US, "%d g", item.getTluszcze()));

        holder.btn_produkt_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) {
                    listener.onProduktAdded(item);
                }
            }
        });

        holder.ll_produkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produktClickListener!=null) {
                    produktClickListener.onProduktClicked(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return produktyLista.size();
    }

    public interface onBtnProduktListener {
        void onProduktAdded(ProduktItem produkt);
    }

    public interface onProduktClickListener {
        void onProduktClicked(ProduktItem produkt);
    }
}

class ProduktViewHolder extends RecyclerView.ViewHolder {

    LinearLayout ll_produkt;
    TextView tv_produkt_nazwa;
    ImageButton btn_produkt_added;
    TextView tv_produkt_porcja;
    TextView tv_produkt_kalorie;
    TextView tv_bialko;
    TextView tv_weglowodany;
    TextView tv_tluszcze;

    public ProduktViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_produkt_nazwa = itemView.findViewById(R.id.tv_produkt_nazwa);
        btn_produkt_added = itemView.findViewById(R.id.btn_produkt_added);
        tv_produkt_porcja = itemView.findViewById(R.id.tv_produkt_porcja);
        tv_produkt_kalorie = itemView.findViewById(R.id.tv_produkt_kalorie);
        tv_bialko = itemView.findViewById(R.id.tv_bialko);
        tv_weglowodany = itemView.findViewById(R.id.tv_weglowodany);
        tv_tluszcze = itemView.findViewById(R.id.tv_tluszcze);
        ll_produkt = itemView.findViewById(R.id.ll_produkt);
    }
}
