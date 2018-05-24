package com.example.luiz.teacherassistent.Controle;


        import android.util.Log;

        import java.util.ArrayList;

/**
 * Created by Chico on 26/03/2018.
 */

public class Correcao {
    private ArrayList<String> resolucaoCorreta;
    private ArrayList<String> resolucaoUser;
    private  int auxiliar;
    private String erro;

    private static Correcao getInstance;
    public Correcao(){

    }
    public void setResolucaoCorreta(ArrayList<String> correta){
        this.resolucaoCorreta=correta;
    }
    public void setResolucaoUser(String resolucao) {
        ArrayList<String> res = new ArrayList<>();
        for(int i = 0; i<resolucao.length();i++){
            auxiliar=0;
            if(resolucao.charAt(i)=='\n'){
                res.add(resolucao.subSequence(auxiliar,i).toString());
                auxiliar=i+1;
            }
        }
        resolucaoUser = res;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public ArrayList<String> getResolucaoCorreta() {
        return resolucaoCorreta;
    }
    public void convertStringForArray(String correta){
        int auxiliar=0;
        ArrayList<String> res = new ArrayList<>();
        for(int i = 0; i<correta.length();i++){
            if(correta.charAt(i)=='\n'){
                String texto = correta.subSequence(auxiliar,i).toString();
                auxiliar=i+1;
                res.add(texto);
                i++;
            }
        }
        resolucaoUser = res;
    }
    public String corrigir(){
        String status;
        String erro;
        ArrayList<String> erros = new ArrayList<>();
            for (int i = 0; i < resolucaoCorreta.size(); i++) {
                Log.d("Teste", resolucaoUser.get(i));
                for (int j = 0; j < resolucaoUser.size(); j++) {
                    Log.d("Teste2", resolucaoUser.get(j));
                    Log.d("Teste3", resolucaoCorreta.get(i));
                    if (resolucaoCorreta.get(i).equals(resolucaoUser.get(j))) {
                        i++;
                        status = "tá certinho";
                        erro = null;
                    } else {
                        status = null;
                        erro = "" + j;
                        erros.add(erro);
                        if (j == resolucaoUser.size() - 1) {
                            return erros.get(0);
                        }
                    }
                    if (status != null)
                        Log.d("Status", status);
                }
                i = resolucaoCorreta.size();
            }
            erro = "";
        return erro;
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
