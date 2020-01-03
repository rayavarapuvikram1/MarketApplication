package com.example.marketapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity
{
    public FirebaseAuth firebaseAuth;
    Button sign_up_button;
    TextInputEditText email_signup, password_signup;
    ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        sign_up_button = findViewById(R.id.signup_button);
        email_signup = findViewById(R.id.signup_email);
        password_signup = findViewById(R.id.signup_password);
        progressDialog1 = new ProgressDialog(this);
        sign_up_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog1.setMessage("Signing Up you");
                progressDialog1.show();
                String email_idd = email_signup.getText().toString().trim();
                String passwordd = password_signup.getText().toString().trim();
                if (email_idd.isEmpty() || passwordd.isEmpty())
                {
                    if (email_idd.isEmpty())
                    {
                        progressDialog1.dismiss();
                        email_signup.setError("Need Proper email");
                        email_signup.requestFocus();
                    }
                    else if (passwordd.isEmpty())
                    {
                        progressDialog1.dismiss();
                        password_signup.setError("Need Proper Password");
                        password_signup.requestFocus();
                    }

                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email_idd, passwordd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (!task.isSuccessful())
                            {
                                progressDialog1.dismiss();
                                Toast.makeText(SignupActivity.this, "UNSUCCESSFUL", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                progressDialog1.dismiss();
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }
}
