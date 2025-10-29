package com.example.pierwszawersjaapki.wiecejfragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pierwszawersjaapki.R;

public class WiecejFragmentViewHolder extends RecyclerView.ViewHolder {

    public ImageView ikonaView;
    public TextView nazwaView;
    public TextView opisView;

    public WiecejFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        ikonaView = itemView.findViewById(R.id.iv_wiecej_item_ikona);
        nazwaView = itemView.findViewById(R.id.tv_wiecej_item_nazwa);
        opisView = itemView.findViewById(R.id.tv_wiecej_item_opis);
    }
}
