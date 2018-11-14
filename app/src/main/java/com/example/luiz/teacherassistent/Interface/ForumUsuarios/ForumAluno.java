package com.example.luiz.teacherassistent.Interface.ForumUsuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.CorretoAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;

/**
 * Created by Chico on 28/02/2018.
 */

public class ForumAluno extends AppCompatActivity{
    private FloatingActionButton continuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_erro_page);
        continuar = findViewById(R.id.voltar);
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForumAluno.this, MenuAluno.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ForumAluno.this, MenuAluno.class);
        startActivity(intent);
    }
}
