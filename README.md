# SpeedPaste

## About

SpeedPaste is a free program that allows quick pasting to various 'pastebin' sites.

Support for a pastebin site can be added by extending the "PasteService" class and using the ServiceInfo annotation with that class.

PasteServices are loaded from the bin directory in the program itself, and will load from either:
 "%APPDATA%/SpeedPaste/services" on Windows, or,
 "user.home/.SpeedPaste/services" on other operating systems.


## Features

Currently, SpeedPaste has the following features:

* Saves a history of pastes with links locally
* One click pasting from the system tray
* Extensible and able to load 'paste services' from a local directory
* Built-in support for [Strictfp Paste](http://paste.strictfp.com), and more to come.
* Can be started on system start up on Windows systems.

## Building

SpeedPaste can be built using Apache Ant. It is guaranteed to compile with a Java 7 compiler, and may compile under Java 6.

There are currently no plans to use any Java 8 code.

Simply run `ant` in the project root to start automatic compilation. This will also create a runnable binary under the build directory.

## Usage

If using the pre-built binary, run as:

    java -jar SpeedPaste.jar

or, if building yourself, compile using the provided ant build then run from the project's root as:

    java -jar build/SpeedPaste.jar


## Licensing

Application paste icon from: http://www.gettyicons.com/free-icon/112/must-have-icon-set/free-paste-icon-png/

Thank you for using SpeedPaste. SpeedPaste is free software.

SpeedPaste is licensed under the LGPL, see the included file 'lgpl-3.0.txt' for more information. 