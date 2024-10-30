package models;

import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Storage {

    private int[] scoreInt = new int[15];
    private int sum1;
    private int bonus1;
    private int total1;
    private int sum2;
    private int bonus2;
    private int total2;
    Die[] dice = new Die[5];
    YatzyResultCalculator points;

    public Storage(RaffleCup raffleCup) {

    points = new YatzyResultCalculator(raffleCup.getDice());

    }
    //Update Sum and Bonus
    private void updateSum(){

        int sum = 0;

        for (int index = 0; index < 6; index++) {

            sum += scoreInt[index];
        }

        this.sum1 = sum;

        if (sum >= 63) bonus1 = 50;
        else bonus1 = 0;
    }

    //Update Total
    private void updateTotal(){

        int total = 0;

        for(int index = 5; index < scoreInt.length; index++) {

            total += scoreInt[index];
        }

        total += sum1;

        total += bonus1;
    }

    public void updateScores(int arrayPlacement, YatzyResultCalculator points){

        if (arrayPlacement < 6)
            this.scoreInt[arrayPlacement] = points.upperSectionScore(arrayPlacement+1);

        if (arrayPlacement == 6)
            this.scoreInt[arrayPlacement] = points.onePairScore();

        if (arrayPlacement == 7)
            this.scoreInt[arrayPlacement] = points.twoPairScore();

        if (arrayPlacement == 8)
            this.scoreInt[arrayPlacement] = points.threeOfAKindScore();

        if (arrayPlacement == 9)
            this.scoreInt[arrayPlacement] = points.fourOfAKindScore();

        if (arrayPlacement == 10)
            this.scoreInt[arrayPlacement] = points.smallStraightScore();

        if (arrayPlacement == 11)
            this.scoreInt[arrayPlacement] = points.largeStraightScore();

        if (arrayPlacement == 12)
            this.scoreInt[arrayPlacement] = points.fullHouseScore();

        if (arrayPlacement == 13)
            this.scoreInt[arrayPlacement] = points.chanceScore();

        if (arrayPlacement == 14)
            this.scoreInt[arrayPlacement] = points.yatzyScore();

        updateSum();
        updateTotal();

    }

    public int[] getScoreInt(int arrayPlacement){

        updateScores(arrayPlacement,points);
        return this.scoreInt;

    }

    public int getSum1(){return this.sum1;}

    public int getBonus1(){return this.bonus1;}

    public int getTotal1(){return this.total1;}

}
