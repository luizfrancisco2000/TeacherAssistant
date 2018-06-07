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
    public String corrigir() {
        String status = null;
        String erro = "";
        ArrayList<String> erros = new ArrayList<>();
        /*for(ArrayList<String> q: resolucaoCorreta) {
            for (int i = 0; i < q.size(); i++) {
                Log.d("Teste", resolucaoUser.get(i));
                for (int j = 0; j < resolucaoUser.size(); j++) {
                    Log.d("Teste2", resolucaoUser.get(j));
                    Log.d("Teste3", q.get(i));
                    if (q.get(i).equals(resolucaoUser.get(j))) {
                        i++;
                        status = "tá certinho";
                        erros.clear();
                        erro = "";
                    } else {
                        status = null;
                        erro = "" + j;
                        erros.add(erro);
                    }
                    if (status != null && j==resolucaoUser.size()-1){
                        Log.d("Status", status);
                        return erro;
                    }
                }
                i = resolucaoCorreta.size();
            }
            if(erros.isEmpty() || erros.size()==0) {
                erro = "";
            }else{
                erro=erros.get(0);
            }
        }*/
    for(int q=0; q<resolucaoCorreta.size();q++){
        for (int i = 0; i < resolucaoCorreta.get(q).size(); i++) {
            for (int j = 0; j < resolucaoUser.size(); j++) {
                Log.d("Teste2", resolucaoUser.get(j));
                Log.d("Teste3", resolucaoCorreta.get(q).get(i));
                if (resolucaoCorreta.get(q).get(i).equals(resolucaoUser.get(j))) {
                    i++;
                    status = "tá certinho";
                    erros.clear();
                    erro = "";
                } else {
                    status = null;
                    erro = "" + j;
                    erros.add(erro);
                }
                if (status != null && j == resolucaoUser.size() - 1) {
                    Log.d("Status", status);
                    return erro;
                }
            }
        }
    }
        if(erros.isEmpty() || erros.size()==0) {
            erro = "";
        }else{
            erro=erros.get(0);
        }
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
