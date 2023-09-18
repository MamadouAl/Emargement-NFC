package com.example.emargeleocarte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDonnee extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "etudiants.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "etudiants";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_NUMERO_ETUDIANT = "numero_etudiant";
    private static final String COLUMN_IDENTIFIANT_NFC = "identifiant_nfc";

    //Requete de la création de la table
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOM + " TEXT, " +
            COLUMN_PRENOM + " TEXT, " +
            COLUMN_NUMERO_ETUDIANT + " INTEGER, " +
            COLUMN_IDENTIFIANT_NFC + " TEXT);";

    public BaseDonnee (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // si une nouvelle version de la base de données est disponible
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_IDENTIFIANT_NFC + " TEXT");
        }
    }

    /**
     * Methode d'insertion d'un etudiant dans la bd
     * @param nom
     * @param prenom
     * @param numeroEtudiant
     * @param identifiantNfc
     * @return renvoie true si insertion effectuée avec succès, false sinon
     */
    public boolean insertEtudiants(String nom, String prenom, String numeroEtudiant, String identifiantNfc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOM, nom);
        contentValues.put(COLUMN_PRENOM, prenom);
        contentValues.put(COLUMN_NUMERO_ETUDIANT, numeroEtudiant);
        contentValues.put(COLUMN_IDENTIFIANT_NFC, identifiantNfc);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    /**
     * Recupère tous les etudiants de la bd
     * @return
     */
    public Cursor getAllEtudiants() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    /* Getters des attributs */
    public static String getNom(){
        return COLUMN_NOM;
    }

    public static String getPrenom(){
        return COLUMN_PRENOM;
    }

    public static String getIne(){
        return COLUMN_NUMERO_ETUDIANT;
    }
    public static String getIdNfc(){
        return COLUMN_IDENTIFIANT_NFC;
    }

}
