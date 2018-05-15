/*package com.example.luiz.teacherassistent.Interface.LoginUsuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Interface.CadastroUsuarios.CadastroAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginAluno extends AppCompatActivity {
    private Button buttonConfirmar;
    private Button buttonCancelar;
    private EditText loginAluno;
    private EditText senhaAluno;
    private TextView cadastrar;
    private FirebaseAuth auth;
    private Aluno aluno;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        buttonCancelar = (Button) findViewById(R.id.cancelar);
        buttonConfirmar = (Button) findViewById(R.id.confirmar);
        loginAluno = (EditText) findViewById(R.id.login);
        senhaAluno = (EditText) findViewById(R.id.senha);
        cadastrar = (TextView) findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(LoginAluno.this, CadastroAluno.class);
                startActivity(it);
            }
        });
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loginAluno.getText().toString().equals("") && !senhaAluno.getText().toString().equals("")){
                    aluno = new Aluno();
                    aluno.setEmail(loginAluno.getText().toString());
                    aluno.setSenha(senhaAluno.getText().toString());
                    validarLogin();
                }else{
                    Toast.makeText(LoginAluno.this,"Campos Vazios",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin(){
        auth = ConfiguracaoDataBase.getAuth();
        auth.signInWithEmailAndPassword(aluno.getEmail(),aluno.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginAluno.this,"Blaala",Toast.LENGTH_SHORT).show();
                    AuthResult authResult = task.getResult();
                    ConfiguracaoDataBase.setUsuarioLogado(authResult.getUser());
                    aluno.setId(ConfiguracaoDataBase.getUsuarioLogado().getUid());
                    buscar();
                    abrirTelaPrincipal();
                }
            }
        });
    }
    public void buscar(){
        ConfiguracaoDataBase.getFirebase().child("aluno").child(aluno.getId());
        ValueEventListener post = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aluno = dataSnapshot.getValue(Aluno.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ConfiguracaoDataBase.getFirebase().addListenerForSingleValueEvent(post);
    }
    private void abrirTelaPrincipal() {
        Intent abrirMenu = new Intent(LoginAluno.this,MenuAluno.class);
        startActivity(abrirMenu);
    }
}*/

package com.example.luiz.teacherassistent.Interface.LoginUsuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginAluno extends AppCompatActivity {
    private Button buttonConfirmar;
    private Button buttonCancelar;
    private EditText loginAluno;
    private EditText senhaAluno;
    private TextView cadastrar;
    private FirebaseAuth auth;
    private Aluno aluno;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfiguracaoDataBase.getAuth().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent  = new Intent(LoginAluno.this, MenuAluno.class);
                    startActivity(intent);
                }
                else {
                    Log.d("Erro","Deu merda");

                }
            }
        });
    }
}