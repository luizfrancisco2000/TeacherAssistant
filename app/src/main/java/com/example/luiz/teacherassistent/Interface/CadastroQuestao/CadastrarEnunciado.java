package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Chico on 28/02/2018.
 */

public class CadastrarEnunciado extends AppCompatActivity{
    //botões
    private Button fotoEnunciado;
    private EditText editEnunciado;
    private  ImageView imagemEnunciado;
    protected final int GALERIA_IMAGENS = 1;
    private  FloatingActionButton continuarCadastro;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    //constantes e variaveis
    private final int  PERMISSAO_REQUEST =2;
    private Questao questao;
    LoginProfessor log;
    // ferramentas
    private TextRecognizer ocrEnunciado;
    private Bitmap imageGaleria;
    @Override
    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        fotoEnunciado = (Button) findViewById(R.id.fotoEnuciado);
        editEnunciado = (EditText) findViewById(R.id.enunciadoEditText);
        continuarCadastro = (FloatingActionButton) findViewById(R.id.ContinuarProcesso);
        imagemEnunciado = (ImageView) findViewById(R.id.fotoEnunciadoMostra);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        disciplina = (TextView) findViewById(R.id.DisciplinaProf);
        validarPermissao();
        fotoEnunciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
        if(log.professor.getMateria().equals("fisica")){
            radioFisica.setSelected(true);
        }else if(log.professor.getMateria().equals("quimica")){
            radioQuimica.setSelected(true);
        }else if(log.professor.getMateria().equals("matematica")){
            radioMatematica.setSelected(true);
        }
        continuarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questao = new Questao();
                questao.setEnunciado(editEnunciado.getText().toString());
                //professor.setAtivo(false);
                if(radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                    if(questao.getMateria().equals(log.professor.getMateria())) {
                        Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                        alerta.setTitle("Atenção").setMessage("Você não é professor da disciplina de"+questao.getMateria()+"\nDeseja continuar?");
                        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(questao.getMateria().equals(log.professor.getMateria())) {
                                    Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                }else if(radioMatematica.isChecked()){
                    if(questao.getMateria().equals(log.professor.getMateria())) {

                    }
                    else{
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                        alerta.setTitle("Atenção").setMessage("Você não é professor da disciplina de"+questao.getMateria()+"\nDeseja continuar?");
                        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(questao.getMateria().equals(log.professor.getMateria())) {

                                }
                            }
                        });
                        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                }else if(radioQuimica.isChecked()){
                    if(questao.getMateria().equals(log.professor.getMateria())) {

                    }
                    else{
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                        alerta.setTitle("Atenção").setMessage("Você não é professor da disciplina de"+questao.getMateria()+"\nDeseja continuar?");
                        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                }else{
                    disciplina.setText(disciplina.getText()+"     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);
                }
            }
        });
    }
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            imagemEnunciado.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage,filePath,null,null,null);
            c.moveToFirst();
            String picturePath = c.getString(c.getColumnIndex(filePath[0]));
            c.close();
            imagemEnunciado.setDrawingCacheEnabled(true);
            imagemEnunciado.buildDrawingCache();
            imageGaleria = (BitmapFactory.decodeFile(picturePath));
           // imagemEnunciado.setImageBitmap(imageGaleria);
            ocrEnunciado = new TextRecognizer.Builder(imagemEnunciado.getContext()).build();
            if(!ocrEnunciado.isOperational()){
                Log.w("Cadastro","Detector dependecies are not yet avaiable");
            }else{
                Frame frame = new Frame.Builder().setBitmap(imageGaleria).build();
                SparseArray<TextBlock> itens = ocrEnunciado.detect(frame);
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < itens.size();++i) {
                    TextBlock item = itens.valueAt(i);
                    text.append(item.getValue());
                    text.append("\n");
                }
                editEnunciado.setText(text);
            }
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 100, 100, true);
            imagemEnunciado.setImageBitmap(bitmapReduzido);
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
    public void buscar(){
        if(!buscarBancoDeDados());
        Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
        startActivity(intent);
    }
    public boolean buscarBancoDeDados(){
        private boolean controle;
            DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
            salve.child("questao").child(questao.getEnunciado()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                        alerta.setTitle("Atenção").setMessage("Questão já cadastrada");
                        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                controle = true;
                            }
                        });
                        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                controle = false;
                            }
                        });
                    }
                    else {
                        Log.d("Erro", "Mais uma vez");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Erro", databaseError.getMessage());
                    controle = false;
                }
            });
            return controle;
    }

}
