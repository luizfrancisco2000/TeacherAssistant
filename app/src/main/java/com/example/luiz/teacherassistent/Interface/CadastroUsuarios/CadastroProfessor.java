package com.example.luiz.teacherassistent.Interface.CadastroUsuarios;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Helper.Base64Custom;
import com.example.luiz.teacherassistent.Helper.Preferencias;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Chico on 28/02/2018.
 */

public class CadastroProfessor extends AppCompatActivity{
    private FirebaseAuth autenticacao;
    private Professor professor;
    private final int  PERMISSAO_REQUEST =2;
    protected final int GALERIA_IMAGENS = 1;
    private ImageView image;
    private Button buttonConfirmar;
    private Uri uri;
    private EditText nomeProfessor;
    private EditText senhaProfessor;
    private EditText loginProfessor;
    private EditText emailProfessor;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    private TextView email;
    private FloatingActionButton continuar;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    StorageReference mountainsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cadastrar_professor);
        nomeProfessor = (EditText) findViewById(R.id.nomeProfessor);
        senhaProfessor  = (EditText)findViewById(R.id.senhaProfessor);
        loginProfessor  = (EditText) findViewById(R.id.loginProfessor);
        emailProfessor  = (EditText) findViewById(R.id.emailProfessor);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        disciplina = (TextView) findViewById(R.id.DisciplinaProf);
        buttonConfirmar = (Button) findViewById(R.id.confirmar_professor);
        image = (ImageView) findViewById(R.id.fotoProfessor);
        professor = new Professor();
        professor.setUserType(2);
        validarPermissao();
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                professor.setAtivo(false);
                if(radioFisica.isChecked()) {
                    professor.setMateria("fisica");
                }else if(radioMatematica.isChecked()){
                    professor.setMateria("matematica");
                }else if(radioQuimica.isChecked()){
                    professor.setMateria("quimica");
                }else{
                    disciplina.setText(disciplina.getText()+"     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);

                }
                professor.setEmail(emailProfessor.getText().toString());
                professor.setSenha(senhaProfessor.getText().toString());
                professor.setNome(nomeProfessor.getText().toString());
                cadastrarProfessor();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
    }
    protected void cadastrarProfessor(){
        autenticacao = ConfiguracaoDataBase.getAuth();
        autenticacao.createUserWithEmailAndPassword(
                professor.getEmail(),professor.getSenha()
        ).addOnCompleteListener(CadastroProfessor.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(uri!=null) {
                        mountainsRef = storageRef.child("professor: " + professor.getEmail() + ".jpg");
                        mountainsRef.putFile(uri);
                    }
                    Toast.makeText(CadastroProfessor.this,"professor cadastrado com sucesso",Toast.LENGTH_SHORT).show();
                    //String identifacador = Base64Custom.codificarBase64(professor.getEmail());
                    FirebaseUser usuario = task.getResult().getUser();
                    professor.setId(usuario.getUid());
                    professor.salvar();
                    Preferencias preferencias = new Preferencias(CadastroProfessor.this);
                    preferencias.salvarPreferencias(professor.getEmail(), professor.getNome());
                    concluir();
                }else{
                    String error;
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        error = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "email invalido";
                    }
                    catch (FirebaseAuthInvalidUserException e){
                        error = "Esse email já foi cadastrado no sistema";
                    }
                    catch (Exception e) {
                        error = "Cadastrado não realizado";
                    }
                    Toast.makeText(CadastroProfessor.this,error,Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        });
    }
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage,filePath,null,null,null);
            c.moveToFirst();
            String picturePath = c.getString(c.getColumnIndex(filePath[0]));
            c.close();
            uri = selectedImage;
            Bitmap imageGaleria = (BitmapFactory.decodeFile(picturePath));
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                imageGaleria.setHeight(100);
                imageGaleria.setWidth(100);
                Log.w("CadaastroEnunciado","Alteração Feita com sucesso");
            }
            else{
                Log.w("CadastroEnunciado","Error 404");
            }*/
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 100, 100, true);
            image.setImageBitmap(bitmapReduzido);
        }
    }
    public void validarPermissao() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }
    }
    public void concluir(){
        Intent intent = new Intent(CadastroProfessor.this,InstrucaoCadastroProfessor.class);
        startActivity(intent);
    }
}