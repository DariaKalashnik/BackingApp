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
import com.example.kalas.backingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> mStepModel;
    private StepOnClickHandler mHandler;

    public interface StepOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(StepOnClickHandler onClickHandler) {
        this.mStepModel = new ArrayList<>();
        this.mHandler = onClickHandler;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewDataBinding mBinding;

        StepViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            View view = binding.getRoot();
            view.setOnClickListener(this);
        }

        private void bind(Object obj) {
            mBinding.setVariable(BR.stepModel, obj);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            Step step = mStepModel.get(getAdapterPosition());
            mHandler.onClick(step);
        }
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(mStepModel.get(position));
    }


    public void addStep(List<Step> steps) {
        mStepModel = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mStepModel == null) ? 0 : mStepModel.size();
    }
}
