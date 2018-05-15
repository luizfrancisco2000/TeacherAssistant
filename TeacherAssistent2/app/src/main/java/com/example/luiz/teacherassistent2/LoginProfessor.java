package com.example.luiz.teacherassistent2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Chico on 28/02/2018.
 */

public class LoginProfessor extends AppCompatActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_login_aluno);
            Button buttonConfirmar = (Button) findViewById(R.id.confirmar);
            Button buttonCancelar = (Button) findViewById(R.id.cancelar);
            TextView cadastrar = (TextView) findViewById(R.id.cadastrar);
            cadastrar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent it = new Intent(LoginProfessor.this, CadastroProfessor.class);
                    startActivity(it);
                }
            });
        }
}
