package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginUIController {
    @FXML
    private TextField steamIdInput;
    @FXML
    private Button submitBtn;

    // Takes the Steam64 ID and passes it to the API
    public void sendData() {
        String input = steamIdInput.getText();

        // Checks if the input is a num and the exact length of the Steam64 ID
        if (input.matches("[0-9]+") && input.length() == 17){
            System.out.println("Good");
        } else{
           Alert incorrectInput = new Alert(Alert.AlertType.ERROR);
           incorrectInput.setTitle("ERROR");
           incorrectInput.setHeaderText("Invalid Steam64 ID");
           incorrectInput.show();
        }
    }



}

