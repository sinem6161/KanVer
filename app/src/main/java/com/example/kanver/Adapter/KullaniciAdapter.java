package com.example.kanver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanver.MessageActivity;
import com.example.kanver.Model.Kullanici;
import com.example.kanver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Kullanici> mKullanicilar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public KullaniciAdapter(Context mcontext, ArrayList<Kullanici> mKullanicilar){
        this.mcontext=mcontext;
        this.mKullanicilar=mKullanicilar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.kan_kullanici_ogesi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KullaniciAdapter.ViewHolder holder, int position) {
        final Kullanici kullanici = mKullanicilar.get(position);
        holder.ad.setText(kullanici.getAd());

        if(kullanici.getId().equals(user.getUid())){
            holder.ad.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.GONE);
            holder.mesajGonder.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.mesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userId",kullanici.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImage, mesajGonder;
        public TextView ad;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.resim_kullanici);
            ad = itemView.findViewById(R.id.kullaniciAdi);
            mesajGonder = itemView.findViewById(R.id.mesajGonderimi);
        }
    }
}
