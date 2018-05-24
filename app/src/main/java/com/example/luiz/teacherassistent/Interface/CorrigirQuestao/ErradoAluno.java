package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;

public class ErradoAluno extends AppCompatActivity{
    private FloatingActionButton acao;
    private Button forumAluno;
    private TextView erro;
    Correcao correcao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_erro);
        acao = (FloatingActionButton) findViewById(R.id.ConcluirCorrecao2);
        forumAluno = (Button) findViewById(R.id.forum12);
        erro= (TextView) findViewById(R.id.erroAluno);
        correcao = Correcao.getInstance();
        erro.setText(correcao.getErro());
        erro.setTextColor(Color.WHITE);
        acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ErradoAluno.this, MenuAluno.class);
                startActivity(intent);
            }
        });
        forumAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ErradoAluno.this, ForumAluno.class);
                startActivity(intent);
            }
        });
    }
}
