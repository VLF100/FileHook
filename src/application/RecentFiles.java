package application;

import java.util.LinkedList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Model for the save & load of recent files.
 *
 */
public class RecentFiles {
	private static List<OpenedFile> recentFilesList = null;
	
	public static void addToList(String line, String path, String nickname) {
		recentFilesList.add(new OpenedFile(path, Integer.parseInt(line)));
		saveFile();
	}
	
	private static void saveFile() {
		try {
			FileOutputStream f = new FileOutputStream(new File("recent.save"));
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(recentFilesList);
			o.close();
			f.close();
		} catch (Exception e) {
			System.err.println("An error occurred saving the recent file. Please restart and try again.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<OpenedFile> readFile() {
		if(recentFilesList == null){
			FileInputStream fi = null;
			ObjectInputStream oi = null;
			try {
				fi = new FileInputStream(new File("recent.save"));
				oi = new ObjectInputStream(fi);
				recentFilesList = (List<OpenedFile>) oi.readObject();
			} catch (Exception e) {
				recentFilesList = new LinkedList<OpenedFile>();
			}
			if (oi != null) {
				try {
					oi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return recentFilesList;
	}
	
	public static class OpenedFile implements Serializable {

		private static final long serialVersionUID = 1L;
		private String path = null;
		private int line = 0;
		private String nickname = null;

		public OpenedFile(String path, int line, String nickname) {
			this.path = path;
			this.line = line;
			this.nickname = nickname;
		}
		
		public OpenedFile(String path, int line) {
			this.path = path;
			this.line = line;
		}

		public String getName() {
			return (nickname!=null)?nickname:path;
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
		
		public String toString(){
			return this.line+" : "+((this.nickname==null)?this.path:nickname);
		}

		public String getPath() {
			return this.path;
		}
	}

}

