package main;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Start {

    static final int latticeSize = 60;
    static final int wordSize = 4;  //not specified - doesn't change the results
    static final double a = 0.05; //population turnover constant
    static final double b = 5; //population turnover constant
    static final double communicationProbability = 0.15;
    static final double mutationProbability = 0.001;
    static final int iterations = 100;
    static final int seed = -1;

    static final boolean linePlot = false;
    static final boolean scatterPlot = true;

    static final int countInterval = 1;
    static final boolean languageCount = true;
    static int languageCountIndex = 0;
    static final boolean learningCount = false;
    static int learningCountIndex = 0;

    static Agent[][] lattice;
    static double totalSumWeight = 0;
    static int alive = latticeSize*latticeSize;
    static int communicationSuccesses = 0;
    static int communicationFailures = 0;
    static int births = 0;
    static int deaths = 0;
    static Integer[] languages = new Integer[iterations/countInterval];
    Double[] learningAverage = new Double[iterations/countInterval];

    void start(Stage stage) {

        if(seed != -1)Rand.r = new Random(seed);


        lattice = new Agent[latticeSize][latticeSize]; //book indexing
        for (int i = 0; i < latticeSize; i++) {
            for (int j = 0; j < latticeSize; j++) {
                lattice[i][j] = new Agent(i,j);
            }
        }

        for (int g = 0; g < iterations; g++) {
            for (int i = 0; i < latticeSize; i++) {
                for (int j = 0; j < latticeSize; j++) {
                    if(!lattice[i][j].dead)lattice[i][j].trigger();
                }
            }

            if (languageCount && linePlot){
                if (g % countInterval == 0) {
                    LanguageCount.countLanguages();
                    System.out.println(languages[languageCountIndex - 1]);
                }
            }
            if(learningCount){
                if (g % countInterval == 0) {
                    calculateLearningAbility();
                    System.out.println(learningAverage[learningCountIndex - 1]);
                }
            }
        }

        System.out.println("communicationSuccesses: "+communicationSuccesses);
        System.out.println("communicationFailures: "+communicationFailures);
        System.out.println("births: "+births);
        System.out.println("deaths: "+deaths);
        System.out.println("alive: "+alive);
        System.out.println();
        if(languageCount){
            if(linePlot)LinePlot.plot(stage,languages,"Languages");
            if(scatterPlot)LanguageCount.plotLanguages(stage);
        }
        if(learningCount)LinePlot.plot(stage,learningAverage,"Learning ability average");

    }



    void calculateLearningAbility(){
        double sum = 0;
        for (int i = 0; i < latticeSize; i++) {
            for (int j = 0; j < latticeSize; j++) {
                if(lattice[i][j].dead)continue;
                sum += lattice[i][j].learningAbility;
            }
        }
        sum/=alive;
        learningAverage[learningCountIndex++] = sum;
    }

    /*
    double totTrueWeight = 0;
    double totWeight = 0;
    for (int i = 0; i < latticeSize; i++) {
        for (int j = 0; j < latticeSize; j++) {
            if(!lattice[i][j].dead){
                totWeight+=lattice[i][j].sumWeight;
                for (Word word: lattice[i][j].words) {
                    totTrueWeight+=word.weight;
                }
            }
        }
    }
    System.out.println("totalSumWeight: "+totalSumWeight+" totWeight:"+totWeight+" totTrueWeight:"+totTrueWeight);
     */
}
