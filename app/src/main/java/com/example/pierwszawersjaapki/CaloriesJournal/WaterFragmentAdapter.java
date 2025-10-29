package com.example.pierwszawersjaapki.CaloriesJournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;
import com.example.pierwszawersjaapki.wiecejfragment.WiecejFragmentAdapter;

import java.util.List;

public class WaterFragmentAdapter extends RecyclerView.Adapter<WaterFragmentViewHolder>{

    Context context;
    List<WaterCup> waterCups;
    OnWaterChangedListener listener;

    public WaterFragmentAdapter(Context context, List<WaterCup> waterCups, OnWaterChangedListener listener) {
        this.context = context;
        this.waterCups = waterCups;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WaterFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WaterFragmentViewHolder(LayoutInflater.from(context).inflate(R.layout.cup_of_water, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WaterFragmentViewHolder holder, int position) {
        holder.iv_cup_of_water.setImageResource(waterCups.get(position).getImage());

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition!=RecyclerView.NO_POSITION) {
                WaterCup clickedCup = waterCups.get(currentPosition);

                if (!clickedCup.isFUll()) {
                    clickedCup.setFUll(true);
                    clickedCup.setImage(R.drawable.glass_full);
                    if (listener!=null) listener.onWaterChanged(+200);
                } else {
                    clickedCup.setFUll(false);
                    clickedCup.setImage(R.drawable.glass_empty);
                    if (listener!=null) listener.onWaterChanged(-200);
                }

                // odśwież tylko ten element
                notifyItemChanged(currentPosition);

            }
        });
    }

    @Override
    public int getItemCount() {
        return waterCups.size();
    }

    public interface OnWaterChangedListener {
        void onWaterChanged(int change);
    }
}

class WaterFragmentViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv_cup_of_water;

    public WaterFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_cup_of_water = itemView.findViewById(R.id.iv_cup_of_water);
    }
}