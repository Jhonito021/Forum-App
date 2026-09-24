package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.R;
import com.example.model.Utilisateur;

import java.util.List;

public class UtilisateurAdapter extends ArrayAdapter<Utilisateur> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Utilisateur utilisateur);
    }

    private final Context context;
    private final List<Utilisateur> utilisateurs;
    private final OnDeleteClickListener deleteClickListener;

    public UtilisateurAdapter(Context context, List<Utilisateur> utilisateurs, OnDeleteClickListener listener) {
        super(context, 0, utilisateurs);
        this.context = context;
        this.utilisateurs = utilisateurs;
        this.deleteClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_utilisateur, parent, false);
        }

        Utilisateur utilisateur = getItem(position);

        TextView tvItemUser = convertView.findViewById(R.id.tvItemUser);
        ImageButton btnDeleteUser = convertView.findViewById(R.id.btnDeleteUser);

        if (utilisateur != null) {
            // Exact format requested: "1 - Soa | 45 ans"
            String displayText = (position + 1) + " - " + utilisateur.getNom() + " | " + utilisateur.getAge() + " ans";
            tvItemUser.setText(displayText);

            btnDeleteUser.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(utilisateur);
                }
            });
        }

        return convertView;
    }
}
