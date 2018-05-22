package com.example.luiz.teacherassistent.Controle;


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

    public String corrigir(){
        String status = null;
        String erro = null;
        for (int i =0; i<resolucaoCorreta.size();i++){
            do {
                for (int j = 0; j < resolucaoUser.size(); j++) {
                    if (resolucaoCorreta.get(i).equals(resolucaoUser.get(i))) {
                        i++;
                        status = "tá certinho";
                    } else {
                        status = null;
                        erro = "" + j;
                    }
                }
            }while(!erro.equals(null));
            i=resolucaoCorreta.size();
        }
        this.erro=erro;
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
