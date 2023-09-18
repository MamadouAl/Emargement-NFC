package com.example.emargeleocarte;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

//import androidx.arch.core.executor.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


//import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;




public class MainActivity extends AppCompatActivity {
    private BaseDonnee db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération les boutons
        Button ajoutEtudiant = findViewById(R.id.btn_ajouter_etudiant);
        Button afficherEmargement = findViewById(R.id.btn_afficher_emargement);
        //Button pdf = findViewById(R.id.btn_pdf);

        // Lier les boutons aux activités correspondantes
        ajoutEtudiant.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MonActivity.class);
            startActivity(intent);
        });

        //Affichage de l'emargement des étudiants
        afficherEmargement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AfficherEtudiantsActivity.class);
                startActivity(intent);

            }
        });

        //Generation de pdf
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button monPdf = findViewById(R.id.btn_pdf);
        monPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PdfActivity.class);
                startActivity(intent);
            }
        });


        // Fermer l'application
        Button quitter_appli = findViewById(R.id.quiter_app);
        quitter_appli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Méthode appelée lorsque l'activité MonActivity se termine et retourne un résultat
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifier que le résultat vient bien de MonActivity et est OK
        if (requestCode == MonActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            // Afficher un toast pour indiquer que l'ajout de l'étudiant a été effectué
            Toast.makeText(this, "Etudiant ajouté ", Toast.LENGTH_SHORT).show();
        }
    }
    //
}



