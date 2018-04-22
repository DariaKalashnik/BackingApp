package com.example.kalas.backingapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.adapter.RecipesAdapter;
import com.example.kalas.backingapp.api.ApiClient;
import com.example.kalas.backingapp.api.ApiInterface;
import com.example.kalas.backingapp.databinding.FragmentListMainBinding;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainListFragment extends Fragment implements RecipesAdapter.RecipeOnClickHandler {

    private OnFragmentInteractionListener mListener;
    private FragmentListMainBinding mBinding;
    private RecipesAdapter mAdapter;
    private Unbinder mUnbinder;

    @BindInt(R.integer.span_count)
    int spanCount;

    // Empty public constructor
    public MainListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_main, container, false);
        View view = mBinding.getRoot();
        mUnbinder = ButterKnife.bind(this, view);
        mAdapter = new RecipesAdapter(MainListFragment.this);
        Utils.configViews(MainListFragment.this, mAdapter, mBinding, spanCount);
        loadRecipes();
        return view;
    }

    public void loadRecipes() {
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<ArrayList<Recipe>> callResponse = mApiInterface.getRecipeInfo();
        callResponse.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = response.body();
                    mAdapter.addRecipe(recipes);
                    mAdapter.notifyDataSetChanged();

                } else {
                    Utils.showToast(getContext(), "Error!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                // Display Toast message if request is failed
                Utils.showToast(getContext(), "Failure!");
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(Recipe recipe) {
        mListener.onFragmentInteraction(recipe);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe recipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
