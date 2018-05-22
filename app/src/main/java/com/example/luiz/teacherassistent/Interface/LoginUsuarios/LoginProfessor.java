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

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Helper.Base64Custom;
import com.example.luiz.teacherassistent.Interface.CadastroUsuarios.CadastroProfessor;
import com.example.luiz.teacherassistent.Interface.CadastroUsuarios.InstrucaoCadastroProfessor;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginProfessor extends AppCompatActivity {
    private Button buttonConfirmar;
    private Button buttonCancelar;
    private EditText loginprofessor;
    private EditText senhaprofessor;
    private TextView cadastrar;
    private FirebaseAuth auth;
    public  Professor professor;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        buttonCancelar = (Button) findViewById(R.id.cancelar);
        buttonConfirmar = (Button) findViewById(R.id.confirmar);
        loginprofessor = (EditText) findViewById(R.id.login);
        senhaprofessor = (EditText) findViewById(R.id.senha);
        cadastrar = (TextView) findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent(LoginProfessor.this, CadastroProfessor.class);
                startActivity(it);
            }
        });
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loginprofessor.getText().toString().equals("") && !senhaprofessor.getText().toString().equals("")){
                    professor = new Professor();
                    professor.setEmail(loginprofessor.getText().toString());
                    professor.setSenha(senhaprofessor.getText().toString());
                    validarLogin();
                }else{
                    Toast.makeText(LoginProfessor.this,"Campos Vazios",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validarLogin(){
        auth = ConfiguracaoDataBase.getAuth();
        auth.signInWithEmailAndPassword(professor.getEmail(),professor.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Eae", "createUserWithEmail:onComplete:" + task.isSuccessful());

                if(task.isSuccessful()){
                    Toast.makeText(LoginProfessor.this,"Blaala",Toast.LENGTH_SHORT).show();
                    AuthResult authResult = task.getResult();
                    FirebaseUser usuarioLogado = authResult.getUser();
                    professor.setId(usuarioLogado.getUid());
                    buscar();
                }
            }
        });
    }
    public void buscar(){
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("professor").child(professor.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        professor = dataSnapshot.getValue(Professor.class);
                        Log.d("Cidadão2",professor.getNome());
                        abrirTelaPrincipal(professor);
                    }
                    else {
                        Log.d("Erro", "Mais uma vez");
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Erro", databaseError.getMessage());
            }
        });
    }
    private void abrirTelaPrincipal(Professor professorLogado) {
        if(professorLogado.getAtivo()) {
            Intent intent = new Intent(LoginProfessor.this, MenuProfessor.class);
            startActivity(intent);
            professor = professorLogado;
            Professor.setInstance(professor);
        }
        else{
            Intent intent = new Intent(LoginProfessor.this, InstrucaoCadastroProfessor.class);
            startActivity(intent);
        }
    }
}
