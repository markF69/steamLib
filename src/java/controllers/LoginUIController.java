package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import other.ApiLogic;

import java.awt.*;
import java.net.URI;

public class LoginUIController {
    @FXML
    private Hyperlink HL64;
    @FXML
    private Hyperlink HLApi;
    @FXML
    private Button submitBtn;
    @FXML
    private Text loadingTxt;
    @FXML
    private ProgressIndicator loadingProgress;
    @FXML
    private TextField steamKeyInput;
    @FXML
    private TextField steamIdInput;

    // Takes the Steam64 ID and passes it to the API
    public void sendData() {
        String steamID = steamIdInput.getText();
        String steamKey = steamKeyInput.getText();
        int statusCode = 0;

        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("ERROR");


        // Checks if the Steam64 ID and Steam Key are in the correct format
        if ((steamID.matches("[0-9]+") && steamID.length() == 17) && (steamKey.matches("[0-9A-F]+") && steamKey.length() == 32)){
            System.out.println("Sent");

            // Reveals the loading animation
            loadingTxt.setVisible(true);
            loadingProgress.setVisible(true);

            // Disables inputs
            submitBtn.setDisable(true);
            steamIdInput.setDisable(true);
            steamKeyInput.setDisable(true);

            // Checks if auth is currect, then sends the ID and Key to be used by the API
            try {
                statusCode = ApiLogic.authCheck(steamID,steamKey);

                if (statusCode == 401){
                    errorAlert.setHeaderText("Invalid Steam Web Api Key");
                    errorAlert.show();
                    clearEntry();
                } else if (statusCode == 64){
                    errorAlert.setHeaderText("Invalid steam64 ID");
                    errorAlert.show();
                    clearEntry();
                }
                else {
                    ApiLogic.collectData(steamID, steamKey);

                    // Opens the library window
                    Parent root = FXMLLoader.load(getClass().getResource("/UI/Library_UI.fxml"));

                    Stage stage = (Stage) submitBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));

                }

            } catch (Exception e){
                e.printStackTrace();
            }

        }
        // Shows an alert popup if the information is not in the correct format
        else {
            errorAlert.setHeaderText("Invalid Steam64 ID/Key format");
            errorAlert.show();
        }
    }

    // Clears data for new entry
    public void clearEntry(){
        // Hides the loading animation
        loadingTxt.setVisible(false);
        loadingProgress.setVisible(false);

        // Enables inputs
        submitBtn.setDisable(false);
        steamIdInput.setDisable(false);
        steamKeyInput.setDisable(false);

        steamKeyInput.clear();
        steamIdInput.clear();
    }

    // Redirects to browser for more info
    public void info(ActionEvent event) throws Exception{
        if (event.getSource() == HL64){
            Desktop.getDesktop().browse(new URI("https://steamcommunity.com/discussions/forum/1/144512942754828440"));
        }
        if (event.getSource() == HLApi){
            Desktop.getDesktop().browse(new URI("https://steamcommunity.com/dev/apikey"));
        }
    }
}

