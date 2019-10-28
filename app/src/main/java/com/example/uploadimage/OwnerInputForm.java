package com.example.uploadimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.database_connection.manager.OwnerManager;
import com.example.database_connection.model.Owner;
import com.example.database_connection.sql_lite.DbHelper;
import com.example.utils.LayersManager;

public class OwnerInputForm extends AppCompatActivity {

    private EditText nameToAdd;
    private EditText surnameToAdd;
    private EditText telephoneToAdd;
    private EditText licensePlateToAdd;
    private Button addRecord;
    private Button fromInpFormToMainScreen;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_input_form);

        nameToAdd = findViewById(R.id.nameToAdd);
        surnameToAdd = findViewById(R.id.surnameToAdd);
        telephoneToAdd = findViewById(R.id.telephoneToAdd);
        licensePlateToAdd = findViewById(R.id.licensePlateToAdd);
        addRecord = findViewById(R.id.addRecord);
        fromInpFormToMainScreen = findViewById(R.id.fromInpFormToMainScreen);
        dbHelper = new DbHelper(this);

        final boolean nameIsEmpty = nameToAdd.getText().toString().isEmpty();
        final boolean surnameIsEmpty = surnameToAdd.getText().toString().isEmpty();
        final boolean telephoneIsEmpty = telephoneToAdd.getText().toString().isEmpty();
        final boolean licensePlateIsEmpty = licensePlateToAdd.getText().toString().isEmpty();

        String lpNumber = getIntent().getStringExtra("lpNumber");
        licensePlateToAdd.setText(lpNumber);

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameIsEmpty && !surnameIsEmpty && !telephoneIsEmpty && !licensePlateIsEmpty){
                    OwnerManager.addOwnerToDb(dbHelper,createOwner());
                    Toast.makeText(OwnerInputForm.this,"Success", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(OwnerInputForm.this,"Fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        fromInpFormToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayersManager.goToMainScreen(OwnerInputForm.this);
            }
        });

    }

    private Owner createOwner(){
        Owner owner = new Owner();
        owner.setName(String.valueOf(nameToAdd.getText()));
        owner.setSurname(String.valueOf(surnameToAdd.getText()));
        owner.setTelephone(String.valueOf(telephoneToAdd.getText()));
        owner.setLicensePlate(String.valueOf(licensePlateToAdd.getText()));
        return owner;
    }
}
