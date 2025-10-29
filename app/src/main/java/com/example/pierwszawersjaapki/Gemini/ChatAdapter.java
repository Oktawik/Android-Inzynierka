package com.example.pierwszawersjaapki.Gemini;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;
import java.util.List;

// Zmieniamy generyczny typ na RecyclerView.ViewHolder, aby obsłużyć WIELE ViewHolderów
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> messages;

    // Definiujemy stałe dla typów widoków
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 0;
    private static final int VIEW_TYPE_LOADING = 2; // <-- NOWY TYP

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        // Sprawdzamy najpierw, czy to ładowanie
        if (message.isLoading()) {
            return VIEW_TYPE_LOADING;
        }
        // Potem sprawdzamy, czy to użytkownik
        if (message.isUser()) {
            return VIEW_TYPE_USER;
        }
        // W przeciwnym razie to bot
        return VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // Influjemy odpowiedni layout w zależności od typu widoku
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_user, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_BOT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_bot, parent, false);
            return new MessageViewHolder(view);
        } else { // VIEW_TYPE_LOADING
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view); // <-- Zwracamy nowy ViewHolder
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bindujemy dane tylko jeśli to MessageViewHolder (User lub Bot)
        // LoadingViewHolder nie potrzebuje żadnych danych
        if (holder.getItemViewType() == VIEW_TYPE_USER || holder.getItemViewType() == VIEW_TYPE_BOT) {
            ((MessageViewHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

// Ten ViewHolder zostaje bez zmian
class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_message;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_message = itemView.findViewById(R.id.message_text);

        // Możesz usunąć logikę DisplayMetrics, jeśli layouty item_message_...
        // mają ustawione `android:layout_width="wrap_content"`
        // i są w kontenerze, który ogranicza szerokość (np. przez marginesy)
        DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();
        int maxWidth = (int) (displayMetrics.widthPixels * 0.8);
        tv_message.setMaxWidth(maxWidth);
    }

    public void bind(Message message) {
        tv_message.setText(message.getText());
    }
}

// <-- NOWY VIEWHOLDER DLA ŁADOWANIA
// Nie musi nic robić, tylko trzymać widok
class LoadingViewHolder extends RecyclerView.ViewHolder {
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}