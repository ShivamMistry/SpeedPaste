SpeedPaste is a free program that allows quick pasting to various 'pastebin' sites.

Support for a pastebin site can be added by extending the "PasteService" class and using the ServiceInfo annotation with that class.

PasteServices are loaded from the bin directory in the program itself, and will load from either:
 "%APPDATA%/SpeedPaste/services" on Windows
 and "user.home/.SpeedPaste/services" on other operating systems. 
 
Run as:
	java -jar SpeedPaste.jar
or, if building yourself, compile then run from the project's root as:
	java -cp bin com.speed.paste.PasteGui


Paste icon from http://www.gettyicons.com/free-icon/112/must-have-icon-set/free-paste-icon-png/

Thank you for using SpeedPaste.

SpeedPaste is licensed under the LGPL, see the included file 'lgpl-3.0.txt' for more information. 