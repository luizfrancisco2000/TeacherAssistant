package com.example.luiz.teacherassistent.Controle;

import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chico on 28/02/2018.
 */

public class Aluno extends Usuario {
    int serie;
    public Aluno() {
    }
    public void salvar(){
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("aluno").child(String.valueOf(getId())).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> hashMapAluno = new HashMap<>();
        hashMapAluno.put("id", getId());
        hashMapAluno.put("nome", getNome());
        hashMapAluno.put("serie", getSerie());
        hashMapAluno.put("email", getEmail());
        hashMapAluno.put("senha", getSenha());
        hashMapAluno.put("login", getLogin());
        hashMapAluno.put("userType", getUserType());
        return  hashMapAluno;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getSerie() {
        return serie;
    }

}
