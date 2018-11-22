package com.example.luiz.teacherassistent.Interface.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.luiz.teacherassistent.R;


public class EnunciadoFragment extends Fragment implements View.OnFocusChangeListener{

    private EditText editEnunciado;
    public static String texto;
    public EnunciadoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enunciado, container, false);
        editEnunciado = (EditText) view.findViewById(R.id.enunciadoEditText);
        editEnunciado.setOnFocusChangeListener(this);
        return view;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            texto = editEnunciado.getText().toString();
            Log.d("Teste", texto);
        }
    }
}