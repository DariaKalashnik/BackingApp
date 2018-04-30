package com.example.kalas.backingapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.adapter.StepAdapter;
import com.example.kalas.backingapp.adapter.StepAdapter.StepOnClickHandler;
import com.example.kalas.backingapp.databinding.FragmentListStepsBinding;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.model.Step;
import com.example.kalas.backingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPES_KEY;

public class StepsListFragment extends Fragment implements StepOnClickHandler, View.OnClickListener {

    private FragmentListStepsBinding mBinding;
    private OnFragmentInteractionListener mListener;
    private StepAdapter mStepAdapter;
    private Recipe mRecipe;
    private ArrayList<Recipe> mRecipes;

    public StepsListFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity().getIntent() != null) {
            mRecipes = getActivity().getIntent().getParcelableArrayListExtra(RECIPES_KEY);
        }
        // Inflate the layout for the fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_steps, container, false);

        mRecipe = Utils.setRecipe(mRecipes);

        // Display the Ingredients of the selected Recipe
        setIngredients();

        // Display the list of Steps of the selected Recipe
        setListOfSteps();

        mBinding.expandLayout.clickableLayout.setOnClickListener(this);
        Utils.configViews(StepsListFragment.this, mStepAdapter, mBinding);
        return mBinding.getRoot();
    }

    /**
     * Hold the reference to the parent Activity (StepsListActivity) to report the
     * task's current progress and results
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.error_fragment_listener_implementation_required));
        }
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(Step step) {
        mListener.onFragmentInteraction(step.getId());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Utils.expendableLayoutSettings(id, mBinding);
    }

    private void setListOfSteps() {
        LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mBinding.recyclerViewStep.setLayoutManager(layoutManager);
        List<Step> steps = mRecipe.getSteps();
        mStepAdapter = new StepAdapter(this);
        mStepAdapter.addStep(steps);
    }

    private void setIngredients() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Utils.displayIngredientData(mRecipe, builder);
        mBinding.recipeIngredientsContent.setText(builder.toString());
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int selectedStepId);
    }
}
