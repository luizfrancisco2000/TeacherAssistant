package com.example.luiz.teacherassistent.Controle;

import android.net.Uri;

/**
 * Created by Chico on 01/03/2018.
 */

public class Usuario {
    Uri foto;
    String email;
    String senha;
    String nome;
    String id;
    String login;
    int userType;
    public Usuario() {
    }
    public String getId(){
        return id;
    }
    
    public void setId(String id){
        this.id=id;
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

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
