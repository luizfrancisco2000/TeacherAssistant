package com.example.luiz.teacherassistent2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.*;
import android.widget.TextView;

public class LoginAluno extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_aluno);
        Button buttonConfirmar = (Button) findViewById(R.id.confirmar);
        Button buttonCancelar = (Button) findViewById(R.id.cancelar);
        TextView cadastrar = (TextView) findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(LoginAluno.this, CadastroAluno.class);
                startActivity(it);
            }
        });
    }
    public void enviarDados(View view){

    }

    public void postHttp(String login, String senha){
    }
}
