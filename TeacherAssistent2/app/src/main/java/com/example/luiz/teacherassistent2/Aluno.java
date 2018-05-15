package com.example.luiz.teacherassistent2;

import android.media.Image;

/**
 * Created by Chico on 28/02/2018.
 */

public class Aluno extends Usuario {
    int serie;
    public Aluno(Image foto, String email, String senha, String nome, String login, int serie){
        new Usuario(foto,email,senha,nome,login);
        setSerie(serie);
    }

    public Aluno() {
        new Usuario();
         getSerie();
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getSerie() {
        return serie;
    }
}
