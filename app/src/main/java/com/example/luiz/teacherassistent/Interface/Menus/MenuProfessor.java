package com.example.luiz.teacherassistent.Interface.Menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.luiz.teacherassistent.Interface.CadastroQuestao.InstrucaoCadastro;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.TelaInstrucaoProfessor;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumProfessor;
import com.example.luiz.teacherassistent.R;

/**
 * Created by Chico on 28/02/2018.
 */

public class MenuProfessor extends AppCompatActivity {
    //botoes
    private Button forumProfessor;
    private Button correcaoProfessor;
    private Button cadastrarQuestao;
    private Button listaDeExercicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_professor);
        forumProfessor = (Button) findViewById(R.id.forumProfessor);
        correcaoProfessor = (Button) findViewById(R.id.corrigirQuestaoProfessor);
        cadastrarQuestao = (Button) findViewById(R.id.cadastrarQuestaoProfessor);
        listaDeExercicio = (Button) findViewById(R.id.exercicioProfessor);
        forumProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuProfessor.this, ForumProfessor.class);
                startActivity(intent);
            }
        });
        correcaoProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuProfessor.this, TelaInstrucaoProfessor.class);
                startActivity(intent);
            }
        });
        cadastrarQuestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuProfessor.this, InstrucaoCadastro.class);
                startActivity(intent);
            }
        });
        listaDeExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
    }
}
