package gui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.RaffleCup;
import models.Die;
import models.YatzyResultCalculator;

import java.util.ArrayList;

public class YatzyGui extends Application {

    private static int firstUsedColumn = 1;
    private final ArrayList<TextField> scoreTextFields = new ArrayList<>();
    private final ArrayList<Label> diceMaster = new ArrayList<>();
    private final ArrayList<CheckBox> diceKeepBox = new ArrayList<>();
    private final ArrayList<GridPane> playerScores = new ArrayList<>();
    private final TextField sum1 = new TextField();
    private final TextField bonus1 = new TextField();
    private final TextField total1 = new TextField();
    private final TextField sum2 = new TextField();
    private final TextField bonus2 = new TextField();
    private final TextField total2 = new TextField();
    private final Label turnsLeftLbl = new Label();
    private final Button throwDice = new Button("Throw"); //Knappen til at kaste terninger
    private final int scoreTextFieldSize = 50; //Sætter størrelse på textfield for scores
    private int throwsLeft = 2; //Starter med at være 2 da den starter med at slå en gang for spiller
    RaffleCup raffleCup = new RaffleCup(); //Create raffleCup
    Tab spiller1 = new Tab("Spiller 1");
    Tab spiller2 = new Tab("Spiller 2");

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Yatzy");
        GridPane mainPane = new GridPane();
        GridPane diePane = new GridPane();
        playerScores.add(createScore());
        playerScores.add(createScore());

        //Laver TabPane med 2 tabs
        TabPane tabPane = new TabPane();

        mainPane.add(diePane,0,0);
        mainPane.add(tabPane,0,2);

        spiller1.setContent(playerScores.get(0));
        spiller2.setContent(playerScores.get(1));

        this.initContent(diePane);

        for(GridPane pane : playerScores)
                this.initContentScore(pane);

        tabPane.getTabs().addAll(spiller1,spiller2);
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Setup af de to gridpanes i UI med border
    private void initContent(GridPane diePane){

        diePane.setGridLinesVisible(false);

        diePane.setPadding(new Insets(20));
        diePane.setHgap(10);
        diePane.setVgap(10);

        terningerUpset(diePane);
        diePane.setStyle(setBorderColorForGridPane());
    }

    private void initContentScore(GridPane scorePane){

        scorePane.setGridLinesVisible(false);

        scorePane.setPadding(new Insets(20));
        scorePane.setHgap(10);
        scorePane.setVgap(10);

        scoreUpset(scorePane);
        scorePane.setStyle(setBorderColorForGridPane());
    }

    //row 1-5 skal bruges til terninger
    private void terningerUpset(GridPane pane){

        for(int index = 0; index < 5; index++){
            addTerning(pane,index);
            pane.add(addCheckBox(),index,1);
        }

        Die[] dice;

        raffleCup.throwDice();
        dice = raffleCup.getDice();

        for(int index = 0; index < dice.length; index++) diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));

        pane.add(turnsLeftLbl,1,4,2,1);
        turnsLeftLbl.setText(throwsLeft + " Throws Left");

        pane.add(throwDice,3,4);
        throwDice.setOnAction(event -> throwDiceUpdater());

    }

    //row 6+ skal bruges til score
    private void scoreUpset(GridPane pane){

        int start = 1;
        int startRow = 0;

        for(int index = start; index <= 6; index++){

            addEnSlagsType(pane,index,startRow);

            startRow++;

        }

        pane.add(addLabel("Sum"),firstUsedColumn,7);
        pane.add(sum1,firstUsedColumn+1,7);
        sum1.setPrefWidth(scoreTextFieldSize);

        pane.add(addLabel("Bonus"),firstUsedColumn,8);
        pane.add(bonus1,firstUsedColumn+1,8);
        bonus1.setPrefWidth(scoreTextFieldSize);

        startRow = 0;

        firstUsedColumn = firstUsedColumn + 5;

        for(int index = start; index <= 4; index++){

            addParOgEns(pane,index,startRow);
            startRow++;

        }
        addRest(pane,startRow);

    }

    //All add of Labels, TextFields and Buttons (This also counts visability of dice).
    private void addEnSlagsType(GridPane pane, int start, int startRow){

        pane.add(addLabel(start + "'ere"),firstUsedColumn,startRow);
        pane.add(addTextField(),firstUsedColumn+1,startRow);
        //pane.add(addButton(),firstUsedColumn+2,startRow);

    }

    //Tilføjer par og ens til listen i bunden.
    private void addParOgEns(GridPane pane, int start, int startRow){

        if(start <= 2){

            pane.add(addLabel(start + " par"),firstUsedColumn,startRow);
            pane.add(addTextField(),firstUsedColumn+1,startRow);
            //pane.add(addButton(),firstUsedColumn+2,startRow);

        }

        else{

            pane.add(addLabel(start + " ens"),firstUsedColumn,startRow);
            pane.add(addTextField(),firstUsedColumn+1,startRow);
            //pane.add(addButton(),firstUsedColumn+2,startRow);

        }

    }

    //Tilføjer resten af listen
    private void addRest(GridPane pane, int startRow){

        pane.add(addLabel("Lille straight"),firstUsedColumn,startRow);
        pane.add(addLabel("Stor straight"),firstUsedColumn,startRow+1);
        pane.add(addLabel("Fuldt hus"),firstUsedColumn,startRow+2);
        pane.add(addLabel("Chance"),firstUsedColumn,startRow+3);
        pane.add(addLabel("Yatzy"),firstUsedColumn,startRow+4);

        for(int index = startRow; index <= startRow+4; index++){

            pane.add(addTextField(),firstUsedColumn+1,index);
            //pane.add(addButton(),firstUsedColumn+2,index);

        }

        pane.add(addLabel("Total"),firstUsedColumn-5,startRow+6);
        pane.add(total1,firstUsedColumn-4,startRow+6);
        total1.setPrefWidth(scoreTextFieldSize);

    }

    //Laver labels
    private Label addLabel(String text){
        return  new Label(text);
    }

    //Laver textfield og tilføjer dem til ArrayList samt UI
    private TextField addTextField(){

        TextField textField = new TextField();
        textField.setPrefWidth(50);
        scoreTextFields.add(textField);

        int placement = scoreTextFields.size()-1;
        textField.setOnMouseClicked(event  -> this.chooseFieldAction(event,placement));

        return textField;
    }

    //Laver rectanglet og lbl til terningerne
    private void addTerning(GridPane pane, int column){

        Rectangle rectangle = new Rectangle(50,50);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);

        Label lbl = new Label();

        this.diceMaster.add(lbl);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle,lbl);

        pane.add(stackPane,column,0);
    }

    //Creater check og putter den i arrayListen for checkboxe
    private CheckBox addCheckBox(){

        CheckBox checkBox = new CheckBox();

        diceKeepBox.add(checkBox);

        return checkBox;
    }

    //Update Sum and Bonus
    private void updateSum(){

        int sum = 0;

        for(int index = 0; index < 6; index++) {

            if(scoreTextFields.get(index).getText().isEmpty()) sum += 0;

            else sum += Integer.parseInt(scoreTextFields.get(index).getText());
        }

        this.sum1.setText(Integer.toString(sum));

        if(sum >= 63) bonus1.setText("50");
        else bonus1.setText("0");

    }

    //Update Total
    private void updateTotal(){

        int total = 0;

        for(int index = 5; index < scoreTextFields.size(); index++) {

            if(scoreTextFields.get(index).getText().isEmpty()) total += 0;

            else total += Integer.parseInt(scoreTextFields.get(index).getText());
        }

        if(!sum1.getText().isEmpty()) total += Integer.parseInt(sum1.getText());

        if(!bonus1.getText().isEmpty()) total += Integer.parseInt(bonus1.getText());


        this.total1.setText(Integer.toString(total));

    }

    //Throw dice update
    private void throwDiceUpdater(){

        Die[] dice = new Die[5];

        if(throwsLeft <= 0) ;

        else if(throwsLeft == 3){

            raffleCup.throwDice();
            dice = raffleCup.getDice();


            for(int index = 0; index < dice.length; index++){

                diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));

            }
            throwsLeft--;

            turnsLeftLbl.setText(throwsLeft + " Throws Left");

        }

        else{

            raffleCup.throwDice();
            dice = raffleCup.getDice();


            for(int index = 0; index < dice.length; index++){

                if(!diceKeepBox.get(index).isSelected()) diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));

            }
            throwsLeft--;

            turnsLeftLbl.setText(throwsLeft + " Throws Left");

        }
    }

    //Når man vælger et sted at tilføje point, og bruger YatzyResultCalculator til at udregne point der skal tilføjes
    private void chooseFieldAction(Event event, int arrayPlacement){

        TextField textField = (TextField) event.getSource();

        Die[] dice = raffleCup.getDice();

        if (throwsLeft == 3);
        else {
            if(textField.getText().isEmpty()) {
                for (int index = 0; index < dice.length; index++) {

                    dice[index].setEyes(Integer.parseInt(diceMaster.get(index).getText()));

                }

                YatzyResultCalculator points = new YatzyResultCalculator(dice);

                if (arrayPlacement < 6)
                    textField.setText(Integer.toString(points.upperSectionScore(arrayPlacement + 1)));

                if (arrayPlacement == 6)
                    textField.setText(Integer.toString(points.onePairScore()));

                if (arrayPlacement == 7)
                    textField.setText(Integer.toString(points.twoPairScore()));

                if (arrayPlacement == 8)
                    textField.setText(Integer.toString(points.threeOfAKindScore()));

                if (arrayPlacement == 9)
                    textField.setText(Integer.toString(points.fourOfAKindScore()));

                if (arrayPlacement == 10)
                    textField.setText(Integer.toString(points.smallStraightScore()));

                if (arrayPlacement == 11)
                    textField.setText(Integer.toString(points.largeStraightScore()));

                if (arrayPlacement == 12)
                    textField.setText(Integer.toString(points.fullHouseScore()));

                if (arrayPlacement == 13)
                    textField.setText(Integer.toString(points.chanceScore()));

                if (arrayPlacement == 14)
                    textField.setText(Integer.toString(points.yatzyScore()));

                throwsLeft = 3;

                for (CheckBox checkBox : diceKeepBox) checkBox.setSelected(false);
                turnsLeftLbl.setText(throwsLeft + " Throws Left");

                updateSum();
                updateTotal();
            }
        }
    }

    // Sætter farve til kanten på de to GridPanes
    private String setBorderColorForGridPane(){

        String borderColor = "#252525";

        return "-fx-border:" + borderColor + "; -fx-border-width: 1; -fx-border-style: solid; -fx-border-insets: 5;";
    }

    private GridPane createScore(){
        GridPane scorePane = new GridPane();
        return (scorePane);

    }
}