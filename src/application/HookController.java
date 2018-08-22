package application;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Controller of the actions the user does.
 *
 */
public class HookController {

	private static FileReader fileReader;
	private static RecentFiles recentFiles = null;

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
		recentFiles = RecentFiles.readRecent();
		Main.showRecent(recentFiles);
	}

	//Save progress button. Saves current file and line on "recents" file.
	@FXML
	void saveClick(MouseEvent event) {
		if (recentFiles == null)
			recentFiles = RecentFiles.readRecent();
		if (recentFiles == null)
			recentFiles = new RecentFiles();
		if (fileReader != null) {
			String[] nameline = fileReader.getNameLine();
			recentFiles.saveFile(nameline[0], nameline[1]);
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
	public static void openRecent(String currentItemSelected) {
		String path = currentItemSelected.split(" : ")[1];
		File file = new File(path);
		if (file != null) {
			if (fileReader != null)
				fileReader.close();
			HookController.fileReader = new FileReader(file);
			int line = Integer.parseInt(currentItemSelected.split(" : ")[0]);
			if (line > 0)
				fileReader.goTo(line);
		}
	}

	//Beta button. Shows beta features side menu.
    @FXML
    void betaClick(MouseEvent event) {
    	Main.showBeta();
    }

    //X button from the beta features side menu. Closes the menu.
    @FXML
    void closeBeta(MouseEvent event) {
    	Main.closeBeta();
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

}
