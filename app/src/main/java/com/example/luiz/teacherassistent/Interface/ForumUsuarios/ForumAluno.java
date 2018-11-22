package com.example.luiz.teacherassistent.Interface.ForumUsuarios;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Helper.ConnectionTest;
import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.CorretoAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuPrincipal;
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
        ContextParse.setContext(getApplicationContext());
        if(ConnectionTest.isOnline()){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Atenção").setMessage("Dispositivc desconectado\n Deseja encerrar?");
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ForumAluno.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
            });
            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alerta.create().show();
        }
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
