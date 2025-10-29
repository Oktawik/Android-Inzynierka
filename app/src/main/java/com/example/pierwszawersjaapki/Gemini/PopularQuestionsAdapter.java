package com.example.pierwszawersjaapki.Gemini;

import android.app.sdksandbox.AppOwnedSdkSandboxInterface;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import java.util.List;

public class PopularQuestionsAdapter extends RecyclerView.Adapter<PopularQuestionsViewHolder> {

    Context context;
    List<PopularQuestionsItem> popularQuestionsList;
    PopularQuestionClickListener listener;

    public PopularQuestionsAdapter(Context context, List<PopularQuestionsItem> popularQuestionsList, PopularQuestionClickListener listener) {
        this.context = context;
        this.popularQuestionsList = popularQuestionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PopularQuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularQuestionsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chatbot_popular_question, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularQuestionsViewHolder holder, int position) {
        holder.iv_chatbot_popular_question.setImageResource(popularQuestionsList.get(position).getPopular_question_image());
        holder.tv_chatbot_popular_question.setText(popularQuestionsList.get(position).getPopular_question_text());

        PopularQuestionsItem item = popularQuestionsList.get(position);

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(popularQuestionsList.get(currentPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularQuestionsList.size();
    }

    public interface PopularQuestionClickListener {
        void onItemClick(PopularQuestionsItem item);
    }
}

class PopularQuestionsViewHolder extends RecyclerView.ViewHolder {

    ImageView iv_chatbot_popular_question;
    TextView tv_chatbot_popular_question;

    public PopularQuestionsViewHolder(@NonNull View itemView) {
        super(itemView);

        iv_chatbot_popular_question = itemView.findViewById(R.id.iv_chatbot_popular_question);
        tv_chatbot_popular_question = itemView.findViewById(R.id.tv_chatbot_popular_question);
    }
}
