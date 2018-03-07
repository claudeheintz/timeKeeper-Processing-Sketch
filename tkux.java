


public class tkux {
	// most locations and sizes are calculated from width and height after fullScreen()

static	    int progress_h = 75;

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
static    boolean use_full_screen = false;

static  	int bottom_dy = 50;                              //50     --100

static  	int timeDiffButton_w = 50;                       //50;    --100
static  	int timeDiffButton_h = 40;                       //40;    --80
static  	int timeDiffButton_dx = 100;                     //100    --200
static  	int timeDiffButton_dy = 25;                      //25     --75
static  	int timeDiffText_dx = (timeDiffButton_dx+timeDiffButton_w)/2;
static  	float timeDiffFontSize = 24.0f;                  //24.0f  -- 48.0f

static  	int onDeck_x = 100;                              // 100   --100
static  	float onDeckFontSize = 32.0f;                    // 32.0f --64.0f

static  	float nowTimeFontSize = 48.0f;                   // 48.0f     --96.0f
static  	int nowTime_dy = 50;                             // 50        --100
static  	float elapsedRemainingFontSize = 72.0f;          // 72.0f     --150.0f
static  	float elapsedRemainingLabelFontSize = 32.0f;     // 32.0f     --48.0f
static  	int elapsedRemainingLabel_dy = 100;              // 100       --200

static  	float currentTitleFontSize = 64.0f;              // 64.0f     --96.0f

}