package com.example.emargeleocarte;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class AfficherEtudiantsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_etudiants);

        /* Bouton de retour */
        Button retour = findViewById(R.id.btn_retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView listViewEtudiants = findViewById(R.id.list_view_etudiants);
        BaseDonnee maBase = new BaseDonnee(this);

        // Récupérer les données de la base de données
        Cursor cursor = maBase.getAllEtudiants();

        // Créer une liste d'objets pour stocker les données récupérées
        ArrayList<HashMap<String, String>> etudiantsList = new ArrayList<>();

        // Parcourir le curseur et stocker les données dans la liste
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> etudiant = new HashMap<>();
                etudiant.put("ID", cursor.getString(0));
                etudiant.put("Nom", cursor.getString(1));
                etudiant.put("Prenom", cursor.getString(2));
                etudiant.put("Numero_Etudiant", cursor.getString(3));
                etudiant.put("Identifiant_NFC", cursor.getString(4));
                etudiantsList.add(etudiant);
            } while (cursor.moveToNext());
        }

        // Créer un adaptateur pour lier les données à la listeView
        ListAdapter adapter = new SimpleAdapter(
                AfficherEtudiantsActivity.this,
                        etudiantsList,
                        R.layout.etudiant_list_item,
                        new String[]{ "ID", "Nom", "Prenom", "Numero_Etudiant", "Identifiant_NFC"},
                        new int[]{ R.id.text_view_id, R.id.text_view_nom, R.id.text_view_prenom, R.id.text_view_numero_etudiant, R.id.text_view_identifiant_nfc});


        // Afficher la liste des étudiants inscrits
        listViewEtudiants.setAdapter(adapter);
    }
}

