package com.example.kanver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtUsername, txtEmail, txtCity, txtPassword;
    private TextView txtHaveAccount;
    private Button btnRegister;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;


    public void init(){
        txtUsername = (EditText) findViewById(R.id.inputUsername);
        txtEmail = (EditText) findViewById(R.id.inputEmail);
        txtCity = (EditText) findViewById(R.id.inputCity);
        txtPassword = (EditText) findViewById(R.id.inputPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtHaveAccount = findViewById(R.id.alreadyHaveAccount);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        //rootReference = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();
            }
        });
    }

    private void registerUser() {
        String username=txtUsername.getText().toString();
        String email=txtEmail.getText().toString();
        String city=txtCity.getText().toString();
        String password=txtPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Kullanıcı adı boş olamaz!",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"E-mail boş olamaz!",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Şehir boş olamaz!",Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Şifre boş olamaz!",Toast.LENGTH_LONG).show();
        } else{
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String ad=txtUsername.getText().toString();
                        String mail=txtEmail.getText().toString();
                        String sehir=txtCity.getText().toString().toLowerCase();
                        String parola=txtPassword.getText().toString();

                        HashMap<String, Object> hashMap = new HashMap();
                        hashMap.put("Id", auth.getUid());
                        hashMap.put("ad", ad);
                        hashMap.put("email", mail);
                        hashMap.put("sehir", sehir);
                        hashMap.put("parola", parola);
                        hashMap.put("adres","");
                        hashMap.put("kanGrubu", "");

                        String currentUserID = auth.getUid();
                        firestore.collection("Users").document(currentUserID).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                Toast.makeText(RegisterActivity.this, "Kayıt işlemi başarılı!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Kayıt işlemi başarısız!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "İşlem başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}