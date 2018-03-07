import java.util.*;
import java.text.*;
import java.io.*;
import lx4p.*;

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
  times = new TimePeriods(sketchPath("")+"/timekeeper.txt");
  getNextPeriod();
  
  // compute size & locations for screen
  tkux.initForApplet(this, tkux.DEFAULT_UX_TYPE);
  //tkux.initForApplet(this, tkux.HDPI_UX_TYPE);
  
  plusButton = new LXPAdjustPlusButton(tkux.remaining_x,
                                        tkux.bottom_y-tkux.timeDiffButton_dy,
                                        tkux.timeDiffButton_w, tkux.timeDiffButton_h, "+");
  minusButton = new LXPAdjustMinusButton(tkux.remaining_x+tkux.timeDiffButton_dx,
                                        tkux.bottom_y-tkux.timeDiffButton_dy,
                                        tkux.timeDiffButton_w, tkux.timeDiffButton_h, "-");
}


void draw() {
  background(0);
  
  //adjust buttons
  textSize(tkux.timeDiffFontSize);
  plusButton.draw(this);
  minusButton.draw(this);
  
  // time adjustment
  textAlign(PApplet.CENTER);
  fill(255);
  text(Long.toString(adjust/60000), tkux.remaining_x+tkux.timeDiffText_dx, tkux.bottom_y-(tkux.timeDiffButton_dy/2));
  
  // the current time (now)
  now = new Date();
  nowString = nowDisplayFormat.format(now);
  textSize(tkux.nowTimeFontSize);
  fill(255);
  text(nowString, width/2 , tkux.nowTime_dy);
  
  // on deck
  textSize(tkux.onDeckFontSize);
  textAlign(PApplet.LEFT);
  text("On Deck: " + onDeck, tkux.onDeck_x, tkux.bottom_y);
  
  // --------------- current time period ---------------
  
  if ( current != null ) {
    // adjust now, if needed,  then calculate elapsed and remaining
    long n = now2Time(now) - adjust;
    et = n - current.startTime;
    rt = current.endTime - n;
    
    elapsedString = time2String(et);
    remainingString = time2String(rt);
    
    
    // --------------- elapsed/remaining ---------------
    
    textSize(tkux.elapsedRemainingFontSize);
    textAlign(PApplet.RIGHT);
    text(elapsedString, tkux.elapsed_x, tkux.elapsed_remaining_y);
    textSize(tkux.elapsedRemainingLabelFontSize);
    text("Elapsed: ", tkux.elapsed_x, tkux.elapsed_remaining_y-tkux.elapsedRemainingLabel_dy);
    
    textSize(tkux.elapsedRemainingFontSize);
    textAlign(PApplet.LEFT);
    text(remainingString, tkux.remaining_x, tkux.elapsed_remaining_y);
    textSize(tkux.elapsedRemainingLabelFontSize);
    text("Remaining: ", tkux.remaining_x, tkux.elapsed_remaining_y-tkux.elapsedRemainingLabel_dy);
    
    textSize(tkux.currentTitleFontSize);
    textAlign(PApplet.CENTER);
    text(current.title, width/2, tkux.progress_y+200);
    
    // --------------- progress bar ---------------
    if ( et > 0 ) {
      fill(64, 64, 96);
      rect(tkux.progress_x,tkux.progress_y, tkux.progress_w, tkux.progress_h);
      
      if ( rt < 0 ) {
        et = duration;
      }
      ex = map(et, 0, duration, 0, tkux.progress_w);
      
      if ( rt > 120000 ) {
        fill(0, 255, 0);
      } else if ( rt > 0 ) {
        fill(255, 255, 0);
      } else {
        fill(255, 0, 0);
      }
      rect(tkux.progress_x, tkux.progress_y, ex, tkux.progress_h);
      
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
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      
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
    
    public TimePeriod(String str) {
      Vector<String> pv = substringsUsingSeperator(str, "\t");
      if ( pv.size() == 3 ) {
        title = pv.elementAt(0);
        startTime = string2Time(pv.elementAt(1));
        endTime = string2Time(pv.elementAt(2));
        System.out.println(stringOfLength(title, 32) + ": " + stringOfLength(pv.elementAt(1), 23) + " - " + stringOfLength(pv.elementAt(2), 23) + " => " + time2String(endTime-startTime));
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
        Enumeration<String> en = fv.elements();
        while ( en.hasMoreElements() ) {
          ps = en.nextElement();
          periods.addElement(new TimePeriod(ps));
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