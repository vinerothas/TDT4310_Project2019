package main;

import java.util.ArrayList;
import java.util.Iterator;

public class Agent {

    ArrayList<Word> words = new ArrayList<>();
    double learningAbility;
    int i;
    int j;
    double age = 0;
    double sumWeight = 0;
    boolean dead = false;

    Agent(int i, int j){
        learningAbility = Rand.r.nextDouble();
        this.i = i;
        this.j = j;

        Word word = new Word();
        words.add(word);
        updateWeight(1,word);
    }

    void trigger(){
        age+=1;
        double pComm = Rand.r.nextDouble();
        if(pComm < Start.communicationProbability){
            communicate();
        }else{
            double avgWeight = Start.totalSumWeight/(double)Start.alive;
            double pSurv = Math.exp(-age*Start.a)*(1-Math.exp(-Start.b*sumWeight/avgWeight));
            double p = Rand.r.nextDouble();
            if(pSurv>p){ //breed
                breed();
            }else { //die
                Start.totalSumWeight -= sumWeight;
                sumWeight = 0;
                Start.alive--;
                Start.deaths++;
                dead = true;
            }
        }

    }

    void communicate(){
        //use a single words from inventory by weight probability
        // if empty inventory, generate new word
        //check if a random neighbour has the same word

        Word pickedWord = null;
        double prob = Rand.r.nextDouble()*sumWeight;
        double treshold = 0;
        if(words.isEmpty()){
            //generate random new word and use it
            pickedWord = new Word();
            words.add(pickedWord);
            updateWeight(1,pickedWord);
        }else{
            //pick a word to communicate with
            for (Word word : words) {
                treshold+=word.weight;
                if(prob<treshold){
                    pickedWord = word;
                    break;
                }
            }
        }



        // find a valid direction to communicate with a neighbour
        Agent neighbour = null;
        int alive = 0;
        if (i != 0 && !Start.lattice[i-1][j].dead){
            alive++;
            neighbour = Start.lattice[i-1][j];
        }
        if (j!=Start.latticeSize-1 && !Start.lattice[i][j+1].dead){
            alive++;
            neighbour = Start.lattice[i][j+1];
        }
        if(i != Start.latticeSize-1 && !Start.lattice[i+1][j].dead){
            alive++;
            neighbour = Start.lattice[i+1][j];
        }
        if(j!=0 && !Start.lattice[i][j-1].dead){
            alive++;
            neighbour = Start.lattice[i][j-1];
        }

        if(alive==0)return; //no valid neighbours
        else if (alive>1){ //multiple valid neighbours, choose at random
            while(true){
                int direction = Rand.r.nextInt(4); //0-up 1-right 2-down 3-left
                if(direction==0){
                    if (i == 0 || Start.lattice[i-1][j].dead)continue;
                    neighbour = Start.lattice[i-1][j];
                }else if(direction == 1){
                    if (j==Start.latticeSize-1 || Start.lattice[i][j+1].dead)continue;
                    neighbour = Start.lattice[i][j+1];
                }else if(direction==2){
                    if (i == Start.latticeSize-1 || Start.lattice[i+1][j].dead)continue;
                    neighbour = Start.lattice[i+1][j];
                }else if(direction == 3){
                    if (j==0 || Start.lattice[i][j-1].dead)continue;
                    neighbour = Start.lattice[i][j-1];
                }
                break;
            }
        }

        if(pickedWord== null){ //fix inaccurate weight sums
            recomputeWeights();
            communicate();
            return;
        }
        boolean success = neighbour.hear(pickedWord.literal);
        if(success){
            updateWeight(learningAbility, pickedWord);
            Start.communicationSuccesses++;
        }else{
            updateWeight(-learningAbility, pickedWord);
            Start.communicationFailures++;
        }

    }

    boolean hear(String literal){
        Iterator<Word> it = words.iterator();
        while(it.hasNext()){
            Word word = it.next();
            if(word.literal.equals(literal)){
                //matching word found, increase its weight
                updateWeight(learningAbility, word);
                return true;
            }
        }

        //no match found, add word to inventory
        Word newWord = new Word(literal);
        words.add(newWord);
        updateWeight(1, newWord);
        return false;
    }

    void breed(){
        // find an empty spot in the neighbourhood
        Agent spot = null;
        int spots = 0;
        if (i != 0 && Start.lattice[i-1][j].dead){
            spots++;
            spot = Start.lattice[i-1][j];
        }
        if (j!=Start.latticeSize-1 && Start.lattice[i][j+1].dead){
            spots++;
            spot = Start.lattice[i][j+1];
        }
        if(i != Start.latticeSize-1 && Start.lattice[i+1][j].dead){
            spots++;
            spot = Start.lattice[i+1][j];
        }
        if(j!=0 && Start.lattice[i][j-1].dead){
            spots++;
            spot = Start.lattice[i][j-1];
        }

        if(spots==0)return; //no valid spots
        else if (spots>1){ //multiple valid spots, choose at random
            while(true){
                int direction = Rand.r.nextInt(4); //0-up 1-right 2-down 3-left
                if(direction==0){
                    if (i == 0 || !Start.lattice[i-1][j].dead)continue;
                    spot = Start.lattice[i-1][j];
                }else if(direction == 1){
                    if (j==Start.latticeSize-1 || !Start.lattice[i][j+1].dead)continue;
                    spot = Start.lattice[i][j+1];
                }else if(direction==2){
                    if (i == Start.latticeSize-1 || !Start.lattice[i+1][j].dead)continue;
                    spot = Start.lattice[i+1][j];
                }else if(direction == 3){
                    if (j==0 || !Start.lattice[i][j-1].dead)continue;
                    spot = Start.lattice[i][j-1];
                }
                break;
            }
        }

        spot.reproduce(this);
    }

    void reproduce(Agent parent){
        double pMut = Rand.r.nextDouble();
        if(pMut<Start.mutationProbability)learningAbility = Rand.r.nextDouble();
        else learningAbility = parent.learningAbility;

        dead = false;
        words = new ArrayList<>();
        age = 0;
        Start.alive++;
        Start.births++;

        Word word = null;
        pMut = Rand.r.nextDouble();
        if(pMut<Start.mutationProbability){ // generate new word
            word = new Word();
        }else { // inherit best word
            double bestWeight = 0;
            Word bestWord = null;
            for (Word w: parent.words) {
                if (w.weight > bestWeight) {
                    bestWeight = w.weight;
                    bestWord = w;
                }
            }
            if (bestWord == null) return; // no inherited word

            pMut = Rand.r.nextDouble();
            word = new Word(bestWord.literal);
            if(pMut<Start.mutationProbability){ //mutate the inherited word
                word.mutate();
            }
        }

        words.add(word);
        updateWeight(1,word);

    }

    void updateWeight(double change, Word word){
        if (change<0 && -change > word.weight) {
            sumWeight -= word.weight;
            Start.totalSumWeight -= word.weight;
            words.remove(word);
        }else{
            word.weight += change;
            sumWeight += change;
            Start.totalSumWeight += change;
        }
    }

    void recomputeWeights(){
        Start.totalSumWeight -= sumWeight;
        sumWeight = 0;
        for (Word word: words) {
            Start.totalSumWeight += word.weight;
            sumWeight += word.weight;
        }
    }

}
