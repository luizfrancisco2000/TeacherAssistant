package com.example.luiz.teacherassistent.Controle;


        import android.util.Log;

        import java.util.ArrayList;

/**
 * Created by Chico on 26/03/2018.
 */

public class Correcao {
    private ArrayList<ArrayList<String>> resolucaoCorreta;
    private ArrayList<String> resolucaoUser;
    private  int auxiliar;
    private String erro;

    private static Correcao getInstance;
    public Correcao(){

    }
    public void setResolucaoCorreta(ArrayList<ArrayList<String>> correta){
        this.resolucaoCorreta = correta;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public ArrayList<ArrayList<String>> getResolucaoCorreta() {
        return resolucaoCorreta;
    }
    public void convertStringForArray(String correta){
        int auxiliar = 0;
        String texto = "";
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < correta.length(); i++) {
            if (correta.charAt(i) == '\\') {
                if (correta.charAt(i + 1) == '\\') {
                    texto = correta.substring(auxiliar, i);
                    Log.d("Teste", texto);
                    auxiliar = i + 2;
                    res.add(texto);
                }
                i++;
            }
        }
        if(texto.length()!=0){
            texto = correta.substring(auxiliar,correta.length()-1);
        }else{
            texto = correta.substring(auxiliar,correta.length());
        }
        Log.d("Teste1: ", texto);
        res.add(texto);
        if(resolucaoUser==null){
            resolucaoUser = new ArrayList<>();
        }
        resolucaoUser = res;
    }
    public String corrigir() {
        auxiliar=1001;
       boolean certo = false;
       erro="";
       for(ArrayList<String> correta:resolucaoCorreta){
           for (int i = 0; i < correta.size(); i++) {
               for (int j = 0; j < resolucaoUser.size(); j++) {
                   if (correta.get(i).equals(resolucaoUser.get(j))) {
                       certo = true;
                   }else{
                       if(auxiliar < j || auxiliar>1000){
                           auxiliar=j;
                           certo = false;
                            erro=""+(j+1);
                       }
                   }
                   Log.d("SITUACAO", String.valueOf(certo));
                   Log.d("SITUACAO ERRO", erro);
               }
           }

       }
       if(certo==true){
            return "";
       }else{
            return erro;
       }
    }
    public static Correcao getInstance(){
        if(getInstance==null){
            getInstance = new Correcao();
        }
        return getInstance;
    }
    public static void setInstance(Correcao correcao){
        getInstance = correcao;
    }
}
