package com.omurgun.mybackendloginbywepapi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView txtName,txtSurname;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        ShowUserInformation();
    }
    private void init() {
        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        sharedPreferences = this.getSharedPreferences("com.omurgun.MyBackendLoginByWepApi", Context.MODE_PRIVATE);
    }
    private void ShowUserInformation() {
        final String name = sharedPreferences.getString("name","null");
        final String surname = sharedPreferences.getString("surname","null");
        txtName.setText(name);
        txtSurname.setText(surname);
    }
}