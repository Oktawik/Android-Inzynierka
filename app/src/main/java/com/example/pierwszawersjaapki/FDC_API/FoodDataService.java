package com.example.pierwszawersjaapki.FDC_API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodDataService {
    @GET("v1/foods/search")
    Call<FoodSearchResponse> searchFoods(
            @Query("api_key") String api_key,
            @Query("query") String searchQuery,
            @Query("pageSize") int pageSize
    );
}
