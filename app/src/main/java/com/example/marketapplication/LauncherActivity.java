package com.example.marketapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity
{
    Button login_button;
    TextView sign_up_button;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    TextInputEditText email_id_element, password_element;
    /*
    <com.google.android.material.textfield.TextInputLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:passwordToggleEnabled="true">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textPassword"/>
    </com.google.android.material.textfield.TextInputLayout>

    // In gradle
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'com.android.support:design:28.0.0'
    */
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        email_id_element = findViewById(R.id.login_email_id);
        password_element = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login);
        sign_up_button = findViewById(R.id.sign_up);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null)
                {
                    Toast.makeText(LauncherActivity.this, "Logged In Good TO GO", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(LauncherActivity.this, "Need Credentials", Toast.LENGTH_SHORT).show();

                }
            }
        };
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog.setMessage("Logging In");
                progressDialog.show();
                String email_idd = email_id_element.getText().toString().trim();
                String passwordd = password_element.getText().toString().trim();
                if (email_idd.isEmpty() || passwordd.isEmpty())
                {
                    if (email_idd.isEmpty())
                    {
                        progressDialog.dismiss();
                        email_id_element.setError("Need Proper email");
                        email_id_element.requestFocus();
                    }
                    else if (passwordd.isEmpty())
                    {
                        progressDialog.dismiss();
                        password_element.setError("Need Proper Password");
                        password_element.requestFocus();
                    }

                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(email_idd, passwordd).addOnCompleteListener(LauncherActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (!task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LauncherActivity.this, "UNSUCCESSFUL SIGN IN", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LauncherActivity.this, "SIGN IN Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
        sign_up_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
