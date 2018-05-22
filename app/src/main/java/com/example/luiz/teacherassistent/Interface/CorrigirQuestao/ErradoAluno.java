package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;

public class ErradoAluno extends AppCompatActivity{
    private FloatingActionButton acao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_erro);
        acao = (FloatingActionButton) findViewById(R.id.ConcluirProcesso2);
        acao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ErradoAluno.this, MenuAluno);
            }
        });
    }
}
