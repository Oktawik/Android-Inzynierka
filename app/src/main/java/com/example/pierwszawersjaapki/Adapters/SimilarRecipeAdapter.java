package com.example.pierwszawersjaapki.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.Listeners.RecipeClickListener;
import com.example.pierwszawersjaapki.Models.SimilarRecipeResponse;
import com.example.pierwszawersjaapki.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeViewHolder>{

    Context context;
    List<SimilarRecipeResponse> list;
    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {
        holder.tv_similar_title.setText(list.get(position).title);
        holder.tv_similar_title.setSelected(true);

        holder.tv_similar_serving.setText(list.get(position).servings + " Osoby");
        holder.tv_similar_serving.setSelected(true);

        Picasso.get().load("https://img.spoonacular.com/recipes/"+list.get(position).id+"-480x360."+list.get(position).imageType).into(holder.iv_similar);

        holder.similar_recipe_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SimilarRecipeViewHolder extends RecyclerView.ViewHolder {
    CardView similar_recipe_holder;
    TextView tv_similar_title, tv_similar_serving;
    ImageView iv_similar;

    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        similar_recipe_holder = itemView.findViewById(R.id.similar_recipe_holder);
        tv_similar_title = itemView.findViewById(R.id.tv_similar_title);
        tv_similar_serving = itemView.findViewById(R.id.tv_similar_serving);
        iv_similar = itemView.findViewById(R.id.iv_similar);
    }
}
