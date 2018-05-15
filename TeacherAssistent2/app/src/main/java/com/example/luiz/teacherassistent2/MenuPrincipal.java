package com.example.luiz.teacherassistent2;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = openOrCreateDatabase("TeacherAssistent", Context.MODE_PRIVATE,null);
        /*setContentView(R.layout.layout_menu_principal);
        Button buttonAluno = (Button) findViewById(R.id.aluno);
        buttonAluno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(MenuPrincipal.this, LoginAluno.class);
                startActivity(it);
            }
        });*/
    }
}