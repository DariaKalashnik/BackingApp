package com.example.kalas.backingapp.utils;

public class BuildConfig {

    // Base URL for the Recipes
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    // Key to store list of Recipes
    public static final String RECIPES_KEY = "RECIPES LIST";

    // Key to store selected Step from the list and get it in the DetailsFragment after
    public static final String SELECTED_STEP_KEY = "SELECTED STEP";

    // Dash symbol with spaces, used to display list of Ingredients
    public static final String DASH = " - ";

    // Key for SharedPreferences that stores ingredient info in the widget
    public static final String SHARED_PREFS = "SHARED PREFERENCES";

    // Key to store ingredient info in the widget
    public static final String KEY_INGREDIENT_TEXT = "INGREDIENTS DATA";

    // Key to store the state of the ExoPlayer
    public static final String CURRENT_PLAYER_STATE = "PLAYER STATE";

    // Key to store the position of the ExoPlayer
    public static final String CURRENT_PLAYER_POSITION = "PLAYER POSITION";
}
