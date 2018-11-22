package com.example.luiz.teacherassistent.Interface.ForumUsuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;

/**
 * Created by Chico on 28/02/2018.
 */

public class ForumProfessor extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextParse.setContext(getApplicationContext());
        setContentView(R.layout.layout_erro_page);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ForumProfessor.this, MenuProfessor.class);
        startActivity(intent);
    }
}
