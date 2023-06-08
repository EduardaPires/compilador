package compiladorl3;

/**
 *
 * @author rod e duds
 */

public class Sintatico {
    private Lexico lexico;
    private Token token;

    public Sintatico(Lexico lexico) {
        this.lexico = lexico;
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

    // private void end(){
    //     if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
    //         System.out.println("Tudo certo com seu código!");
    //     }
    // }

    //fazer linguagem livre de contexto
    private void insideMainBlock() { //verifica se abriu, passa pro proximo bloco e quando voltar dele verifica dse a chave fecha
        if (!lexEquals("{")) { // verifica se abriu o "{ da main"
            throw new RuntimeException("Você precisa abrir o bloco da main com '{'");
        }
        this.token = this.lexico.nextToken(); // pega próximo token.
        this.declararVar();
        //this.reservadaOuIdentificador(); 
        this.comando();
        
        if(!lexEquals("}")){
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
                if (!lexEquals(";")){
                    throw new RuntimeException("Você não colocou o ; poxa :(");
                }
                this.token = this.lexico.nextToken();
            }
        }

    }

        //comando basico ou iteração
    private void comando() {

        if ((this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA)) {
            if (lexEquals("if")) {
                //this.comandos --> ve se é if ou while e a partir disso leva pra func dif
                this.expressao();
                this.token = this.lexico.nextToken(); 
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
        //this.token = this.lexico.nextToken();
        
        if(this.lexico.nextToken().getLexema().equals("else")){
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


    
    // private void declararVar() {
    //     this.token = this.lexico.nextToken();
    //     if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
    //         throw new RuntimeException("Nome de variável inválida");
    //     } else {
    //         ATRIBUICAO();
    //     }
    // }

    // //comando basico ou iteração
    // private void reservadaOuIdentificador() {

    //     if ((this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA)) {
    //         this.reservada();
    //     } else if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
    //         this.ATRIBUICAO();
    //     }
    //     this.token = this.lexico.nextToken();
    //     //volta para o bloco main
    // }

    // private void reservada() {
    //     if (lexEquals("int") || lexEquals("float") || lexEquals("double")) { // se for igual a tipo identificador, é pq                                                              // faremos atribuição
    //         this.declararVar();
    //     } else if (lexEquals("if") || lexEquals("else") ) {
    //         this.condicional();
    //     } else if (lexEquals("while")) {
    //         this.loopWhile();
    //     } else {
    //         throw new RuntimeException("o que misera tu botou ai bixo");
    //     }
    // }

    private void ATRIBUICAO() {
        this.token = this.lexico.nextToken();

        if (getTokenLex().equals("=")) {
  //          tiposAtribuicao();
        } else {
            throw new RuntimeException("A atribuição deve ser feita com '='' !");
        }
    }

    private void analisarExpressao(){
        if ((this.token.getTipo() == Token.TIPO_IDENTIFICADOR)) {
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

    // private void loopWhile() {

    // }
    

    // private void tiposAtribuicao() {

    //     this.token = this.lexico.nextToken();
    //     if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
    //         verificarOperadores(); 
    //     } else if (this.token.getTipo() == Token.TIPO_REAL || this.token.getTipo() == Token.TIPO_INTEIRO) {
    //         verificarOperadores();
    //     } else if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
    //         return;
    //     } else {
    //         throw new RuntimeException("Há um valor inválido na atribuição!");
    //     }
    // }

    // private void verificarOperadores() {
    //     this.token = this.lexico.nextToken();

    //     if (lexEquals("}")) {
    //         //this.token = this.lexico.nextToken();
    //         reservadaOuIdentificador();
    //     }
    //     if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
    //         tiposAtribuicao();
    //     } else {
    //         throw new RuntimeException("Há um valor inválido na atribuição!");
    //     }

    // }

    // private void E(){
    // this.T();
    // this.El();
    // }

    // private void El(){
    // if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
    // this.OP();
    // this.T();
    // this.El();
    // }else{
    // }
    // }

    // private void T(){
    // if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR ||
    // this.token.getTipo() == Token.TIPO_INTEIRO ||
    // this.token.getTipo() == Token.TIPO_REAL){
    // this.token = this.lexico.nextToken();
    // }else{
    // throw new RuntimeException("Oxe, era para ser um identificador "
    // + "ou número pertinho de " + this.token.getLexema());
    // }
    // }

    // private void OP(){
    // if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
    // this.token = this.lexico.nextToken();
    // }else{
    // throw new RuntimeException("Oxe, era para ser um operador "
    // + "aritmético (+,-,/,*) pertinho de " +
    // this.token.getLexema());
    // }
    // }

    private Boolean lexEquals(String str) {
        return this.token.getLexema().equals(str);
    }

    private String getTokenLex() {
        return this.token.getLexema();
    }
}
