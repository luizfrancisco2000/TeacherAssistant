package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.TextView;

import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class CorrigirBuscarResolucaoAluno extends AppCompatActivity{
    Correcao correcao;
    private EditText resolucaoAluno;
    private ImageView fotoResolucaoAluno;
    private FloatingActionButton concluir;
    private Button buttonEscolherFoto;
    private Bitmap imageGaleria;
    private TextRecognizer ocrResolucao;

    protected final int GALERIA_IMAGENS = 1;
    private final int  PERMISSAO_REQUEST =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        validarPermissao();
        resolucaoAluno = (EditText) findViewById(R.id.ResolucaoEdit);
        buttonEscolherFoto = (Button) findViewById(R.id.FotoResolucao);
        fotoResolucaoAluno = (ImageView) findViewById(R.id.imageResourceID);
        concluir = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        correcao = Correcao.getInstance();
        Log.d("Teste",correcao.getResolucaoCorreta().get(0).toString());
        fotoResolucaoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
        buttonEscolherFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALERIA_IMAGENS);
            }
        });
        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aux = resolucaoAluno.getText().toString();
                correcao.convertStringForArray(aux);
                String erroString = correcao.corrigir();
                if(!erroString.equals("")){
                    correcao.setErro(erroString);
                    Correcao.setInstance(correcao);
                    Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, ErradoAluno.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, CorretoAluno.class);
                    startActivity(intent);
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
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==GALERIA_IMAGENS){
            Uri selectedImage = data.getData();
            fotoResolucaoAluno.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage,filePath,null,null,null);
            c.moveToFirst();
            String picturePath = c.getString(c.getColumnIndex(filePath[0]));
            c.close();
            fotoResolucaoAluno.setDrawingCacheEnabled(true);
            fotoResolucaoAluno.buildDrawingCache();
            imageGaleria = (BitmapFactory.decodeFile(picturePath));
            fotoResolucaoAluno.setImageBitmap(imageGaleria);
            ocrResolucao = new TextRecognizer.Builder(fotoResolucaoAluno.getContext()).build();
            if(!ocrResolucao.isOperational()){
                Log.w("Resolucao","Detector dependecies are not yet avaiable");
            }else{
                Frame frame = new Frame.Builder().setBitmap(imageGaleria).build();
                SparseArray<TextBlock> itens = ocrResolucao.detect(frame);
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < itens.size();++i) {
                    TextBlock item = itens.valueAt(i);
                    text.append(item.getValue());
                    text.append("\n");
                }
                resolucaoAluno.setText(text);
            }
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 100, 100, true);
            fotoResolucaoAluno.setImageBitmap(bitmapReduzido);
        }
    }
}
