package com.decode.disastershield;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LOGIN extends AppCompatActivity {
    private EditText EmailAdd , Password ;
    private FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailAdd = (EditText) findViewById(R.id.EmailAddress);
        Password = (EditText) findViewById(R.id.Password);

        auth = FirebaseAuth.getInstance();

        Button b = findViewById(R.id.Rescued);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EMAIL = EmailAdd.getText().toString();
                String PASS = Password.getText().toString();

                if (TextUtils.isEmpty(EMAIL)) {
                    Toast.makeText(LOGIN.this, "Re-Enter the Email", Toast.LENGTH_LONG).show();
                    EmailAdd.requestFocus();
                    EmailAdd.setError("Email Can't be empty");
                }
                else if (TextUtils.isEmpty(PASS)) {
                    Toast.makeText(LOGIN.this,"Re-Enter the Password",Toast.LENGTH_LONG).show();
                    Password.requestFocus();
                    Password.setError("Password Can't be empty");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence) EMAIL).matches()) {
                    Toast.makeText(LOGIN.this,"Re-Enter the Email",Toast.LENGTH_LONG).show();
                    EmailAdd.requestFocus();
                    EmailAdd.setError("Enter a Valid Email");
                }
                else{
                    loginusr(EMAIL,PASS);
                }
            }
        });
    }

    private void loginusr(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LOGIN.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LOGIN.this,"Log in unsuccessful",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(LOGIN.this, com.decode.disastershield.View.class);
                startActivity(intent);
            }
        });
    }
}