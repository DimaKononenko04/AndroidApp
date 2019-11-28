package com.example.uploadimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import com.example.database_connection.manager.OwnerManager;
import com.example.database_connection.model.Owner;
import com.example.database_connection.sql_lite.DbHelper;
import com.example.utils.LayersManager;

public class OwnerInfoPage extends AppCompatActivity {

    private TextView name;
    private TextView surname;
    private TextView telephone;
    private TextView licensePlateNumber;
    private Button fromInfoPageToMainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_info_page);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        telephone = findViewById(R.id.telephone);
        licensePlateNumber = findViewById(R.id.licensePlateNumber);
        fromInfoPageToMainScreen = findViewById(R.id.fromInfoPageToMainScreen);
        checkIfOwnerIsInDb();

        fromInfoPageToMainScreen.setOnClickListener(v -> LayersManager.goToMainScreen(OwnerInfoPage.this));
    }

    private void checkIfOwnerIsInDb(){
        String filter = getIntent().getStringExtra("Filter");
        DbHelper dbHelper = new DbHelper(OwnerInfoPage.this);
        Owner ownerInfo = OwnerManager.getOwnerInfo(dbHelper, filter);
        if (ownerInfo != null){
            name.setText(ownerInfo.getName());
            surname.setText(ownerInfo.getSurname());
            telephone.setText(ownerInfo.getTelephone());
            licensePlateNumber.setText(ownerInfo.getLicensePlate());
        }else {
            selectOption();
        }
    }

    private void selectOption(){
        final CharSequence[] items ={"Yes", "No"};
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerInfoPage.this);
        builder
                .setTitle("\tНе знайдено в БД\n\tДодати новий запис?")
                .setItems(items, (dialog, which) -> {
                    if (items[which].equals("Так")){
                        showOwnerInputForm();
                    }else if (items[which].equals("Ні")){
                        LayersManager.goToMainScreen(OwnerInfoPage.this);
                    }
                });
        builder.show();
    }

    private void showOwnerInputForm(){
        Intent intent = new Intent(OwnerInfoPage.this,OwnerInputForm.class);
        intent.putExtra("lpNumber",getIntent().getStringExtra("Filter"));
        startActivity(intent);
    }
}
