
package compiladorl3;
public class CompiladorL3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Lexico lexico = new Lexico("src\\compiladorl3\\codigo.txt"); //pega o arquivo txt como entrada
        Token t = null; //inicialização do token (classe Token)
        while((t = lexico.nextToken()) != null){ //enquanto o token não acabar, o próximo char será lido
            System.out.println(t.toString()); //classe Token armazena os tipos de tokens
        }


    }
    
}
