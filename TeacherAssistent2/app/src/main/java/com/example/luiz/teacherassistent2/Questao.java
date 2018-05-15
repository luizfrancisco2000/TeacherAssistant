package com.example.luiz.teacherassistent2;

import java.util.ArrayList;

/**
 * Created by Chico on 28/02/2018.
 */

public class Questao {
    String materia;
    String enunciado;
    int codigo;
    ArrayList<String> resolucao;
    public Questao(String materia, String enunciado, int codigo, ArrayList<String> resolucao){
        setMateria(materia);
        setEnunciado(enunciado);
        setCodigo(codigo);
        setResolucao(resolucao);
    }

    public Questao() {
        getMateria();
        getEnunciado();
        getCodigo();
        getResolucao();
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setResolucao(ArrayList<String> resolucao) {
        this.resolucao = resolucao;
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

    public ArrayList<String> getResolucao() {
        return resolucao;
    }
}
