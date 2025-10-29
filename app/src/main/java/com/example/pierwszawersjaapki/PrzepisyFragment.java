package com.example.pierwszawersjaapki;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pierwszawersjaapki.Adapters.RandomRecipeAdapter;
import com.example.pierwszawersjaapki.Listeners.RandomRecipeResponseListener;
import com.example.pierwszawersjaapki.Listeners.RecipeClickListener;
import com.example.pierwszawersjaapki.Models.RandomRecipeApiResponse;
import com.example.pierwszawersjaapki.Models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrzepisyFragment extends Fragment {

    private static final String TAG = "PrzepisyFragment";
    ProgressDialog dialog;
    RequestManager manager;
    TranslationManager translationManager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;
    private boolean isModelReady = false;

    public PrzepisyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Ładowanie...");

        // Inicjalizacja translatora
        translationManager = new TranslationManager(getActivity());

        // Pobierz model tłumaczenia
        downloadTranslationModel();
    }

    private void downloadTranslationModel() {
        // Spróbuj w tle pobrać model, ale nie blokuj UI
        new Thread(() -> {
            translationManager.ensureModelDownloaded(new TranslationManager.ModelDownloadListener() {
                @Override
                public void onModelDownloaded() {
                    isModelReady = true;
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Log.d(TAG, "Model ready for translation");
                            Toast.makeText(getActivity(), "Tłumaczenie gotowe!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onModelDownloadError(String error) {
                    // Nie ustawiaj isModelReady na false - pozwól na próbę tłumaczenia
                    isModelReady = true; // Spróbujemy tłumaczyć - model może się pobrać podczas tłumaczenia
                    Log.e(TAG, "Model download error: " + error);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), "Tłumaczenie dostępne (model pobierze się automatycznie)", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }).start();

        // Od razu ustaw jako gotowe - model pobierze się automatycznie przy pierwszym użyciu
        isModelReady = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_przepisy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.sp_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.tags_display,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        searchView = view.findViewById(R.id.sv_przepisy);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isModelReady) {
                    Toast.makeText(getActivity(), "Poczekaj na pobranie modelu tłumaczenia", Toast.LENGTH_SHORT).show();
                    return true;
                }
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.setMessage("Ładowanie przepisów...");
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView = view.findViewById(R.id.recycler_random);
        recyclerView.setHasFixedSize(true);

        manager = new RequestManager(getActivity());
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            Log.d(TAG, "Recipes fetched: " + response.recipes.size());

            if (!isModelReady) {
                Toast.makeText(getActivity(), "Model tłumaczenia jeszcze nie gotowy", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                displayRecipes(response.recipes); // Wyświetl bez tłumaczenia
                return;
            }

            dialog.setMessage("Tłumaczenie...");
            translateRecipes(response.recipes);
        }

        @Override
        public void didError(String message) {
            dialog.dismiss();
            Log.e(TAG, "Error fetching recipes: " + message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private void translateRecipes(List<Recipe> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            dialog.dismiss();
            return;
        }

        Log.d(TAG, "Starting translation for " + recipes.size() + " recipes");

        final int[] translatedCount = {0};
        final int totalRecipes = recipes.size();

        for (Recipe recipe : recipes) {
            if (recipe.title != null && !recipe.title.isEmpty()) {
                Log.d(TAG, "Translating recipe: " + recipe.title);

                translationManager.translateText(recipe.title, new TranslationManager.TranslationListener() {
                    @Override
                    public void onTranslationComplete(String translatedText) {
                        Log.d(TAG, "Translation completed: " + recipe.title + " -> " + translatedText);
                        recipe.title = translatedText;
                        translatedCount[0]++;

                        if (translatedCount[0] == totalRecipes) {
                            Log.d(TAG, "All recipes translated");
                            displayRecipes(recipes);
                        }
                    }

                    @Override
                    public void onTranslationError(String error) {
                        Log.e(TAG, "Translation error: " + error);
                        translatedCount[0]++;

                        if (translatedCount[0] == totalRecipes) {
                            displayRecipes(recipes);
                        }
                    }
                });
            } else {
                translatedCount[0]++;
                if (translatedCount[0] == totalRecipes) {
                    displayRecipes(recipes);
                }
            }
        }
    }

    private void displayRecipes(List<Recipe> recipes) {
        Log.d(TAG, "Displaying recipes");
        dialog.dismiss();
        randomRecipeAdapter = new RandomRecipeAdapter(getActivity(), recipes, recipeClickListener);
        recyclerView.setAdapter(randomRecipeAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!isModelReady) {
                Toast.makeText(getActivity(), "Poczekaj na pobranie modelu tłumaczenia", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] valueArray = getResources().getStringArray(R.array.tags);

            tags.clear();
            tags.add(valueArray[position]);
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
            dialog.setMessage("Ładowanie przepisów...");
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (translationManager != null) {
            translationManager.close();
        }
    }

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            RecipeDetailsFragment fragment = RecipeDetailsFragment.newInstance(id, null);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null) // umozliwia powrót przyciskiem "wstecz"
                    .commit();
        }
    };
}