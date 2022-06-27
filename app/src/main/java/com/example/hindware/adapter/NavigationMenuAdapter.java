package com.example.hindware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hindware.R;
import com.example.hindware.databinding.ItemMenuListBinding;
import com.example.hindware.model.MenuListDataBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by SandeepY on 10122020
 **/

public class NavigationMenuAdapter extends RecyclerView.Adapter<NavigationMenuAdapter.NavigationMenuViewHolder> {

    private ArrayList<MenuListDataBean> menuDataList;
    private Context context;
    private PublishSubject<MenuListDataBean> publishSelectedMenuData = PublishSubject.create();

    public NavigationMenuAdapter(ArrayList<MenuListDataBean> menuDataList, Context context) {
        this.menuDataList = menuDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public NavigationMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_list, viewGroup, false);
        return new NavigationMenuAdapter.NavigationMenuViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationMenuViewHolder holder, int position) {
        holder.setData(menuDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return menuDataList.size();
    }

    class NavigationMenuViewHolder extends RecyclerView.ViewHolder {

        private ItemMenuListBinding binding;

        NavigationMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        void setData(final MenuListDataBean bean, int pos) {
            if (null != bean.getmDL_IMAGEURL()) {
                Glide.with(context)
                        .load(bean.getmDL_IMAGEURL())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivMenuIcon);
                binding.tvMenuName.setText(bean.getmDL_TITLE());
            } else {
                binding.tvMenuName.setText(bean.getmDL_TITLE());
            }

            binding.llMenuMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishSelectedMenuData.onNext(bean);
                }
            });
        }
    }

    public Observable<MenuListDataBean> getSelectedMenu() {
        return publishSelectedMenuData.hide();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
