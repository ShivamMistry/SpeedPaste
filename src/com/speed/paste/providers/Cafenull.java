package com.speed.paste.providers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.speed.paste.ServiceInfo;
import com.speed.paste.PasteService;

@ServiceInfo(name = "Cafenull", url = "http://paste.cafenull.com")
public class Cafenull extends PasteService {
	private final static String[] HIGHLIGHT_LANGS = new String[] { "C", "C++",
			"C#", "CSS", "HTML", "Java", "Javascript", "Perl", "PHP", "Python",
			"Ruby", "Scala", "Text" };
	private JCheckBox privCheck;
	private JComboBox highlighting;
	private Pattern pattern = Pattern
			.compile("document\\.location = \"(http://paste.cafenull.com/paste\\.php\\?i=.+?)\"");

	public Cafenull() {
		privCheck = new JCheckBox("Private");
		highlighting = new JComboBox();
		highlighting.setModel(new DefaultComboBoxModel(HIGHLIGHT_LANGS));
		highlighting.setSelectedIndex(5);
	}

	public void addComps(JPanel contentPanel) {
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 106, 75, 0 };
		gbl_contentPanel.rowHeights = new int[] { 23, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			if (privCheck == null)
				privCheck = new JCheckBox("Private");
			GridBagConstraints gbc_privCheck = new GridBagConstraints();
			gbc_privCheck.insets = new Insets(0, 0, 5, 5);
			gbc_privCheck.anchor = GridBagConstraints.NORTHWEST;
			gbc_privCheck.gridx = 0;
			gbc_privCheck.gridy = 0;
			contentPanel.add(privCheck, gbc_privCheck);
		}
		{
			JLabel lblHighlighting = new JLabel("Highlighting:");
			GridBagConstraints gbc_lblHighlighting = new GridBagConstraints();
			gbc_lblHighlighting.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc_lblHighlighting.insets = new Insets(0, 0, 0, 5);
			gbc_lblHighlighting.gridx = 0;
			gbc_lblHighlighting.gridy = 1;
			contentPanel.add(lblHighlighting, gbc_lblHighlighting);
		}
		{
			if (highlighting == null) {
				highlighting = new JComboBox();
				highlighting
						.setModel(new DefaultComboBoxModel(HIGHLIGHT_LANGS));
				highlighting.setSelectedIndex(5);
			}
			GridBagConstraints gbc_highlighting = new GridBagConstraints();
			gbc_highlighting.weightx = 1;
			gbc_highlighting.fill = GridBagConstraints.REMAINDER;
			gbc_highlighting.gridx = 1;
			gbc_highlighting.gridy = 1;
			contentPanel.add(highlighting, gbc_highlighting);
		}
	}

	public String paste(String data) {
		HttpURLConnection conn = null;
		try {
			final String encoded = URLEncoder.encode(data, "UTF-8");
			conn = (HttpURLConnection) new URL(
					"http://paste.cafenull.com/paste.php").openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					Integer.toString(encoded.length()));

			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(
					conn.getOutputStream()));
			write.write(String.format(
					privCheck.isSelected() ? "data=%s&lang=%s&priv=%s"
							: "data=%s&lang=%s", encoded, highlighting
							.getSelectedIndex(), "on"));
			write.flush();
			write.close();
			BufferedReader read = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String t;
			String url = null;
			while ((t = read.readLine()) != null) {
				Matcher matcher = pattern.matcher(t);
				if (matcher.find()) {
					url = matcher.group(1);
				}
			}
			read.close();
			return url;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

}
