package com.example.luiz.teacherassistent2;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by Chico on 01/03/2018.
 */

public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;
    Image foto;
    String email;
    String senha;
    String nome;
    String login;

    public Usuario() {
        getFoto();
        getEmail();
        getSenha();
        getNome();
        getLogin();
    }
    public Usuario(Image foto, String email, String senha, String nome, String login){
        setFoto(foto);
        setEmail(email);
        setSenha(senha);
        setNome(nome);
        setLogin(login);
        
    }

    public void setFoto(Image foto) {
        this.foto = foto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Image getFoto() {
        return foto;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }
}
