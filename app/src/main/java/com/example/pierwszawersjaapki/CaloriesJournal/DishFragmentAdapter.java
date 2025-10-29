package com.example.pierwszawersjaapki.CaloriesJournal;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pierwszawersjaapki.R;

import org.w3c.dom.Text;

import java.util.List;

public class DishFragmentAdapter extends RecyclerView.Adapter<DishFragmentViewHolder>{
    Context context;
    List<DishItem> items;
    OnAddClickListener addClickListener;
    OnCameraAddClickListener cameraAddClickListener;
    OnMealTimingClickListener onMealTimingClickListener;

    public DishFragmentAdapter(Context context, List<DishItem> items, OnAddClickListener addClickListener, OnCameraAddClickListener onCameraAddClickListener, OnMealTimingClickListener onMealTimingClickListener) {
        this.context = context;
        this.items = items;
        this.addClickListener = addClickListener;
        this.cameraAddClickListener = onCameraAddClickListener;
        this.onMealTimingClickListener = onMealTimingClickListener;
    }

    @NonNull
    @Override
    public DishFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishFragmentViewHolder(LayoutInflater.from(context).inflate(R.layout.meal_timings_calorie_journal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishFragmentViewHolder holder, int position) {
        DishItem currentItem = items.get(position);

        // Obliczanie postępu (przykład: procent spożytych kalorii)
        int kalorieSpozyte = currentItem.getKalorieSpozyte();
        int kaloriePotrzebne = currentItem.getKaloriePotrzebne();

        // Zapobieganie dzieleniu przez zero
        if (kaloriePotrzebne > 0) {
            // Obliczanie procentu: (Spożyte / Potrzebne) * 100
            // Używamy rzutowania na float/double, aby uzyskać dokładny wynik zmiennoprzecinkowy,
            // a następnie zaokrąglamy go do inta, zgodnie z tym, co wcześniej omawialiśmy.
            int progressValue = (int) Math.round((double) kalorieSpozyte * 100 / kaloriePotrzebne);

            // Zabezpieczenie przed przekroczeniem 100%
            if (progressValue > holder.pb_dish.getMax()) {
                progressValue = holder.pb_dish.getMax();
            }

            // Ustawienie postępu na ProgressBarze
            holder.pb_dish.setProgress(progressValue); // Ustawia np. na 50
        } else {
            holder.pb_dish.setProgress(0);
        }

        holder.iv_dish_image.setImageResource(items.get(position).getIkona());
        holder.tv_dish_title.setText(items.get(position).getNazwa());
        holder.tv_dish_calories_consumed.setText(String.valueOf(items.get(position).getKalorieSpozyte()));
        holder.tv_dish_calores_needed.setText(" / " + String.valueOf(items.get(position).getKaloriePotrzebne()) + " kcal");
        holder.tv_dishes_ate.setText(items.get(position).getZjedzoneProdukty());

        holder.btn_dish_add.setOnClickListener(v-> {
            if(addClickListener!=null) {
                addClickListener.onAddClickListener(currentItem);
            }
        });

        holder.btn_dish_add_camera.setOnClickListener(v-> {
            if(cameraAddClickListener != null) {
                cameraAddClickListener.onCameraAddClick(currentItem);
            }
        });

        holder.ll_meal_timings.setOnClickListener(v-> {
            if(onMealTimingClickListener!=null) {
                onMealTimingClickListener.onMealTimingCliked(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface  OnMealTimingClickListener {
        void onMealTimingCliked(DishItem dishItem);
    }

    public interface OnCameraAddClickListener {
        void onCameraAddClick(DishItem dishItem);
    }

    public interface OnAddClickListener {
        void onAddClickListener(DishItem dishItem);
    }
}

class DishFragmentViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout ll_meal_timings;
    public ProgressBar pb_dish;
    public ImageView iv_dish_image;
    public TextView tv_dish_title;
    public TextView tv_dish_calories_consumed;
    public TextView tv_dish_calores_needed;
    public TextView tv_dishes_ate;
    public ImageButton btn_dish_add;
    public ImageButton btn_dish_add_camera;

    public DishFragmentViewHolder(@NonNull View itemView) {
        super(itemView);
        pb_dish = itemView.findViewById(R.id.pb_dish);
        iv_dish_image = itemView.findViewById(R.id.iv_dish_image);
        tv_dish_title = itemView.findViewById(R.id.tv_dish_title);
        tv_dish_calories_consumed = itemView.findViewById(R.id.tv_dish_calories_consumed);
        tv_dish_calores_needed = itemView.findViewById(R.id.tv_dish_calories_needed);
        tv_dishes_ate = itemView.findViewById(R.id.tv_dishes_ate);
        btn_dish_add = itemView.findViewById(R.id.btn_dish_add);
        btn_dish_add_camera = itemView.findViewById(R.id.btn_dish_add_camera);
        ll_meal_timings = itemView.findViewById(R.id.ll_meal_timings);
    }
}
