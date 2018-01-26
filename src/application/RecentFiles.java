package application;

import java.util.LinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RecentFiles implements Serializable  {

	private static final long serialVersionUID = 1L;

	private class OpenedFile{
				private String path = null;
		private int line = 0;
		public OpenedFile(String path,int line){
			this.path = path;
			this.line = line;
		}
		public String getPath() {
			return path;
		}
		public int getLine() {
			return line;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			OpenedFile other = (OpenedFile) obj;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			return true;
		}
	}
	
	private LinkedList<OpenedFile> list = null;
	
	public void saveFile(String line,String path){
		OpenedFile recentFile = new OpenedFile(path,Integer.parseInt(line));
		list.remove(recentFile);
		list.add(recentFile);
		saveRecent(this);
	}
	
	public static RecentFiles readRecent(){
		RecentFiles recf = null;
		FileInputStream fi = null;
		ObjectInputStream oi = null;
		try {
			fi = new FileInputStream(new File("src/application/recent.save"));
			oi = new ObjectInputStream(fi);
			recf = (RecentFiles) oi.readObject();
		} catch (Exception e) {
			System.err.println("An error occurred loading the recent files. Please restart and try again.");
		}
		if(oi!=null){
			try {
				oi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(fi!=null){
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return recf;
	}
	
	public static void saveRecent(RecentFiles recentFiles){
		try {
			FileOutputStream f = new FileOutputStream(new File("src/application/recent.save"));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(recentFiles);
			o.close();
			f.close();
		} catch (Exception e) {
			System.err.println("An error occurred saving the recent file. Please restart and try again.");
			e.printStackTrace();
		}
	}
	
	
}
