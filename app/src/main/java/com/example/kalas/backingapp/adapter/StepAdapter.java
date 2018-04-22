package com.example.kalas.backingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kalas.backingapp.BR;
import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalas on 4/2/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private List<Step> mModel;
    private StepAdapter.StepOnClickHandler mHandler;

    private Context mContext;


    public interface StepOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(Context context, StepOnClickHandler onClickHandler) {
        mContext = context;
        this.mModel = new ArrayList<>();
        mHandler = onClickHandler;
    }

    // tutorials used: https://inducesmile.com/android/how-to-set-recycleview-item-row-background-color-in-android/

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewDataBinding mBinding;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();
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
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
            }
            else {
                selectedItems.put(getAdapterPosition(), true);
                view.setSelected(true);
            }

            Step step = mModel.get(getAdapterPosition());
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
        holder.bind(mModel.get(position));

    }


    public void addStep(List<Step> steps){
        mModel = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mModel == null) ? 0 : mModel.size();
    }
}
