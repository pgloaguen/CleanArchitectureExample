package com.pgloaguen.mycleanarchitectureexample.feature.listrepo;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.pgloaguen.domain.entity.RepoEntity;
import com.pgloaguen.mycleanarchitectureexample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.VH> {

    @NonNull
    private List<RepoEntity> data = new ArrayList<>();

    @NonNull
    private final PublishSubject<RepoEntity> favoriteSubject = PublishSubject.create();

    @NonNull
    private final PublishSubject<RepoEntity> itemClickSubject = PublishSubject.create();

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
        holder.favorite.setImageResource(repoEntity.isFavorite() ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off);
        RxView.clicks(holder.favorite).map(__ -> repoEntity).subscribe(favoriteSubject);
        RxView.clicks(holder.itemView).map(__ -> repoEntity).subscribe(itemClickSubject);
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
                return data.get(oldItemPosition).id() == data.get(newItemPosition).id();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return data.get(oldItemPosition).equals(newData.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);

        this.data = newData;
    }

    public Observable<RepoEntity> favorite() {
        return favoriteSubject;
    }

    public Observable<RepoEntity> itemClick() {
        return itemClickSubject;
    }

    static class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.desc)
        TextView desc;

        @BindView(R.id.btn_favorite)
        ImageView favorite;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}