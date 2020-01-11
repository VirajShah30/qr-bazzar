package com.example.virtualbilingassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signIn extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    EditText semail,spassword;
    Button signInButton;

    private FirebaseAuth.AuthStateListener mAuthStateListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        mFirebaseAuth = FirebaseAuth.getInstance();
        semail = findViewById(R.id.semail);
        spassword = findViewById(R.id.spassword);

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null)
        {
            Toast.makeText(signIn.this,"You are logged in",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(signIn.this,Home.class));
        }
        else{
            signInButton = findViewById(R.id.signInButton);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = semail.getText().toString();
                    String pass = spassword.getText().toString();
                    if (email.isEmpty()) {
                        semail.setError("Provide an email");
                        semail.requestFocus();
                    } else if (pass.isEmpty()) {
                        spassword.setError("Provide an password");
                        spassword.requestFocus();
                    } else if (!(email.isEmpty() && pass.isEmpty())) {
                        mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(signIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(signIn.this, "Sign in Unsuccessful", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(signIn.this, Home.class));
                                    overridePendingTransition(R.anim.sile_in_right,R.anim.slide_out_left);
                                }
                            }
                        });
                    }
                }
            });

        }
    }
    public void openRegisterNew(View view)
    {
        Intent intent = new Intent(signIn.this, registerNew.class);
        startActivity(intent);
        overridePendingTransition(R.anim.sile_in_right,R.anim.slide_out_left);

    }
    public void resetPass(View view)
    {
        String email = semail.getText().toString();
        if(email.isEmpty())
        {
            semail.setError("Provide an email to reset");
            semail.requestFocus();
        }
        else
        {
            mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(signIn.this,"Reset Email Sent",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(signIn.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
