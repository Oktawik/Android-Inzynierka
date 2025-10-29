package com.example.pierwszawersjaapki.wiecejfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.ChatbotFragment;
import com.example.pierwszawersjaapki.R;

import java.util.List;

public class WiecejFragmentAdapter extends RecyclerView.Adapter<WiecejFragmentViewHolder>{

    Context context;
    List<Item> items;
    OnItemClickListener listener;

    public WiecejFragmentAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WiecejFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WiecejFragmentViewHolder(LayoutInflater.from(context).inflate(R.layout.wiecej_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WiecejFragmentViewHolder holder, int position) {
        holder.nazwaView.setText(items.get(position).getName());
        holder.ikonaView.setImageResource(items.get(position).getImage());
        holder.opisView.setText(items.get(position).getOpis());

        Item currentItem = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(items.get(currentPosition));
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}
