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
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtPassword,txtEmail,txtUsername,txtSurname,txtUserNameName;
    private Button btnRegister,btnLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void createNewAccount() {
        final String name = txtUsername.getText().toString();
        final String surname = txtSurname.getText().toString();
        final String username = txtUserNameName.getText().toString();
        final String password = txtPassword.getText().toString();
        final String email = txtEmail.getText().toString();


        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"name Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(surname))
        {
            Toast.makeText(this,"Surname Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this,"username Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Email Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Password Cannot Be Empty!",Toast.LENGTH_LONG).show();
        }
        else
        {
            try {
                registerControl(name,surname,username,email,password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private void registerControl(final String name, final String surname, final String username, final String email, final String password) throws JSONException {

        // your ip adress
        String url = String.format("http://000.000.0.00:51321/api/user");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name);
        jsonBody.put("surname", surname);
        jsonBody.put("userName", username);
        jsonBody.put("email", email);
        jsonBody.put("password", password);
        final String mRequestBody = jsonBody.toString();
        System.out.println("request body : "+mRequestBody);
        System.out.println("url :"+url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("LOG_RESPONSE"+ response);
                saveToPhoneMemory(name,surname,username,email);
                goHome();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("3");
                System.out.println("LOG_RESPONSE"+ error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    System.out.println("2 : "+mRequestBody.getBytes("utf-8"));
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                System.out.println("1");
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }
    private void saveToPhoneMemory(final String name, final String surname, final String username, final String email) {
        sharedPreferences.edit().putString("name", name).apply();
        sharedPreferences.edit().putString("surname", surname).apply();
        sharedPreferences.edit().putString("username", username).apply();
        sharedPreferences.edit().putString("email", email).apply();

    }
    private void goHome() {
        Toast.makeText(RegisterActivity.this,"Your account has been successfully created",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void init() {
        txtSurname = findViewById(R.id.userSurnametxt);
        txtUserNameName = findViewById(R.id.userNameNametxt);
        txtPassword = findViewById(R.id.passwordtxt);
        txtEmail = findViewById(R.id.emailtxt);
        txtUsername = findViewById(R.id.usernametxt);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = this.getSharedPreferences("com.omurgun.MyBackendLoginByWepApi", Context.MODE_PRIVATE);
    }

}