package com.example.hindware.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hindware.R;

/**
 * Created by SandeepY on 16122020
 **/

public class TransactionDialog extends DialogFragment {

    private String strResponseCode, strMessage, strQrCode, strProdName, strDesc, strPoints, strDecider;
    private TextView tvOK;
    private MyDialogCloseListener mListener;

    public TransactionDialog(String strResponseCode, String strMessage, String strQrCode, String strProdName,
            String strDesc, String strPoints, String strDecider) {
        this.strResponseCode = strResponseCode;
        this.strMessage = strMessage;
        this.strQrCode = strQrCode;
        this.strProdName = strProdName;
        this.strDesc = strDesc;
        this.strPoints = strPoints;
        this.strDecider = strDecider;
    }

    private static final String TAG = "TransactionDialog";
    private ImageView ivTransImage;
    private TextView tvQRCodeDialog, tvQRProdNameDialog, tvQRPointsDialog, tvQRDescDialog, tvTransAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_transaction, container, false);

        /*
         * ivTransImage = view.findViewById(R.id.ivTransImage);
         * tvQRCodeDialog = view.findViewById(R.id.tvQRCodeDialog);
         */
        tvQRProdNameDialog = view.findViewById(R.id.tvQRProdNameDialog);
        tvQRDescDialog = view.findViewById(R.id.tvQRDescDialog);
        tvQRPointsDialog = view.findViewById(R.id.tvQRPointsDialog);
        /* tvOK = view.findViewById(R.id.tvOK); */

        /*
         * if (strDecider.equalsIgnoreCase("ClaimUPI")) {
         * if (strResponseCode.equalsIgnoreCase("00")) {
         * ivTransImage.setImageResource(R.drawable.ic_success);
         * tvQRCodeDialog.setText("QR Code: " + strQrCode);
         * tvQRProdNameDialog.setText("Product Name: " + strProdName);
         * tvQRDescDialog.setVisibility(View.GONE);
         * tvQRPointsDialog.setText("Points: " + strPoints);
         * } else {
         * ivTransImage.setImageResource(R.drawable.ic_fail);
         * tvQRPointsDialog.setText(strMessage);
         * }
         * } else {
         * if (strResponseCode.equalsIgnoreCase("00")) {
         * ivTransImage.setImageResource(R.drawable.ic_success);
         * tvQRCodeDialog.setText("QR Code: " + strQrCode);
         * tvQRProdNameDialog.setText("Product Name: " + strProdName);
         * tvQRDescDialog.setText("Description: " + strDesc);
         * tvQRDescDialog.setVisibility(View.VISIBLE);
         * tvQRPointsDialog.setText("Points: " + strPoints);
         * } else {
         * ivTransImage.setImageResource(R.drawable.ic_fail);
         * tvQRPointsDialog.setText(strMessage);
         * }
         * }
         */
        tvQRProdNameDialog.setText(strProdName);
        tvQRDescDialog.setText(strDesc);
        tvQRPointsDialog.setText(strPoints);

        /*
         * tvOK.setOnClickListener(new View.OnClickListener() {
         * 
         * @Override
         * public void onClick(View view) {
         *//*
            * if (strDecider.equalsIgnoreCase("claimupi"))
            * getActivity().finish();
            * else
            *//*
               * dismiss();
               * }
               * });
               */

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (strDecider.equalsIgnoreCase(getActivity().getResources().getString(R.string.claim_upi_controller)))
            getActivity().finish();
        else {
            dismiss();
            this.mListener.handleDialogClose();
        }
    }

    /*
     * @Override
     * public void onAttach(@NonNull Context context) {
     * super.onAttach(context);
     * try {
     * this.mListener = (MyDialogCloseListener) context;
     * }catch (final ClassCastException e) {
     * throw new ClassCastException("" + " must implement OnCompleteListener");
     * }
     * }
     */

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if (!strDecider.equalsIgnoreCase("claimupi")) {
            try {
                this.mListener = (MyDialogCloseListener) activity;
            } catch (final ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
            }
        }
    }

    public interface MyDialogCloseListener {
        public void handleDialogClose();
    }
}
