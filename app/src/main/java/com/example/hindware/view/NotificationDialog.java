package com.example.hindware.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hindware.R;
import com.example.hindware.adapter.LanguageAdapter;
import com.example.hindware.adapter.NotificationAdapter;
import com.example.hindware.model.NotificationBean;

import java.util.ArrayList;

/**
 * Created by SandeepY on 17032021
 **/

public class NotificationDialog extends DialogFragment {

    private ArrayList<NotificationBean> notificationBeanList;
    private NotificationAdapter notificationAdapter;
    private RecyclerView rvNoti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notification, container, false);
        rvNoti = view.findViewById(R.id.rvNoti);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        NotificationBean notificationBean1 = new NotificationBean();
        notificationBean1.setNotiIcon("");
        notificationBean1.setNotiName("4 new messages");

        NotificationBean notificationBean2 = new NotificationBean();
        notificationBean2.setNotiIcon("");
        notificationBean2.setNotiName("3 news reports");

        NotificationBean notificationBean3 = new NotificationBean();
        notificationBean3.setNotiIcon("");
        notificationBean3.setNotiName("500 points redeemed");

        NotificationBean notificationBean4 = new NotificationBean();
        notificationBean4.setNotiIcon("");
        notificationBean4.setNotiName("1000 points claimed");

        NotificationBean notificationBean5 = new NotificationBean();
        notificationBean5.setNotiIcon("");
        notificationBean5.setNotiName("2 products added in catalogue");

        notificationBeanList = new ArrayList<>();
        notificationBeanList.add(notificationBean1);
        notificationBeanList.add(notificationBean2);
        notificationBeanList.add(notificationBean3);
        notificationBeanList.add(notificationBean4);
        notificationBeanList.add(notificationBean5);
        notificationAdapter = new NotificationAdapter(notificationBeanList, getActivity(), NotificationDialog.this);
        rvNoti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvNoti.setAdapter(notificationAdapter);
    }
}
