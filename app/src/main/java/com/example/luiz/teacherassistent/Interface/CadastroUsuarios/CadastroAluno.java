/*package com.example.luiz.teacherassistent.Interface.CadastroUsuarios;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Helper.Base64Custom;
import com.example.luiz.teacherassistent.Helper.Preferencias;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginAluno;
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


public class CadastroAluno extends AppCompatActivity {
    //protected ImageView image;
//    protected EditText nome,login,serie,senha,email;
 //   protected Button buttonConfirmar;
  //  protected Aluno aluno;
  //  protected final int GALERIA_IMAGENS = 1;
  //  protected Uri uri=null;
 //   private final int  PERMISSAO_REQUEST =2;
 //   private FirebaseAuth autenticacao;
 //   FirebaseStorage firebaseStorage;
 //   StorageReference storageRef;
  //  StorageReference mountainsRef;
  //  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
        setContentView(R.layout.layout_cadastro_aluno);
        nome = (EditText) findViewById(R.id.nomeCadAluno);
        login = (EditText) findViewById(R.id.loginCadAluno);
        serie = (EditText) findViewById(R.id.serieCadAluno);
        senha = (EditText) findViewById(R.id.senhaCadAluno);
        email = (EditText) findViewById(R.id.emailCadAluno);
        image = (ImageView) findViewById(R.id.fotoCadAluno);
        buttonConfirmar = (Button) findViewById(R.id.confirmarCadAluno);
        aluno = new Aluno();
        aluno.setUserType(1);
        validarPermissao();
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int serieaux = (int) serie.getText().charAt(0);
                aluno.setNome(nome.getText().toString());
                aluno.setSenha(senha.getText().toString());
                aluno.setEmail(email.getText().toString());
                aluno.setSerie(serieaux);
                aluno.setLogin(login.getText().toString());
                cadastrarAluno();
            }
        });
        image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
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
            image.setImageURI(uri);
            image.setDrawingCacheEnabled(true);
            image.buildDrawingCache();
            Bitmap imageGaleria = (BitmapFactory.decodeFile(picturePath));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                imageGaleria.setHeight(100);
                imageGaleria.setWidth(100);
                Log.w("CadaastroEnunciado","Alteração Feita com sucesso");
            }
            else{
                Log.w("ResolucaoEnunciado","Error 404");
            }
            image.setImageBitmap(imageGaleria);
        }
    }
    public void validarPermissao(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO_REQUEST);
            }
        }

    }
    public void cadastrarAluno(){
        autenticacao = ConfiguracaoDataBase.getAuth();
        autenticacao.createUserWithEmailAndPassword(
                aluno.getEmail(),aluno.getSenha()
        ).addOnCompleteListener(CadastroAluno.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(uri!=null) {
                        mountainsRef = storageRef.child("aluno: " + aluno.getEmail() + ".jpg");
                        mountainsRef.putFile(uri);
                    }
                    Toast.makeText(CadastroAluno.this,"Aluno cadastrado com sucesso",Toast.LENGTH_SHORT).show();
                    String identifacador = Base64Custom.codificarBase64(aluno.getEmail());
                    FirebaseUser usuario = task.getResult().getUser();
                    aluno.setId(identifacador);
                    aluno.salvar();
                    Preferencias preferencias = new Preferencias(CadastroAluno.this);
                    preferencias.salvarAlunoPreferencias(aluno);
                    abrirLoginAluno();
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
                    Toast.makeText(CadastroAluno.this,error,Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        });
    }
    public void abrirLoginAluno(){
        Intent intent = new Intent(CadastroAluno.this,LoginAluno.class);
        startActivity(intent);
    }
}*/
