package com.vhuthu.adventuregame.controller;

import com.vhuthu.adventuregame.models.*;
import com.vhuthu.adventuregame.models.roomConnect;
import javafx.animation.PathTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Scanner;


public class Controller {
    public Controller() {

    }

    room[][] graph;
    LinkedList<roomConnect> linesF = new LinkedList<>();
    LinkedList<room> box = new LinkedList<>();
    ArrayList<String> connect = new ArrayList<>();
    ArrayList<Item> carry = new ArrayList<>();
    ArrayList<Item> allItems = new ArrayList<>();
    ListProperty<Item> observableAlbums = new SimpleListProperty<>();
    IntegerProperty scoreNum = new SimpleIntegerProperty(0);
    Integer run = 0;
    boolean playing = false;

    int identityLine = 0;
    int todraw = 0;

    Media coinMedia = new Media(new File("sound/pickCoin.mp3").toURI().toString());
    MediaPlayer coinMediaPlayer = new MediaPlayer(coinMedia);

    Media songM = new Media(new File("sound/last.mp3").toURI().toString());
    MediaPlayer songMediaPlayer = new MediaPlayer(songM);

    Media walkMedia = new Media(new File("sound/walk.mp3").toURI().toString());
    MediaPlayer walkMediaPlayer = new MediaPlayer(walkMedia);

    Media unlockMedia = new Media(new File("sound/unlock.mp3").toURI().toString());
    MediaPlayer wunlockMediaPlayer = new MediaPlayer(unlockMedia);

    Player player;
    Pane mapPane;
    room currentMove = null;
    room nextMove = null;
    GridPane gr;
    TextField input;
    roomConnect curPath = null;
    ListView inventory;
    ListView forUsers;
    TextField scoreDisp;
    Text paneTxt;
    ImageView on;
    ImageView off;
    ImageView curS;
    MenuBar menu;
    MenuItem restart;
    Scene scene;
    Stage stage;
    Stage mainStage;
    Stage saving;

    Button newGame;
    Button cancel;
    Button save;

    ListView<String> forOverwrite;
    TextField userName;
    Label userNameL;
    String intro;

    String dummyDots = "";


    /**
     * Finds the row in which a particular room is in
     */
    private int getRow(Rectangle e) {
        for (int x = 0; x < graph.length; x++) {
            for (int y = 0; y < graph.length; y++) {
                room dd = graph[x][y];
                if (dd == e) {
                    return x;
                }
            }
        }
        return -1;
    }
    /**
     * Finds the coloumn in which a particular room is in
     */
    private int getCol(Rectangle e) {
        for (int x = 0; x < graph.length; x++) {
            for (int y = 0; y < graph.length; y++) {
                room dd = graph[x][y];
                if (dd == e) {
                    return y;
                }
            }
        }
        return -1;
    }

    /**
     * Finds Line connecting two rooms
     */
    private roomConnect findConnect(room a, room b) {
        Character aa = ' ';
        Character bb = ' ';
        for (int x = 0; x < box.size(); x++) {
            room cur = box.get(x);
            if (cur.equals(a)) {
                aa = cur.getIdentifier();
            }
            if (cur.equals(b)) {
                bb = cur.getIdentifier();
            }
        }
        for (int x = 0; x < linesF.size(); x++) {
            String path = aa + "" + bb;
            roomConnect cur = linesF.get(x);
            if (cur.pathValue().equals(path)) {
                return cur;
            }
        }
        return null;
    }

    /**
     * Finds returns a room with a particular identity passed
     */
    private room getRoom(Character character) {
        for (int x = 0; x < box.size(); x++) {
            room y = box.get(x);
            if (y.getIdentifier() == character) {
                return y;
            }
        }
        return null;
    }

    /**
     *Draws all the rooms on the map
     */

    private void boxes() throws Exception {
        File file = new File("docs/map.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String string;
        int boxes = 0;
        room cur = null;
        while ((string = br.readLine()) != null) {
            if (string.contains("#")) {
                boxes++;
                continue;
            }
            if (boxes == 0) {
                String[] coordinate = string.split(",");
                int x = Integer.parseInt(coordinate[1]);
                int y = Integer.parseInt(coordinate[2]);
                if (coordinate.length == 4) {
                    int number = Integer.parseInt(coordinate[3]);
                    cur = firstLastR(number, coordinate[0]);
                    box.add(cur);
                } else {
                    cur = drawRect(coordinate[0]);
                    box.add(cur);
                }
                gr.add(cur, x, y);
            } else {
                connect.add(string);
            }

            cur.setVisible(false);
        }
    }

    /**
     * Represents map as a 2d aray
     */
    private void doGraphRoom() {
        int colCount = getColCount(gr);
        int rowCount = getRowCount(gr);
        graph = new room[rowCount][colCount];
        for (int i = 0; i < gr.getChildren().size(); i++) {
            Node child = gr.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                Integer colINdex = GridPane.getColumnIndex(child);
                if (rowIndex != null && colINdex != null) {
                    graph[rowIndex][colINdex] = (room) child;
                }
            }
        }
    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if (rowIndex != null) {
                    numRows = Math.max(numRows, rowIndex + 1);
                }
            }
        }
        return numRows;
    }

    private int getColCount(GridPane pane) {
        int numCols = pane.getColumnConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer colIndex = GridPane.getColumnIndex(child);
                if (colIndex != null) {
                    numCols = Math.max(numCols, colIndex + 1);
                }
            }
        }
        return numCols;
    }

    private void drawLines() {
        for (int x = 0; x < connect.size(); x++) {
            String cur = connect.get(x);
            Character a = cur.charAt(0);
            Character b = cur.charAt(2);
            room start = null;
            room end = null;
            for (int y = 0; y < box.size(); y++) {
                room current = box.get(y);
                Character check = current.getIdentifier();
                if (a.equals(check)) {
                    start = current;
                }
                if (b.equals(check)) {
                    end = current;
                }
            }
            roomConnect newoneF = draw(start.getLayoutX(), start.getLayoutY(), end.getLayoutX(), end.getLayoutY());
            newoneF.setFromTo(a, b);
            roomConnect newoneB = draw(end.getLayoutX(), end.getLayoutY(), start.getLayoutX(), start.getLayoutY());
            newoneB.setFromTo(b, a);
            linesF.add(newoneF);
            linesF.add(newoneB);
            mapPane.getChildren().addAll(newoneF, newoneB);
            newoneF.toBack();
            newoneB.toBack();
        }
    }

    public void connectToUI(Stage mainStage, Scene scene, Stage details, Stage saving) throws Exception {
        player = (Player) scene.lookup("#player");
        scoreDisp = (TextField) scene.lookup("#score");
        gr = (GridPane) scene.lookup("#gr");
        paneTxt = (Text) scene.lookup("#PaneText");
        scoreDisp.textProperty().bind(scoreNum.asString());
        mapPane = (Pane) scene.lookup("#mapPane");
        input = (TextField) scene.lookup("#input");
        inventory = (ListView) scene.lookup("#inventory");
        on = (ImageView) scene.lookup("#on");
        off = (ImageView) scene.lookup("#off");
        menu = (MenuBar) scene.lookup("#menu");
        this.scene = scene;
        this.saving = saving;
        this.mainStage = mainStage;
        stage = details;

        newGame = (Button) saving.getScene().lookup("#newSave");

        userNameL = (Label) saving.getScene().lookup("#usernameL");
        userName = (TextField) saving.getScene().lookup("#usernameT");
        cancel = (Button) saving.getScene().lookup("#btnCancel");
        save = (Button) saving.getScene().lookup("#saveBtn");

        forUsers = (ListView) details.getScene().lookup("#forUsers");

        forOverwrite = (ListView) saving.getScene().lookup("#forUsers");
        restart = menu.getMenus().get(0);

        paneTxt.setFill(Color.GREEN);


        player.setLayoutY(0);
        player.setLayoutX(0);
        curS = on;
        off.setVisible(false);
        off.setOnMouseClicked(event -> func());
        on.setOnMouseClicked(event -> func());

        inventory.setFocusTraversable(false);
        mapPane.setBackground(new Background(new BackgroundFill(Color.rgb(36, 47, 51), CornerRadii.EMPTY, Insets.EMPTY)));
        boxes();
        doGraphRoom();
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyPres(event);
            }
        });
        doReadFromXML();
        observableAlbums.set(FXCollections.observableArrayList(carry));
        inventory.itemsProperty().bind(observableAlbums);
        songMediaPlayer.play();
        songMediaPlayer.setVolume(0.1);

        Menu menuu = menu.getMenus().get(0);
        MenuItem save = menuu.getItems().get(0);
        MenuItem restart = menuu.getItems().get(1);
        restart.setOnAction(event -> reStart());

        save.setOnAction(event -> showing());

        MenuItem savedGames = menuu.getItems().get(2);
        savedGames.setOnAction(event -> getSaved());


        dummyDots += "............................................................................................................................";
    }

    private void showing() {

        saving.show();
        ArrayList<String> listSaved = new ArrayList();
        ListProperty<String> observeListSaved = new SimpleListProperty();
        observeListSaved.set(FXCollections.observableArrayList(listSaved));

        doFileNames(observeListSaved);
        forOverwrite.setItems(observeListSaved);

        forOverwrite.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("You are about to overwrite selected saved game.");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                String path = "docs/savedGames/" + newValue + ".txt";
                File file = new File(path);
                savePrintWriter(file);
            } else {

            }

        });

        newGame.setOnAction(event -> {
            userName.setVisible(true);
            userNameL.setVisible(true);
        });

        cancel.setOnAction(event -> saving.close());
        save.setOnAction(event -> {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Confirmation");
            alert1.setHeaderText("Saving game....");
            if (userName.getText().equals("") || observeListSaved.contains(userName.getText())) {
                alert1.setContentText("Game not saved,enter a valid name of check if it is not in use already");

            } else {
                String path = "docs/savedGames/" + userName.getText() + ".txt";
                File file = new File(path);
                savePrintWriter(file);
                alert1.setContentText("Game saved successfully");
            }
            alert1.show();
        });
    }

    private void savePrintWriter(File file) {
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.print(currentMove.getIdentifier() + "$");
            pw.print(visited() + "$");
            pw.print(identityLineS() + "$");
            pw.print(player.carryingString() + "$");
            pw.print(scoreNum.getValue() + "$");
            pw.print(currentMove.getIdentifier() + "," + nextMove.getIdentifier() + "," + curPath.getIdentifier());
            pw.close();
        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    private String identityLineS() {
        String linesId = "";
        for (int x = 0; x < linesF.size(); x++) {
            roomConnect cur = linesF.get(x);
            if (cur.isVisible()) {
                linesId += cur.getIdentifier() + ",";
            }
        }
        return linesId;
    }

    private String visited() {
        String identifier = "";
        for (int x = 0; x < box.size(); x++) {
            room xc = box.get(x);
            if (xc.isVisible()) {
                identifier += xc.getIdentifier() + ";" + xc.getInvenS() + "&";
                System.out.println("the string was  " + xc.getInvenS());
            }
        }
        return identifier;
    }

    private void doFileNames(ListProperty<String> observeListPaths) {
        String[] pathnames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File("docs/savedGames");

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            observeListPaths.add(pathname.split("\\.")[0]);
        }
    }

    private void getSaved() {

        ArrayList<String> listPaths = new ArrayList();
        ListProperty<String> observeListPaths = new SimpleListProperty();
        observeListPaths.set(FXCollections.observableArrayList(listPaths));

        doFileNames(observeListPaths);


        forUsers.setItems(observeListPaths);

        forUsers.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            if (linesF.size() == 0) {
                drawLines();
            }
            observableAlbums.clear();
            hideAll();
            player.setLayoutY(0);
            player.setLayoutX(0);
            player.toFront();
            String path = "docs/savedGames/" + newValue.toString() + ".txt";
            File file = new File(path);
            try {
                Scanner scan = new Scanner(file);
                String data = scan.nextLine();
                String dataArr[] = data.split("\\$");
                String rooms[] = dataArr[1].split("&");

                for (int x = 0; x < rooms.length; x++) {
                    String roomInven[] = rooms[x].split(";");
                    Character y = roomInven[0].charAt(0);
                    room cur = getRoom(y);


                    cur.setVisible(true);
                    cur.clear();

                    if (roomInven.length > 1) {
                        String inven[] = roomInven[1].split(",");
                        for (int i = 0; i < inven.length; i++) {
                            Integer r = Integer.parseInt(inven[i]);
                            Item now = getItem(r);
                            cur.addItem(now);
                        }
                    }

                }
                String lines[] = dataArr[2].split(",");
                for (int x = 0; x < lines.length; x++) {
                    Integer y = Integer.parseInt(lines[x]);
                    roomConnect cur = getLine(y);
                    cur.setVisible(true);
                }
                String inven[] = dataArr[3].split(",");
                for (int x = 0; x < inven.length; x++) {
                    if (!inven[x].equals("")) {
                        Integer y = Integer.parseInt(inven[x]);
                        Item cur = getItem(y);
                        carry.add(cur);
                        observableAlbums.add(cur);
                    }
                }

                Integer score = Integer.parseInt(dataArr[4]);
                scoreNum.set(score);

                String[] move = dataArr[5].split(",");
                currentMove = getRoom(move[0].charAt(0));
                nextMove = getRoom(move[1].charAt(0));
                curPath = getLine(Integer.parseInt(move[2]));

                player.setTranslateX(curPath.getEndX() - 8);
                player.setTranslateY(curPath.getEndY() - 10);

                linesLock();
            } catch (Exception io) {
                io.printStackTrace();
            }
        }));


        stage.show();
    }

    private roomConnect getLine(int id) {
        for (int x = 0; x < linesF.size(); x++) {
            roomConnect cur = linesF.get(x);
            System.out.println("current line id  " + cur.getIdentifier());
            if (cur.getIdentifier() == id)
                return cur;
        }
        return null;
    }

    private void hideAll() {
        for (int x = 0; x < linesF.size(); x++) {
            linesF.get(x).setVisible(false);
        }
        for (int x = 0; x < box.size(); x++) {
            box.get(x).setVisible(false);
        }
    }

    private Item getItem(int id) {
        for (int x = 0; x < allItems.size(); x++) {
            Item cur = allItems.get(x);
            if (cur.getIdentifier() == id)
                return cur;
        }
        return null;
    }

    private void func() {
        if (curS == on) {
            songMediaPlayer.setVolume(0);
            off.setVisible(true);
            on.setVisible(false);
            curS = off;
        } else {
            songMediaPlayer.setVolume(1);
            on.setVisible(true);
            off.setVisible(false);
            curS = on;
        }

    }

    private void reStart() {
        try {
            for (int x = 0; x < linesF.size(); x++) {
                linesF.get(x).setVisible(false);
            }
            for (int x = 0; x < box.size(); x++) {
                box.get(x).setVisible(false);
                box.get(x).lockAll();
            }

            linesF = new LinkedList<>();
            box = new LinkedList<>();
            connect = new ArrayList<>();
            carry = new ArrayList<>();
            allItems = new ArrayList<>();
            observableAlbums = new SimpleListProperty<>();
            scoreNum = new SimpleIntegerProperty(0);
            run = 0;
            playing = false;

            identityLine = 0;
            currentMove = null;
            nextMove = null;
            curPath = null;

            carry.removeAll(carry.subList(0, carry.size()));
            carry.removeAll(carry.subList(0, carry.size()));
            observableAlbums.removeAll(observableAlbums.subList(0, observableAlbums.size()));
            scoreNum = new SimpleIntegerProperty(0);
            run = 0;
            playing = false;
            player.dropAll();
            currentMove = null;
            nextMove = null;
            curPath = null;


            player.setTranslateY(2);
            player.setTranslateX(2);
            player.setLayoutY(0);
            player.setLayoutX(0);

            boxes();
            doGraphRoom();
            doReadFromXML();

            observableAlbums.set(FXCollections.observableArrayList(carry));
            inventory.itemsProperty().bind(observableAlbums);


        } catch (Exception c) {
        }
    }

    private void dolinesDoors(room cur, Line ln, Character dir) {

        ln.setStroke(Color.BROWN);
        ln.setStrokeWidth(5);

        if (cur.getDoor(dir).getShowDoor() != null) {
            cur.getDoor(dir).getShowDoor().setVisible(false);
        }
        mapPane.getChildren().add(ln);
        System.out.println(cur.getIdentifier());
        cur.getDoor(dir).setShowDoor(ln);
        cur.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (cur.isVisible() && cur.getDoor(dir).getLocked() == true) {
                cur.getDoor(dir).getShowDoor().setVisible(true);
            } else cur.getDoor(dir).getShowDoor().setVisible(false);
        });

    }

    private void linesLock() {
        for (int x = 0; x < box.size(); x++) {
            room cur = box.get(x);


            if (cur.getDoorE().getLocked() == true) {

                Line ln = new Line(cur.getLayoutX() + 20, cur.getLayoutY(), cur.getLayoutX() + 20, cur.getLayoutY() + 20);
                dolinesDoors(cur, ln, 'E');
            }
            if (cur.getDoorS().getLocked() == true) {
                Line ln = new Line(cur.getLayoutX(), cur.getLayoutY() + 20, cur.getLayoutX() + 20, cur.getLayoutY() + 20);
                dolinesDoors(cur, ln, 'S');
            }
            if (cur.getDoorW().getLocked() == true) {
                Line ln = new Line(cur.getLayoutX(), cur.getLayoutY(), cur.getLayoutX(), cur.getLayoutY() + 20);
                dolinesDoors(cur, ln, 'W');
            }
            if (cur.getDoorN().getLocked() == true) {
                Line ln = new Line(cur.getLayoutX(), cur.getLayoutY(), cur.getLayoutX() + 20, cur.getLayoutY());
                dolinesDoors(cur, ln, 'N');
            }
        }
    }

    private void keyPres(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (currentMove == null && input.getText().equals("enter")) {
                player.setLayoutY(box.getFirst().getLayoutY());
                player.setLayoutX(box.getFirst().getLayoutX());
                player.toFront();

                currentMove = box.getFirst();
                paneTxt.setText(paneTxt.getText() + "\n" + currentMove.toString() + "\n" + dummyDots);

            } else {
                try {
                    doSomething();
                } catch (Exception ex) {
                }
                if (curPath != null) {
                    curPath.setVisible(true);
                }
            }
            if (linesF.size() == 0) {
                drawLines();
            }
            if (currentMove != null) {
                input.clear();
                currentMove.setVisible(true);
                player.setRoom(currentMove);
            }
            if (todraw == 0) {
                linesLock();
            }
        }
    }

    /**
     * unlocks door
     */
    private void unlockDoor(Door door) {
        if (door.getLocked()) {
            boolean bo = player.hasKey(currentMove.getIdentifier());
            if (bo) {
                wunlockMediaPlayer.setStopTime(Duration.seconds(4));
                wunlockMediaPlayer.play();
                playing = true;
                door.unlock();

            }
        }
    }

    private void doingPath() {
        if (playing) {
            wunlockMediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    wunlockMediaPlayer.stop();
                    try {
                        doPath();
                    } catch (Exception c) {
                    }
                    playing = false;
                }
            });
            currentMove = nextMove;
        } else {
            doPath();
            currentMove = nextMove;
        }

    }

    private void doSomething() {


        if (todraw == 0) {
            linesLock();
            todraw++;
        }
        String inputt = input.getText();
        if (inputt.equals("e") || inputt.equals("E")) {
            if (currentMove.getDoorE().getLocked()) {
                unlockDoor(currentMove.getDoorE());
            }
            if (!currentMove.getDoorE().getLocked()) {
                if (findConnect(currentMove, east(getRow(currentMove), getCol(currentMove))) != null) {
                    nextMove = east(getRow(currentMove), getCol(currentMove));
                    curPath = findConnect(currentMove, nextMove);
                    doingPath();
                }
            }
        }
        if (inputt.equals("w") || inputt.equals("W")) {

            if (currentMove.getDoorW().getLocked()) {
                unlockDoor(currentMove.getDoorW());
            }
            if (!currentMove.getDoorW().getLocked()) {
                if (findConnect(currentMove, west(getRow(currentMove), getCol(currentMove))) != null) {
                    if(currentMove.getDemand()>0){
                        System.out.println("It is.............................................................");
                    }
                    else {
                    nextMove = west(getRow(currentMove), getCol(currentMove));


                        curPath = findConnect(currentMove, nextMove);
                        doingPath();
                    }
                }
            }
        }
        if (inputt.equals("S") || inputt.equals("s")) {
            if (currentMove.getDoorS().getLocked()) {
                unlockDoor(currentMove.getDoorS());
            }
            if (!currentMove.getDoorS().getLocked()) {
                if (findConnect(currentMove, south(getRow(currentMove), getCol(currentMove))) != null) {
                    nextMove = south(getRow(currentMove), getCol(currentMove));
                    curPath = findConnect(currentMove, nextMove);
                    doingPath();
                }
            }
        }
        if (inputt.equals("N") || inputt.equals("n")) {
            if (currentMove.getDoorN().getLocked()) {
                unlockDoor(currentMove.getDoorN());
            }
            if (!currentMove.getDoorN().getLocked()) {
                if (findConnect(currentMove, north(getRow(currentMove), getCol(currentMove))) != null) {
                    nextMove = north(getRow(currentMove), getCol(currentMove));
                    curPath = findConnect(currentMove, nextMove);
                    doingPath();
                }
            }
        }
        if (inputt.equals("look")) {
            doDark();
        }
        if (inputt.startsWith("grab")) {
            String[] strings = inputt.split(" ");
            String itemTo = strings[1];
            String lighter="";
            Item pickup = currentMove.getItem(itemTo.substring(0, 2));
            if (pickup != null) {
                int picked =0;
                if (pickup instanceof Treasure) {
                    currentMove.setCoinActive();
                    Treasure treasure = (Treasure) pickup;
                    scoreNum.set(scoreNum.getValue() + treasure.getWorth());
                    weAt(currentMove, treasure.getTreaSureCoin());
                }
                if(pickup instanceof Dog){
                    Dog dog=(Dog)pickup;
                    dog.cry();
                }

                if(pickup.getDesc().equals("lamp")&&player.hasLamp()){
                    paneTxt.setText(paneTxt.getText() + "\n" + "Okay..."+"you already picked up a lamp" + "\n" + dummyDots);
                    picked++;
                }
                if(pickup.getDesc().equals("lighter")){
                    if(player.hasLamp()){
                        lamp carryingLamp=(lamp) player.getLamp();
                        carryingLamp.setOn();
                        lighter="lamp has been torched";
                    }
                    else {paneTxt.setText(paneTxt.getText() + "\n" + "Okay..."+"get a lamp first" + "\n" + dummyDots);
                    picked++;
                    }
                }
                if(picked==0){
                player.pickUp(pickup);
                observableAlbums.add(pickup);
                currentMove.removeItem(pickup);
                currentMove.doDescrTooltip();
                paneTxt.setText(paneTxt.getText() + "\n" + "Okay..." + pickup.getDesc() + "  picked up" + lighter+ "\n" + dummyDots);}
            } else
                paneTxt.setText(paneTxt.getText()+"\n" + "Sorry i didnt understand your statement please try again" + "\n" + dummyDots);
        }
        if (inputt.startsWith("drop")) {
            String[] strings = inputt.split(" ");
            String itemTo = strings[1].substring(0, 2);
            for (int x = 0; x < observableAlbums.getSize(); x++) {
                if (observableAlbums.get(x).getDesc().startsWith(itemTo)) {
                    Item y = observableAlbums.remove(x);
                    scoreNum.set(player.drop(itemTo));
                    paneTxt.setText(paneTxt.getText() + "\n" + "Okay..." + y.getDesc() + " dropped" + "\n" + dummyDots);
                }
            }
        }


        if(nextMove.getIdentifier()>'D'&&nextMove.getIdentifier()<'J') {
            getDog().bark();

        }

    }
    /**
     * moves player along line
     */
    private void doPath() {
        if (run == 0) {
            player.setLayoutX(0);
            player.setLayoutY(0);
            run++;
        }
        PathTransition transition = new PathTransition();
        mapPane.setBackground(new Background(new BackgroundFill(Color.rgb(36, 47, 51), CornerRadii.EMPTY, Insets.EMPTY)));
        player.toFront();
        transition.setNode(player);
        transition.setDuration(Duration.seconds(1));
        transition.setCycleCount(1);
        transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        transition.setPath(curPath);
        transition.play();




        walkMediaPlayer.play();
        transition.setOnFinished(event -> {
            walkMediaPlayer.stop();
        });
        currentMove.leaveRoom();
        nextMove.enterRoom();
        doDark();

    }
    private Dog getDog(){
        for(int x=0;x<allItems.size();x++) {
            Item cur = allItems.get(x);
            if (cur instanceof Dog) {
                return (Dog) cur;
            }
        }
        return null;
    }
    /**
     * if room is dark no description of items is provided
     */
    private void doDark(){

        if(!nextMove.getDark()){
            paneTxt.setText(paneTxt.getText() + "\n" + nextMove.toString() + "\n" + dummyDots);}
        else if(nextMove.getDark()&&player.lampOn())
            paneTxt.setText(paneTxt.getText() + "\n" + nextMove.toString() + "\n" + dummyDots);
        else {
            paneTxt.setText(paneTxt.getText() + "\n" + "The room is dark you cant see anything and you dont know where you are.Find a lamp and a lighter in one of the rooms so you can see" + "\n" + dummyDots);
        }
    }
    private roomConnect draw(double sx, double sy, double ex, double ey) {
        roomConnect line = new roomConnect(identityLine);
        identityLine++;
        line.setStartX(sx + 10);
        line.setStartY(sy + 10);
        line.setEndX(ex + 10);
        line.setEndY(ey + 10);
        line.setStroke(Color.DARKGRAY);
        line.setStrokeWidth(8);
        return line;
    }
    /**
     * draws first and last rooom
     */
    private room firstLastR(int pos, String id) {
        room newRoom = new room(id.charAt(0));
        newRoom.setHeight(20);
        newRoom.setWidth(20);
        if (pos == 0) {
            newRoom.setFill(Color.RED);
        } else newRoom.setFill(Color.BLUE);
        return newRoom;
    }
    /**
     * draws room on map
     */
    private room drawRect(String id) {
        room newRoom = new room(id.charAt(0));
        newRoom.setHeight(20);
        newRoom.setWidth(20);
        return newRoom;
    }
    /**
     * Finds the room north to current
     */
    private room north(int row, int col) {
        for (int x = row - 1; x >= 0; x--) {
            if (x >= 0) {
                if (graph[x][col] != null) {
                    return graph[x][col]; }
            }
        }return null;
    }
    /**
     * Finds the room south to current
     */
    private room south(int row, int col) {
        for (int x = row + 1; x <= graph.length; x++) {
            if (graph[x][col] != null) {
                return graph[x][col];
            }
        }return null;
    }
    /**
     * Finds the room east to current
     */
    private room east(int row, int col) {
        for (int x = col + 1; x <= graph.length; x++) {
            if (graph[row][x] != null) {
                return graph[row][x];
            }
        }return null;
    }
    /**
     * Finds the room west to current
     */
    private room west(int row, int col) {
        for (int x = col - 1; x >= 0; x--) {
            if (x >= 0) {
                if (graph[row][x] != null) {
                    return graph[row][x];
                }
            }
        }
        return null;
    }
    private void weAt(room roomCur, ImageView coin){
        coin.setLayoutX(roomCur.getLayoutX());
        coin.setLayoutY(roomCur.getLayoutY());
        Line line = new Line(roomCur.getX() + 10, roomCur.getY(), roomCur.getX() + 10, roomCur.getY() - 50);
        line.setVisible(false);
        mapPane.getChildren().addAll(line, coin);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setPath(line);
        pathTransition.setNode(coin);
        pathTransition.setDuration(Duration.seconds(2));
        pathTransition.setOnFinished(event -> {
            coin.setVisible(false);
            roomCur.visited();
            coinMediaPlayer.stop();
        });

        coinMediaPlayer.play();
        pathTransition.play();
    }

    /**
     * reads game data from an xml file
     */

    private void doReadFromXML() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File("docs/room.xml");
        Document doc = builder.parse(file);
        XPathFactory xpFactory = XPathFactory.newInstance();
        XPath path = xpFactory.newXPath();
        int idNow=0;


        String dummy="............................................................................................................................";
        XPathExpression exp=path.compile("/GAME/INTRO");
        String d=exp.evaluate(doc);
        intro=d;
        paneTxt.setText(d+"\n"+dummy+"\n"+dummy);


        for (int x=0;x<box.size();x++) {
            room cur = box.get(x);
            String expression = "/GAME/ROOM[@id=" + "'" + cur.getIdentifier() + "'" + "]";
            XPathExpression expr = path.compile(expression);
            NodeList resultSet = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            NodeList data = resultSet.item(0).getChildNodes();

            org.w3c.dom.Node description=null;
            org.w3c.dom.Node itemms=null;
            org.w3c.dom.Node doors=null;
            org.w3c.dom.Node dark=null;

            for(int y=0;y<data.getLength();y++){
                org.w3c.dom.Node t=data.item(y);
                if(t.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE&&t.getNodeName().equals("DESCRIPTION")){
                    description=t;
                }
                if(t.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE&&t.getNodeName().equals("DARK")){
                    dark=t;
                }
                if(t.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE&&t.getNodeName().equals("INVENTORY")){
                    itemms=t;
                }
                if(t.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE&&t.getNodeName().equals("DOORS")){
                    doors=t;
                }
            }
            cur.setRoomDesrc(description.getTextContent());


            System.out.println(dark.getTextContent());
            if(dark.getTextContent().equals("YES")){

                cur.setDark(true);
            }


            if(cur.getIdentifier()<='E'){
                cur.never();
            }
            if(cur.getIdentifier()=='I'){
                cur.setDemand();
                System.out.println("Demand.......................................................................................");
            }

            NodeList itemList = itemms.getChildNodes();
            for (int i = 0; i < itemList.getLength(); i++) {
                org.w3c.dom.Node q = itemList.item(i);
                if(q.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE) {
                    String string=q.getTextContent();
                    if(string.contains("*")){
                        lamp forLight=new lamp();
                        forLight.setOff();
                        cur.roomHasLamp();
                        forLight.setDesc(string.substring(1,string.length()));
                        allItems.add(forLight);
                        cur.addItem(forLight);
                        forLight.setIdentifier(idNow);
                    }
                    else if(string.contains(";")){
                        String[] values=string.split(";");
                        Key key=new Key();
                        key.setDesc(values[1]);
                        key.setIdentifier(idNow);
                        idNow++;
                        key.setKeyFor(values[0].charAt(0));
                        cur.addItem(key);
                        allItems.add(key);
                    }
                    else if(string.contains("dog")){
                        Dog mydog=new Dog();
                        mydog.setIdentifier(idNow);
                        mydog.setDesc(string);
                        cur.addItem(mydog);
                        allItems.add(mydog);
                    }
                    else if (string.contains(",")){
                        String[] values=string.split(",");
                        Treasure treasure=new Treasure();
                        treasure.setDesc(values[1]);
                        treasure.setIdentifier(idNow);
                        idNow++;
                        treasure.setWorth(Integer.parseInt(values[0]));
                        cur.addItem(treasure);
                        allItems.add(treasure);
                    }
                    else {
                        Item item=new Item(q.getTextContent());
                        item.setIdentifier(idNow);
                        idNow++;
                        cur.addItem(item);
                        allItems.add(item);
                    }
                }
            }
            NodeList doorsList=doors.getChildNodes();
            for (int i=0;i<doorsList.getLength();i++){
                org.w3c.dom.Node q = doorsList.item(i);
                String direction=q.getTextContent();
                if(direction.equals("S")){
                    cur.getDoorS().lock();
                }
                if(direction.equals("N")){
                    cur.getDoorN().lock();
                }
                if(direction.equals("E")){
                    cur.getDoorE().lock();
                }
                if(direction.equals("W")){
                    cur.getDoorW().lock();
                }
            }
            cur.setPlayer(player);
            if(!cur.getDark()&&cur.getFill()!=Color.RED&&cur.getFill()!=Color.BLUE){cur.setFill(Color.DARKGRAY);}
            else if(cur.getDark()&&cur.getFill()!=Color.RED&&cur.getFill()!=Color.BLUE){cur.setFill(Color.BLACK);}
            cur.doDescrTooltip();
        }
    }


}


