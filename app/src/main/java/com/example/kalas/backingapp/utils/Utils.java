package com.example.kalas.backingapp.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.adapter.RecipesAdapter;
import com.example.kalas.backingapp.adapter.StepAdapter;
import com.example.kalas.backingapp.databinding.FragmentListMainBinding;
import com.example.kalas.backingapp.databinding.FragmentListStepsBinding;
import com.example.kalas.backingapp.fragments.MainListFragment;
import com.example.kalas.backingapp.fragments.StepsListFragment;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalas on 4/1/2018.
 */

public class Utils {

    // Method to show the Toast messages
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Method to set the View for the MainListFragment
    public static void configViews(MainListFragment mainListFragment, RecipesAdapter recipesAdapter, FragmentListMainBinding binding, int spanCount) {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(mainListFragment.getContext(), spanCount));
        binding.recyclerView.setAdapter(recipesAdapter);
    }

    // Method to set the View for the MainListFragment
    public static void configViews(StepsListFragment stepFragment, StepAdapter adapter, FragmentListStepsBinding binding) {
        binding.recyclerViewStep.setHasFixedSize(true);
        binding.recyclerViewStep.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        binding.recyclerViewStep.setLayoutManager(new GridLayoutManager(stepFragment.getContext(), 1));
        binding.recyclerViewStep.setAdapter(adapter);
    }

    // Method expand or collapse ExpendableLayout
    public static void expendableLayoutSettings(int id, FragmentListStepsBinding binding) {
        switch (id) {
            case R.id.expand_layout:
                if (!binding.expandableLayoutStepDescription.isExpanded()) {
                    binding.expandableLayoutStepDescription.expand();
                    binding.expandLayout.arrowDown.setVisibility(View.GONE);
                    binding.expandLayout.arrowUp.setVisibility(View.VISIBLE);
                } else {
                    binding.expandableLayoutStepDescription.collapse();
                    binding.expandLayout.arrowDown.setVisibility(View.VISIBLE);
                    binding.expandLayout.arrowUp.setVisibility(View.GONE);
                }
                break;
        }
    }

    // Method to set Recipe from passed Recipes
    public static Recipe setRecipe(ArrayList<Recipe> recipes) {
        Recipe recipe = null;
        for (int i = 0; i < recipes.size(); i++) {
            recipe = recipes.get(i);
        }
        return recipe;
    }
}
