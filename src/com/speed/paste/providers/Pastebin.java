package com.speed.paste.providers;

import javax.swing.JPanel;

import com.speed.paste.ServiceInfo;
import com.speed.paste.PasteService;

@ServiceInfo(name = "Pastebin", url = "http://pastebin.com", hidden = true)
public class Pastebin extends PasteService {

	public void addComps(JPanel contentPanel) {

	}

	@Override
	public String paste(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
