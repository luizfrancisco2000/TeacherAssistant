package com.example.luiz.teacherassistent2;
import android.media.Image;

/**
 * Created by Chico on 28/02/2018.
 */

public class Professor extends Usuario {
    String materia;
    boolean ativo;
    public Professor(Image foto, String email, String senha, String nome, String login, String materia, boolean ativo){
        new Usuario(foto,email,senha,nome,login);
        setMateria(materia);
        setAtivo(ativo);
    }
    public Professor(){
        new Usuario();
        getMateria();
        getAtivo();
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
