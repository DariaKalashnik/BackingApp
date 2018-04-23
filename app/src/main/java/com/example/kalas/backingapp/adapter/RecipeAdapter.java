package com.example.kalas.backingapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.BR;
import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.StepViewHolder> {

    private List<Recipe> mRecipeModel;
    private RecipeOnClickHandler mHandler;
    private ViewDataBinding mBinding;

    public RecipeAdapter(RecipeOnClickHandler onClickHandler) {
        this.mRecipeModel = new ArrayList<>();
        this.mHandler = onClickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(mRecipeModel.get(position));
    }

    public void addRecipe(List<Recipe> recipes){
        mRecipeModel = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mRecipeModel == null) ? 0 : mRecipeModel.size();
    }

    public interface RecipeOnClickHandler {
        void onClick(Recipe recipe);
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
            Recipe recipe = mRecipeModel.get(getAdapterPosition());
            mHandler.onClick(recipe);
        }
    }
}
