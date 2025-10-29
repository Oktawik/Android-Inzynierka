package com.example.pierwszawersjaapki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;
import com.example.pierwszawersjaapki.Models.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionsIngredientsViewHolder> {

    Context context;
    List<Ingredient> list;

    public InstructionsIngredientsAdapter(Context context, List<Ingredient> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public InstructionsIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsIngredientsViewHolder holder, int position) {
        String imageName = list.get(position).image;
        String imageUrl = "https://spoonacular.com/cdn/ingredients_250x250/" + imageName;

        // Logowanie do debugowania
        android.util.Log.d("ImageURL", "Loading: " + imageUrl);

        holder.tv_instructions_step_item.setText(list.get(position).name);
        holder.tv_instructions_step_item.setSelected(true);

        Picasso.get()
                .load(imageUrl)
                .into(holder.iv_instructions_step_items, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        android.util.Log.d("Picasso", "Success: " + imageName);
                    }

                    @Override
                    public void onError(Exception e) {
                        android.util.Log.e("Picasso", "Error loading: " + imageName + " - " + e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionsIngredientsViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_instructions_step_items;
    TextView tv_instructions_step_item;

    public InstructionsIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_instructions_step_items = itemView.findViewById(R.id.iv_instructions_step_items);
        tv_instructions_step_item = itemView.findViewById(R.id.tv_instructions_step_item);
    }
}
