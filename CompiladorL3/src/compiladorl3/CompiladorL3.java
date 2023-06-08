

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

public class CompiladorL3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Lexico lexico = new Lexico("C:/Learning/compilador/CompiladorL3/src/compiladorl3/codigo.txt"); //pega o arquivo txt como entrada
        Token t = null; //inicialização do token (classe Token)
        // ("C:/Users/Eduarda AP/OneDrive/Área de Trabalho/compilador/CompiladorL3/src/compiladorl3/codigo.txt"
        // while((t = lexico.nextToken()) != null){ //enquanto o token não acabar, o próximo char será lido
        //     System.out.println(t.toString()); //classe Token armazena os tipos de tokens
        // }

        Sintatico sintatico = new Sintatico(lexico);
        sintatico.mainDeclaration();


    }
    
}
