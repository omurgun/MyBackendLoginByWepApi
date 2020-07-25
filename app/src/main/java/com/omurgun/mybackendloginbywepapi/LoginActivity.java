package com.omurgun.mybackendloginbywepapi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin,btnRegister;
    private EditText txtEmail,txtPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        userControl();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void userControl() {
        final String name = sharedPreferences.getString("name","null");
        if (!name.equals("null"))
        {
            goHome();
        }
    }
    private void init() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.emailtxt);
        txtPassword = findViewById(R.id.passwordtxt);
        sharedPreferences = this.getSharedPreferences("com.omurgun.MyBackendLoginByWepApi", Context.MODE_PRIVATE);
    }
    private void loginUser() {

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email Cannot Be Empty!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password Cannot Be Empty!", Toast.LENGTH_LONG).show();
        }
        else
        {

            loginControl(email, encryptThisString(password));
        }
    }
    private void saveToPhoneMemory(final String name, final String surname, final String username, final String email) {
        sharedPreferences.edit().putString("name", name).apply();
        sharedPreferences.edit().putString("surname", surname).apply();
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("email", email).apply();

    }
    private void loginControl(String email, String password) {

        // your ip adress
        String url = String.format("http://000.000.0.00:51321/api/user?Username=%s&Password=%s",email,password);
        System.out.println("url :"+url);
        RequestQueue  requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response : "+response);

                 String name="";
                 String surname="";
                 String username="";
                 String email="";

                try {
                    name = response.getString("name");
                    surname = response.getString("surname");
                    username = response.getString("username");
                    email = response.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveToPhoneMemory(name,surname,username,email);
                goHome();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error :"+error.getMessage());
                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
    private void goHome() {
        Intent intentLogin = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intentLogin);
        finish();
    }
    private static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-384
            MessageDigest md = MessageDigest.getInstance("SHA-384");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}