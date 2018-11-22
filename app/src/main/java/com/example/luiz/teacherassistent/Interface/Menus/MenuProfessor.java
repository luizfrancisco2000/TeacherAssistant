package com.example.luiz.teacherassistent.Interface.Menus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Helper.ConnectionTest;
import com.example.luiz.teacherassistent.Interface.CadastroQuestao.InstrucaoCadastro;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.TelaInstrucaoProfessor;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Chico on 28/02/2018.
 */

public class MenuProfessor extends AppCompatActivity {
    //botoes
    private Button forumProfessor;
    private Button correcaoProfessor;
    private Button cadastrarQuestao;
    private FirebaseAuth auth;
    private Button listaDeExercicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_professor);
        forumProfessor = (Button) findViewById(R.id.forumProfessor);
        correcaoProfessor = (Button) findViewById(R.id.corrigirQuestaoProfessor);
        cadastrarQuestao = (Button) findViewById(R.id.cadastrarQuestaoProfessor);
        listaDeExercicio = (Button) findViewById(R.id.exercicioProfessor);
        if(ConnectionTest.isOnline()){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Atenção").setMessage("Dispositivc desconectado\n Deseja encerrar?");
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MenuProfessor.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(MenuProfessor.this, ForumProfessor.class);
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
                    auth = ConfiguracaoDataBase.getAuth();
                    auth.signOut();
                    Toast.makeText(MenuProfessor.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MenuProfessor.this, MenuPrincipal.class);
                    startActivity(intent);
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
