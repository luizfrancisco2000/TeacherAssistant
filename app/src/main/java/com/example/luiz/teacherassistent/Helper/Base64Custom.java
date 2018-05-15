package com.example.luiz.teacherassistent.Helper;

import android.util.Base64;

/**
 * Created by Chico on 20/03/2018.
 */

public class Base64Custom {
    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }
    public static String decodificarBase64(String texto){
        return new String(Base64.decode(texto,Base64.DEFAULT));
    }
}


