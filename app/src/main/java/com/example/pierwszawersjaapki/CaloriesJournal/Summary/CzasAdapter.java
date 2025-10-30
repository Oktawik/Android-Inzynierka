package com.example.pierwszawersjaapki.CaloriesJournal.Summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;

public class CzasAdapter extends RecyclerView.Adapter<CzasViewHolder> {

    Context context;
    List<CzasItem> list;
    private OnCzasClickListener listener;

    public CzasAdapter(Context context, List<CzasItem> list, OnCzasClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CzasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CzasViewHolder(LayoutInflater.from(context).inflate(R.layout.dziennik_podsumowanie_czas_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CzasViewHolder holder, int position) {
        holder.tv_czas_item.setText(list.get(position).getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null) {
                    CzasItem clickedItem = list.get(position);
                    listener.OnCzasClicked(clickedItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnCzasClickListener {
        void OnCzasClicked(CzasItem item);
    }
}

class CzasViewHolder extends RecyclerView.ViewHolder {

    TextView tv_czas_item;

    public CzasViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_czas_item = itemView.findViewById(R.id.tv_czas_item);
    }
}
