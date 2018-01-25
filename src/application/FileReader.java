package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

	private File file = null;
	private FileInputStream fstream = null;
	private BufferedReader br = null;
	private int lineNumber;
	
	public FileReader(File file){
		this.file = file;
		showNameFile(this.file.getName());
		startLines();
		this.lineNumber = 0;
		try {
			this.fstream = new FileInputStream(this.file);
			this.br = new BufferedReader(new InputStreamReader(this.fstream,"UTF8"));
		} catch (Exception e) {
			System.err.println("An error occurred starting the file. Please restart and try again.");
		}
		
	}

	private void startLines() {
		Main.setLine(0,"Ready!");
	}

	private void showNameFile(String name) {
		Main.showFileName(name);
	}
	
	public void readLine(){
		String strLine;
		try {
			if(br != null){
				strLine = br.readLine();
				if(strLine!=null){
					strLine = strLine.replaceAll("^\\s+", "");
					Main.setLine(++this.lineNumber,strLine);
					Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
					Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
					systemClipboard.setContents(new StringSelection(strLine), null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred reading the next line of the file. Please restart and try again.");
		}
	}
	
	public void close(){
		try {
			this.br.close();
			this.fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Oops! This should have not happened! Restart the program and try again");
		}
	}

	public void goTo(int numberLine) {
		int lineCounter = 0;
		try {
			this.close();
			this.fstream = new FileInputStream(this.file);
			this.br = new BufferedReader(new InputStreamReader(this.fstream,"UTF8"));
			while (lineCounter++ < numberLine-1 && this.br.readLine() != null);
			this.lineNumber = numberLine-1;
			this.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred reading the next line of the file. Please restart and try again.");
		}
		
	}
	
}

