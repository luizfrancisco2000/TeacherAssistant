package com.example.luiz.teacherassistent.Interface.Menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginProfessor;
import com.example.luiz.teacherassistent.R;


public class MenuPrincipal extends AppCompatActivity {
    private Button buttonAluno;
    private Button buttonProfessor;
    Professor professor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_principal);
        buttonAluno = (Button) findViewById(R.id.aluno);
        buttonProfessor=(Button) findViewById(R.id.Professor);
        buttonAluno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this,MenuAluno.class);
                startActivity(it);
            }
        });
        buttonProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuPrincipal.this, LoginProfessor.class);
                startActivity(it);
            }
        });
    }
}