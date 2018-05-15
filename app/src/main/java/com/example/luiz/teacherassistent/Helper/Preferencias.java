package com.example.luiz.teacherassistent.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Controle.Professor;

/**
 * Created by Chico on 20/03/2018.
 */

public class Preferencias {
    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "teacherassistent.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_INDENTIFICADOR = "indetificarUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    public Preferencias(Context context){
        this.context=context;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }
    public void salvarPreferencias(String email, String nome){
        editor.putString(CHAVE_INDENTIFICADOR,email);
        editor.putString(CHAVE_NOME,nome);
        editor.commit();
    }
    public String getIndentificador(){
        return preferences.getString(CHAVE_INDENTIFICADOR,null);
    }
    public String getNome(){
        return preferences.getString(CHAVE_NOME,null);
    }
}
