package com.decode.disastershield;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Act4 extends AppCompatActivity {
private EditText Orgname , email , phone , location , password , confirmpassword ;
public Button b1;

private static final String Tag = "Act4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act4);

        Orgname =  findViewById(R.id.Org);
        email = findViewById(R.id.EmailAddress);
        phone = findViewById(R.id.Phonenumber);
        location = findViewById(R.id.PostalAddress);
        password = findViewById(R.id.Password);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        b1 = findViewById(R.id.Rescued);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Organisation_Name = Orgname.getText().toString();
                String Email = email.getText().toString();
                String Phone = phone.getText().toString();
                String Location = location.getText().toString();
                String Password = password.getText().toString();
                String confirmpass = confirmpassword.getText().toString();

                if (!Password.equals(confirmpass))
                {
                    Toast.makeText(Act4.this,"Password Doesn't match",Toast.LENGTH_LONG).show();
                    password.requestFocus();
                    password.setError("Password Missmatch");
                } else if (TextUtils.isEmpty(Organisation_Name)) {
                    Toast.makeText(Act4.this,"Re-Enter the Oraganisation Name",Toast.LENGTH_LONG).show();
                    Orgname.requestFocus();
                    Orgname.setError("Organisation Name Can't be empty!");
                }
                else if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(Act4.this,"Re-Enter the Email",Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                    phone.setError("Email Can't be empty");
                }
                else if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(Act4.this,"Re-Enter the Phone",Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                    phone.setError("Phone Can't be empty");
                }
                else if (TextUtils.isEmpty(Location)) {
                    Toast.makeText(Act4.this,"Re-Enter the Address",Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                    phone.setError("Address Can't be empty");
                }
                else if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(Act4.this,"Re-Enter the Password",Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                    phone.setError("Password Can't be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence) Email).matches()) {
                    Toast.makeText(Act4.this,"Re-Enter the Email",Toast.LENGTH_LONG).show();
                    email.requestFocus();
                    email.setError("Enter a Valid Email");
                }
                else if (Phone.length() != 10) {
                    Toast.makeText(Act4.this,"Re-Enter the Phone NUmber",Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                    phone.setError("Enter a Valid Phone");
                }
                else {
                    registeruser(Organisation_Name,Email,Phone,Location,Password,confirmpass);
                }
            }
        });
    }

    private void registeruser(String Organisation_Name, String Email, String Phone, String Location, String Password, String confirmpass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final Task<AuthResult> userWithEmailAndPassword = auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(Act4.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Act4.this,"User Registered Successfully",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    ReadWriteuserDetails WriteuserDetails = new ReadWriteuserDetails(Organisation_Name, Phone, Location);

                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered_Organisations");
                    referenceprofile.child(firebaseUser.getUid()).setValue(WriteuserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseUser.sendEmailVerification();
                            if (task.isSuccessful())
                            {


                                Toast.makeText(Act4.this,"Verify Your Email",Toast.LENGTH_LONG).show();

                   Intent intent = new Intent(Act4.this,MainActivity.class);
                    /*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);*/
                    startActivity(intent);
                    finish();
                            }


                        }
                    });


                }
                else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        password.setError("Your Password is too weak");
                        password.requestFocus();
                    }
                    catch (FirebaseAuthEmailException e) {
                        email.setError("Please Enter a Valid Email");
                        email.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e)
                    {
                        email.setError("Organisation Already Registered");
                        email.requestFocus();
                    }
                    catch (Exception e){
                        Log.e(Tag,e.getMessage());
                        Toast.makeText(Act4.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
