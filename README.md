# timeKeeper-Processing-Sketch

timeKeeper is a Processing sketch that keeps track of working time periods.
timeKeeper shows the progress of clock time for the duration of the period both in elapsed and remaining time.
There is a progress bar that changes color as a warning when it is near the end of the working time.

See "Releases" for stand-alone applications for MacOS, Windows 32bit and Windows 64bit.  Each application has a bundled Java(tm) runtime and does not require Processing.    The MacOS application is code-signer by DeveloperID.  The Windows versions are unsigned.

If using the source with Processing as a sketch, the LXForProcessing library (https://github.com/claudeheintz/LXforProcessing) is required.

------------------------------------------ 
  
timeKeeper uses two files for customizing its behavior and for defining the time periods.

"timeKeeper.properties" is a Java properties file that contains user interface values for both regular and high DPI displays
    (set display_type=1 for Windows high DPI displays)
"timeKeeper.properties" also contains the warning time in minutes and a path to the master time period file.

File format of the time period file consists of tab separated fields on one of two types of lines:

   Full start and end time

      [title] [tab] [start_time] [tab] [end_time]

      Group1, Title1^03/22/18 8:00:00am CDT^03/22/18 8:16:00am CDT
      
      (caret ^ = tab)

   -OR-

   Calculate from previous end

        [title] [tab] [">"] {tab] [gap in minutes] [tab] [period in minutes]

        Group2, Title2^>^2^16
        
        (caret ^ = tab)

In order to see how timeKeeper works, change the first line of the default timeKeeper.txt file to today's date and time (now for the start and 5 minutes from now for the end).  If it is 1pm on July 7th, central daylight time, the first line would read:

      Group1, Title1^07/07/18 1:00:00pm CDT^07/07/18 1:05:00pm CDT

(where each caret ^ =  tab)