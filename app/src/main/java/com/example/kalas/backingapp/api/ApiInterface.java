package com.example.kalas.backingapp.api;

import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipeData();
}

