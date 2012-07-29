package com.speed.paste;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.xml.bind.DatatypeConverter;

import com.speed.paste.providers.Cafenull;
import com.speed.paste.providers.SfpPaste;

/**
 * 
 * This class creates the GUI and provides the main application for SpeedPaste.
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
public class PasteGui {

	private JFrame pasteFrame;
	private JTextField textField;
	private Set<PasteService> services;
	private TrayIcon icon;
	private PopupMenu menu;
	private static File dataDir;
	private static Image pasteImage;

	@SuppressWarnings("unchecked")
	/*
	 * Pastebin provider hasn't been written yet.
	 */
	private static final Class<? extends PasteService>[] DEFAULT_PROVIDERS = new Class[] {
			Cafenull.class, /* Pastebin.class, */SfpPaste.class };

	private static final String PASTE_32 = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AA"
			+ "ADVUlEQVRYhb2XPUwTYRjHf0dsrUF6daFBsH6RgImCuEqUBAeJEaObEwMDg3GW0cGBMKkJDjIZ4"
			+ "2ZiCImROKCJrDaVGJWglmAUUj9SSqmlcI/D3bV3vbv2WtF/mvS96/ve83v/z9OnbxURAUBRFNw0"
			+ "Ojoqrh8YGhsbU4EcUKg0z0u7zMH0rZ5fQKR8QlJtpbu723VxIpHg2jk1fb7vSPHe9jas/tDSI3c"
			+ "TMT9guyzjSM+l0dKV4cy9Gw+Yn593Xby8vExfRzs9F66jO6mBCA9v31GBNuBLLQAA5NdXWE99AH"
			+ "SAtrY2Ojs7XReLCNmfb/m++BwRjabmY+xu2k82JwAqsFopuCuANTgIsViMbDbrujgWi1F4hzFfW"
			+ "E+9Z3fTftM896KqCCBiC47AYNcXPn9aIrueI5fL8zuXJ5/fZHOzwFZhC0VRSiu0gmW9PzkcsAY3"
			+ "d3bo8AHjngEoeq4RDdAQ0WoO7AEgDhfEuP4avOLrgd9WoXd4nMdXNx5MTk4OzszMrFGhEF0cKAF"
			+ "Iccf69cDAgC+IVCpFMplsHxkZeRIKhS5OTU0lvSCcDhR3DU5H/ElVVQKBAP39/R3ANOAJUVaEll"
			+ "2DJee1AQSDQYLBIEBVCJca0EpjK0SNamxsJBwOs7a2VhGiwQHgUuUlKP8KhULMzs4yNzfHwsICk"
			+ "UikY2hoaBrYBwTMec4+YAZzpKI2RaNRotEomqaRyWTY2NggHo93AM1YfiPsDpgAxZ1bXKhTDQ0N"
			+ "qKpKS0sLS0tLAHvxdMBW9WK83GvgxetFXwB9p9qL43Q6Dfqmi23aJQXbZhewdcNKD/4b2QAEMdq"
			+ "qCWDpB/9IZQ6Y+S//Cu5MCqoDoKegNDbfSwBmlzzbc9QXQLUjn7cDRnCpoxPWIkcRimzbi88j+M"
			+ "v4R18BqjnlcKCp+TiZlQReRWhaeubkEfyothSgEQq3Egq32oMLrPy0P7AWeQW3AWxtkX726L76L"
			+ "aXxO+8M0js8bgN49SbpK3hv16GKnxcBLt+Mx9CP0iouB8qnw8xZAU6fOOgLoJpj1hTk0M/xFY/S"
			+ "9aTAL0CBOv9eVZOvGvAr86SzU/INoGkaExMTZDKZ/w8gIiiKogIH0Iv0b5TGkmqlvFF4FZmiKAF"
			+ "gD5bDRJ0qYDkR/QHmh8Z+U162swAAAABJRU5ErkJggg==";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		dataDir = (createDataDir());
		try {
			pasteImage = ImageIO.read(new ByteArrayInputStream(
					DatatypeConverter.parseBase64Binary(PASTE_32)));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					final PasteGui window = new PasteGui();
					window.pasteFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static File createDataDir() {
		String os_name = System.getProperty("os.name");
		File file;
		if (os_name.toLowerCase().startsWith("windows")) {
			file = new File(System.getenv("APPDATA") + File.separator
					+ "SpeedPaste" + File.separator);

		} else {
			file = new File(System.getProperty("user.home") + File.separator
					+ ".SpeedPaste" + File.separator);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		File history = new File(file.getAbsolutePath() + File.separator
				+ "history");
		File services = new File(file.getAbsolutePath() + File.separator
				+ "services");
		if (!services.exists()) {
			services.mkdir();
		}
		if (!history.exists()) {
			history.mkdir();
		}
		return file;
	}

	/**
	 * Create the GUI.
	 */
	public PasteGui() {

		// create tray icon
		if (SystemTray.isSupported())
			try {
				icon = new TrayIcon(PasteGui.pasteImage);
				SystemTray.getSystemTray().add(icon);
				menu = new PopupMenu();
			} catch (Exception e) {
				e.printStackTrace();
			}
		ActionListener trayListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof MenuItem) {
					MenuItem item = (MenuItem) e.getSource();
					if (item.getLabel().equals("Show/Hide GUI")) {
						pasteFrame.setVisible(!pasteFrame.isVisible());
					} else if (item.getLabel().equals("Exit")) {
						PasteGui.this.pasteFrame.dispose();
						SystemTray.getSystemTray().remove(PasteGui.this.icon);
					}
				} else if (e.getSource() instanceof TrayIcon) {
					pasteFrame.setVisible(!pasteFrame.isVisible());
					if (!pasteFrame.isFocusOwner() && pasteFrame.isVisible()) {
						pasteFrame.requestFocus();
					}
				}
			}

		};
		icon.addActionListener(trayListen);
		MenuItem item = new MenuItem("Show/Hide GUI"), exitItem = new MenuItem(
				"Exit");
		item.addActionListener(trayListen);
		exitItem.addActionListener(trayListen);
		try {
			loadProviders();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		for (final PasteService s : services) {
			s.readHistory();
			MenuItem i = new MenuItem(s.toString());
			i.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					String data = getClipboard();
					if (data.isEmpty()) {
						return;
					}
					final String url = s.paste(data);
					s.writeHistory(url, System.currentTimeMillis());
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(new StringSelection(url), null);
				}

			});
			menu.add(i);
		}
		menu.add(item);
		menu.add(exitItem);
		icon.setPopupMenu(menu);

		initialise();
	}

	private void loadProviders() throws URISyntaxException {
		services = new TreeSet<PasteService>();
		for (Class<? extends PasteService> c : DEFAULT_PROVIDERS) {
			try {
				PasteService service = c.newInstance();
				services.add(service);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		final File data = new File(getDataDir().getAbsolutePath()
				+ File.separator + "services");
		loadProviders(data, data);
	}

	private void loadProviders(final File first, final File current) {
		if (!current.exists())
			return;
		for (File f : current.listFiles()) {
			if (!f.exists()) {
				continue;
			} else if (f.isDirectory()) {
				loadProviders(first, f);
			} else {
				try {
					URLClassLoader loader = new URLClassLoader(new URL[] { f
							.toURI().toURL() });
					String clazz_name = f.getAbsolutePath().replace(
							first.getAbsolutePath(), "");
					if (clazz_name.startsWith(File.separator)) {
						clazz_name = clazz_name.substring(1);
					}
					clazz_name = clazz_name.replace(".class", "").replace(
							File.separator, ".");
					if (clazz_name.contains("$")) {
						continue;
					}
					try {
						Class<?> clazz = loader.loadClass(clazz_name);
						if (PasteService.class.isAssignableFrom(clazz)
								&& (clazz.getModifiers() & Modifier.ABSTRACT) == 0) {
							ServiceInfo info = clazz
									.getAnnotation(ServiceInfo.class);
							if (info != null && info.hidden()) {
								continue;
							}
							services.add((PasteService) clazz.newInstance());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					loader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getClipboard() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard()
				.getContents(null);
		if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			String data;
			try {
				data = ((String) t.getTransferData(DataFlavor.stringFlavor))
						.trim();
			} catch (Exception e) {
				return "";
			}
			return data;
		}
		return "";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialise() {
		pasteFrame = new JFrame();
		pasteFrame.setIconImage(PasteGui.pasteImage);
		pasteFrame.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosed(WindowEvent arg0) {
				SystemTray.getSystemTray().remove(icon);
			}

			public void windowClosing(WindowEvent arg0) {
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
				pasteFrame.setVisible(false);
			}

			public void windowOpened(WindowEvent arg0) {
			}

		});
		pasteFrame.setTitle("Speed Paste");
		pasteFrame.setBounds(100, 100, 513, 642);
		pasteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pasteFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		pasteFrame.setPreferredSize(new Dimension(500, 200));
		JPanel pastePanel = new JPanel();
		pasteFrame.getContentPane().add(pastePanel);
		pastePanel.setLayout(new GridLayout(0, 1, 0, 0));
		pastePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		JScrollPane scrollPane = new JScrollPane();
		pastePanel.add(scrollPane);

		final JEditorPane pasteText = new JEditorPane();
		scrollPane.setViewportView(pasteText);
		pasteText.setToolTipText("Enter text to send to paste site here");
		pasteText.setText("Enter paste text");
		pasteText.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseClicked(MouseEvent arg0) {
				if (pasteText.getText().equals("Enter paste text")) {
					pasteText.setText("");
				}
			}
		});
		JPanel optionsPanel = new JPanel();
		pasteFrame.getContentPane().add(optionsPanel);
		GridBagLayout gbl_optionsPanel = new GridBagLayout();
		gbl_optionsPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_optionsPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_optionsPanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_optionsPanel.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		optionsPanel.setLayout(gbl_optionsPanel);

		final JComboBox comboBox = new JComboBox();
		final JPanel temp = new JPanel();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = comboBox.getSelectedItem().toString();
				for (PasteService serv : services) {
					ServiceInfo provider = serv.getClass().getAnnotation(
							ServiceInfo.class);
					if (provider == null) {
						continue;
					} else {
						if (provider.name().equals(selected)) {
							temp.removeAll();
							serv.addComps(temp);
							temp.updateUI();
							break;
						}
					}
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(services.toArray()));
		comboBox.setSelectedIndex(0);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		optionsPanel.add(comboBox, gbc_comboBox);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_1
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 1;
		optionsPanel.add(scrollPane_1, gbc_scrollPane_1);

		JPanel panel = temp;
		scrollPane_1.setViewportView(panel);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		optionsPanel.add(textField, gbc_textField);
		textField.setColumns(10);

		JButton btnPaste = new JButton("Paste");
		btnPaste.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String sel = comboBox.getSelectedItem().toString();
				for (PasteService serv : services) {
					if (serv.toString().equals(sel)) {
						final String url = serv.paste(pasteText.getText());
						serv.writeHistory(url, System.currentTimeMillis());
						textField.setText(url);

					}
				}
			}

		});
		JButton btnHistory = new JButton("History");
		btnHistory.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String sel = comboBox.getSelectedItem().toString();
				for (PasteService serv : services) {
					if (serv.toString().equals(sel)) {
						new PasteHistoryDialog(serv);
					}
				}
			}
		});
		GridBagConstraints gbc_btnHistory = new GridBagConstraints();
		gbc_btnHistory.gridx = 0;
		gbc_btnHistory.gridy = 3;
		optionsPanel.add(btnHistory, gbc_btnHistory);
		GridBagConstraints gbc_btnPaste = new GridBagConstraints();
		gbc_btnPaste.gridx = 2;
		gbc_btnPaste.gridy = 3;
		optionsPanel.add(btnPaste, gbc_btnPaste);
		pasteFrame.setLocationRelativeTo(null);
		pasteFrame.pack();
		btnPaste.requestFocusInWindow();
	}

	public static File getDataDir() {
		return dataDir;
	}

}
