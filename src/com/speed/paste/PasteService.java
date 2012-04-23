package com.speed.paste;

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

}
