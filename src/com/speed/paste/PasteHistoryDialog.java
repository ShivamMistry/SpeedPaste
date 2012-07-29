package com.speed.paste;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.speed.paste.PasteService.PasteHistory;

/**
 * 
 * Builds a GUI interface to view the paste history of this service.
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
public class PasteHistoryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private DefaultListModel<PasteHistory> model;
	private JList<PasteHistory> list;

	/**
	 * Create the dialog.
	 */
	public PasteHistoryDialog(final PasteService service) {
		setTitle(service.toString() + " History");
		final Set<PasteHistory> history = service.getHistory();
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			model = new DefaultListModel<PasteHistory>();
			for (PasteHistory h : history) {
				model.addElement(h);
			}
			list = new JList<PasteHistory>(model);
			list.setFixedCellHeight(15);
			JScrollPane scrlPane = new JScrollPane(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrlPane.setViewportView(list);
			scrlPane.setSize(contentPanel.getSize());
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(scrlPane, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRemoveAll = new JButton("Remove All");
				btnRemoveAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						model.clear();
						service.getHistory().clear();
						try {
							FileOutputStream fos = new FileOutputStream(service
									.getHistoryFile());
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
				buttonPane.add(btnRemoveAll);
			}
			{
				JButton btnRemove = new JButton("Remove");
				btnRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (list.getSelectedIndex() != -1) {
							List<PasteHistory> selected = list
									.getSelectedValuesList();
							history.removeAll(selected);
							for (PasteHistory h : selected) {
								model.removeElement(h);
							}
							try {
								FileOutputStream fos = new FileOutputStream(
										service.getHistoryFile());
								for (PasteHistory h : history) {
									fos.write((h.time + " : " + h.link + "\r\n")
											.getBytes());
								}
								fos.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						}
					}
				});
				buttonPane.add(btnRemove);
			}
			{
				JButton btnCopy = new JButton("Copy");
				btnCopy.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (list.getSelectedIndex() != -1) {
							List<PasteHistory> selected = list
									.getSelectedValuesList();
							StringBuilder links = new StringBuilder();
							for (PasteHistory hist : selected) {
								links.append(hist.link).append("\r\n");
							}
							Toolkit.getDefaultToolkit()
									.getSystemClipboard()
									.setContents(
											new StringSelection(links
													.toString()), null);
						}
					}
				});
				buttonPane.add(btnCopy);
			}
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent arg0) {
						PasteHistoryDialog.this.dispose();
					}
				});
				{
					JButton btnCopyAll = new JButton("Copy All");
					btnCopyAll.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							Set<PasteHistory> pastes = service.getHistory();
							StringBuilder links = new StringBuilder();
							for (PasteHistory hist : pastes) {
								links.append(hist.link).append("\r\n");
							}
							Toolkit.getDefaultToolkit()
									.getSystemClipboard()
									.setContents(
											new StringSelection(links
													.toString()), null);
						}
					});
					buttonPane.add(btnCopyAll);
				}
				cancelButton.setActionCommand("Close");
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
