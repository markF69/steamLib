package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import other.Game;

import java.util.ArrayList;

public class LibraryUIController {
    @FXML
    private TableColumn nameTC;
    @FXML
    private TableColumn playtimeTC;
    @FXML
    private TableColumn lastPlayedTC;
    @FXML
    private ImageView avatarImg;
    @FXML
    private Text nameTxt;
    @FXML
    private TableView libraryTV;

    // Collects the data from the API class to be used for displaying
    public void collectData(ArrayList<Game> libraryGames, String profileName, String profilePicture, int totalGameCount){

    }
}
