package com.example.kalas.backingapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.BR;
import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalas on 4/1/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.StepViewHolder> {

    private List<Recipe> mModel;
    private RecipeOnClickHandler mHandler;
    private ViewDataBinding mBinding;


    public interface RecipeOnClickHandler {
        void onClick(Recipe recipe);
    }


    public RecipesAdapter(RecipeOnClickHandler onClickHandler) {
        this.mModel = new ArrayList<>();
        mHandler = onClickHandler;
    }


    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        StepViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            View view = binding.getRoot();
            view.setOnClickListener(this);
        }

        private void bind(Object obj) {
            mBinding.setVariable(BR.recipeModel, obj);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mModel.get(getAdapterPosition());
            mHandler.onClick(recipe);
        }
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_recipes, parent, false);
        return new StepViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(mModel.get(position));
    }

    public void addRecipe(List<Recipe> recipes){
        mModel = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mModel == null) ? 0 : mModel.size();
    }
}
