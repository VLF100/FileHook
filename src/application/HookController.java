package application;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class HookController {

	private static Window window;
	private static FileReader fileReader;
	private static RecentFiles recentFiles = null;

	public HookController(Window window) {
		HookController.window = window;
	}

	@FXML
	void hookClick(MouseEvent event) {
		if (fileReader != null)
			fileReader.readLine();
	}

	void hookClick() {
		if (fileReader != null)
			fileReader.readLine();
	}

	@FXML
	void reloadClick(MouseEvent event) {
		if (fileReader != null) {
			int line = Main.getNumberLine();
			if (line > 0)
				fileReader.goTo(line);
		}
	}

	@FXML
	void selectClick(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open script file...");
		File file = fileChooser.showOpenDialog(HookController.window);
		if (file != null) {
			if (fileReader != null)
				fileReader.close();
			HookController.fileReader = new FileReader(file);
		}
	}

	@FXML
	void inputNumber(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			if (fileReader != null) {
				int line = Main.getNumberLine();
				if (line > 0)
					fileReader.goTo(line);
			}
	}

	@FXML
	void recentClick(MouseEvent event) {
		recentFiles = RecentFiles.readRecent();
		Main.showRecent(recentFiles);
	}

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

	@FXML
	void closeClick(MouseEvent event) {
		Main.closeRecent();
	}
	
	//Raw SciADV parsing
	@FXML
	void blueSkyFire(MouseEvent event) {
		Main.blueSkyMode = !Main.blueSkyMode;
		/*System.out.println("Blue Sky checkbox fired: " + Main.blueSkyMode);*/
	}

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

    @FXML
    void betaClick(MouseEvent event) {
    	Main.showBeta();
    }

    @FXML
    void closeBeta(MouseEvent event) {
    	Main.closeBeta();
    }

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
    			//System.out.println("File " + listOfFiles[i].getName());
    		  } else if (listOfFiles[i].isDirectory()) {
    		    continue;
    			//System.out.println("Directory " + listOfFiles[i].getName());
    		  }
    	}
    }

}
