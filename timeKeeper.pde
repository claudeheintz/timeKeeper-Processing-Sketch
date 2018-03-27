/*
Copyright (c) 2018, Claude Heintz
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of LXforProcessing nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import java.util.*;
import java.text.*;
import java.io.*;

// requires LXforProcessing library => https://github.com/claudeheintz/LXforProcessing
import lx4p.*;

/*
 *  TimeKeeper app keeps track of working time periods.
 *  TimeKeeper shows the progress of clock time for the duration of the period both in elapsed and remaining time.
 *  There is a progress bar that changes color as a warning when it is near the end of the working time.
 *  
 *  TimeKeeper uses two files for customizing its behavior and for defining the time periods.
 *
 *  "timeKeeper.properties" is a Java properties file that contains user interface values for both regular and high DPI displays
 *  "timeKeeper.properties" also contains the warning time in minutes and a path to the master time period file
 *
 *  File format of the time period file consists of tab separated fields on one of two types of lines:
 *
 *   Full start and end time
 *
 *      [title] [tab] [start_time] [tab] [end_time]
 *
 *      Group1, Title1  03/22/18 8:00:00am CDT  03/22/18 8:16:00am CDT
 *
 *   -OR-
 *
 *   Calculate from previous end
 *
 *        [title] [tab] [">"] {tab] [gap in minutes] [tab] [period in minutes]
 *
 *        Group2, Title2  >  2  16
 */

Date now;
SimpleDateFormat nowDisplayFormat = new SimpleDateFormat("hh:mm:ss");
SimpleDateFormat nowDayFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
SimpleDateFormat textFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ssa z");
String nowString;
String elapsedString;
String remainingString;
long et;
long rt;
float ex;
long duration;
long adjust = 0;

TimePeriods times;
TimePeriod current;
String onDeck = "";

public class LXPAdjustPlusButton extends LXPButton {
    public LXPAdjustPlusButton(int xp, int yp, int w, int h, String t) {
      super(xp,yp,w,h,t);
    }
    
    public void mouseClicked() {
      adjust += 60000;
    }
    
 }

LXPAdjustPlusButton plusButton;

public class LXPAdjustMinusButton extends LXPButton {
    public LXPAdjustMinusButton(int xp, int yp, int w, int h, String t) {
      super(xp,yp,w,h,t);
    }
    
    public void mouseClicked() {
      adjust -= 60000;
      if ( adjust < 0 ) {
        adjust = 0;
      }
    }
    
 }
 
LXPAdjustMinusButton minusButton;



void setup() {
  size(1400, 740);
  //fullScreen();
  
  frameRate(10);
  
  // compute size & locations for screen
  timeKeeperUX.initForApplet(this);

  times = new TimePeriods(timeKeeperUX.getTimesFilePath(sketchPath("")));
  getNextPeriod();
  
  
  
  plusButton = new LXPAdjustPlusButton(timeKeeperUX.plusX(),
                                       timeKeeperUX.plusY(),
                                       timeKeeperUX.plusW(),
                                       timeKeeperUX.plusH(), "+");
                                       
  minusButton = new LXPAdjustMinusButton(timeKeeperUX.minusX(),
                                       timeKeeperUX.minusY(),
                                       timeKeeperUX.minusW(),
                                       timeKeeperUX.minusH(), "-");
}


void draw() {
  background(0);
  
  // time adjustment
  timeKeeperUX.drawTimeOffset(this, Long.toString(adjust/60000));
  //adjust buttons
  plusButton.draw(this);
  minusButton.draw(this);
  
  // the current time (now)
  now = new Date();
  nowString = nowDisplayFormat.format(now);
  timeKeeperUX.drawTimeNow(this, nowString);
  
  // on deck
  timeKeeperUX.drawOnDeck(this, onDeck);
  
  // --------------- current time period ---------------
  
  if ( current != null ) {
    // adjust now, if needed,  then calculate elapsed and remaining
    long n = now2Time(now) - adjust;
    et = n - current.startTime;
    rt = current.endTime - n;
    
    elapsedString = time2String(et);
    remainingString = time2String(rt);
    

    // --------------- elapsed/remaining ---------------
    
    timeKeeperUX.drawElapsed(this, elapsedString);
    timeKeeperUX.drawRemaining(this, remainingString);
    
    // --------------- current title ---------------
    

    timeKeeperUX.drawCurrentTitle(this, current.title);
    
    // --------------- progress bar ---------------
    if ( et > 0 ) {
      timeKeeperUX.drawProgressBar(this, et, rt, duration);
      
      if ( current.checkCompleted(n) ) {
        getNextPeriod();
      }
      
    }  // elapsed > 0
    
  }    // current != null
  
}

void mousePressed() {
  if ( mouseX > width - 200 ) {
    if ( mouseY < 200 ) {
      exit();
    }
  }
}

void mouseReleased() {
  plusButton.mouseReleased();
  minusButton.mouseReleased();
}


void getNextPeriod() {
  current = times.nextUncompleted();
  onDeck = times.onDeckTitle(current);
  if ( current != null ) {
    duration = current.endTime - current.startTime;
  }
}

long now2Time(Date nowdate) {
  Date d;
  String nowstr;
  long r = 0;
  try {
    nowstr = textFormat.format(nowdate);
    d = textFormat.parse(nowstr);
    r = d.getTime();
  } catch (Exception e){
  }
  return r;
}

long string2Time(String tstr) {
  Date d;
  long r = 0;
  try {
    d = textFormat.parse(tstr);
    r = d.getTime();
  } catch (Exception e){
  }
  return r;
}

String time2String(long time) {
  boolean negative = false;
  long t = time;
  if ( t < 0 ) {
    t = -time;
    negative = true;
  }
  
  // break into h:mm:ss
  long hr = 3600000;
  long h = t / hr;
  long rem = t % hr;
  long min = 60000;
  long m = rem / min;
  rem = rem % min;
  long s = rem / 1000l;
  
  //build the string
  String hrpart = "";
  if ( negative ) {
    hrpart = "-";
  }
  if ( h > 0 ) {
    hrpart = hrpart + Long.toString(h) + ":";
  }
  String minpart;
  if (( m > 9 ) || ( h == 0 )) {
    minpart = Long.toString(m) + ":";
  } else {
    minpart = "0" + Long.toString(m) + ":";
  }
  String secpart;
  if ( s > 9 ) {
    secpart = Long.toString(s);
  } else {
    secpart = "0" + Long.toString(s);
  }
  return (hrpart+minpart+secpart);
}

String time2dateString(long time) {
  return textFormat.format(new Date(time));
}

String stringOfLength(String str, int len) {
  if ( str.length() < len ) {
    String rs = str;
    int pad = len - str.length();
    for (int j=0; j<pad; j++) {
      rs += " ";
    }
    return rs;
  }
  if ( str.length() > len ) {
    return str.substring(0,len);
  }
  
  return str;
}

String substringBeforeSeperator(String s, String sp) {
    int spi = s.indexOf(sp);
    if ( spi < 0 ) {  //not found
      return null;    //null??
    }
    return s.substring(0,spi);
  }

String substringAfterSeperator(String s, String sp) {
    int spi = s.indexOf(sp);
    if ( spi < 0 ) {  //not found
      return null;    //null??
    }
    int loc = spi + sp.length();
    return s.substring(loc, s.length());
  }
  
Vector<String> substringsUsingSeperator(String s, String ss) {
    Vector<String> rv = new Vector<String>();
    String cs;
    String rs = s;
    String ts;
    boolean done = false;
    
    while ( !done ) {
      cs = substringBeforeSeperator(rs, ss);
      if ( cs == null ) {
        done = true;
      } else {
        rv.addElement(cs);
        ts = substringAfterSeperator( rs, ss );
        if ( ts != null ) {
          rs = ts;
        } else {
          done = true;
        }
      }
    }
    if ( (rs != null) && (rs.length() > 0) ) {
      rv.addElement(rs);
    }
    
    return rv;
  }
  
  Vector<String> readFile(String fileName) throws IOException {
      FileInputStream fis = new FileInputStream(fileName);
      InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
      BufferedReader br = new BufferedReader(isr);
      
      try {
          String line = br.readLine();
          Vector<String> rv = new Vector<String>();

          while (line != null) {
              rv.add(line);
              line = br.readLine();
          }
          return rv;
      } finally {
          br.close();
      }
  }
  
  public class TimePeriod {
    
    public long startTime;
    public long endTime;
    public boolean completed = false;
    public String title;
    
    public TimePeriod(String str, long last_end) {
      Vector<String> pv = substringsUsingSeperator(str, "\t");
      if ( pv.size() == 3 ) {
        title = pv.elementAt(0);
        startTime = string2Time(pv.elementAt(1));
        endTime = string2Time(pv.elementAt(2));
        System.out.println(stringOfLength(title, 32) + ": " + stringOfLength(pv.elementAt(1), 23) + " - " + stringOfLength(pv.elementAt(2), 23) + " => " + time2String(endTime-startTime));
      } else if ( pv.size() == 4 ) {
        title = pv.elementAt(0);
        startTime = last_end + timeKeeperUX.minutesToMilliseconds(pv.elementAt(2));
        endTime = startTime + timeKeeperUX.minutesToMilliseconds(pv.elementAt(3));
        System.out.println(stringOfLength(title, 32) + ": " + time2dateString(startTime) + " - " + time2dateString(endTime) + " => " + time2String(endTime-startTime));
      } else {
        System.out.println("Error creating TimePeriod using " + str);
        title = "ERROR";
      }
    }
    
    public boolean checkCompleted(long t) {
      if ( t > (endTime+60000) ) {
        completed = true;
      }
      
      return completed;
    }
    
  }
  
  public class TimePeriods {
    
    public Vector<TimePeriod> periods;
    
    public TimePeriods(String fileName) {
      periods = new Vector<TimePeriod>();
      Vector<String> fv = null;
  
      try {
        fv = readFile(fileName);
      } catch (Exception e) {
        System.out.println("could not read file");
      }
  
      if ( fv != null ) {
        String ps;
        TimePeriod ntp;
        long last_end = 0;
        Enumeration<String> en = fv.elements();
        while ( en.hasMoreElements() ) {
          ps = en.nextElement();
          ntp = new TimePeriod(ps, last_end);
          last_end = ntp.endTime;
          periods.addElement(ntp);
        }
      }
    }
    
    
    public TimePeriod nextUncompleted() {
      TimePeriod tp;
      Enumeration<TimePeriod> en = periods.elements();
      while ( en.hasMoreElements() ) {
        tp = en.nextElement();
        if ( ! tp.completed ) {
          return tp;
        }
      }
      
      return null;
    }
    
    
    public String onDeckTitle(TimePeriod tp) {
      if ( tp != null ) {
        int index = periods.indexOf(tp);
        if ( index >= 0 ) {
          if ( index < (periods.size() - 1) ) {
            return periods.elementAt(index+1).title;
          }
        }
      }
      
      return "";
    }
  }