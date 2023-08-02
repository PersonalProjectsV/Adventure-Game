package com.vhuthu.adventuregame;

import com.vhuthu.adventuregame.controller.Controller;
import com.vhuthu.adventuregame.models.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = createMainScene();
        Stage details=createSavedGames(primaryStage);
        Stage saving=createSaveStage(primaryStage);
        Controller controller = new Controller();


        controller.connectToUI(primaryStage,scene, details,saving);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Saving Dog Adventure");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    private Scene createMainScene() {
        BorderPane root = new BorderPane();


        MenuBar menuBar = new MenuBar();

        menuBar.setPrefSize(Double.MAX_VALUE,45);

        // --- Menu File
        Menu menuFile = new Menu("Game");
        menuBar.setId("menu");
        MenuItem save=new MenuItem("Save");
        MenuItem restart=new MenuItem("Restart");
        MenuItem savedGames=new MenuItem("Saved Games");
        MenuItem close=new MenuItem("Close");
        menuFile.getItems().addAll(save,restart,savedGames,close);





        menuBar.getMenus().add(menuFile);
        root.setTop(menuBar);

        Insets insets = new Insets(10);

        VBox left = new VBox(30);

        VBox right = new VBox();
        left.setMaxWidth(Double.MAX_VALUE);
        left.setFillWidth(true);

        ListView inventory = new ListView<>();
        inventory.setId("inventory");

        Pane forins=new Pane();

        ScrollPane forinss = new ScrollPane();

        forinss.setStyle("-fx-background-color: transparent");
        //forinss.setPadding(new Insets(10,10,10,10));

        forinss.setVvalue(1);
        forinss.setPadding(new Insets(60,55,40,55));
        forins.getChildren().add(forinss);
        //TextFlow instruction = new TextFlow();
        //instruction.setTextAlignment(TextAlignment.CENTER);
        Text txt = new Text();

        txt.setTextAlignment(TextAlignment.CENTER);

        txt.setId("PaneText");
        txt.setFill(Color.WHITE);
        txt.setWrappingWidth(495);
        //instruction.setMaxWidth(570);
        //instruction.getChildren().add(txt);

        forinss.setMaxWidth(650);

        forinss.setMaxHeight(580);
        forinss.setContent(txt);
        //forins.getChildren().add(instruction);
        root.getChildren().add(txt);
        forins.setMinWidth(598);
        forins.setMinHeight(576);
        forins.setMaxWidth(598);
        forins.setMaxHeight(576);

        inventory.setMaxHeight(300);
        inventory.setPrefWidth(350);

        File imageee = new File("picture.jpg");
        Image image = new Image(imageee.toURI().toString());
        forins.setBackground(new Background(
                new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true))));


        TextField inputText = new TextField();

        HBox hBox=new HBox();


        Label game = new Label("ADVENTURE GAME");
        style(game);

        Label whats = new Label("WHATS NEXT?");
        style(whats);

        HBox ScoreS=new HBox();

        Label score=new Label("Score:  ");
        TextField scoreDisp=new TextField();
        scoreDisp.setId("score");
        scoreDisp.setMaxWidth(45);
        scoreDisp.setMaxHeight(30);
        scoreDisp.setEditable(false);



        hBox.getChildren().addAll(game);

        ScoreS.getChildren().addAll(score,scoreDisp);

        inputText.setMaxHeight(Double.MAX_VALUE);
        inputText.setId("input");

        hBox.setSpacing(15);

        //root.setBackground(new Background(new BackgroundFill(Color.rgb(40, 39, 54), CornerRadii.EMPTY, Insets.EMPTY)));



        score.setFont(new Font(score.getFont().getName(), 20));
        inputText.setPrefSize(50, 75);
        left.getChildren().addAll(hBox, forins, whats, inputText);

        right.setSpacing(30);

        Label inv = new Label("INVENTORY");
        style(inv);
        Label map = new Label("MAP");
        style(map);

        root.setLeft(left);
        root.setRight(right);

        Pane r = new Pane();
        right.setFillWidth(true);
        r.setPrefHeight(375);
        r.setPrefWidth(35);
        r.setId("mapPane");


        right.getChildren().addAll(inv, inventory, map, r);

        createGameSpace(r);

        GridPane gr=new GridPane();
        gr.setGridLinesVisible(false);
        gr.setPadding(new Insets(10, 10, 10, 10));
        gr.setVgap(50);
        gr.setHgap(50);
        r.getChildren().add(gr);
        gr.setId("gr");

        BorderPane.setMargin(left, insets);
        BorderPane.setMargin(right, insets);

        try {

            Label soundL=new Label("Toggle sound");
            soundL.setFont(new Font(score.getFont().getName(), 20));
            hBox.getChildren().add(soundL);

            ScoreS.setSpacing(10);
            ScoreS.setAlignment(Pos.CENTER);
            StackPane stackPane=new StackPane();
            FileInputStream inputstreamOff = new FileInputStream("images/mute.png");
            Image offImage = new Image(inputstreamOff);
            ImageView offImageView = new ImageView(offImage);
            offImageView.setId("off");


            FileInputStream inputstreamOn = new FileInputStream("images/sound.png");
            Image onImage = new Image(inputstreamOn);
            ImageView onImageView = new ImageView(onImage);

            onImageView.setId("on");
            onImageView.setFitHeight(54);
            stackPane.getChildren().addAll(onImageView,offImageView);

            ScoreS.getChildren().addAll(soundL,stackPane);
        }
        catch (Exception ex){}

        left.getChildren().add(ScoreS);
        return new Scene(root, 1000, 900);
    }

    private void style(Label label){
        label.setPadding(new Insets(5, 5, 5, 5));
        label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        label.setFont(new Font(label.getFont().getName(), 20));
    }
    private void createGameSpace(Pane pane) {

        double x = pane.getLayoutX() + 10;
        double y = pane.getLayoutY() + 10;
        double triX = x - 10;
        double triY = y - 10;
        Player player = new Player();
        player.setId("player");
        player.getPoints().addAll(new Double[]{
                triX, triY,
                triX, triY + 20,
                triX + 15, triY + 10.0});
        player.setFill(Color.CADETBLUE);
        player.toFront();
        pane.getChildren().add(player);
    }

    public Stage createSaveStage(Stage owner){
        Stage stage = new Stage();

        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        root.setPadding(new Insets(5));

        root.setAlignment(Pos.CENTER);

        ListView forUsers=new ListView();
        forUsers.setId("forUsers");
        root.getChildren().add(forUsers);

        Button newSave=new Button("New Save");
        newSave.setId("newSave");

        root.getChildren().add(newSave);
        VBox box=new VBox();

        Label userNameL=new Label("Enter your name");
        userNameL.setVisible(false);
        userNameL.setId("usernameL");
        TextField userNameT=new TextField();
        userNameT.setId("usernameT");
        userNameT.setVisible(false);
        box.getChildren().addAll(userNameL,userNameT);



        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        Button ok=new Button("Save");
        ok.setId("saveBtn");
        Button cancel=new Button("Cancel");
        cancel.setId("btnCancel");
        hBox.getChildren().addAll(ok,cancel);

        root.getChildren().addAll(box,hBox);


        // bit of a cheat, but really too simple to create a new controller

        stage.setScene(new Scene(root));

        // set stage details
        stage.setTitle("Save");

        // only has close button
        stage.initStyle(StageStyle.UTILITY);

        // rest of application cannot be interacted with until this stage is closed


        // this stage belongs to another, i.e. will close if the owner does
        stage.initOwner(owner);

        stage.setMinHeight(250);
        return stage;
    }
    public Stage createSavedGames(Stage owner) {
        Stage stage = new Stage();

        VBox root = new VBox();
        root.setSpacing(10);
        root.setFillWidth(true);
        root.setPadding(new Insets(5));

        ListView forUsers=new ListView();
        forUsers.setId("forUsers");
        root.getChildren().add(forUsers);

        // bit of a cheat, but really too simple to create a new controller

        stage.setScene(new Scene(root));

        // set stage details
        stage.setTitle("Saved");

        // only has close button
        stage.initStyle(StageStyle.UTILITY);

        // rest of application cannot be interacted with until this stage is closed


        // this stage belongs to another, i.e. will close if the owner does
        stage.initOwner(owner);

        stage.setMinHeight(250);
        return stage;
    }

}