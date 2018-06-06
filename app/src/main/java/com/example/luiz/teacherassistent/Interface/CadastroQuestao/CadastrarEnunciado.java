package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginProfessor;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Chico on 28/02/2018.
 */

public class CadastrarEnunciado extends AppCompatActivity{
    //botões
    private Button fotoEnunciado;
    private EditText editEnunciado;
    private Spinner assuntos;
    private  ImageView imagemEnunciado;
    private final int GALERIA_IMAGENS = 1;
    private  FloatingActionButton continuarCadastro;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    private AlertDialog alert;
    //constantes e variaveis
    private final int  PERMISSAO_REQUEST =2;
    Questao questao;
    Professor professor;
    // ferramentas
    private TextRecognizer ocrEnunciado;
    private Bitmap imageGaleria;
    @Override
    protected void onCreate(final Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        fotoEnunciado = (Button) findViewById(R.id.fotoEnuciado);
        editEnunciado = (EditText) findViewById(R.id.enunciadoEditText);
        continuarCadastro = (FloatingActionButton) findViewById(R.id.ContinuarProcessoCad);
        imagemEnunciado = (ImageView) findViewById(R.id.fotoEnunciadoMostra);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        disciplina = (TextView) findViewById(R.id.DisciplinaProf);
        assuntos = (Spinner) findViewById(R.id.assunto);
        validarPermissao();
        fotoEnunciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
        professor = Professor.getInstance();

        Log.d("Teste",professor.getMateria());
        if(!professor.equals(null)){
            if(professor.getMateria().equals("fisica")){
                radioFisica.setChecked(true);
            }else if(professor.getMateria().equals("quimica")){
                radioFisica.setChecked(true);
            }else if(professor.getMateria().equals("matematica")){
                radioFisica.setChecked(true);
            }
        }
        else{
            Intent intent = new Intent(CadastrarEnunciado.this, MenuProfessor.class);
            startActivity(intent);
        }
        if(radioFisica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.fisica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }else if(radioQuimica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.quimica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }else if(radioMatematica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.matematica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }
        radioQuimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    radioQuimica.setChecked(true);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.quimica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    assuntos.setAdapter(adapter);
            }
        });
        radioFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFisica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.fisica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        radioMatematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMatematica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.matematica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        continuarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questao = new Questao();
                questao.setEnunciado(editEnunciado.getText().toString());
                //professor.setAtivo(false);
                if (radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                    int aux;
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    procurarQuestao();
                }else if (radioMatematica.isChecked()) {
                    questao.setMateria("matematica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    procurarQuestao();
                } else if (radioQuimica.isChecked()) {
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    questao.setMateria("quimica");
                    procurarQuestao();
                } else {
                    disciplina.setText(disciplina.getText() + "     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);
                    onCreate(onSaveInstanceState);
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
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 300, 300, true);
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
    /*public void buscarBancoDeDados(){
        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference();
        Log.d("Erro1", questao.getEnunciado());
        mRef.child("questao").child(questao.getMateria()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Questao questao = dataSnapshot.getValue(Questao.class);
                    erroDeBusca(questao);
                }
                else{
                    Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void erroDeBusca(Questao questao) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
        alerta.setTitle("Atenção").setMessage("Questao já cadsatrada" + questao.getMateria() + "\nDeseja continuar?");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                startActivity(intent);
            }
        });
        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }*/



    private void procurarQuestao() {
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(questao.getMateria())).child(
                String.valueOf(questao.getAssunto())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        try{
                            if(dataSnapshot.getValue(Questao.class).getEnunciado()!=null){
                                if(dataSnapshot.getValue(Questao.class).getEnunciado().equals(questao.getEnunciado())){
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                                    alerta.setTitle("Atenção").setMessage("Questao já cadsatrada" + questao.getMateria() + "\nDeseja continuar?");
                                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            questao = dataSnapshot.getValue(Questao.class);
                                            abrirTelaPrincipal();
                                        }
                                    });
                                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    alert = alerta.create();
                                    alert.show();
                                }
                            }
                            else{
                                abrirTelaPrincipal();
                            }
                        }catch(Exception e){
                            abrirTelaPrincipal();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
        });
    }
    private void abrirTelaPrincipal() {
        Questao.setInstance(questao);
        Intent intent = new Intent(CadastrarEnunciado.this,CadastrarResolucao.class);
        startActivity(intent);
    }
}
