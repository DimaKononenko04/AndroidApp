package com.example.uploadimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.database_connection.manager.OwnerManager;
import com.example.database_connection.model.Owner;
import com.example.database_connection.sql_lite.DbHelper;

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
        checkIfOwnerIsInDb();
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
                .setTitle("\tNo info in DB\n\tAdd new record?")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Yes")){
                            showOwnerInputForm();
                        }else if (items[which].equals("No")){
                            dialog.dismiss();
                        }
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
