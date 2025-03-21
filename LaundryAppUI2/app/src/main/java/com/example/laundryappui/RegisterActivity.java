package com.example.laundryappui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tv;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase userDatabase;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDatabase = UserDatabase.getInstance(this);
        userDao = userDatabase.userDao();

        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirm = findViewById(R.id.editTextRegConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();
                if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"All field is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final UserEntity userEntity = userDao.getUserByUsername(username);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (userEntity != null){
                                    Toast.makeText(RegisterActivity.this, "User already exist.", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {

                                    if (password.compareTo(confirm) == 0) {
                                        if (isValid(password)) {

                                            executorService.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    UserEntity newuserEntity = new UserEntity();
                                                    newuserEntity.username = username;
                                                    newuserEntity.password = password;
                                                    newuserEntity.email = email;
                                                    userDao.registerUser(newuserEntity);
                                                }
                                            });

                                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this, "Password must contain at least 8 characters.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegisterActivity.this,"Password and Confirm password didn't match",Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        });

                    }
                });

            }
        });

    }
    public static boolean isValid(String passwordhere) {
        int f1=0,f2=0,f3=0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1=1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2=1;
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if(c>=33&&c<=46||c==64){
                    f3=1;
                }
            }
            if(f1==1 && f2==1 && f3==1)
                return true;
            return false;
        }
    }
}