package com.example.emargeleocarte;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MonActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    public static final int REQUEST_CODE = 100;
    private EditText etuNom,    //Nom de l'etudiant
                     etuPrenom, //Prenom de l'etudiant
                     etuINE,    //Numero de l'etudiant
                     etuNfcID; //ID Nfc de l'etudiant
    private BaseDonnee dataBase;
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon);

        // Récupérer les EditText
        etuNom = findViewById(R.id.et_nom);
        etuPrenom = findViewById(R.id.et_prenom);
        etuINE = findViewById(R.id.et_numero_etudiant);
        etuNfcID = findViewById(R.id.et_nfc);


        // Créer une instance de base de donnée
        dataBase = new BaseDonnee(this);

        /* Bouton de retour */
        Button retour = findViewById(R.id.btn_retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Récupération du bouton "Ajouter"
        Button btnAjouter = findViewById(R.id.btn_ajouter_etudiant);
        // Lier le bouton à la méthode d'ajout d'étudiant
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterEtudiant();
            }
        });

        // Initialisation de l'adaptateur NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Vérification de la disponibilité du NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "Le NFC n'est pas disponible sur cet appareil.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Configuration du bouton de scan NFC
        Button scanNfc = findViewById(R.id.btn_nfc);
        scanNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activation de la lecture NFC
                nfcAdapter.enableReaderMode(MonActivity.this, MonActivity.this, NfcAdapter.FLAG_READER_NFC_A, null);
            }
        });

    }

    /**
     * Methode d'ajout d'un etudiant à la base de donnée
     */
    private void ajouterEtudiant() {
        // Récupérer les données saisies par l'utilisateur
        String nom = etuNom.getText().toString();
        String prenom = etuPrenom.getText().toString();
        String numeroEtudiant = etuINE.getText().toString();
        String nfc = etuNfcID.getText().toString();

        // Vérifier si les champs ne sont pas vides
        if(nom.isEmpty() || prenom.isEmpty() || numeroEtudiant.isEmpty() || nfc.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs svp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ajouter l'étudiant à la base de données
        boolean resultat = dataBase.insertEtudiants(nom, prenom, numeroEtudiant, nfc);

        // Vérifier si l'ajout a été effectué avec succès
        if(!resultat) {
            Toast.makeText(this, "Erreur lors de l'ajout de l'étudiant", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Etudiant ajouté avec succès !", Toast.LENGTH_SHORT).show();

            // Vider les champs de saisie après l'ajout
            etuNom.setText("");
            etuPrenom.setText("");
            etuINE.setText("");
            etuNfcID.setText("");
        }
    }

    /**
     * Methode de recuperation de l'ID nfc
     * @param tag
     */
    @Override
    public void onTagDiscovered(Tag tag) {
        // Récupération de l'identifiant NFC
        byte[] idBytes = tag.getId();
        String nfcID = bytesToHex(idBytes);

        // Affichage de l'identifiant dans le champ EditText (une Maj)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etuNfcID.setText(nfcID);
            }
        });

        // Désactivation de la lecture NFC
        nfcAdapter.disableReaderMode(this);
    }

    /**
     * Methode de conversion du tag en hexa décimale
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Methode de deconnexion à la bd
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermer la base de données
        dataBase.close();
    }
}
