package com.example.hindware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hindware.R;
import com.example.hindware.databinding.ItemNotiBinding;
import com.example.hindware.model.LanguageResponseList;
import com.example.hindware.model.NotificationBean;
import com.example.hindware.view.NotificationDialog;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by SandeepY on 17032021
 **/

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHoler> {

    private ArrayList<NotificationBean> notificationList;
    private Context context;
    private PublishSubject<String> publishSelectedNoti = PublishSubject.create();
    private NotificationDialog dialog;

    public NotificationAdapter(ArrayList<NotificationBean> langList, Context context, NotificationDialog dialog) {
        this.notificationList = langList;
        this.context = context;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public NotificationViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new NotificationAdapter.NotificationViewHoler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHoler holder, int position) {
        holder.setData(notificationList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationViewHoler extends RecyclerView.ViewHolder {

        private ItemNotiBinding binding;

        NotificationViewHoler(@NonNull View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        void setData(final NotificationBean bean, int pos) {
            binding.tvNotiName.setText(bean.getNotiName());

            binding.llNotiMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    public Observable<String> getSelectNoti() {
        return publishSelectedNoti.hide();
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
