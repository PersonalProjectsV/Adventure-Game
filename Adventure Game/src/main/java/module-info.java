module com.vhuthu.adventuregame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.xml;


    opens com.vhuthu.adventuregame to javafx.fxml;
    exports com.vhuthu.adventuregame;
}