package models;

public class Storage {

    private int[] scoreInt = new int[18];
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

        scoreInt[15] = sum;

        if (sum >= 63) scoreInt[16] = 50;
        else scoreInt[16] = 0;
    }

    //Update Total
    private void updateTotal(){

        int total = 0;

        for(int index = 6; index < scoreInt.length-3; index++) {

            total += scoreInt[index];
        }

        total = total + scoreInt[15] + scoreInt[16];

        scoreInt[17] = total;
    }

    private void updateScores(int arrayPlacement, YatzyResultCalculator points){

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
}
