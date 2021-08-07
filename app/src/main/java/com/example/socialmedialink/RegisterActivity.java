package com.example.socialmedialink;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmailet, mPasswordet;
    Button mRegisterbtn;
    TextView mHaveAccountTV;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//act bar and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Create account");

        //create back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mEmailet=findViewById(R.id.emailEt);
        mPasswordet=findViewById(R.id.passwordEt);
        mRegisterbtn=findViewById(R.id.registerBtn);
        mHaveAccountTV=findViewById(R.id.have_accountTv);
        mAuth = FirebaseAuth.getInstance();


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering User....");

        mRegisterbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1nput emall, password

                String email = mEmailet. getText().toString ().trim () ;
                String password = mPasswordet.getText () .toString ().trim ();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                    mEmailet. setError ("Invalid Email") ;
                    mEmailet. setFocusable (true) ;
                }
                else if (password. length () <6)
                {
                    mPasswordet.setError ("Password length at least 6 characters ");
                    mPasswordet.setFocusable (true) ;
                }
                else
                    registerUser (email, password);
            }
        });
        //handle the login text view clicklistener
        mHaveAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            progressDialog.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user name and email id from auth
                            String email = user.getEmail();
                            String uid = user.getUid();

                           /* //when user is registered store userinfo in firebase using hashmap
                            HashMap<Object,String> hashMap= new HashMap<>();
                            //put the info in hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name"," ");
                            hashMap.put("phone"," ");
                            hashMap.put("image"," ");

                            //firebase database instance
                            FirebaseDatabase database= FirebaseDatabase.getInstance();

                            //path to store user data named "Users"
                            DatabaseReference reference= database.getReference("Users");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);*/


                            Toast.makeText(RegisterActivity.this, "Registered..../n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user and dismiss progress dialog.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}