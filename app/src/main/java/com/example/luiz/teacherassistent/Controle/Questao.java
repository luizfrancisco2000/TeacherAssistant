package com.example.luiz.teacherassistent.Controle;

import android.util.Log;

import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chico on 28/02/2018.
 */

public class Questao {
    private String materia;
    private String enunciado;
    private String codigo;
    private ArrayList<String> resolucao;
    private static Questao getInstance;

    private Correcao correcao;
    public Questao() {
    }
    public void salvar(){
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(getMateria())).setValue(this);
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapQuestao = new HashMap<>();
        hashMapQuestao.put("materia", getMateria());
        hashMapQuestao.put("enunciado", getEnunciado());
        hashMapQuestao.put("resolucao", getResolucao());
        return hashMapQuestao;
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
        int auxiliar=0;
        ArrayList<String> res = new ArrayList<>();
        for(int i = 0; i<correta.length();i++){
            if(correta.charAt(i)=='\n'){
                String texto = correta.subSequence(auxiliar,i).toString();
                auxiliar=i+1;
                res.add(texto);
                i++;
            }
        }
        resolucao = res;
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

    public Correcao exportResolucao(){
        correcao = new Correcao();
        correcao.setResolucaoCorreta(getResolucao());
        return correcao;
    }
    public static Questao getInstance(){
        if(getInstance==null){
            getInstance = new Questao();
        }
        return getInstance;
    }
    public static void setInstance(Questao questao){
        getInstance = questao;
    }
}
