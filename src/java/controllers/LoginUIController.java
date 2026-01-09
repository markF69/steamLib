package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginUIController {
    @FXML
    private TextField steamKeyInput;
    @FXML
    private TextField steamIdInput;
    @FXML
    private Button submitBtn;

    // Takes the Steam64 ID and passes it to the API
    public void sendData() {
        String steamID = steamIdInput.getText();
        String steamKey = steamKeyInput.getText();

        // Checks if the Steam64 ID and Steam Key are in the correct format
        if ((steamID.matches("[0-9]+") && steamID.length() == 17) && (steamKey.matches("[0-9A-F]+") && steamKey.length() == 32)){
            System.out.println("Good");
            // prosledjuje api logic-u steamID
        } else{
           Alert incorrectInput = new Alert(Alert.AlertType.ERROR);
           incorrectInput.setTitle("ERROR");
           incorrectInput.setHeaderText("Invalid Steam64 ID");
           incorrectInput.show();
        }
    }



}

