package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import application.RecentFiles.OpenedFile;

/**
 * Controller of the actions the user does.
 *
 */
public class HookController {

	private static FileReader fileReader;

	public HookController() {}

	//Next line button. Advances to next line
	@FXML
	void hookClick(MouseEvent event) {
		if (fileReader != null)
			fileReader.readLine();
	}

	//Reload button. Reloads the wind and copies the line again.
	@FXML
	void reloadClick(MouseEvent event) {
		if (fileReader != null) {
			int line = Main.getNumberLine();
			if (line > 0)
				fileReader.goTo(line);
		}
	}

	//Select Script button. Open dialog to select file from explorer.
	@FXML
	void selectClick(MouseEvent event) {
		File file = Main.selectScript();
		if (file != null) {
			//If a file was already open, close it
			if (fileReader != null)
				fileReader.close();
			HookController.fileReader = new FileReader(file);
		}
	}

	//Number label control. If pressed enter goes to the line introduced.
	@FXML
	void inputNumber(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			if (fileReader != null) {
				int line = Main.getNumberLine();
				if (line > 0)
					fileReader.goTo(line);
			}
	}

	//Recent button. Shows side menu of recent files.
	@FXML
	void recentClick(MouseEvent event) {
		Main.showRecent();
	}

	//Save progress button. Saves current file and line on "recents" file.
	@FXML
	void saveClick(MouseEvent event) {
		 if (event.getButton() == MouseButton.SECONDARY) {
	            return;
	       }
		if (fileReader != null) {
			String[] nameline = fileReader.getNameLine();
			RecentFiles.addToList(nameline[0], nameline[1], null);
		}
	}
	
	//Accept button on nickname modal. Saves current file and line on "recents" file with a custom nickname.
	static void saveClickWithNick(String nick) {
		if (fileReader != null) {
			String[] nameline = fileReader.getNameLine();
			RecentFiles.addToList(nameline[0], nameline[1], nick);
		}
	}

	//X button in the recent files side menu. Closes the menu.
	@FXML
	void closeClick(MouseEvent event) {
		Main.closeRecent();
	}
	
	
	//Blue Sky mode checkbox. Parsing for scripts with ruby text.
	@FXML
	void blueSkyFire(MouseEvent event) {
		Main.blueSkyMode = !Main.blueSkyMode;
		/*System.out.println("Blue Sky checkbox fired: " + Main.blueSkyMode);*/
	}

	//Function to open a file from the recent files side menu.
	public static void openRecent(OpenedFile currentItemSelected) {
		File file = new File(currentItemSelected.getPath());
		if (file != null) {
			if (fileReader != null)
				fileReader.close();
			HookController.fileReader = new FileReader(file);
			int line = currentItemSelected.getLine();
			if (line > 0)
				fileReader.goTo(line);
		}
	}

	//Delta button. Shows delta features side menu.
    @FXML
    void deltaClick(MouseEvent event) {
    	Main.showDelta();
    }

    //X button from the delta features side menu. Closes the menu.
    @FXML
    void closeDelta(MouseEvent event) {
    	Main.closeDelta();
    }
    
    //Function to enable/disable the "Always on top" functionality with the checkbox.
    @FXML
    void topFire(MouseEvent event) {
    	Main.toogleAlwaysOnTop();
    }

    //Next Script/Arrow button. Goes to next script file in alphabetical order.
    @FXML
    void nextClick(MouseEvent event) {
    	if(fileReader == null)
    		return;
    	
    	String name = fileReader.getName();
    	String path = fileReader.getPath();
    	
    	if(path == null)
    		return;
    	
    	File folder = new File(path);
    	File[] listOfFiles = folder.listFiles();
    	
    	class CompFiles implements Comparator<File> {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
    		
    	}
    	
    	Comparator<File> compFiles = new CompFiles();
    	
    	Arrays.sort(listOfFiles,compFiles);
    	
    	for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) {
    			if(listOfFiles[i].getName().equals(name))
    				if(i+1 < listOfFiles.length){
    					System.out.println("File " + listOfFiles[i+1].getName());
    					HookController.fileReader.close();
    					HookController.fileReader = new FileReader(listOfFiles[i+1]);
    				}
    		  } else if (listOfFiles[i].isDirectory()) {
    		    continue;
    		  }
    	}
    }
    
    @FXML
    void saveWithNick(ActionEvent event) {
    	if(fileReader != null)
    		Main.showNicknameModal();
    }
    
    @FXML
    void checkDrag(DragEvent event) {
    	//System.out.println("something i being dragged");
    }
    
    @FXML
    void loadDraggedFile(DragEvent event) {
    	System.out.println("something was being dragged");
    }

}
