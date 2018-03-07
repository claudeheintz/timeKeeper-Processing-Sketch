import processing.core.*; 

// tkux is a static class designed to hold display location and size values


public class tkux {

	// most locations and sizes are calculated from width and height after fullScreen() or size()
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

//-------------------------------------------- non computed values

//                                                         //mac   --HDPI

static  	int bottom_dy = 50;                              //50         --100
static  	int timeDiffButton_w = 50;                       //50;        --100
static  	int timeDiffButton_h = 40;                       //40;        --80
static  	int timeDiffButton_dx = 100;                     //100        --200
static  	int timeDiffButton_dy = 25;                      //25         --75
static  	float timeDiffFontSize = 24.0f;                  //24.0f      -- 48.0f
static      int timeDiffText_dx = (timeDiffButton_dx+timeDiffButton_w)/2;

static  	int onDeck_x = 100;                              // 100       --100
static  	float onDeckFontSize = 32.0f;                    // 32.0f     --64.0f

static  	float nowTimeFontSize = 48.0f;                   // 48.0f     --96.0f
static  	int nowTime_dy = 50;                             // 50       --100
static  	float elapsedRemainingFontSize = 72.0f;          // 72.0f    --150.0f
static  	float elapsedRemainingLabelFontSize = 32.0f;     // 32.0f     --48.0f
static  	int elapsedRemainingLabel_dy = 100;              // 100       --200

static  	float currentTitleFontSize = 64.0f;              // 64.0f     --96.0f
static    int   currentTitle_dy = 200;                     // 200       --255

public static final int DEFAULT_UX_TYPE = 0;
public static final int HDPI_UX_TYPE    = 1;

	public static void initForApplet(PApplet p, int type) {
	  tkux.progress_w = (3 * p.width) / 4;
  
	  tkux.bottom_y = p.height - tkux.bottom_dy;
	  tkux.center_x = p.width/2;
	  tkux.center_y = p.height/2;
	  tkux.progress_y = tkux.center_y - tkux.progress_h;
	  tkux.progress_x = tkux.center_x - (tkux.progress_w/2);
  
	  tkux.elapsed_x = tkux.progress_x + 200;
	  tkux.remaining_x = tkux.progress_x + tkux.progress_w - 200;
	  tkux.elapsed_remaining_y = tkux.progress_y - tkux.progress_h - 30;
  
	  if ( type == HDPI_UX_TYPE ) {
      progress_h = 125;
      currentTitle_dy = 255;
      
			bottom_dy = 100;                             //50         --100
  
			timeDiffButton_w = 100;                      //50;        --100
			timeDiffButton_h = 80;                       //40;        --80
			timeDiffButton_dx = 200;                     //100        --200
			timeDiffButton_dy = 75;                      //25         --75
			timeDiffFontSize = 48.0f;                    //24.0f      -- 48.0f
			timeDiffText_dx = (timeDiffButton_dx+timeDiffButton_w)/2;
  
			onDeck_x = 100;                              // 100       --100
			onDeckFontSize = 64.0f;                      // 32.0f     --64.0f
  
			nowTimeFontSize = 96.0f;                     // 48.0f     --96.0f
			nowTime_dy = 100;                            // 50        --100
			elapsedRemainingFontSize = 160.0f;           // 72.0f     --150.0f
			elapsedRemainingLabelFontSize = 48.0f;       // 32.0f     --48.0f
			elapsedRemainingLabel_dy = 200;              // 100       --200
  
			currentTitleFontSize = 112.0f;                // 64.0f     --96.0f    
	  }
	}

} // <- class tkux