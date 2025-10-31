package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;

public class WartoscOdzywczaAdapter extends RecyclerView.Adapter<WartoscOdzywczaViewHolder>{
    Context context;
    List<WartoscOdzywczaItem> list;

    public WartoscOdzywczaAdapter(Context context, List<WartoscOdzywczaItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WartoscOdzywczaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WartoscOdzywczaViewHolder(LayoutInflater.from(context).inflate(R.layout.dziennik_podsumowanie_wartosci_odzywcze_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WartoscOdzywczaViewHolder holder, int position) {
        holder.tv_wartosc_odzywcza_nazwa.setText(list.get(position).getNazwa());
        holder.tv_wartosc_odzywcza_jednostka.setText(list.get(position).getJednostka());
        holder.tv_wartosc_odzywcza_ilosc.setText(String.valueOf(list.get(position).getIlosc()));
        holder.tv_wartosc_odzywcza_cel.setText(String.valueOf(list.get(position).getCel()));
        holder.pb_wartosc_odzywcza_progress.setProgress(list.get(position).getProgres());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class WartoscOdzywczaViewHolder extends RecyclerView.ViewHolder {

    TextView tv_wartosc_odzywcza_nazwa;
    TextView tv_wartosc_odzywcza_jednostka;
    TextView tv_wartosc_odzywcza_ilosc;
    TextView tv_wartosc_odzywcza_cel;
    ProgressBar pb_wartosc_odzywcza_progress;

    public WartoscOdzywczaViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_wartosc_odzywcza_nazwa = itemView.findViewById(R.id.tv_wartosc_odzywcza_nazwa);
        tv_wartosc_odzywcza_jednostka = itemView.findViewById(R.id.tv_wartosc_odzywcza_jednostka);
        tv_wartosc_odzywcza_ilosc = itemView.findViewById(R.id.tv_wartosc_odzywcza_ilosc);
        tv_wartosc_odzywcza_cel = itemView.findViewById(R.id.tv_wartosc_odzywcza_cel);
        pb_wartosc_odzywcza_progress = itemView.findViewById(R.id.pb_wartosc_odzywcza_progress);
    }
}