# timeKeeper-Processing-Sketch

timeKeeper is a Processing sketch that keeps track of working time periods.
timeKeeper shows the progress of clock time for the duration of the period both in elapsed and remaining time.
There is a progress bar that changes color as a warning when it is near the end of the working time.
  
timeKeeper uses two files for customizing its behavior and for defining the time periods.

"timeKeeper.properties" is a Java properties file that contains user interface values for both regular and high DPI displays
    (set display_type=1 for high DPI)
"timeKeeper.properties" also contains the warning time in minutes and a path to the master time period file.

File format of the time period file consists of tab separated fields on one of two types of lines:

   Full start and end time

      [title] [tab] [start_time] [tab] [end_time]

      Group1, Title1  03/22/18 8:00:00am CDT  03/22/18 8:16:00am CDT

   -OR-

   Calculate from previous end

        [title] [tab] [">"] {tab] [gap in minutes] [tab] [period in minutes]

        Group2, Title2  >  2  16
