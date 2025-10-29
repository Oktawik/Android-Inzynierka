package com.example.pierwszawersjaapki;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pierwszawersjaapki.Adapters.IngredientsAdapter;
import com.example.pierwszawersjaapki.Adapters.InstructionsAdapter;
import com.example.pierwszawersjaapki.Adapters.SimilarRecipeAdapter;
import com.example.pierwszawersjaapki.Listeners.InstructionsListener;
import com.example.pierwszawersjaapki.Listeners.RecipeClickListener;
import com.example.pierwszawersjaapki.Listeners.RecipeDetailsListener;
import com.example.pierwszawersjaapki.Listeners.SimilarRecipesListener;
import com.example.pierwszawersjaapki.Models.InstructionsResponse;
import com.example.pierwszawersjaapki.Models.RecipeDetailsResponse;
import com.example.pierwszawersjaapki.Models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    int id;
    TextView tv_meal_name;
    TextView tv_meal_source;
    ImageView iv_meal_image;
    TextView tv_meal_summary;
    RecyclerView rv_meal_ingredients;
    RecyclerView rv_meal_similar;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    RecyclerView rv_meal_instructions;
    InstructionsAdapter instructionsAdapter;

    private String recipeId;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Tworzy nową instancję fragmentu z ID przepisu
     *
     * @param recipeId ID przepisu do wyświetlenia
     * @param param2 Dodatkowy parametr (opcjonalny)
     * @return Nowa instancja RecipeDetailsFragment
     */
    public static RecipeDetailsFragment newInstance(String recipeId, String param2) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId);
        if (param2 != null) {
            args.putString("param2", param2);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        findViews(view);

        if (getArguments() != null) {
            recipeId = getArguments().getString(ARG_RECIPE_ID);
            // Próba zmiany Stringa na Int
            try {
                id = Integer.parseInt(recipeId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // obsluga sytuacji gdy string nie jest liczbą
            }
        }

        manager = new RequestManager(getContext());
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Ładowanie szczegółów...");
        dialog.show();

        return view;
    }

    private void findViews(View view) {
        tv_meal_name = view.findViewById(R.id.tv_meal_name);
        tv_meal_source = view.findViewById(R.id.tv_meal_source);
        iv_meal_image = view.findViewById(R.id.iv_meal_image);
        tv_meal_summary = view.findViewById(R.id.tv_meal_summary);
        rv_meal_ingredients = view.findViewById(R.id.rv_meal_ingredients);
        rv_meal_similar = view.findViewById(R.id.rv_meal_similars);
        rv_meal_instructions = view.findViewById(R.id.rv_meal_instructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            tv_meal_name.setText(response.title);
            tv_meal_source.setText(response.sourceName);
            tv_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(iv_meal_image);

            rv_meal_ingredients.setHasFixedSize(true);
            rv_meal_ingredients.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(getContext(), response.extendedIngredients);
            rv_meal_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            rv_meal_similar.setHasFixedSize(true);
            rv_meal_similar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(getContext(), response, recipeClickListener);
            rv_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            RecipeDetailsFragment fragment = RecipeDetailsFragment.newInstance(id,null);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout,fragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            rv_meal_instructions.setHasFixedSize(true);
            rv_meal_instructions.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(getContext(), response);
            rv_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}