/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tarci
 */
public class Lexico {
    private char[] conteudo;
    private int indiceConteudo;
    
    public Lexico(String caminhoCodigoFonte){
        try {
            String conteudoStr;
            conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            this.conteudo = conteudoStr.toCharArray();
            this.indiceConteudo = 0;                        
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    //Retorna próximo char teste
    private char nextChar(){
        return this.conteudo[this.indiceConteudo++];
    }
    
    //Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar(){
        return indiceConteudo < this.conteudo.length;
    }
    


    //Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back(){
        this.indiceConteudo--;
    }
    
    //Identificar se char é letra minúscula    
    private boolean isLetra(char c){
        return (c >= 'a') && (c <= 'z');
    }
    
    //Identificar se char é dígito
    private boolean isDigito(char c){
        return (c >= '0') && (c <= '9');
    }
    
    //Método retorna próximo token válido ou retorna mensagem de erro.
    public Token nextToken(){
        Token token = null;
        char c;
        int estado = 0;
        
        StringBuffer lexema = new StringBuffer();
        while(this.hasNextChar()){
            c = this.nextChar(); //ver porque n le tudo!!!         
            switch(estado){ //falta atribuição e tokens criados
                case 0:
                    if(c == ' ' || c == '\t' || c == '\n' || c == '\r' ){ //caracteres de espaço em branco ASCII tradicionais 
                        estado = 0;
                    }
                    else if(this.isLetra(c) || c == '_'){ //id
                        lexema.append(c);
                        estado = 1;
                    }
                    else if(this.isDigito(c)){ //real ou int
                        lexema.append(c);
                        estado = 2;
                    }
                     else if (c == '\''){//char
                         lexema.append(c);
                         estado = 6; 
                     }
                    else if(c == ')' || c == '(' || c == '{' || c == '}' || c == ',' ||c == ';'){//char especial
                        lexema.append(c);
                        estado = 5;
                    } 
                    else if (c == '<' || c == '>'){ //op relacional
                        lexema.append(c);
                        estado = 8;
                    }
                    else if (c == '='){
                        lexema.append(c);
                        estado = 12;
                    }
                    else if(c == '$'){//fim
                        lexema.append(c);
                        estado = 99;
                        this.back();
                    } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                        lexema.append(c);
                        estado = 10;
                    }
                    else{
                        lexema.append(c);
                        throw new RuntimeException("Erro: token inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 1:
                    if(this.isLetra(c) || this.isDigito(c) || c == '_'){
                        lexema.append(c);
                        estado = 1;                        
                    }else{
                        this.back();
                        String lex = lexema.toString();
                        if (lex.compareTo("int")==0 || lex.compareTo("float")==0 || lex.compareTo("if")==0 || lex.compareTo("else")==0 || lex.compareTo("main")==0 ||lex.compareTo("char")==0) {
                            estado = 9;
                        }
                        else {
                            return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);   
                        }                     
                    }
                    break;
                case 2:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 2;
                    }else if(c == '.'){
                        lexema.append(c);
                        estado = 3;
                    }
                    else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_INTEIRO);
                    }
                    break;
                case 3:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 4;
                    }else{
                        lexema.append(c);
                        throw new RuntimeException("Erro: número float inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 4:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 7;
                    }
                    else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 5:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL); 
                    //break;
                case 6:
                    if(this.isDigito(c) || this.isLetra(c)){
                        lexema.append(c);
                        estado = 7;
                    }
                    else {
                        lexema.append(c);
                        throw new RuntimeException("Erro: char mal formado \"" + lexema.toString() + "\"");
                    }
                    break;
                case 7:
                    if(c == '\''){
                        lexema.append(c);
                        estado = 11;
                    }
                    else {
                        lexema.append(c);
                        throw new RuntimeException("Erro: char mal formado \"" + lexema.toString() + "\"");
                    }
                    break;
                case 8:
                    if (c == '=') {
                        lexema.append(c);
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL); 
                    }
                    else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL); 
                    }
                    //break;
                case 9:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA); 
                    //break;
                case 10:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO); 
                    //break;
                case 11:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CHAR);
                case 12:
                    if (c == '=') {
                        lexema.append(c);
                        estado = 13;
                    }
                    else {
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_ATRIBUICAO);
                    }
                case 13:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                case 99:
                    return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO); 
                    //break;
            }
        }                
        return token;
    }   
}
