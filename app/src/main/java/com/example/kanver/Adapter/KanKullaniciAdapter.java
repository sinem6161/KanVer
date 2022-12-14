package com.example.kanver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanver.MessageActivity;
import com.example.kanver.Model.KanKullanici;
import com.example.kanver.Model.Kullanici;
import com.example.kanver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class KanKullaniciAdapter extends RecyclerView.Adapter<KanKullaniciAdapter.ViewHolder> {

    private Context mcontext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public KanKullaniciAdapter(Context mcontext, List<Kullanici> mKullanicilar){
        this.mcontext=mcontext;
        this.mKullanicilar=mKullanicilar;
    }

    @NonNull
    @Override
    public KanKullaniciAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.kullanici_ogesi, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KanKullaniciAdapter.ViewHolder holder, int position) {
        Kullanici kullanici = mKullanicilar.get(position);
        holder.ad.setText(kullanici.getAd());
        holder.kanGrubu.setText(kullanici.getKanGrubu());
        holder.adres.setText(kullanici.getAdres());

        if(kullanici.getId().equals(user.getUid())){
            holder.ad.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.GONE);
            holder.mesajGonder.setVisibility(View.GONE);
            holder.adres.setVisibility(View.GONE);
            holder.kanGrubu.setVisibility(View.GONE);
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
        public TextView ad, kanGrubu, adres;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.kan_resim_kullanici);
            ad = itemView.findViewById(R.id.kanKullaniciAdi);
            mesajGonder = itemView.findViewById(R.id.kanMesajGonderimi);
            adres= itemView.findViewById(R.id.adres);
            kanGrubu = itemView.findViewById(R.id.kanGrubu);
        }
    }
}
