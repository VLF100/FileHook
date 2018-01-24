package application;

import java.io.File;

public class FileReader {

	private File file = null;
	
	public FileReader(File file){
		this.file = file;
		showNameFile(this.file.getName());
		startLines();
	}

	private void startLines() {
		Main.setLine(0,"Ready!");
	}

	private void showNameFile(String name) {
		Main.showFileName(name);
	}
	
	
}
