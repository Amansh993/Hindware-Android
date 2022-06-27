package com.example.hindware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hindware.R;
import com.example.hindware.databinding.ItemLanguageBinding;
import com.example.hindware.model.LanguageResponseList;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by SandeepY on 11122020
 **/

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private ArrayList<LanguageResponseList> langList;
    private Context context;
    private PublishSubject<LanguageResponseList> publishSelectedLang = PublishSubject.create();

    public LanguageAdapter(ArrayList<LanguageResponseList> langList, Context context) {
        this.langList = langList;
        this.context = context;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new LanguageAdapter.LanguageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.setData(langList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return langList.size();
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        private ItemLanguageBinding binding;

        LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        void setData(final LanguageResponseList bean, int pos) {
            binding.tvLangName.setText(bean.getlNG_NAME());

            binding.rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.rlMain.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_red));
                    publishSelectedLang.onNext(bean);
                }
            });
        }
    }

    public Observable<LanguageResponseList> getSelectLang() {
        return publishSelectedLang.hide();
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
