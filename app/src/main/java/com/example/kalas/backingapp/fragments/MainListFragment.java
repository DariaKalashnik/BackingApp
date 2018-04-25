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
import com.example.kalas.backingapp.adapter.RecipeAdapter;
import com.example.kalas.backingapp.api.ApiClient;
import com.example.kalas.backingapp.api.ApiInterface;
import com.example.kalas.backingapp.databinding.FragmentListMainBinding;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.kalas.backingapp.activities.MainListActivity.sRecipesList;

public class MainListFragment extends Fragment implements RecipeAdapter.RecipeOnClickHandler {

    @BindInt(R.integer.span_count)
    int spanCount;
    private OnFragmentInteractionListener mListener;
    private RecipeAdapter mAdapter;
    private Unbinder mUnbinder;

    public MainListFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        FragmentListMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_main, container, false);
        View view = binding.getRoot();
        mUnbinder = ButterKnife.bind(this, view);
        mAdapter = new RecipeAdapter(MainListFragment.this);
        Utils.configViews(MainListFragment.this, mAdapter, binding, spanCount);
        loadRecipeData();
        return view;
    }

    public void loadRecipeData() {
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Call<ArrayList<Recipe>> callResponse = mApiInterface.getRecipeData();
        callResponse.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    sRecipesList = response.body();
                    mAdapter.addRecipe(sRecipesList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getContext(), getResources().getString(R.string.error_unsuccessful_response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                // Display Toast message if request is failed
                Utils.showToast(getContext(), getResources().getString(R.string.failure_getting_response));
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.fragment_listener_implementation_error));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Recipe recipe);
    }
}
