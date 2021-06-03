package com.example.tunisairapp.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;
import com.example.tunisairapp.models.IapiConnexion;
import com.example.tunisairapp.models.User;

import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL="http://"+Constants.IP_ADRESS+"/api_app_androide/";
    Button btnValidLogin;
    private IapiConnexion apiCnx;
    /*ImageView logo_img;
    TextView logo_title, logo_subtitle;*/
    private static final Pattern PASSWORED_Pattern = Pattern.compile("^" + ".{4,}" +"$");
    private EditText mailUser;
    private EditText passwordUser;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);


        mailUser=findViewById(R.id.emailUser);
        passwordUser=findViewById(R.id.passwordUser);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        btnValidLogin = findViewById(R.id.btn_ValidLogin);
        btnValidLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgressBar();
                User user=new User(mailUser.getText().toString(),passwordUser.getText().toString());
                    loginUser(user);
            }
        });
    }


    private Boolean verifyUser(User user) {

        if(TextUtils.isEmpty(user.getEmail())|| Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()==false)
        {
            return false;

        }

        return true;


    }
    public void loginUser(User user){


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", user.getEmail())
                .addFormDataPart("password", user.getPassword())
                .build();
        Call<List<User>> call=apiCnx.loginUser(requestBody);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                progressDialog.dismiss();
                User user=response.body().get(0);
                if(user!=null){
                    saveUser(user.getEmail(),user.getMatricule());
                }else{
                    alertError("LOGIN","Invalid email or password");
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressDialog.dismiss();
                alertError("SERVER","Something went wrong!please retry!");
            }
        });
    }

    private void alertError(String title,String message){

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i1) -> {
                })
                .show();
    }




    private void saveUser(String email,String matricule) {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.USER_CONNECTED, true);
        editor.putString(Constants.USER_MAIL, email);
        editor.putString(Constants.USER_MATRICULE, matricule);
        editor.apply();
        redirectUser();
    }
    private void redirectUser() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void displayProgressBar() {
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }



    public boolean checkEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();

        //return Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }
    public boolean chekpassword(String password){
        if(PASSWORED_Pattern.matcher(password).matches())
        {
            return true;
        }
        else return false;
    }
    }
