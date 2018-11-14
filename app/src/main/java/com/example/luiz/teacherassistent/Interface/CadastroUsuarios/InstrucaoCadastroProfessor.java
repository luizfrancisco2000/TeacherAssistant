package com.example.luiz.teacherassistent.Interface.CadastroUsuarios;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Interface.Menus.MenuPrincipal;
import com.example.luiz.teacherassistent.R;

public class InstrucaoCadastroProfessor extends AppCompatActivity {
    private TextView email;
    private FloatingActionButton continuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_instrucoes_professor);
            continuar = (FloatingActionButton) findViewById(R.id.concluirCadastroProfessor);
            email = (TextView) findViewById(R.id.emailCP);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("email",email.getText());
                    Toast.makeText(InstrucaoCadastroProfessor.this, "Campo Copiado!", Toast.LENGTH_SHORT).show();
                    /*TextView tv;
                    String stringYouExtracted = tv.getText().toString();
                    int startIndex = tv.getSelectionStart();
                    int endIndex = tv.getSelectionEnd();
                    stringYouExtracted = stringYouExtracted.subString(startIndex, endIndex);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(stringYouExtracted);


                    if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(stringYouExtracted);
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", stringYouExtracted);
                                clipboard.setPrimaryClip(clip);
                    }*/
                }
            });
            continuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(InstrucaoCadastroProfessor.this, MenuPrincipal.class);
                    startActivity(intent);
                }
            });

        }
}
