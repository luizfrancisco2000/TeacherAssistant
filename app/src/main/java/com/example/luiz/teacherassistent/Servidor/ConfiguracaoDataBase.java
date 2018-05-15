package com.example.luiz.teacherassistent.Servidor;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Chico on 19/03/2018.
 */

public class ConfiguracaoDataBase {
    private static DatabaseReference referencia;
    private static FirebaseAuth auth;
    public static DatabaseReference getFirebase(){
        if(referencia == null){
            referencia = FirebaseDatabase.getInstance().getReference();

        }
        return referencia;
    }
    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
