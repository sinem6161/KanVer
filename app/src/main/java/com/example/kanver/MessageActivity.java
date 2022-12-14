package com.example.kanver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanver.Adapter.ChatAdapter;
import com.example.kanver.Adapter.KullaniciAdapter;
import com.example.kanver.Model.Chat;
import com.example.kanver.Model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private ImageButton geriTusu;
    private CircleImageView profilResim;
    private TextView kullaniciAdi;
    private EditText mesajGirdi;
    private ImageView fotoEkle, gonder;
    private Intent intent;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> mMesaj = new ArrayList<>();

    private void init(){
        geriTusu = findViewById(R.id.geriTusu);
        profilResim = findViewById(R.id.profilResim);
        kullaniciAdi = findViewById(R.id.kullaniciAdMesaj);
        mesajGirdi = findViewById(R.id.mesajGirisAlani);
        fotoEkle = findViewById(R.id.fotoEkle);
        gonder = findViewById(R.id.gonderBtn);

        recyclerView = findViewById(R.id.recyclerMesaj);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        chatAdapter = new ChatAdapter(getApplicationContext(),mMesaj);
        recyclerView.setAdapter(chatAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();

        kullaniciBilgisiAl();
    }

    private void kullaniciBilgisiAl(){
        intent = getIntent();
        final String userId = intent.getStringExtra("userId");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici =snapshot.getValue(Kullanici.class);
                kullaniciAdi.setText(kullanici.getAd());

                mesajOku(firebaseUser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mesajGirdisi = mesajGirdi.getText().toString();

                if(!mesajGirdisi.equals("")){
                    mesajGonder(firebaseUser.getUid(), userId, mesajGirdisi);
                    mesajAl(firebaseUser.getUid(), userId, mesajGirdisi);
                }else{
                    Toast.makeText(MessageActivity.this, "Mesaj Kısmı Boş Olamaz", Toast.LENGTH_LONG).show();
                }

                mesajGirdi.setText("");
            }
        });
    }

    private void mesajGonder(String gonderen, String alici, String mesaj){
        HashMap<String, Object> hashMap =new HashMap();
        hashMap.put("Gonderen:", gonderen);
        hashMap.put("Alıcı:", alici);
        hashMap.put("Mesaj:", mesaj);
        hashMap.put("Resim", "");

        FirebaseDatabase.getInstance().getReference().child("Mesajlar").child(firebaseUser.getUid()).push().setValue(hashMap);
    }

    private void mesajAl(String gonderen, String alici, String mesaj){
        HashMap<String, Object> hashMap =new HashMap();
        hashMap.put("Gonderen:", gonderen);
        hashMap.put("Alıcı:", alici);
        hashMap.put("Mesaj:", mesaj);
        hashMap.put("Resim", "");

        FirebaseDatabase.getInstance().getReference().child("Mesajlar").child(alici).push().setValue(hashMap);
    }

    private void mesajOku(String gonderenId, String aliciId){
        FirebaseDatabase.getInstance().getReference("Mesajlar").child(firebaseUser.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMesaj.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){

                            Chat chat = snapshot1.getValue(Chat.class);

                            if(chat.getAlici().equals(gonderenId) && chat.getGonderen().equals(aliciId) ||
                                chat.getAlici().equals(aliciId) && chat.getGonderen().equals(gonderenId)){

                                mMesaj.add(chat);
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}