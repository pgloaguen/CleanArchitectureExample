package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pgloaguen.mycleanarchitectureexample.R;
import com.pgloaguen.domain.entity.RepoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.VH> {

    private List<RepoEntity> repoEntities;
    private OnRepoClick listener;

    public RepoAdapter() {}


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        RepoEntity repoEntity = repoEntities.get(position);
        holder.name.setText(repoEntity.name());
        holder.desc.setText(repoEntity.desc());
        holder.itemView.setOnClickListener(__ -> {
            if(listener != null) {
                listener.onRepoClick(repoEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repoEntities.size();
    }

    public void setData(List<RepoEntity> repoEntities) {
        this.repoEntities = repoEntities;
        notifyDataSetChanged();
    }

    public void setListener(OnRepoClick listener) {
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.desc)
        TextView desc;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRepoClick {
        void onRepoClick(RepoEntity repoEntity);
    }
}