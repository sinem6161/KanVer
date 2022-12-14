package com.example.kanver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText userName, userAddress;
    private CircleImageView userProfileImage;

    private String currentUserId;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    private void Init(){
        updateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        userName = (EditText) findViewById(R.id.set_user_name);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);
        userAddress = (EditText) findViewById(R.id.set_user_address);

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Init();

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setUserName = userName.getText().toString();
                String setUserAddress = userAddress.getText().toString();

                if(TextUtils.isEmpty(setUserName)){
                    Toast.makeText(SettingsActivity.this, "Kullanıcı adı boş bırakılamaz!", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(setUserAddress)){
                    Toast.makeText(SettingsActivity.this, "Adres boş bırakılamaz!", Toast.LENGTH_LONG).show();
                }
                else{
                    HashMap<String, String> profileMap = new HashMap<>();
                        profileMap.put("uid", currentUserId);
                        profileMap.put("name", setUserName);
                        profileMap.put("adres", setUserAddress);
                    rootRef.child("Users").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                                Toast.makeText(SettingsActivity.this, "Profil başarılı bir şekilde güncellendi!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SettingsActivity.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("name") && snapshot.hasChild("image")){
                    String retrieveUsername = snapshot.child("name").getValue().toString();
                    String retrieveAddress = snapshot.child("address").getValue().toString();
                    String retrieveImage = snapshot.child("image").getValue().toString();

                    userName.setText(retrieveUsername);
                    userAddress.setText(retrieveAddress);

                }
                else if(snapshot.exists() && snapshot.hasChild("name")){
                    String retrieveUsername = snapshot.child("name").getValue().toString();
                    String retrieveAddress = snapshot.child("address").getValue().toString();

                    userName.setText(retrieveUsername);
                    userAddress.setText(retrieveAddress);
                }
                else{
                    Toast.makeText(SettingsActivity.this, "Lütfen profilizi güncelleyin", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}