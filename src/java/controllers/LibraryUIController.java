package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import other.ApiLogic;
import other.DisplayData;
import other.Game;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class LibraryUIController implements Initializable {
    @FXML
    private TableColumn<Game, String> nameTC;
    @FXML
    private TableColumn<Game, String> playtimeTC;
    @FXML
    private TableColumn<Game, String> lastPlayedTC;
    @FXML
    private ImageView avatarImg;
    @FXML
    private Text nameTxt;
    @FXML
    private TableView<Game> libraryTV;

    // Runs the methods at the start
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayData();
    }

    // Displays data that has been collected from the API class
    public void displayData(){
        DisplayData data = ApiLogic.returnData();

        // Assigns the data
        ArrayList<Game> games = data.getLibraryGames();
        String profileName = data.getProfileName();
        String profilePicture = data.getProfilePicture();
        int gameCount = data.getTotalGameCount();

        // This makes the cells call the getters so that they can display data
        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        playtimeTC.setCellValueFactory(new PropertyValueFactory<>("playtimeHours"));
        lastPlayedTC.setCellValueFactory(new PropertyValueFactory<>("lastPlayedDate"));

        // Puts items
        libraryTV.setItems(FXCollections.observableArrayList(games));

    }


}
