package com.example.pierwszawersjaapki.CaloriesJournal;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;

public class MacroAdapter extends RecyclerView.Adapter<MacroViewHolder>{
    Context context;
    List<MacroItem> macroItemList;

    public MacroAdapter(Context context, List<MacroItem> macroItemList) {
        this.context = context;
        this.macroItemList = macroItemList;
    }

    @NonNull
    @Override
    public MacroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MacroViewHolder(LayoutInflater.from(context).inflate(R.layout.item_macros, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MacroViewHolder holder, int position) {
        MacroItem item = macroItemList.get(position);
        holder.tv_macro_name.setText(macroItemList.get(position).getName());
        holder.pb_macro.setProgress(macroItemList.get(position).getProgress());
        holder.tv_macro_eaten.setText(String.valueOf(macroItemList.get(position).getCaloriesEaten()));
        holder.tv_macro_needed.setText(" / "+String.valueOf(macroItemList.get(position).getCaloriesGoal())+" g");

        int actualColor = ContextCompat.getColor(context, item.getColor());
        holder.pb_macro.getProgressDrawable().setColorFilter(actualColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount() {
        return macroItemList.size();
    }
}

class MacroViewHolder extends RecyclerView.ViewHolder {

    TextView tv_macro_name;
    ProgressBar pb_macro;
    TextView tv_macro_eaten;
    TextView tv_macro_needed;

    public MacroViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_macro_name = itemView.findViewById(R.id.tv_macro_name);
        pb_macro = itemView.findViewById(R.id.pb_macro);
        tv_macro_eaten = itemView.findViewById(R.id.tv_macro_eaten);
        tv_macro_needed = itemView.findViewById(R.id.tv_macro_needed);
    }
}