package com.example.emargeleocarte;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfActivity extends AppCompatActivity {

    private BaseDonnee maBase;
    private String monFichier = "etudiantsNFC.pdf";
    private Document document;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        /* Bouton de retour */
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        maBase = new BaseDonnee(this);
        Cursor cursor = maBase.getAllEtudiants();

        // Vérifier si le dossier de stockage externe est disponible et a l'autorisation d'écriture
        if (estDispo()) {
            document = new Document();

            // Ouvrir un nouveau document PDF
            try {
                PdfWriter.getInstance(document, new FileOutputStream(getFichier()));
                document.open();

                // Ajouter un titre
                Paragraph titre = new Paragraph("Liste des étudiants presents à l'examen ");
                titre.setAlignment(Element.ALIGN_CENTER);
                document.add(titre);
                document.add(new Paragraph("\n"));

                // Ajouter un tableau pour les données
                PdfPTable table = new PdfPTable(new float[]{1, 2, 2, 3, 3});
                table.setWidthPercentage(100);
                table.addCell("ID");
                table.addCell("Nom");
                table.addCell("Prénom");
                table.addCell("Numéro étudiant");
                table.addCell("Présence");

                // Parcourir le curseur et ajouter les données au tableau
                if (cursor.moveToFirst()) {
                    do {
                        table.addCell(cursor.getString(0));
                        table.addCell(cursor.getString(1));
                        table.addCell(cursor.getString(2));
                        table.addCell(cursor.getString(3));
                        table.addCell("PRESENT");
                    } while (cursor.moveToNext());
                }

                document.add(table);

            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }
            Toast.makeText(this, "Génération effectuée avec succès :)", Toast.LENGTH_LONG).show();
        }


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvMessage = findViewById(R.id.tv_succes);
        tvMessage.setText("\nPDF généré avec succès : " + monFichier +"\n" +
                "Rendez-vous sur votre espace de stockage(Documents/PDF) pour pouvoir le recupérer \n");
        tvMessage.setVisibility(View.VISIBLE);

    }

    private File getFichier() {
        // Créer un dossier de stockage pour les fichiers PDF s'il n'existe pas
        File repertoire = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS) + "/PDF");
        if (!repertoire.exists()) {
            repertoire.mkdir();
        }

        // Créer un nouveau fichier avec le nom de fichier spécifié
        File fichier = new File(repertoire, monFichier);
        if (fichier.exists()) {
            fichier.delete();
        }
        return fichier;
    }

    // Vérifier si le stockage externe est disponible et a l'autorisation d'écriture
    public boolean estDispo() {
        String statut = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(statut)) {
            return true;
        }
        return false;
    }
}

