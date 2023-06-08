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

    :(main(){


    
    public void mainDeclaration() {// S determina estado inicial
        this.token = this.lexico.nextToken();
        if (!lexEquals(":(")) {
            throw new RuntimeException("Falta o primeiro termo da função principal ':('");
        }
        this.token = this.lexico.nextToken();

        if (!lexEquals("main")) {
            throw new RuntimeException("Falta o segundo termo da função principal 'main' ");
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

        // só continua para esse if se as chamadas das funções seguintes não der erro
        // algum.
        if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            System.out.println("Tudo certo com seu código!");
        } else {
            throw new RuntimeException("Algum erro ocorreu.");
            // teste
        }
    }

    private void insideMainBlock() { //
        if (!lexEquals("{")) { // verifica se abriu o "{ da main"
            throw new RuntimeException("Você precisa abrir o bloco da main com '{'");
        }
        this.token = this.lexico.nextToken(); // pega próximo token.

        this.reservadaOuIdentificador(); //reservada=if,else,while,double,int ou nome de variavel (semantico irá identificar se a variavel já foi declarada)
        /*
         * Ao chegar aqui nosso código está assim:
         * main(){
         * aqui ele vai entrar na funcao CS
         * 
         */

        // Esse if só será executado se não houver nenhum erro em CS e nas suas chamadas
        // seguintes.
        if (!lexEquals("}")) { // verifica se fechou o "}" da main
            throw new RuntimeException("Você precisa fechar o bloco da main com '}'");
        }
    }

    private void reservadaOuIdentificador() {

        if ((this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA)) {
            this.reservada();
        } else if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.ATRIBUICAO();
        } else if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            return;
        } else {
            throw new RuntimeException("Essa palavra não é válida dentro da main");
        }
    }

    private void reservada() {
        if (lexEquals("int") || lexEquals("float") || lexEquals("double")) { // se for igual a tipo identificador, é pq                                                              // faremos atribuição
            this.declararVar();
        } else if (lexEquals("if") || lexEquals("else") ) {
            this.condicional();
        } else if (lexEquals("while")) {
            this.loopWhile();
        } else {
            throw new RuntimeException("o que misera tu botou ai bixo");
        }
    }

    private void declararVar() {
        this.token = this.lexico.nextToken();
        if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
            throw new RuntimeException("Nome de variável inválida");
        } else {
            ATRIBUICAO();
        }
    }

    private void ATRIBUICAO() {
        this.token = this.lexico.nextToken();

        if (getTokenLex().equals("=")) {
            tiposAtribuicao();
        } else {
            throw new RuntimeException("A atribuição deve ser feita com '='' !");
        }
    }

    private void condicional() {

        if (!lexEquals("if")) {
            throw new RuntimeException("Palavra-chave 'if' esperada na estrutura condicional.");
        }

        // Avança para o próximo token
        this.token = this.lexico.nextToken();

        // Verifica se há um "(" após o "if"
        if (!lexEquals("(")) {
            throw new RuntimeException("Falta abrir parênteses na estrutura condicional.");
        }

        // Avança para o próximo token
        this.token = this.lexico.nextToken();

        // Chama a função para análise da expressão condicional
        this.analisarExpressao();

        // Verifica se há um ")" após a expressão condicional
        if (!lexEquals(")")) {
            throw new RuntimeException("Falta fechar parênteses na estrutura condicional.");
        }

        this.token = this.lexico.nextToken();
        this.reservadaOuIdentificador();

        //this.analisarBlocoCodigo();

        if (lexEquals("else")) {

            this.token = this.lexico.nextToken();

            // Chama a função para análise do bloco de código do "else"
           // this.analisarBlocoCodigo();
        }
    }

    private void analisarExpressao(){
        if ((this.token.getTipo() == Token.TIPO_IDENTIFICADOR)) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Tem que ser uma condição miseravi");
        }

        if ((this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL)) {
            this.token = this.lexico.nextToken();
        }

        if ((this.token.getTipo() == Token.TIPO_IDENTIFICADOR)) {
        
        }


    }

    private void loopWhile() {

    }
    

    private void tiposAtribuicao() {
        this.token = this.lexico.nextToken();
        if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            verificarOperadores(); 
        } else if (this.token.getTipo() == Token.TIPO_REAL) {
            verificarOperadores();
        } else {
            throw new RuntimeException("Há um valor inválido na atribuição!");
        }
    }

    private void verificarOperadores() {
        this.token = this.lexico.nextToken();

        if (this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            return;
        }

        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            tiposAtribuicao();
        } else {
            throw new RuntimeException("Há um valor inválido na atribuição!");
        }

    }

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
