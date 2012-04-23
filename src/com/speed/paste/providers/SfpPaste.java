package com.speed.paste.providers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.speed.paste.PasteService;
import com.speed.paste.ServiceInfo;

/**
 * 
 * This class accesses Strictfp Paste for SpeedPaste.
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
 */
@ServiceInfo(name = "Strictfp Paste", url = "http://paste.strictfp.com")
public class SfpPaste extends PasteService {

	private JComboBox highlighting;
	private JCheckBox privCheck;
	private static final String[] HIGHLIGHT_LANGS = new String[] { "C++", "C#",
			"CSS", "Delphi", "Groovy", "Java", "JavaScript", "Perl", "PHP",
			"Plain text", "Python", "Ruby", "SQL", "Visual Basic", "XML/HTML" };

	public SfpPaste() {
		privCheck = new JCheckBox("Private");
		highlighting = new JComboBox();
		highlighting.setModel(new DefaultComboBoxModel(HIGHLIGHT_LANGS));
		highlighting.setSelectedIndex(5);
	}

	public void addComps(final JPanel contentPanel) {
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
				highlighting.setModel(new DefaultComboBoxModel(new String[] {
						"C++", "C#", "CSS", "Delphi", "Groovy", "Java",
						"JavaScript", "Perl", "PHP", "Plain text", "Python",
						"Ruby", "SQL", "Visual Basic", "XML/HTML" }));
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

	@Override
	public String paste(final String data) {
		HttpURLConnection conn = null;
		try {
			final String encoded = URLEncoder.encode(data, "UTF-8");
			conn = (HttpURLConnection) new URL(
					"http://paste.strictfp.com/index.php").openConnection();
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
					privCheck.isSelected() ? "paste=%s&language=%s&submit=%s&private=%s"
							: "paste=%s&language=%s&submit=%s", encoded,
					highlighting.getSelectedItem().toString().toLowerCase(),
					"Paste", "Private"));
			write.flush();
			write.close();
			conn.getInputStream().read();
			String url = conn.getURL().toString();
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
