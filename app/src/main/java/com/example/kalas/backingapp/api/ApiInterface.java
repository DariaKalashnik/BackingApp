package com.example.kalas.backingapp.api;

import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.kalas.backingapp.utils.BuildConfig.BASE_URL;

/**
 * Created by kalas on 4/1/2018.
 */

public interface ApiInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipeInfo();
}

