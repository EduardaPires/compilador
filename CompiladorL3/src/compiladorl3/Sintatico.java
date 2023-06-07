/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

/**
 *
 * @author rod e duds
 */

public class Sintatico {
    private Lexico lexico;
    private Token token;
    
    public Sintatico(Lexico lexico){
        this.lexico = lexico;
    }
    
    public void mainDeclaration(){//S determina estado inicial
        this.token = this.lexico.nextToken();
        if(!token.getLexema().equals(":(")){
            throw new RuntimeException("Falta o primeiro termo da função principal ':(')");
        }
        this.token = this.lexico.nextToken();

        if(!token.getLexema().equals("main")){
            throw new RuntimeException("Falta o segundo termo da função principal 'main' ");
        }

        // n entrou pq tem token main, pega próximo.
        this.token = this.lexico.nextToken();
        if(!token.getLexema().equals("(")){
            throw new RuntimeException("faltou abrir o parênteses da main!");
        }
        // n entrou pq tem token "(", pega o próximo.

        this.token = this.lexico.nextToken();
        if(!token.getLexema().equals(")")){
            throw new RuntimeException("faltou fechar o parênteses da main!");
        }
        // n entrou pq tem token ")", pega o próximo.
        this.token = this.lexico.nextToken();
        
        this.insideMain(); // chama o B (entra no B)

        // só continua para esse if se as chamadas das funções seguintes não der erro algum.
        if(this.token.getTipo() == Token.TIPO_FIM_CODIGO){
            System.out.println("Tudo certo com seu código!");        
        }else{
            throw new RuntimeException("Algum erro ocorreu.");
            // teste
        }
    }
    
    private void insideMain(){ // 
        if(!this.token.getLexema().equals("{")){  // verifica se abriu o "{ da main"
            throw new RuntimeException("Oxe, tave esperando um \"{\" pertinho de " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();  // pega próximo token.

        this.CS();  // chama a função de CS
        /*Ao chegar aqui nosso código está assim:
         * main(){
         * aqui ele vai entrar na funcao CS
         * 
         */

         // Esse if só será executado se não houver nenhum erro em CS e nas suas chamadas seguintes.
        if(!this.token.getLexema().equals("}")){  // verifica se fechou o "}" da main
            throw new RuntimeException("Oxe, tava esperando um \"}\" pertinho de " + this.token.getLexema());
        }        
        this.token = this.lexico.nextToken();        
    }
    
    private void CS(){  //
        if((this.token.getTipo() == Token.TIPO_IDENTIFICADOR) ||
            this.token.getLexema().equals("int") ||
            this.token.getLexema().equals("float")){
            
            this.C();
            this.CS();
        }else{
        
        }       
    }
    
    private void C(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.ATRIBUICAO();            
        }else if(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float")){
            this.DECLARACAO();
        }else{
            throw new RuntimeException("Oxe, eu tava esperando tu "
                    + "declarar um comando pertinho de :" + this.token.getLexema());
        }
    }
        
    private void DECLARACAO(){
        if(!(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float"))){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Tu vacilou na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(!this.token.getLexema().equalsIgnoreCase(";")){
            throw new RuntimeException("Tu vacilou  na delcaração de variável. "
                    + "Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();        
    }
    
    private void ATRIBUICAO(){
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        this.E();
        if(!this.token.getLexema().equals(";")){
            throw new RuntimeException("Erro na atribuição. Pertinho de: " + this.token.getLexema());
        }
        this.token = this.lexico.nextToken();                
    }
    
    private void E(){
        this.T();
        this.El();
    }
    
    private void El(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.OP();
            this.T();
            this.El();
        }else{        
        }
    }
    
    private void T(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR || 
                this.token.getTipo() == Token.TIPO_INTEIRO ||
                this.token.getTipo() == Token.TIPO_REAL){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um identificador "
                    + "ou número pertinho de " + this.token.getLexema());
        }
    }
    
    private void OP(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token = this.lexico.nextToken();
        }else{
            throw new RuntimeException("Oxe, era para ser um operador "
                    + "aritmético (+,-,/,*) pertinho de "  + 
                    this.token.getLexema());
        }
    }
    
    
    
}
