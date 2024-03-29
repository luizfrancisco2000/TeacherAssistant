package com.example.luiz.teacherassistent.Controle;

import android.util.Log;

import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chico on 28/02/2018.
 */

public class Questao {
    private String materia;
    private String enunciado;
    private int codigo;
    private ArrayList<ArrayList<String>> resolucao;
    private String assunto;
    private static Questao getInstance;
    private Correcao correcao;

    public Questao() {
    }

    public void salvar() {
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(getMateria())).child(String.valueOf(getAssunto())).child(String.valueOf(codigo)).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMapQuestao = new HashMap<>();
        hashMapQuestao.put("materia", getMateria());
        hashMapQuestao.put("codigo", getCodigo());
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

    public void convertStringForArray(String correta) {
        int auxiliar = 0;
        String texto = "";
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < correta.length(); i++) {
            if (correta.charAt(i) == '\\') {
                if (correta.charAt(i + 1) == '\\') {
                    texto = correta.substring(auxiliar, i);
                    Log.d("Teste", texto);
                    auxiliar = i + 2;
                    res.add(texto);
                }
                i++;
            }
        }
        texto = correta.substring(auxiliar,correta.length()-1);
        Log.d("Teste1: ", texto);
        res.add(texto);
        if(resolucao==null){
            resolucao = new ArrayList<>();
        }
        resolucao.add(res);
    }

    public String getMateria() {
        return materia;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public ArrayList<ArrayList<String>> getResolucao() {
        return resolucao;
    }

    public Correcao exportResolucao() {
        correcao = new Correcao();
        correcao.setResolucaoCorreta(getResolucao());
        return correcao;
    }

    public static Questao getInstance() {
        if (getInstance == null) {
            getInstance = new Questao();
        }
        return getInstance;
    }

    public static void setInstance(Questao questao) {
        getInstance = questao;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
}
