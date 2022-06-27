package com.example.hindware.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hindware.R;

public class ClaimSuccessActivity extends AppCompatActivity {


    TextView firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_success);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);

        Intent intent = getIntent();

        String fName = intent.getStringExtra("firstName");
        String lName = intent.getStringExtra("lastName");

        firstName.setText(fName);
        lastName.setText(lName);


    }
}