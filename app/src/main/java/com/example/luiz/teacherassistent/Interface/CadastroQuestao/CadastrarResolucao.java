package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;


public class CadastrarResolucao extends AppCompatActivity {
    //botões
    private Button fotoResolucao;
    private EditText editResolucao;
    private ImageView imagemResolucao;
    protected final int GALERIA_IMAGENS = 1;
    private FloatingActionButton concluirCadastro;
    //constantes e variaveis
    private final int  PERMISSAO_REQUEST =2;
    private Questao questao;
    // ferramentas
    private TextRecognizer ocrResolucao;
    private Bitmap imageGaleria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        validarPermissao();
        fotoResolucao = (Button) findViewById(R.id.FotoResolucao);
        editResolucao = (EditText) findViewById(R.id.ResolucaoEdit);
        imagemResolucao = (ImageView) findViewById(R.id.imageResourceID);
        concluirCadastro = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        questao = Questao.getInstance();
        fotoResolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
        concluirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Questao.setInstance(questao);
                questao.convertStringForArray(editResolucao.getText().toString());
                if(questao.getResolucao().size()<=1) {
                    questao.salvar();
                    Intent intent = new Intent(CadastrarResolucao.this, MenuProfessor.class);
                    Toast.makeText(CadastrarResolucao.this, "Casdastro realizado com sucesso\n retomando ao menu", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    atualizarBanco();
                }

            }
        });
    }
    public void validarPermissao(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO_REQUEST);
            }
        }

    }
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            imagemResolucao.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage,filePath,null,null,null);
            c.moveToFirst();
            String picturePath = c.getString(c.getColumnIndex(filePath[0]));
            c.close();
            imagemResolucao.setDrawingCacheEnabled(true);
            imagemResolucao.buildDrawingCache();
            imageGaleria = (BitmapFactory.decodeFile(picturePath));
            ocrResolucao = new TextRecognizer.Builder(imagemResolucao.getContext()).build();
            if(!ocrResolucao.isOperational()){
                Log.w("Cadastro","Detector dependecies are not yet avaiable");
            }else{
                Frame frame = new Frame.Builder().setBitmap(imageGaleria).build();
                SparseArray<TextBlock> itens = ocrResolucao.detect(frame);
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < itens.size();++i) {
                    TextBlock item = itens.valueAt(i);
                    text.append(item.getValue());
                    text.append("\n");
                }
                editResolucao.setText(text);
            }
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 100, 100, true);
            imagemResolucao.setImageBitmap(bitmapReduzido);
        }
    }
    public void atualizarBanco(){
        DatabaseReference datbase = ConfiguracaoDataBase.getFirebase();
        Map<String, Object> questaoSalvar = questao.toMap();
        Map<String, Object> questaoAtualizacoes = new HashMap<>();
        questaoAtualizacoes.put("/questao/"+questao.getMateria()+"/"+questao.getAssunto()+"/",questaoSalvar);
        datbase.updateChildren(questaoAtualizacoes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Atualicao","Atualização feita com sucesso");
                Intent intent = new Intent(CadastrarResolucao.this, MenuProfessor.class);
                startActivity(intent);
            }
        });
    }
}
