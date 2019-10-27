package com.example.uploadimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class OwnerInfoPage extends AppCompatActivity {

    TextView name;
    TextView surname;
    TextView telephone;
    TextView licensePlateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_info_page);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        telephone = findViewById(R.id.telephone);
        licensePlateNumber = findViewById(R.id.licensePlateNumber);



    }
}
