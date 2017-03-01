package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.VH> {

    @NonNull
    private List<RepoEntity> data = new ArrayList<>();

    @Nullable
    private OnRepoClick listener;

    public RepoAdapter() {}


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        RepoEntity repoEntity = data.get(position);
        holder.name.setText(repoEntity.name());
        holder.desc.setText(repoEntity.desc());
        holder.favorite.setText(repoEntity.isFavorite() ? "Unfavorite" : "Favorite");
        holder.itemView.setOnClickListener(__ -> {
            if(listener != null) {
                listener.onRepoClick(repoEntity);
            }
        });
        holder.favorite.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRepoFavorite(repoEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull final List<RepoEntity> newData) {

        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return data.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).equals(newData.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).equals(newData.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);

        this.data = newData;
    }

    public void setListener(@Nullable OnRepoClick listener) {
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.desc)
        TextView desc;

        @BindView(R.id.btn_favorite)
        Button favorite;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRepoClick {
        void onRepoClick(RepoEntity repoEntity);
        void onRepoFavorite(RepoEntity repoEntity);
    }
}