package main;

import java.util.Random;

public class Word {

    static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    String literal;
    double weight = 0;

    Word() {
        literal = "";
        for (int i = 0; i < Start.wordSize; i++) {
            literal+=alphabet[Rand.r.nextInt(alphabet.length)];
        }
    }

    Word(String literal) {
        this.literal = literal;
    }

    void mutate(){
        int position = Rand.r.nextInt(literal.length());
        int newChar = Rand.r.nextInt(alphabet.length);
        String newLiteral = "";
        for (int i = 0; i < literal.length(); i++) {
            if(i==position){
                newLiteral += alphabet[newChar];
            }else{
                newLiteral += literal.charAt(i);
            }
        }
        literal = newLiteral;
    }
}
