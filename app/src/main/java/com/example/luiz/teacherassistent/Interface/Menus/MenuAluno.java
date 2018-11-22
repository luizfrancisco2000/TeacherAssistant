package com.example.luiz.teacherassistent.Interface.Menus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Helper.ConnectionTest;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.TelaInstrucaoAluno;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Chico on 28/02/2018.
 */

public class MenuAluno extends AppCompatActivity{
    private Button corrigirQuestoes;
    private Button forum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_aluno);
        if(ConnectionTest.isOnline()){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Atenção").setMessage("Dispositivc desconectado\n Deseja encerrar?");
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Toast.makeText(MenuPrincipal.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
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
        ConfiguracaoDataBase.getAuth().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            }
        });
        corrigirQuestoes = (Button) findViewById(R.id.butCorrigirquest);
        forum = (Button) findViewById(R.id.butonForumAluno);
        corrigirQuestoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAluno.this,TelaInstrucaoAluno.class);
                startActivity(intent);
            }
        });
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAluno.this, ForumAluno.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Atenção").setMessage("Deseja finalizar sessão?");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MenuAluno.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
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
}
