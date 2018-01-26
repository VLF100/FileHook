package application;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;     

public class HookController {
	
	private static Window window;
	private static FileReader fileReader;
	private static RecentFiles recentFiles = null;
	
	public HookController(Window window){
		HookController.window = window;
	}
	
    @FXML
    void hookClick(MouseEvent event) {
    	if(fileReader!=null)
    		fileReader.readLine();
    }
    
    @FXML
    void selectClick(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Script File");
    	File file = fileChooser.showOpenDialog(HookController.window);
        if (file != null) {
        	if(fileReader!=null)
        		fileReader.close();
        	HookController.fileReader = new FileReader(file);
        }
    }
    
    @FXML
    void inputNumber(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)
    		if(fileReader!=null){
    			int line = Main.getNumberLine();
    			if(line > 0)
    				fileReader.goTo(line);
    		}
    }
    
    @FXML
    void recentClick(MouseEvent event) {
    	System.out.println("algo");
    }
    
    @FXML
    void saveClick(MouseEvent event) {
    	if(recentFiles==null)
    		recentFiles = RecentFiles.readRecent();
    	if(recentFiles==null)
    		recentFiles = new RecentFiles();
    	if(fileReader!=null){
    		String[] nameline = fileReader.getNameLine();
    		recentFiles.saveFile(nameline[0],nameline[1]);
    		//DEBUG
    		System.out.println("alga");
    	}
    	//DEBUG
    	System.out.println("no file to save");
    }
}
