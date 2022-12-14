package com.example.kanver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class KanBilgileri extends AppCompatActivity {

    private Button kaydet;
    private Spinner spinner;
    private EditText adres;
    private ImageButton geriTusu;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanbilgileri);

        spinner = (Spinner) findViewById(R.id.spinnerKanGrubu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kanGrubu_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        kaydet = findViewById(R.id.btnKaydet);
        adres = findViewById(R.id.inputAddress);
        geriTusu = findViewById(R.id.geriTusuKanBilgi);

        auth = FirebaseAuth.getInstance();

        ButonTıkla();

        geriTusuTıkla();
    }

    private void geriTusuTıkla(){
        geriTusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KanBilgileri.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ButonTıkla(){
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerString = null;
                spinnerString = spinner.getSelectedItem().toString();
                int nPos = spinner.getSelectedItemPosition();
                Toast.makeText(KanBilgileri.this, "Seçilen kan grubu:" + spinnerString, Toast.LENGTH_SHORT).show();

                String adress = adres.getText().toString();

                if(TextUtils.isEmpty(adress)){
                    Toast.makeText(KanBilgileri.this,"Adres alanı boş olamaz!",Toast.LENGTH_LONG).show();
                }
                else{
                    HashMap<String, Object> hashMap = new HashMap();
                    hashMap.put("adres", adress);
                    hashMap.put("kanGrubu", spinnerString);

                    FirebaseFirestore.getInstance().collection("Users").document(auth.getUid()).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent mainIntent = new Intent(KanBilgileri.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            Toast.makeText(KanBilgileri.this, "Kaydetme işlemi başarılı!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
