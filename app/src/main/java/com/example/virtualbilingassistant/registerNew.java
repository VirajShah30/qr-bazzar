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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerNew extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    EditText remail,rpassword,rname,rphone,rcpass;
    Button rbutton;
    String userId;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_new);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        rname = findViewById(R.id.editText2);
        rphone = findViewById(R.id.editText);
        remail = findViewById(R.id.rEmail);
        rpassword = findViewById(R.id.rPassword);
        rbutton = findViewById(R.id.rButton);
        rcpass = findViewById(R.id.rPassword2);
        rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = rname.getText().toString();
                final String phone = rphone.getText().toString();
                final String cpass = rcpass.getText().toString();
                String email = remail.getText().toString();
                String pass = rpassword.getText().toString();
                if(email.isEmpty())
                {
                    remail.setError("Provide an email");
                    remail.requestFocus();
                }
                else if(pass.isEmpty())
                {
                    rpassword.setError("Provide an password");
                    rpassword.requestFocus();
                }
                else if(name.isEmpty())
                {
                    rname.setError("Provide an name");
                    rname.requestFocus();
                }
                else if(phone.isEmpty())
                {
                    rphone.setError("Provide an number");
                    rphone.requestFocus();
                }
                else if(cpass.isEmpty())
                {
                    rcpass.setError("Confirm Password");
                    rcpass.requestFocus();
                }
                else if(!(email.isEmpty() && pass.isEmpty()) && cpass == pass)
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(registerNew.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(registerNew.this,"Register Unsuccessful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            userId = mFirebaseAuth.getUid();
                            DocumentReference docref = fstore.collection("users").document(userId);
                            Map <String,Object> user = new HashMap<>();
                            user.put("Name",name);
                            user.put("phone",phone);
                            docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(registerNew.this,"Info set",Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(new Intent(registerNew.this,Home.class));
                            overridePendingTransition(R.anim.sile_in_right,R.anim.slide_out_left);
                        }
                        }
                    });
                }
            }
        });
    }
    public void openSignIn(View view)
    {
        Intent intent = new Intent(registerNew.this, signIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.sile_in_right,R.anim.slide_out_left);

    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
