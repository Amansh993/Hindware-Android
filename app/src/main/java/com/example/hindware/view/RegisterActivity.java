package com.example.hindware.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hindware.R;
import com.example.hindware.model.BaseRequestBean;
import com.example.hindware.model.DropDownResponseList;
import com.example.hindware.model.GetIDTypeResponseBean;
import com.example.hindware.model.LogoutRequestBean;
import com.example.hindware.model.Spinnclass;
import com.example.hindware.utility.CommonUtil;
import com.example.hindware.viewmodel.QwikcilverViewModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class RegisterActivity extends BaseActivity implements View.OnClickListener  {


    private Spinner spinner;
    List<DropDownResponseList> spinnerL =new ArrayList<>();

    private static final int FILE_SELECT_CODE = 11;
    private Uri myURI;
    Uri uri;
    Button register, uploadId;
    private String[] items = new String[]{"Customer", "Employee","Date" };
    EditText first_name,last_name, mobile, city, state, pincode, idNumber;
    String fName, lName, mbNumber, cityName, stateName, pCode, spinnerText, id, bankOrUPI;
    LinearLayout bankFormLayout, upiFormLayout;
    RadioButton bankDetailsButton, upiDetailsButton;
    RadioGroup group;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_register);

//        callToGetIDTYpe();


        first_name = (EditText) findViewById(R.id.register_first_name);
        last_name = (EditText) findViewById(R.id.register_last_name);
        mobile = (EditText) findViewById(R.id.register_mobile);
        city = (EditText) findViewById(R.id.register_city);
        state = (EditText) findViewById(R.id.register_state);
        pincode = (EditText) findViewById(R.id.register_pincode);
        spinner = (Spinner) findViewById(R.id.register_id_type);
        bankFormLayout = (LinearLayout) findViewById(R.id.bank_layout);
        upiFormLayout = (LinearLayout) findViewById(R.id.upi_layout);
        group=(RadioGroup) findViewById(R.id.radio_button_group);
        bankDetailsButton = (RadioButton) findViewById(R.id.bank_account_details);
        upiDetailsButton = (RadioButton) findViewById(R.id.upi_id_button);
        idNumber = (EditText) findViewById(R.id.register_id_number);
        uploadId = (Button) findViewById(R.id.register_upload_id);
        register = (Button) findViewById(R.id.register);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.bank_account_details:
                        bankFormLayout.setVisibility(View.VISIBLE);
                        upiFormLayout.setVisibility(View.GONE);
                        upiDetailsButton.setChecked(false);
                        break;
                    case R.id.upi_id_button:
                        bankDetailsButton.setChecked(false);
                        bankFormLayout.setVisibility(View.GONE);
                        upiFormLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        bankFormLayout.setVisibility(View.VISIBLE);
                        upiFormLayout.setVisibility(View.GONE);
                        upiDetailsButton.setChecked(false);
                        break;
                }

            }
        });



//        Intent i=getIntent();
//        spinnerL=(List<DropDownResponseList>)i.getSerializableExtra("spinner");
//        final Spinnclass adp = new Spinnclass(RegisterActivity.this,
//                android.R.layout.simple_spinner_item, spinnerL);
//        spinner.setSelection(0);
//        spinner.setAdapter(adp);

        uploadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                onBrowse();
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);


            }
        });




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fName= first_name.getText().toString();
                 lName= last_name.getText().toString();
                 mbNumber= mobile.getText().toString();
                 cityName= city.getText().toString();
                 stateName= state.getText().toString();
                 pCode= pincode.getText().toString();
                 spinnerText = spinner.getSelectedItem().toString();
                 id= idNumber.getText().toString();

                Log.e("Button Of Register","Register");
                Log.e("Button Of Register",fName);
                Log.e("Button Of Register",lName);
                Log.e("Button Of Register",mbNumber);
                Log.e("Button Of Register",cityName);
                Log.e("Button Of Register",stateName);
                Log.e("Button Of Register",pCode);
                Log.e("Button Of Register",spinnerText);
                Log.e("Button Of Register",id);
                Log.e("Button Of Register",bankOrUPI);

            }
        });




    }




    @Override
    public void onClick(View v) {

    }

    private void onBrowse() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select image file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                     uri = data.getData();
                    myURI = data.getData();

                    Cursor cursor = getContentResolver().query(myURI, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    uploadId.setText(cursor.getString(nameIndex));
                    Log.e("My URI Early",myURI.getLastPathSegment());

//                    callUpdateProfileImageAPi();

                }
                break;
        }
    }




}