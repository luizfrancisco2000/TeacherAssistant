package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;


public class CorretoAluno extends AppCompatActivity {
    private FloatingActionButton acao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_acerto);
        acao = (FloatingActionButton) findViewById(R.id.ConcluirCorrecao1);
        acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CorretoAluno.this, MenuAluno.class);
                startActivity(intent);
            }
        });
    }
}
