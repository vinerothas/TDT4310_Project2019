package main;

import javafx.stage.Stage;

import java.util.ArrayList;

public class LanguageCount {

    static void countLanguages(){
        ArrayList<LangCountHolder> uniqueWords = new ArrayList<>();
        int l = 0;
        for (int i = 0; i < Start.latticeSize; i++) {
            for (int j = 0; j < Start.latticeSize; j++) {
                if(Start.lattice[i][j].dead)continue;
                Word bestWord = null;
                double bestWeight = 0;
                for (Word word: Start.lattice[i][j].words) {
                    if(word.weight> bestWeight){
                        bestWeight = word.weight;
                        bestWord = word;
                    }
                }
                if(bestWord != null){
                    boolean found = false;
                    for (LangCountHolder uniqueWord: uniqueWords) {
                        if(uniqueWord.literal.equals(bestWord.literal)){
                            found = true;
                            uniqueWord.count++;
                            break;
                        }
                    }
                    if(!found) {
                        uniqueWords.add(new LangCountHolder(bestWord.literal,l));
                        l++;
                    }
                }
            }
        }
        Start.languages[Start.languageCountIndex++] = l;
        if(uniqueWords.size()<20){
            printUniqueWords(uniqueWords);
        }
    }

    static void plotLanguages(Stage stage){
        ArrayList<LangCountHolder> uniqueWords = new ArrayList<>();
        int l = 1;
        int[][] points = new int[Start.latticeSize][Start.latticeSize];
        for (int i = 0; i < Start.latticeSize; i++) {
            for (int j = 0; j < Start.latticeSize; j++) {
                if(Start.lattice[i][j].dead){
                    points[i][j]=0;
                    continue;
                }

                Word bestWord = null;
                double bestWeight = 0;
                for (Word word: Start.lattice[i][j].words) {
                    if(word.weight> bestWeight){
                        bestWeight = word.weight;
                        bestWord = word;
                    }
                }
                if(bestWord != null){
                    boolean found = false;
                    for (LangCountHolder uniqueWord: uniqueWords) {
                        if(uniqueWord.literal.equals(bestWord.literal)){
                            found = true;
                            uniqueWord.count++;
                            points[i][j] = uniqueWord.type;
                            break;
                        }
                    }
                    if(!found) {
                        uniqueWords.add(new LangCountHolder(bestWord.literal,l));
                        l++;
                        points[i][j] = l;
                    }
                }else{
                    points[i][j] = 1;
                }
            }
        }

        System.out.println("Languages: "+l);
        printUniqueWords(uniqueWords);

        ScatterPlot.plot(stage,points,l);

    }

    static void printUniqueWords(ArrayList<LangCountHolder> uniqueWords){
        StringBuilder sb = new StringBuilder();
        for (LangCountHolder lc: uniqueWords) {
            sb.append(lc.literal);
            sb.append(":");
            sb.append(lc.count);
            sb.append(" ");
        }
        System.out.println(sb);
    }

}
