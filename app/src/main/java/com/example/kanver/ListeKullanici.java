package com.example.kanver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanver.Adapter.KullaniciAdapter;
import com.example.kanver.Model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListeKullanici extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private ArrayList<Kullanici> mKullanicilar;
    private EditText aramabar;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_liste);

        recyclerView=findViewById(R.id.recyclerListe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mKullanicilar=new ArrayList<>();
        kullaniciAdapter= new KullaniciAdapter(getApplicationContext(),mKullanicilar);
        recyclerView.setAdapter(kullaniciAdapter);
        aramabar = findViewById(R.id.arama_bar);

        kullaniciOku();

        //search
        aramabar.addTextChangedListener(new TextWatcher() {
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

    private void kullaniciOku(){
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(aramabar.getText().toString().equals("")){
                    mKullanicilar.clear();

                    for(DataSnapshot snapshots : snapshot.getChildren()){
                        Kullanici kullanici = snapshots.getValue(Kullanici.class);
                        mKullanicilar.add(kullanici);
                    }
                }

                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Arama(String s){
        Query sorgu = FirebaseDatabase.getInstance().getReference("Users").orderByChild("ad").startAt(s).endAt(s+"\uf8ff");
        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mKullanicilar.clear();

                for(DataSnapshot snapshots : snapshot.getChildren()){
                    Kullanici kullanici = snapshots.getValue(Kullanici.class);
                    mKullanicilar.add(kullanici);
                }

                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
