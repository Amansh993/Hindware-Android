package com.example.hindware.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hindware.R;

import static com.example.hindware.utility.Constants.CONTROLLER;
import static com.example.hindware.utility.Constants.TITLE;

/**
 * Created by SandeepY on 06012021
 **/

public class ScannerDialog extends DialogFragment {

    private LinearLayout llGenCheck, llClaim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_dialog, container, false);

        llGenCheck = view.findViewById(R.id.llGenCheck);
        llClaim = view.findViewById(R.id.llClaim);

        llGenCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(getActivity(), ScanQRNewActivity.class);
                intent.putExtra(TITLE, getActivity().getResources().getString(R.string.genuinity_check_text));
                intent.putExtra(CONTROLLER,
                        getActivity().getResources().getString(R.string.genuinity_check_controller));
                startActivity(intent);
            }
        });

        llClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent = new Intent(getActivity(), ScanQRNewActivity.class);
                intent.putExtra(TITLE, getActivity().getResources().getString(R.string.cliam_upi_text));
                intent.putExtra(CONTROLLER, getActivity().getResources().getString(R.string.claim_upi_controller));
                startActivity(intent);
            }
        });

        return view;
    }
}
