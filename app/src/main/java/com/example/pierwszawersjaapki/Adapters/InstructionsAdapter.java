package com.example.pierwszawersjaapki.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.Models.InstructionsResponse;
import com.example.pierwszawersjaapki.R;
import android.content.Context;

import java.util.List;
import com.example.pierwszawersjaapki.Adapters.InstructionStepAdapter;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsViewHolder>{

    Context context;
    List<InstructionsResponse> list;

    public InstructionsAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {
        holder.tv_instruction_name.setText(list.get(position).name);
        holder.rv_instruction_steps.setHasFixedSize(true);
        holder.rv_instruction_steps.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter instructionStepAdapter = new InstructionStepAdapter(context, list.get(position).steps);
        holder.rv_instruction_steps.setAdapter(instructionStepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionsViewHolder extends RecyclerView.ViewHolder {

    TextView tv_instruction_name;
    RecyclerView rv_instruction_steps;

    public InstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_instruction_name = itemView.findViewById(R.id.tv_instruction_name);
        rv_instruction_steps = itemView.findViewById(R.id.rv_instruction_steps);
    }
}