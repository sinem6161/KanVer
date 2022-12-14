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
import com.example.kanver.Model.Chat;
import com.example.kanver.Model.Kullanici;
import com.example.kanver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int Mesaj_sag=0;
    public static final int Mesaj_sol=1;

    private Context mcontext;
    private List<Chat> mKullanicilar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ChatAdapter(Context mcontext, List<Chat> mKullanicilar){
        this.mcontext=mcontext;
        this.mKullanicilar=mKullanicilar;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Mesaj_sag){
            View view =LayoutInflater.from(mcontext).inflate(R.layout.sag, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
        else{
            View view =LayoutInflater.from(mcontext).inflate(R.layout.sol, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chat chat = mKullanicilar.get(position);
        holder.mesaj.setText(chat.getMesaj());

    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImage, mesajGonder;
        public TextView mesaj, goruldu, tarih;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mesaj = itemView.findViewById(R.id.mesaj);
            goruldu = itemView.findViewById(R.id.goruldu);
            tarih = itemView.findViewById(R.id.tarih);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mKullanicilar.get(position).getGonderen().equals((user.getUid()))){
            return Mesaj_sag;
        }
        else{
            return Mesaj_sol;
        }

    }
}
