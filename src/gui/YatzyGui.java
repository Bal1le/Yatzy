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
import models.Storage;

import java.util.ArrayList;

public class YatzyGui extends Application {

    private static int firstUsedColumn = 1;
    private final ArrayList<TextField> scoreTextFields = new ArrayList<>();
    private final ArrayList<Label> diceMaster = new ArrayList<>();
    private final ArrayList<CheckBox> diceKeepBox = new ArrayList<>();
    private final ArrayList<GridPane> playerScores = new ArrayList<>();
    private final Label turnsLeftLbl = new Label();
    private final int[] pointSavior = new int[36];
    private final Button throwDice = new Button("Throw"); //Knappen til at kaste terninger
    private final int scoreTextFieldSize = 50; //Sætter størrelse på textfield for scores
    private int throwsLeft = 2; //Starter med at være 2 da den starter med at slå en gang for spiller
    RaffleCup raffleCup = new RaffleCup();
    Tab spiller1 = new Tab("Spiller 1");
    Tab spiller2 = new Tab("Spiller 2");

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Yatzy");
        GridPane mainPane = new GridPane();
        GridPane diePane = new GridPane();
        playerScores.add(createScore());
        playerScores.add(createScore());

        Storage showPointsStorage = new Storage(raffleCup);

        //Laver TabPane med 2 tabs
        TabPane tabPane = new TabPane();

        mainPane.add(diePane, 0, 0);
        mainPane.add(tabPane, 0, 2);

        spiller1.setContent(playerScores.get(0));
        spiller2.setContent(playerScores.get(1));

        this.initContent(diePane, showPointsStorage);

        for (GridPane pane : playerScores)
            this.initContentScore(pane);

        tabPane.getTabs().addAll(spiller1, spiller2);
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Setup af de to gridpanes i UI med border
    private void initContent(GridPane diePane, Storage storage) {

        diePane.setGridLinesVisible(false);

        diePane.setPadding(new Insets(20));
        diePane.setHgap(10);
        diePane.setVgap(10);

        terningerUpset(diePane, storage);
        diePane.setStyle(setBorderColorForGridPane());
    }

    private void initContentScore(GridPane scorePane) {

        scorePane.setGridLinesVisible(false);
        Storage storage = new Storage(this.raffleCup);

        scorePane.setPadding(new Insets(20));
        scorePane.setHgap(10);
        scorePane.setVgap(10);

        scoreUpset(scorePane, storage);
        scorePane.setStyle(setBorderColorForGridPane());
    }

    //row 1-5 skal bruges til terninger
    private void terningerUpset(GridPane pane, Storage storage) {

        for (int index = 0; index < 5; index++) {
            addTerning(pane, index);
            pane.add(addCheckBox(), index, 1);
        }

        Die[] dice;

        raffleCup.throwDice();
        dice = raffleCup.getDice();

        for (int index = 0; index < dice.length; index++)
            diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));

        pane.add(turnsLeftLbl, 1, 4, 2, 1);
        turnsLeftLbl.setText(throwsLeft + " Throws Left");

        pane.add(throwDice, 3, 4);
        throwDice.setOnAction(event -> throwDiceUpdater(storage));

    }

    //row 6+ skal bruges til score
    private void scoreUpset(GridPane pane, Storage storage) {

        int start = 1;
        int startRow = 0;

        for (int index = start; index <= 6; index++) {

            addEnSlagsType(pane, index, startRow, storage);

            startRow++;

        }

        startRow = 0;

        firstUsedColumn = firstUsedColumn + 5;

        for (int index = start; index <= 4; index++) {

            addParOgEns(pane, index, startRow, storage);
            startRow++;

        }

        addRest(pane, startRow, storage);

    }

    //All add of Labels, TextFields and Buttons (This also counts visability of dice).
    private void addEnSlagsType(GridPane pane, int start, int startRow, Storage storage) {

        pane.add(addLabel(start + "'ere"), firstUsedColumn, startRow);
        pane.add(addTextField(storage), firstUsedColumn + 1, startRow);
        //pane.add(addButton(),firstUsedColumn+2,startRow);

    }

    //Tilføjer par og ens til listen i bunden.
    private void addParOgEns(GridPane pane, int start, int startRow, Storage storage) {

        if (start <= 2) {

            pane.add(addLabel(start + " par"), firstUsedColumn, startRow);
            pane.add(addTextField(storage), firstUsedColumn + 1, startRow);
            //pane.add(addButton(),firstUsedColumn+2,startRow);

        } else {

            pane.add(addLabel(start + " ens"), firstUsedColumn, startRow);
            pane.add(addTextField(storage), firstUsedColumn + 1, startRow);
            //pane.add(addButton(),firstUsedColumn+2,startRow);

        }

    }

    //Tilføjer resten af listen
    private void addRest(GridPane pane, int startRow, Storage storage) {

        pane.add(addLabel("Lille straight"), firstUsedColumn, startRow);
        pane.add(addLabel("Stor straight"), firstUsedColumn, startRow + 1);
        pane.add(addLabel("Fuldt hus"), firstUsedColumn, startRow + 2);
        pane.add(addLabel("Chance"), firstUsedColumn, startRow + 3);
        pane.add(addLabel("Yatzy"), firstUsedColumn, startRow + 4);

        for (int index = startRow; index <= startRow + 4; index++) {

            pane.add(addTextField(storage), firstUsedColumn + 1, index);

        }

        pane.add(addLabel("Sum"), firstUsedColumn - 5, startRow + 3);
        pane.add(addTextField(storage), firstUsedColumn - 4, startRow + 3);

        pane.add(addLabel("Bonus"), firstUsedColumn - 5, startRow + 4);
        pane.add(addTextField(storage), firstUsedColumn - 4, startRow + 4);

        pane.add(addLabel("Total"), firstUsedColumn - 5, startRow + 6);
        pane.add(addTextField(storage), firstUsedColumn - 4, startRow + 6);
    }

    //Laver labels
    private Label addLabel(String text) {
        return new Label(text);
    }

    //Laver textfield og tilføjer dem til ArrayList samt UI
    private TextField addTextField(Storage storage) {

        TextField textField = new TextField();
        textField.setPrefWidth(50);
        scoreTextFields.add(textField);

        int placement1 = scoreTextFields.size() - 1;
        int placement2 = scoreTextFields.size() - 19;

        if (placement1 < 18) textField.setOnMouseClicked(event -> this.chooseFieldAction(event, placement1, storage));
        else textField.setOnMouseClicked(event -> this.chooseFieldAction(event, placement2, storage));

        return textField;
    }

    //Laver rectanglet og lbl til terningerne
    private void addTerning(GridPane pane, int column) {

        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);

        Label lbl = new Label();

        this.diceMaster.add(lbl);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, lbl);

        pane.add(stackPane, column, 0);
    }

    //Creater check og putter den i arrayListen for checkboxe
    private CheckBox addCheckBox() {

        CheckBox checkBox = new CheckBox();

        diceKeepBox.add(checkBox);

        return checkBox;
    }

    //Throw dice update
    private void throwDiceUpdater(Storage storage) {

        Die[] dice;
        if (throwsLeft == 3) {
            raffleCup.throwDice();
            dice = raffleCup.getDice();
            for (int index = 0; index < dice.length; index++) {
                diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));
            }
            throwsLeft--;
            turnsLeftLbl.setText(throwsLeft + " Throws Left");
            for (CheckBox keepBox : diceKeepBox) {
                keepBox.setDisable(false);
            }
        } else {
            raffleCup.throwDice();
            dice = raffleCup.getDice();
            for (int index = 0; index < dice.length; index++) {
                if (!diceKeepBox.get(index).isSelected())
                    diceMaster.get(index).setText(Integer.toString(dice[index].getEyes()));
            }
            for (int index = 0; index < dice.length; index++) {
                dice[index].setEyes(Integer.parseInt(diceMaster.get(index).getText()));
            }
            raffleCup.setDice(dice);
            throwsLeft--;
            turnsLeftLbl.setText(throwsLeft + " Throws Left");
            if (throwsLeft == 0) {
                for (int index = 0; index < scoreTextFields.size(); index++) {
                    TextField textField = scoreTextFields.get(index);
                    if (textField.getText().isEmpty()) pointSavior[index] = -1;
                    else pointSavior[index] = Integer.parseInt(textField.getText());
                }
                int[] potentialScore = new int[15];
                for (int index = 0; index < 15; index++) potentialScore = storage.getScoreInt(index);
                for (int index = 0; index < 15; index++) {
                    if (!scoreTextFields.get(index).isDisabled()) {
                        scoreTextFields.get(index).setText(Integer.toString(potentialScore[index]));
                        scoreTextFields.get(index + 18).setText(Integer.toString(potentialScore[index]));
                    }
                }
                for (CheckBox keepBox : diceKeepBox) {
                    keepBox.setDisable(true);
                }
            }
        }
    }

    //Når man vælger et sted at tilføje point, og bruger YatzyResultCalculator til at udregne point der skal tilføjes
    private void chooseFieldAction(Event event, int arrayPlacement, Storage storage) {

        TextField textField = (TextField) event.getSource();
        textField.setDisable(true);
        for (int index = 0; index < 36 - 3; index++) {
            if (index < 15 || index >= 18) {
                if (pointSavior[index] == -1) scoreTextFields.get(index).setText("");
            }
        }

        int[] scores = storage.getScoreInt(arrayPlacement);

        if (throwsLeft < 3) {
            if (textField.getText().isEmpty()) {

                textField.setText(Integer.toString(scores[arrayPlacement]));

                throwsLeft = 3;

                for (CheckBox checkBox : diceKeepBox) checkBox.setSelected(false);
                turnsLeftLbl.setText(throwsLeft + " Throws Left");

                if (spiller1.isSelected()) {
                    scoreTextFields.get(15).setText(Integer.toString(scores[15]));
                    scoreTextFields.get(16).setText(Integer.toString(scores[16]));
                    scoreTextFields.get(17).setText(Integer.toString(scores[17]));
                }
                if (spiller2.isSelected()) {
                    scoreTextFields.get(15 + 18).setText(Integer.toString(scores[15]));
                    scoreTextFields.get(16 + 18).setText(Integer.toString(scores[16]));
                    scoreTextFields.get(17 + 18).setText(Integer.toString(scores[17]));
                }
            }
        }
    }

    // Sætter farve til kanten på de to GridPanes
    private String setBorderColorForGridPane() {

        String borderColor = "#252525";

        return "-fx-border:" + borderColor + "; -fx-border-width: 1; -fx-border-style: solid; -fx-border-insets: 5;";
    }

    private GridPane createScore() {
        GridPane scorePane = new GridPane();
        return (scorePane);

    }
}