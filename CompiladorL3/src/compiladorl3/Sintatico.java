package compiladorl3;

import java.util.ArrayList;

/**
 *
 * @author rod e duds
 */

public class Sintatico {
    private Lexico lexico;
    private Token token;
    private ArrayList<String> idList;

    public Sintatico(Lexico lexico) {
        this.lexico = lexico;
        idList = new ArrayList<>();
    }



    
    public void mainDeclaration() {// S determina estado inicial
        this.token = this.lexico.nextToken();

        if (!lexEquals("main")) {
            throw new RuntimeException("Falta o primeiro termo da função principal 'main' ");
        }

        // n entrou pq tem token main, pega próximo.
        this.token = this.lexico.nextToken();
        if (!lexEquals("(")) {
            throw new RuntimeException("faltou abrir o parênteses da main!");
        }
        // n entrou pq tem token "(", pega o próximo.

        this.token = this.lexico.nextToken();
        if (!lexEquals(")")) {
            throw new RuntimeException("faltou fechar o parênteses da main!");
        }
        // n entrou pq tem token ")", pega o próximo.
        this.token = this.lexico.nextToken();

        this.insideMainBlock(); // chama o B (entra no B)
    }

    //fazer linguagem livre de contexto
    private void insideMainBlock() { //verifica se abriu, passa pro proximo bloco e quando voltar dele verifica dse a chave fecha
        if (!lexEquals("{")) { // verifica se abriu o "{ da main"
            throw new RuntimeException("Você precisa abrir o bloco da main com '{'");
        }
        this.token = this.lexico.nextToken(); // pega próximo token.
        this.declararVar();
        //this.reservadaOuIdentificador(); 
        this.comando();
        
        if(lexEquals("}")){
            System.out.println("Código rodou bem!");
            return;  // corrigido problema de fechamento da main (Digs).
        }
        else {
            throw new RuntimeException("Você precisa fechar o bloco da main com '}'");
        }// entra em função para ver se é reservada ou identificador.
        
    }

    private void declararVar() {

        if (lexEquals("int") || lexEquals("float")|| lexEquals("char")) {
            this.token = this.lexico.nextToken();
            if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
                throw new RuntimeException("Nome de variável inválida");
            } else {
                this.token = this.lexico.nextToken();
            }

            if (getTokenLex().equals("=")) {
                this.token = this.lexico.nextToken();
                this.operacoes();
                if (!getTokenLex().equals(";")){
                    throw new RuntimeException("Tem que finalizar com ;");
                }
             } 
     

            if(getTokenLex().equals(";")){
                this.token = this.lexico.nextToken();
            }else{
                throw new RuntimeException("Bote o ponto e vírgula, meu nobre");
            }
        }

    }

        //comando basico ou iteração
    private void comando() {

        if ((this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA)) {
            if (lexEquals("if")) {
                //this.comandos --> ve se é if ou while e a partir disso leva pra func dif
                this.expressao();
                //this.token = this.lexico.nextToken(); 
                this.comando();
            }
            
            if(lexEquals("while")){
                this.bloco();
                this.token = this.lexico.nextToken();
                this.comando();
            }
        } else if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.ATRIBUICAO();
            this.token = this.lexico.nextToken();
            this.comando();
        }else{
            this.declararVar();
        }
        //volta para o bloco main
    }

    private void bloco(){
        
        this.token = this.lexico.nextToken();
        if (!lexEquals("(")) {
            throw new RuntimeException("Falta abrir parênteses na estrutura condicional.");
        }

        this.token = this.lexico.nextToken();
        this.analisarExpressao();

        if (!lexEquals(")")) {
            throw new RuntimeException("Falta fechar parênteses na estrutura condicional.");
        }

        this.token = this.lexico.nextToken();

        if(!lexEquals("{")){
            throw new RuntimeException("Abra as chaves do if!");
        }

        this.token = this.lexico.nextToken();
        this.declararVar();
        //this.reservadaOuIdentificador(); 
        this.comando();

        if(!lexEquals("}")){
            throw new RuntimeException("Feche as chaves do if!");
        }
    }

    private void expressao(){

        this.bloco();

        this.token = this.lexico.nextToken();
        
        if(this.token.getLexema().equals("else")){
            this.token = this.lexico.nextToken();
            //this.token = this.lexico.nextToken();
            if(!lexEquals("{")){
                throw new RuntimeException("Abra as chaves!");
            }
    
            this.token = this.lexico.nextToken();
            this.declararVar();
            //this.reservadaOuIdentificador(); 
            this.comando();
    
            if(!lexEquals("}")){
                throw new RuntimeException("Feche as chaves!");
            }
        }
    }


    private void ATRIBUICAO() {
        this.token = this.lexico.nextToken();

        if (getTokenLex().equals("=")) {
           this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("A atribuição deve ser feita com '='' !");
        }

        this.operacoes();
        if (!getTokenLex().equals(";")){
            throw new RuntimeException("Tem que finalizar com ;");
        }
    }

    private void operacoes(){
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getTipo() == Token.TIPO_REAL || this.token.getTipo() == Token.TIPO_INTEIRO ){
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("O valor atribuido é invalido");
        }

        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token = this.lexico.nextToken();
            this.operacoes();
        }else{
            throw new RuntimeException("Coloque um operador aritmético!!");
        }


    }

    private void analisarExpressao(){
        if((this.token.getTipo() == Token.TIPO_IDENTIFICADOR)) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Era esperado um identificador");
        }

        if ((this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL)) {
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Era esperado operador relacional");
        }

        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getTipo() == Token.TIPO_CHAR || this.token.getTipo() == Token.TIPO_REAL || this.token.getTipo() == Token.TIPO_INTEIRO) {
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Não to entendendo com o que você ta querendo comparar a variável :(");
        }

    }

    private Boolean lexEquals(String str) {
        return this.token.getLexema().equals(str);
    }

    private String getTokenLex() {
        return this.token.getLexema();
    }
}
