# FileHook
A simple Java application for hooking lines of text from a file and copying them directly to the clipboard

Dependencies needed to compile: JavaFX

Basic use:
 * Press [Select Script] to select the source file. The name of the file will be shown, as well as the line number 0.
 * Press [Next line] to start reading the lines which will be automatically copied to the clipboard. The whole or part of the current line will be shown.
 * Alternatively, you can input the number of the line in the number of line field to go directly to that line.
 * NEW: Press [Save progress] button under the line number to save the file which is actually being read and the current line. This will be saved in a separate file called "recent.save" in the same folder in which the jar is located.
 * NEW: To access the history of recent files, press [Recent] under [Select Script] which will prompt a list of recent opened files. Press the [X] button to go back.
 
 * PS: For now there is no encoding option for the files, so it is recommended for the files to be in UTF-8 which is the only format supported. Likewise, non-plain text files as well as RTF are not supported neither. 
 
Features yet to be implemented:
	* Nothing more planned for now
	
