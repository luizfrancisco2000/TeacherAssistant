package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.luiz.teacherassistent.Interface.CadastroQuestao.CadastrarEnunciado;
import com.example.luiz.teacherassistent.R;

public class TelaInstrucaoProfessor extends AppCompatActivity {
    private FloatingActionButton continuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_instrucoes);
        continuar = (FloatingActionButton) findViewById(R.id.ContinuarFuncao);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaInstrucaoProfessor.this, CadastrarEnunciado.class);
                startActivity(intent);
            }
        });
    }
}
