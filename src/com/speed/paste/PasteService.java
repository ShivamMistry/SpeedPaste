package com.speed.paste;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

/**
 * 
 * This class is used to add services to SpeedPaste.
 * 
 * This file is part of SpeedPaste.
 * 
 * SpeedPaste is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SpeedPaste is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SpeedPaste. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Shivam Mistry (Speed)
 * 
 */
public abstract class PasteService implements Comparable<PasteService> {

	private Set<PasteHistory> history = new TreeSet<PasteHistory>();

	class PasteHistory implements Comparable<PasteHistory> {
		String link;
		long time;

		PasteHistory(String link, long time) {
			this.link = link;
			this.time = time;
		}

		String getTimestamp() {
			return new Date(time).toString();
		}

		public int compareTo(PasteHistory arg0) {
			return (int) (arg0.time - time);
		}

		public String toString() {
			return getTimestamp() + " : " + link;
		}

		public boolean equals(Object o) {
			return o instanceof PasteHistory
					&& ((PasteHistory) o).toString().equals(toString());
		}

	}

	public abstract void addComps(final JPanel contentPanel);

	public abstract String paste(String data);

	@Override
	public int compareTo(PasteService o) {
		final ServiceInfo thisProv = this.getClass().getAnnotation(
				ServiceInfo.class);
		final ServiceInfo prov = o.getClass().getAnnotation(ServiceInfo.class);
		if (prov == null && thisProv == null) {
			return 0;
		} else if (thisProv == null) {
			return 1;
		} else if (prov == null) {
			return -1;
		} else
			return thisProv.name().compareTo(prov.name());

	}

	@Override
	public String toString() {
		final ServiceInfo prov = this.getClass().getAnnotation(
				ServiceInfo.class);
		return prov == null ? "" : prov.name();
	}

	private void addHistory(String link, long time) {
		history.add(new PasteHistory(link, time));
	}

	public File getHistoryFile() {
		return new File(PasteGui.getDataDir().getAbsolutePath()
				+ File.separator + "history" + File.separator + toString()
				+ ".hist");
	}

	public Set<PasteHistory> getHistory() {
		return history;
	}

	public void writeHistory(String link, long time) {
		addHistory(link, time);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getHistoryFile(), true);
			out.write((time + " : " + link + "\r\n").getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void readHistory() {
		File file = getHistoryFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader read = new BufferedReader(new FileReader(file));
			String line;
			while ((line = read.readLine()) != null) {
				if (!line.startsWith("#")) {
					String[] parts = line.split(":", 2);
					try {
						long time = Long.parseLong(parts[0].trim());
						String link = parts[1].trim();
						addHistory(link, time);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Failed to parse: " + line);
					}
				}
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
