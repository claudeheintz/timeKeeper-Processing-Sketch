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

import processing.core.*; 
import java.util.*;
import java.io.*;


/*
 * timeKeeperUX is a static class designed to hold display location and size values and provide utility functions
 *
 * The timeKeeperUX class also contains static methods for
 * displaying the parts of the timeKeeper interface.
 *
 */
 
public class timeKeeperUX {

// these locations and sizes are calculated from width and height after fullScreen() or size()

static    int progress_h = 75;

static  	int progress_w = 700;

static  	int elapsed_x = 375;
static  	int remaining_x = 1025;
static  	int elapsed_remaining_y = 250;

static  	int bottom_y = 710;
static  	int center_x = 700;
static  	int center_y = 370;
static  	int progress_y = 300;
static  	int progress_x = 350;

//------------------- non computed values -------------------// default    --HDPI
                                                          
static  	int   bottom_dy = 50;                              // 50         --100
static  	int   timeadj_button_w = 50;                       // 50;        --100
static  	int   timeadj_button_h = 40;                       // 40;        --80
static  	int   timeadj_button_dx = 100;                     // 100        --200
static  	int   timeadj_button_dy = 25;                      // 25         --75
static  	float timeadj_font_size = 24.0f;                   // 24.0f      --48.0f
static    int   timeadj_text_dx = (timeadj_button_dx+timeadj_button_w)/2;
static    int   timeadj_text_dy = timeadj_button_dy - (int)((timeadj_button_h+timeadj_font_size)/2);

static  	int   ondeck_x = 100;                              // 100        --100
static  	float ondeck_font_size = 32.0f;                    // 32.0f      --64.0f

static  	float now_font_size = 48.0f;                       // 48.0f      --96.0f
static  	int   now_dy = 50;                                 // 50         --100
static  	float et_rt_font_size = 72.0f;                     // 72.0f      --150.0f
static  	float et_rt_label_font_size = 32.0f;               // 32.0f      --48.0f
static  	int   et_rt_label_dy = 100;                        // 100        --200

static  	float current_title_font_size = 64.0f;             // 64.0f      --96.0f
static    int   current_title_dy = 200;                      // 200        --255

static    long  warning_milliseconds = 120000;
static  String  times_file_path      = null;

public static final int DEFAULT_UX_TYPE = 0;
public static final int HDPI_UX_TYPE    = 1;

  public static void initForApplet(PApplet p) {
    initFromProperties(p);
    
    // these values are calculated
	  progress_w = (3 * p.width) / 4;
  
	  bottom_y = p.height - bottom_dy;
	  center_x = p.width/2;
	  center_y = p.height/2;
	  progress_y = center_y - progress_h;
	  progress_x = center_x - (progress_w/2);
  
	  elapsed_x = progress_x + 200;
	  remaining_x = progress_x + progress_w - 200;
	  elapsed_remaining_y = progress_y - progress_h - 30;
	}

  public static void initFromProperties(PApplet p) {
     Properties myprops = new Properties();
  
     FileInputStream in = null;
     try {
       in = new FileInputStream ( p.sketchPath("")+"/timeKeeper.properties" );
       myprops.load(in);
       in.close();
      } catch (FileNotFoundException fex) {
       in = null;
       System.out.println("Load Preferences exception: " + fex );
     } catch (IOException iox) {
       in = null;
       System.out.println("Load Preferences exception: " + iox );
     }
     
     int type = getIntegerProperty(myprops, "display_type", DEFAULT_UX_TYPE, 0);
     
     // the following are calculated later and don't necessarily need to be read from
     progress_h = getIntegerProperty(myprops, "progress_h", type, progress_h);
     progress_w = getIntegerProperty(myprops, "progress_w", DEFAULT_UX_TYPE, progress_w);
     elapsed_x = getIntegerProperty(myprops, "elapsed_x", DEFAULT_UX_TYPE, elapsed_x);
     elapsed_remaining_y = getIntegerProperty(myprops, "elapsed_remaining_y", DEFAULT_UX_TYPE, elapsed_remaining_y);
     bottom_y = getIntegerProperty(myprops, "bottom_y", DEFAULT_UX_TYPE, bottom_y);
     center_x = getIntegerProperty(myprops, "center_x", DEFAULT_UX_TYPE, center_x);
     center_y = getIntegerProperty(myprops, "center_y", DEFAULT_UX_TYPE, center_y);
     progress_y = getIntegerProperty(myprops, "progress_y", DEFAULT_UX_TYPE, progress_y);
     progress_x = getIntegerProperty(myprops, "progress_x", DEFAULT_UX_TYPE, progress_x);
     
     // the following are dual default/HDPi fixed values
     bottom_dy = getIntegerProperty(myprops, "bottom_dy", type, bottom_dy);
     timeadj_button_w = getIntegerProperty(myprops, "timeadj_button_w", type, timeadj_button_w);
     timeadj_button_h = getIntegerProperty(myprops, "timeadj_button_h", type, timeadj_button_h);
     timeadj_button_dx = getIntegerProperty(myprops, "timeadj_button_dx", type, timeadj_button_dx);
     timeadj_button_dy = getIntegerProperty(myprops, "timeadj_button_dy", type, timeadj_button_dy);
     timeadj_font_size = getFloatProperty(myprops, "timeadj_font_size", type, timeadj_font_size);
     ondeck_x = getIntegerProperty(myprops, "ondeck_x", type, ondeck_x);
     ondeck_font_size = getFloatProperty(myprops, "ondeck_font_size", type, ondeck_font_size);
     now_font_size = getFloatProperty(myprops, "now_font_size", type, now_font_size);
     now_dy = getIntegerProperty(myprops, "now_dy", type, now_dy);
     et_rt_font_size = getFloatProperty(myprops, "et_rt_font_size", type, et_rt_font_size);
     et_rt_label_font_size = getFloatProperty(myprops, "et_rt_label_font_size", type, et_rt_label_font_size);
     et_rt_label_dy = getIntegerProperty(myprops, "et_rt_label_dy", type, et_rt_label_dy);
     current_title_font_size = getFloatProperty(myprops, "current_title_font_size", type, current_title_font_size);
     current_title_dy = getIntegerProperty(myprops, "current_title_dy", type, current_title_dy);
     
     float warning_min = getFloatProperty(myprops, "warning_min", type, (float)(warning_milliseconds/60000.0));
     warning_milliseconds = minutesToMilliseconds(warning_min);
     
     //calculated from above
     timeadj_text_dx = (timeadj_button_dx+timeadj_button_w)/2;
     timeadj_text_dy = timeadj_button_dy - (int)((timeadj_button_h+timeadj_font_size)/2);
     
     //path to file
     times_file_path = (String)myprops.get("times_file_path");
  }
  
/*
 *  Path to times file
 *  Can be full path or just a file in the sketch working directory
 *
 *  File format consists of tab separated fields on one of two types of lines:
 *
 *   Full start and end times
 *      [title][tab][start_time][tab][end_time][tab]["x"]
 *
 *   -OR-
 *
 *   Calculate from previous end
 *        [title][tab][gap in minutes][tab][period in minutes]
 *
 *
 */
  public static String getTimesFilePath(String def_dir) {
    if ( times_file_path == null ) {
          times_file_path = "timekeeper.txt";
     }
     if ( ! times_file_path.startsWith("/") ) {
          times_file_path = def_dir+"/"+times_file_path;
     }
    return times_file_path;
  }

  public static int getIntegerProperty(Properties myprops, String kstr, int type, int def) {
    String pstr;
  
    if ( type == HDPI_UX_TYPE ) {
      pstr = (String)myprops.get(kstr+"1");
    } else {
      pstr = (String)myprops.get(kstr);
    }
    if ( pstr != null ) {
      return safeParseInt(pstr);
    }
    return def;
  }
  
  public static float getFloatProperty(Properties myprops, String kstr, int type, float def) {
    String pstr;
    if ( type == HDPI_UX_TYPE ) {
      pstr = (String)myprops.get(kstr+"1");
    } else {
      pstr = (String)myprops.get(kstr);
    }
    if ( pstr != null ) {
      return safeParseFloat(pstr);
    }
    return def;
  }
  
  public static long getLongProperty(Properties myprops, String kstr, int type, long def) {
    String pstr;
    if ( type == HDPI_UX_TYPE ) {
      pstr = (String)myprops.get(kstr+"1");
    } else {
      pstr = (String)myprops.get(kstr);
    }
    if ( pstr != null ) {
      return safeParseLong(pstr);
    }
    return def;
  }
  
  public static float safeParseFloat(String f) {
    try {
      return( Float.valueOf(f).floatValue() );
    } catch ( NumberFormatException e ) {}
      catch (NullPointerException ne ) {}
    return (0);
  }
  
  public static long safeParseLong(String f) {
    try {
      return( Long.valueOf(f).longValue() );
    } catch ( NumberFormatException e ) {}
    return (0);
  }
  
  public static int safeParseInt(String s) {
    try {
      return( Integer.parseInt(s) );
    } catch ( NumberFormatException e ) {}
    return (0);
  }
  
  public static long minutesToMilliseconds(String str) {
    float min = timeKeeperUX.safeParseFloat(str);
    return minutesToMilliseconds(min);
  }
  
  public static long minutesToMilliseconds(float min) {
    return (long) min * 60000;
  }

  //  button location and size =========================================

  public static int plusX() {
    return remaining_x;
  }
  
  public static int plusY() {
    return bottom_y-timeadj_button_dy;
  }
  
  public static int plusW() {
    return timeadj_button_w;
  }
  
  public static int plusH() {
    return timeadj_button_h;
  }
  
  public static int minusX() {
    return remaining_x+timeadj_button_dx;
  }
  
  public static int minusY() {
    return bottom_y-timeadj_button_dy;
  }
  
  public static int minusW() {
    return timeadj_button_w;
  }
  
  public static int minusH() {
    return timeadj_button_h;
  }
  
// NOTE:  Drawing methods set graphics state and depend on previous state
//        Use them in this order or else...
  
//  draw offset string =========================================
  
  public static void drawTimeOffset(PApplet p, String str) {
    p.textSize(timeadj_font_size);
    p.textAlign(PApplet.CENTER);
    p.fill(255);
    p.text(str, remaining_x+timeadj_text_dx, bottom_y-timeadj_text_dy);
  }
  
//  draw time now      =========================================
  
  public static void drawTimeNow(PApplet p, String str) {
    p.textSize(now_font_size);
    p.fill(255);
    p.text(str, center_x , now_dy);
    
  }
  
//  draw "on deck"      =========================================
  
  public static void drawOnDeck(PApplet p, String str) {
    p.textSize(ondeck_font_size);
    p.textAlign(PApplet.LEFT);
    p.text("On Deck: " + str, ondeck_x, bottom_y);
  }
  
//  draw elapsed      =========================================
  
  public static void drawElapsed(PApplet p, String str) {
    p.textSize(et_rt_font_size);
    p.textAlign(PApplet.RIGHT);
    p.text(str, elapsed_x, elapsed_remaining_y);
    p.textSize(et_rt_label_font_size);
    p.text("Elapsed: ", elapsed_x, elapsed_remaining_y-et_rt_label_dy);
  }
  
//  draw remaining      =========================================
  
  public static void drawRemaining(PApplet p, String str) {
    p.textSize(et_rt_font_size);
    p.textAlign(PApplet.LEFT);
    p.text(str, remaining_x, elapsed_remaining_y);
    p.textSize(et_rt_label_font_size);
    p.text("Remaining: ", remaining_x, elapsed_remaining_y-et_rt_label_dy);
  }

//  draw current time period title ==============================

  public static void drawCurrentTitle(PApplet p, String str) {
    p.textSize(current_title_font_size);
    p.textAlign(PApplet.CENTER);
    p.text(str, center_x, progress_y+current_title_dy);
  }
  
//  draw progress bar ===========================================

  public static void drawProgressBar(PApplet p, long et, long rt, long duration) {
      p.fill(64, 64, 96);
      p.rect(progress_x, progress_y, progress_w, progress_h);
      
      if ( rt < 0 ) {
        et = duration;
      }
      float ex = p.map(et, 0, duration, 0, progress_w);
      
      if ( rt > warning_milliseconds ) {
        p.fill(0, 255, 0);
      } else if ( rt > 0 ) {
        p.fill(255, 255, 0);
      } else {
        p.fill(255, 0, 0);
      }
      p.rect(progress_x, progress_y, ex, progress_h);
  }

} // <- class timeKeeperUX