package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.UtilisateurAdapter;
import com.example.database.DatabaseHelper;
import com.example.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private EditText etNom;
    private EditText etAge;
    private Button btnAjouter;
    private Button btnLister;
    private Button btnVider;
    private ListView lvUtilisateurs;
    private TextView tvEmptyList;

    private DatabaseHelper databaseHelper;
    private UtilisateurAdapter adapter;
    private List<Utilisateur> listeUtilisateurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de la base de données SQLite
        databaseHelper = new DatabaseHelper(this);

        // Liaison des vues
        etNom = findViewById(R.id.etNom);
        etAge = findViewById(R.id.etAge);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnLister = findViewById(R.id.btnLister);
        btnVider = findViewById(R.id.btnVider);
        lvUtilisateurs = findViewById(R.id.lvUtilisateurs);
        tvEmptyList = findViewById(R.id.tvEmptyList);

        listeUtilisateurs = new ArrayList<>();

        // Bouton Ajouter
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterUtilisateur();
            }
        });

        // Bouton Lister
        btnLister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listerUtilisateurs(true);
            }
        });

        // Bouton Vider
        btnVider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmerViderBase();
            }
        });

        // Chargement initial des données
        listerUtilisateurs(false);
    }

    private void ajouterUtilisateur() {
        String nom = etNom.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        if (nom.isEmpty()) {
            etNom.setError("Veuillez saisir un nom");
            etNom.requestFocus();
            return;
        }

        if (ageStr.isEmpty()) {
            etAge.setError("Veuillez saisir un âge");
            etAge.requestFocus();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 120) {
                etAge.setError("Âge invalide (1 - 120)");
                etAge.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etAge.setError("Âge numérique invalide");
            etAge.requestFocus();
            return;
        }

        // Insertion dans SQLite
        boolean succes = databaseHelper.ajouterUtilisateur(nom, age);

        if (succes) {
            Toast.makeText(this, "Utilisateur « " + nom + " » ajouté !", Toast.LENGTH_SHORT).show();
            // Réinitialiser les champs
            etNom.setText("");
            etAge.setText("");
            etNom.clearFocus();
            etAge.clearFocus();

            cacherClavier();

            // Mettre à jour l'affichage de la liste
            listerUtilisateurs(false);
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }

    private void listerUtilisateurs(boolean afficherMessage) {
        listeUtilisateurs = databaseHelper.getTousLesUtilisateurs();

        if (listeUtilisateurs.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            lvUtilisateurs.setVisibility(View.GONE);
            if (afficherMessage) {
                Toast.makeText(this, "Aucun utilisateur dans la base SQLite.", Toast.LENGTH_SHORT).show();
            }
        } else {
            tvEmptyList.setVisibility(View.GONE);
            lvUtilisateurs.setVisibility(View.VISIBLE);

            adapter = new UtilisateurAdapter(this, listeUtilisateurs, new UtilisateurAdapter.OnDeleteClickListener() {
                @Override
                public void onDeleteClick(Utilisateur utilisateur) {
                    confirmerSuppression(utilisateur);
                }
            });

            lvUtilisateurs.setAdapter(adapter);

            if (afficherMessage) {
                Toast.makeText(this, listeUtilisateurs.size() + " utilisateur(s) chargé(s).", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmerSuppression(final Utilisateur utilisateur) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Supprimer « " + utilisateur.getNom() + " » de la base de données ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    boolean supprime = databaseHelper.supprimerUtilisateur(utilisateur.getId());
                    if (supprime) {
                        Toast.makeText(MainActivity.this, "Utilisateur supprimé", Toast.LENGTH_SHORT).show();
                        listerUtilisateurs(false);
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void confirmerViderBase() {
        if (listeUtilisateurs.isEmpty()) {
            Toast.makeText(this, "La liste est déjà vide", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Vider la base")
                .setMessage("Voulez-vous supprimer tous les utilisateurs enregistrés ?")
                .setPositiveButton("Vider", (dialog, which) -> {
                    databaseHelper.viderTable();
                    Toast.makeText(MainActivity.this, "Base de données vidée", Toast.LENGTH_SHORT).show();
                    listerUtilisateurs(false);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void cacherClavier() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}
