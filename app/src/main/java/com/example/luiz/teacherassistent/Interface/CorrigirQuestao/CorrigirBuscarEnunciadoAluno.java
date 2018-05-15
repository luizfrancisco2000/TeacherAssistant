package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

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
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

/**
 * Created by Chico on 28/02/2018.
 */

public class CorrigirBuscarEnunciadoAluno extends AppCompatActivity {
    //botões
    private Button fotoEnunciado;
    private EditText editEnunciado;
    private ImageView imagemEnunciado;
    protected final int GALERIA_IMAGENS = 1;
    private  FloatingActionButton continuarResolucao;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    //constantes e variaveis
    private final int  PERMISSAO_REQUEST =2;
    private Questao questao;
    private Aluno aluno;
    // ferramentas
    private TextRecognizer ocrEnunciado;
    private Bitmap imageGaleria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        fotoEnunciado = (Button) findViewById(R.id.fotoEnuciado);
        editEnunciado = (EditText) findViewById(R.id.enunciadoEditText);
        continuarResolucao = (FloatingActionButton) findViewById(R.id.ContinuarProcesso);
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
        continuarResolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questao = new Questao();
                questao.setEnunciado(editEnunciado.getText().toString());
                //professor.setAtivo(false);
                if(radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                }else if(radioMatematica.isChecked()){
                    questao.setMateria("matematica");
                }else if(radioQuimica.isChecked()){
                    questao.setMateria("quimica");
                }else{
                    disciplina.setText(disciplina.getText()+"     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);
                }
                questao.buscar();
                if(questao!=null) {
                    Toast.makeText(CorrigirBuscarEnunciadoAluno.this, "Sinto Muito, questão não cadastrada :(", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this,CorrigirBuscarResolucaoAluno.class);
                    startActivity(intent);
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
            imagemEnunciado.setImageBitmap(imageGaleria);
            ocrEnunciado = new TextRecognizer.Builder(imagemEnunciado.getContext()).build();
            if(!ocrEnunciado.isOperational()){
                Log.w("Resolucao","Detector dependecies are not yet avaiable");
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
}