import processing.core.*; 

// tkux is a static class designed to hold display location and size values
//
// The tkux class also contains static methods for
// displaying the parts of the timeKeeper interface.

public class tkux {

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

public static final int DEFAULT_UX_TYPE = 0;
public static final int HDPI_UX_TYPE    = 1;

	public static void initForApplet(PApplet p, int type) {
	  progress_w = (3 * p.width) / 4;
  
	  bottom_y = p.height - bottom_dy;
	  center_x = p.width/2;
	  center_y = p.height/2;
	  progress_y = center_y - progress_h;
	  progress_x = center_x - (progress_w/2);
  
	  elapsed_x = progress_x + 200;
	  remaining_x = progress_x + progress_w - 200;
	  elapsed_remaining_y = progress_y - progress_h - 30;
  
	  if ( type == HDPI_UX_TYPE ) {
      progress_h = 150;
      current_title_dy = 280;
      
			bottom_dy = 100;                             //50         --100
  
			timeadj_button_w = 100;                      //50;        --100
			timeadj_button_h = 80;                       //40;        --80
			timeadj_button_dx = 200;                     //100        --200
			timeadj_button_dy = 75;                      //25         --75
			timeadj_font_size = 48.0f;                    //24.0f      -- 48.0f
			timeadj_text_dx = (timeadj_button_dx+timeadj_button_w)/2;
  
			ondeck_x = 100;                              // 100       --100
			ondeck_font_size = 64.0f;                      // 32.0f     --64.0f
  
			now_font_size = 96.0f;                     // 48.0f     --96.0f
			now_dy = 100;                            // 50        --100
			et_rt_font_size = 160.0f;           // 72.0f     --150.0f
			et_rt_label_font_size = 48.0f;       // 32.0f     --48.0f
			et_rt_label_dy = 200;              // 100       --200
  
			current_title_font_size = 112.0f;                // 64.0f     --96.0f    
	  }
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

} // <- class tkux