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

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btn;
    TextView tv;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private UserDatabase userDatabase;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDatabase = UserDatabase.getInstance(this);
        userDao = userDatabase.userDao();

        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNewUser);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Username/Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(new Runnable() {
                    @Override
                        public void run() {
                            final UserEntity userEntity = userDao.getUserByUsername(username);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (userEntity != null && password.equals(userEntity.password)){
                                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid Username and/or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        insertAdminUser();

    }

    private void insertAdminUser() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (userDao.getUserByUsername("admin") == null) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.username = "admin";
                    userEntity.password = "admin123";
                    userEntity.email = "admin@laundryappui";
                    userDao.registerUser(userEntity);
                }
            }
        });
    }

}