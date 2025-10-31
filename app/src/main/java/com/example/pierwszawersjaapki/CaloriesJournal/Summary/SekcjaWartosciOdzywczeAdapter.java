package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;

public class SekcjaWartosciOdzywczeAdapter extends RecyclerView.Adapter<SekcjaWartosciOdzywczeViewHolder>{
    Context context;
    private List<SekcjaWartosciOdzywcze> sekcje;

    public SekcjaWartosciOdzywczeAdapter(Context context, List<SekcjaWartosciOdzywcze> sekcje) {
        this.context = context;
        this.sekcje = sekcje;
    }

    @NonNull
    @Override
    public SekcjaWartosciOdzywczeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SekcjaWartosciOdzywczeViewHolder(LayoutInflater.from(context).inflate(R.layout.dziennik_podsumowanie_wartosci_odzywcze, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SekcjaWartosciOdzywczeViewHolder holder, int position) {
        holder.tv_wartosc_odzywcza_rodzaj.setText(sekcje.get(position).getNaglowek());

        holder.rv_sekcja_wartosci_odzywcze.setLayoutManager(new LinearLayoutManager(context));

        WartoscOdzywczaAdapter innerAdapter = new WartoscOdzywczaAdapter(context, sekcje.get(position).getListaSkladnikow());
        holder.rv_sekcja_wartosci_odzywcze.setAdapter(innerAdapter);

        holder.rv_sekcja_wartosci_odzywcze.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return sekcje.size();
    }
}

class SekcjaWartosciOdzywczeViewHolder extends RecyclerView.ViewHolder {

    TextView tv_wartosc_odzywcza_rodzaj;
    RecyclerView rv_sekcja_wartosci_odzywcze;

    public SekcjaWartosciOdzywczeViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_wartosc_odzywcza_rodzaj = itemView.findViewById(R.id.tv_wartosc_odzywcza_rodzaj);
        rv_sekcja_wartosci_odzywcze = itemView.findViewById(R.id.rv_sekcja_wartosci_odzywcze);
    }
}
