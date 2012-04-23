package com.speed.paste;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * This annotation is used for providing info about services.
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
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceInfo {
	String name();

	String url();

	boolean hidden() default false;

}
