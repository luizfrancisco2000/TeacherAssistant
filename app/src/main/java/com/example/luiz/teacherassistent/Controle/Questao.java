package com.example.luiz.teacherassistent.Controle;

import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chico on 28/02/2018.
 */

public class Questao {
    String materia;
    String enunciado;
    String codigo;
    ArrayList<String> resolucao;
    DatabaseReference mRef;
    public Questao() {
    }
    public void salvar(){
        mRef = ConfiguracaoDataBase.getFirebase();
        mRef.child("questao").child(String.valueOf(materia+codigo)).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapQuestao = new HashMap<>();
        hashMapQuestao.put("codigo",getCodigo());
        hashMapQuestao.put("enunciado", getEnunciado());
        hashMapQuestao.put("materia", getMateria());
        hashMapQuestao.put("resolucao", getResolucao());
        return hashMapQuestao;
    }
    public void buscar() {
        ConfiguracaoDataBase.getFirebase().child(getCodigo());
        ValueEventListener post = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Questao questao = dataSnapshot.getValue(Questao.class);
                questao.exportResolucao();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mRef.addValueEventListener(post);
    }
        public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setResolucao(ArrayList<String> resolucao) {
        this.resolucao = resolucao;
    }
    public void convertStringForArray(String correta){
        int auxiliar;
        for(int i = 0; i<correta.length();i++){
            int j=0;
            auxiliar=0;
            if(correta.charAt(i)=='\n'){
                resolucao.add(correta.subSequence(auxiliar,i).toString());
                j++;
                auxiliar=i+1;
            }
        }
    }
    public String getMateria() {
        return materia;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList<String> getResolucao() {
        return resolucao;
    }

    public void exportResolucao(){
        Correcao correcao  = new Correcao();
        correcao.setResolucaoCorreta(getResolucao());
    }
}
