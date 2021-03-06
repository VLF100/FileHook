package application;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ModalController {

    @FXML
    void acceptNicknameModal(MouseEvent event) {
    	String nickname = Main.getNicknameModalField();
    	if(nickname==null || nickname.isEmpty() || nickname.length()>25)
    		Main.nicknameModalShowError();
    	else{
    		HookController.saveClickWithNick(nickname);
    		Main.closeNicknameModal();
    	}
    }

    @FXML
    void cancelNicknameModal(MouseEvent event) {
    	Main.closeNicknameModal();
    }

}

