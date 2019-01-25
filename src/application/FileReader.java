package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Model for reading the data and lines from files.
 *
 */
public class FileReader {

	private File file = null;
	private FileInputStream fstream = null;
	private BufferedReader br = null;
	private int lineNumber;
	private int totalLines;

	public FileReader(File file) {
		this.file = file;
		showNameFile(this.file.getName());

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), "UTF8"));
			int lines = 0;
			while (reader.readLine() != null)
				lines++;
			reader.close();
			totalLines = lines;
		} catch (Exception e) {
			System.err.println("An error occurred while reading the file. Please restart and try again.");
		}

		startLines();
		this.lineNumber = 0;
		try {
			this.fstream = new FileInputStream(this.file);
			this.br = new BufferedReader(new InputStreamReader(this.fstream, "UTF8"));
		} catch (Exception e) {
			System.err.println("An error occurred while reading the file. Please restart and try again.");
		}

	}

	private void startLines() {
		Main.setLine(0, "Ready!", totalLines);
	}

	private void showNameFile(String name) {
		Main.showFileName(name);
	}

	public void readLine() {
		String strLine;
		try {
			if (br != null) {
				strLine = br.readLine().trim();
				while(strLine.isEmpty())
				{
					strLine = br.readLine().trim();
				}
				if (strLine != null) {
					if(Main.blueSkyMode) //Parsing for scripts with ruby text
						strLine = parseBlueSky(strLine);
					Main.setLine(++this.lineNumber, strLine, totalLines);
					Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
					Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
					systemClipboard.setContents(new StringSelection(strLine), null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred while trying to read the next line of the file. Please restart and try again.");
		}
	}

	//Parsing for scripts with ruby text
	private String parseBlueSky(String s) {
		return s.replaceAll("\\[name\\]", "")
				.replaceAll("\\[line\\]", "")
				.replaceAll("\\[\\.\\.\\.\\]", "...")
				.replaceAll("\\[\\%p\\]", "")
				.replaceAll("\\[color.+?(?=\\])\\]", "")
				.replaceAll("\\[margin.*\\]", "")
				.replaceAll("③⑤", "--")
				.replaceAll("\\[\\%e\\]", "")
				.replaceAll("\\[ruby-base\\]", "")
				.replaceAll("\\[ruby-text-start\\].*\\[ruby-text-end\\]", "");
	}

	public void close() {
		try {
			this.br.close();
			this.fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Oops! This should have not happened! Please restart and try again.");
		}
	}

	public void goTo(int numberLine) {
		int lineCounter = 0;
		try {
			this.close();
			this.fstream = new FileInputStream(this.file);
			this.br = new BufferedReader(new InputStreamReader(this.fstream, "UTF8"));
			while (lineCounter++ < numberLine - 1 && this.br.readLine() != null)
				;
			this.lineNumber = numberLine - 1;
			this.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error occurred while trying to read the next line of the file. Please restart and try again.");
		}

	}

	public String[] getNameLine() {
		return new String[] { Integer.toString(lineNumber), file.getAbsolutePath() };
	}

	public String getPath() {
		return this.file.getParent();
	}

	public String getName() {
		return this.file.getName();
	}

}
