package com.example.pierwszawersjaapki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.Models.Equipment;
import com.example.pierwszawersjaapki.Models.Ingredient;
import com.example.pierwszawersjaapki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsEquipmentsAdapter extends RecyclerView.Adapter<InstructionsEquipmentsViewHolder> {

    Context context;
    List<Equipment> list;

    public InstructionsEquipmentsAdapter(Context context, List<Equipment> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public InstructionsEquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsEquipmentsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsEquipmentsViewHolder holder, int position) {
        String imageName = list.get(position).image;

        // Jeśli nazwa NIE ma rozszerzenia, dodaj .jpg
        if (!imageName.contains(".")) {
            imageName += ".jpg";
        }

        final String finalImageName = imageName; // Finalna kopia do użycia w callbacku
        String imageUrl = "https://spoonacular.com/cdn/equipment_100x100/" + imageName;

        // Logowanie do debugowania
        android.util.Log.d("ImageURL", "Loading: " + imageUrl);

        holder.tv_instructions_step_item.setText(list.get(position).name);
        holder.tv_instructions_step_item.setSelected(true);

        Picasso.get()
                .load(imageUrl)
                .into(holder.iv_instructions_step_items, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        android.util.Log.d("Picasso", "Success: " + finalImageName);
                    }

                    @Override
                    public void onError(Exception e) {
                        android.util.Log.e("Picasso", "Error loading: " + finalImageName + " - " + e.getMessage());

                        // Spróbuj z .png jeśli .jpg nie zadziałało
                        String pngUrl = imageUrl.replace(".jpg", ".png");
                        android.util.Log.d("ImageURL", "Trying PNG: " + pngUrl);
                        Picasso.get().load(pngUrl).into(holder.iv_instructions_step_items);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionsEquipmentsViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_instructions_step_items;
    TextView tv_instructions_step_item;

    public InstructionsEquipmentsViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_instructions_step_items = itemView.findViewById(R.id.iv_instructions_step_items);
        tv_instructions_step_item = itemView.findViewById(R.id.tv_instructions_step_item);
    }
}
