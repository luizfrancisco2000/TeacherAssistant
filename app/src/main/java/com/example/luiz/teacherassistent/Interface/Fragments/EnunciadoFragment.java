package com.example.luiz.teacherassistent.Interface.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.luiz.teacherassistent.R;


public class EnunciadoFragment extends Fragment  {

    private EditText editEnunciado;
    public EnunciadoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editEnunciado = (EditText) container.findViewById(R.id.enunciadoEditText);
        return inflater.inflate(R.layout.fragment_enunciado, container, false);
    }

    public String getEditEnunciado() {
        return editEnunciado.getText().toString()==null?"deu merda":editEnunciado.getText().toString();
    }
}