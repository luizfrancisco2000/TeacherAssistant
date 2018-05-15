package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.R;

public class CorrigirBuscarResolucaoAluno extends AppCompatActivity{
    private Correcao correcao;
    private EditText resolucaoAluno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        resolucaoAluno = (EditText) findViewById(R.id.ResolucaoEdit);
        correcao.corrigir();
    }
}
