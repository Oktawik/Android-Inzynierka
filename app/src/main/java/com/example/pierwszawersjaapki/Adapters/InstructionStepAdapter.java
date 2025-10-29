package com.example.pierwszawersjaapki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.Models.Step;
import com.example.pierwszawersjaapki.R;

import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewHolder> {

    Context context;
    List<Step> list;

    public InstructionStepAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewHolder holder, int position) {
        holder.tv_instructions_step_number.setText(String.valueOf(list.get(position).number));
        holder.tv_instructions_step_title.setText(list.get(position).step);

        holder.rv_instructions_ingredients.setHasFixedSize(true);
        holder.rv_instructions_ingredients.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsIngredientsAdapter instructionsIngredientsAdapter = new InstructionsIngredientsAdapter(context, list.get(position).ingredients);
        holder.rv_instructions_ingredients.setAdapter(instructionsIngredientsAdapter);

        holder.rv_instructions_equipments.setHasFixedSize(true);
        holder.rv_instructions_equipments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsEquipmentsAdapter instructionsEquipmentsAdapter = new InstructionsEquipmentsAdapter(context, list.get(position).equipment);
        holder.rv_instructions_equipments.setAdapter(instructionsEquipmentsAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionStepViewHolder extends RecyclerView.ViewHolder {

    TextView tv_instructions_step_number, tv_instructions_step_title;
    RecyclerView rv_instructions_equipments, rv_instructions_ingredients;

    public InstructionStepViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_instructions_step_number = itemView.findViewById(R.id.tv_instructions_step_number);
        tv_instructions_step_title = itemView.findViewById(R.id.tv_instructions_step_title);
        rv_instructions_equipments = itemView.findViewById(R.id.rv_instructions_equipments);
        rv_instructions_ingredients = itemView.findViewById(R.id.rv_instructions_ingredients);
    }
}
