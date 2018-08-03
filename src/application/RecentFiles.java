package application;

import java.util.LinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class RecentFiles implements Serializable {

	public String[][] getList() {
		if (this.list == null)
			return null;
		String[][] list = new String[this.list.size()][2];
		int n = 0;
		for (OpenedFile file : this.list) {
			list[n][0] = Integer.toString(file.getLine());
			list[n][1] = file.getPath();
			n++;
		}
		return list;
	}

	private static final long serialVersionUID = 1L;

	private class OpenedFile implements Serializable {

		private static final long serialVersionUID = 1L;
		private String path = null;
		private int line = 0;

		public OpenedFile(String path, int line) {
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

	public void saveFile(String line, String path) {
		if (list == null)
			list = new LinkedList<OpenedFile>();
		OpenedFile recentFile = new OpenedFile(path, Integer.parseInt(line));
		list.remove(recentFile);
		list.add(0, recentFile);
		saveRecent(this);
	}

	public static RecentFiles readRecent() {
		RecentFiles recf = null;
		FileInputStream fi = null;
		ObjectInputStream oi = null;
		try {
			fi = new FileInputStream(new File("recent.save"));
			oi = new ObjectInputStream(fi);
			recf = (RecentFiles) oi.readObject();
		} catch (ObjectStreamException e) {
			recf = new RecentFiles();
		} catch (Exception e) {
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

		return recf;
	}

	public static void saveRecent(RecentFiles recentFiles) {
		try {
			FileOutputStream f = new FileOutputStream(new File("recent.save"));
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
