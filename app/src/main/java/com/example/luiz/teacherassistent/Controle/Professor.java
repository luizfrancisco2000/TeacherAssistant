package com.example.luiz.teacherassistent.Controle;

import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chico on 28/02/2018.
 */

public class Professor extends Usuario {
    String materia;
    boolean ativo;
    public Professor(){
    }
    public void salvar(){
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("professor").child(String.valueOf(getId())).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> hashMapProfessor = new HashMap<>();
        hashMapProfessor.put("id", getId());
        hashMapProfessor.put("nome", getNome());
        hashMapProfessor.put("disciplina", getMateria());
        hashMapProfessor.put("ativo", getAtivo());
        hashMapProfessor.put("email", getEmail());
        hashMapProfessor.put("senha", getSenha());
        hashMapProfessor.put("login", getLogin());
        hashMapProfessor.put("userType", getUserType());
        return  hashMapProfessor;
    }
    public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getMateria() {
        return materia;
    }

    public boolean getAtivo() {
        return ativo;
    }
}
