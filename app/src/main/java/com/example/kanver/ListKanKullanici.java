package com.example.kanver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanver.Adapter.ChatAdapter;
import com.example.kanver.Adapter.KanKullaniciAdapter;
import com.example.kanver.Adapter.KullaniciAdapter;
import com.example.kanver.Model.Chat;
import com.example.kanver.Model.KanKullanici;
import com.example.kanver.Model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListKanKullanici extends AppCompatActivity {

    private ImageButton geriTusu;
    private EditText textArama;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private KanKullaniciAdapter kanKullaniciAdapter;
    private List<Kullanici> mKullanicilar = new ArrayList<Kullanici>();
    private ImageView aramaButon;

    private void init(){
        textArama = findViewById(R.id.textArama);
        aramaButon=findViewById(R.id.aramaButon);
        geriTusu = findViewById(R.id.geriTusuKan);

        recyclerView = findViewById(R.id.recyclerKanKullanici);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        kanKullaniciAdapter = new KanKullaniciAdapter(ListKanKullanici.this,mKullanicilar);
        recyclerView.setAdapter(kanKullaniciAdapter);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kan_kullanici);

        init();

        kullaniciListele();

        //search
        aramaButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textArama.setHint("");
                textArama.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        Arama(s.toString().toLowerCase());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });


        geriDon();

    }

    private void geriDon(){
        geriTusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListKanKullanici.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Arama(String s){
        db.collection("Users").orderBy("sehir").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mKullanicilar.clear();

                if(error != null){
                    Toast.makeText(ListKanKullanici.this, "Hata", Toast.LENGTH_SHORT).show();
                }else {
                    if(value!=null){
                        if(!value.isEmpty()){
                            for(DocumentChange dc : value.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    mKullanicilar.add(dc.getDocument().toObject(Kullanici.class));

                                }
                                kanKullaniciAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
    }
    private void kullaniciListele(){

        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        mKullanicilar.add(dc.getDocument().toObject(Kullanici.class));

                    }
                    kanKullaniciAdapter.notifyDataSetChanged();
                }
            }
        });
//orderBy("sehir", com.google.firebase.firestore.Query.Direction.ASCENDING)
    }
}