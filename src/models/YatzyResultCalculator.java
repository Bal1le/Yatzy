package models;

/**
 * Used to calculate the score of throws with 5 dice
 */
public class YatzyResultCalculator {

    private Die[] dice;
    /**
     *
     * @param dice
     */
    public YatzyResultCalculator(Die[] dice) {

        this.dice = dice;

    }

    /**
     * Calculates the score for Yatzy uppersection
     * @param eyes eye value to calculate score for. eyes should be between 1 and 6
     * @return the score for specified eye value
     */
    public int upperSectionScore(int eyes) {

        Die[] dice = this.dice;
        int score = 0;

        for (Die die : dice)
            if(die.getEyes() == eyes)
                score += die.getEyes();


        return score;
    }

    public int onePairScore() {

        for (int outerIndex = 0; outerIndex < this.dice.length-1; outerIndex++) {

            int paired = this.dice[outerIndex].getEyes();

            for(int innerIndex = outerIndex+1; innerIndex < this.dice.length; innerIndex++){

                if(paired == this.dice[innerIndex].getEyes())
                    return paired*2;

            }
        }


        return 0;
    }

    public int twoPairScore() {

        int firstPairScore = onePairScore();

        if(firstPairScore == 0) return 0;

        int whichPair = firstPairScore / 2;

        int secondPairScore = 0;

        for(int outerIndex = 0; outerIndex < this.dice.length - 1; outerIndex++){
            if(this.dice[outerIndex].getEyes() != whichPair){
                for(int innerIndex = outerIndex+1; innerIndex < this.dice.length; innerIndex++){
                    if(this.dice[outerIndex].getEyes() == this.dice[innerIndex].getEyes()){
                        secondPairScore = this.dice[outerIndex].getEyes()*2;
                        return firstPairScore + secondPairScore;

                    }

                }

            }
        }

        return 0;
    }

    public int threeOfAKindScore() {
        int[] eachKind = new int[6];

        for(Die die : this.dice)
            eachKind[die.getEyes()-1]++;

        for(int index = 0; index < eachKind.length; index++){
            if(eachKind[index] >= 3) return (index+1)*3;
        }

        return 0;
    }

    public int fourOfAKindScore() {

        int[] eachKind = new int[6];

        for(Die die : this.dice)
            eachKind[die.getEyes()-1]++;

        for(int index = 0; index < eachKind.length; index++){
            if(eachKind[index] >= 4) return (index+1)*4;
        }

        return 0;
    }

    public int smallStraightScore() {

        int[] eachKind = new int[6];

        for(Die die : this.dice)
            eachKind[die.getEyes()-1]++;

        for(int index = 0; index < eachKind.length-1;index++){

            if(eachKind[index] == 0) return 0;

        }

        return 15;
    }

    public int largeStraightScore() {
        int[] eachKind = new int[6];

        for(Die die : this.dice)
            eachKind[die.getEyes()-1]++;

        for(int index = 1; index < eachKind.length;index++){

            if(eachKind[index] == 0) return 0;

        }

        return 20;
    }

    public int fullHouseScore() {
        int[] eachKind = new int[6];

        for(Die die : this.dice)
            eachKind[die.getEyes()-1]++;

        for(int outerIndex = 0; outerIndex < eachKind.length;outerIndex++){

            if(eachKind[outerIndex] == 2){

                for(int innerIndex = 0; innerIndex < eachKind.length; innerIndex++){

                    if(eachKind[innerIndex] == 3) return (((outerIndex+1)*2) + ((innerIndex+1)*3));

                }
            }
        }

        return 0;
    }

    public int chanceScore() {
        int score = 0;

        for(Die die : this.dice) score += die.getEyes();

        return score;
    }

    public int yatzyScore() {
        for(int index = 0; index < this.dice.length-1; index++){

            if(dice[index].getEyes() != dice[index+1].getEyes()) return 0;

        }
        return 50;
    }
}
