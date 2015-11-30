package com.mycom.data;

import com.starclub.enrique.R;

public class MyConstants {

	/*     NOTE  -- changes
	 *     res/values/strings.xml  -> app_name  
	 *     AndroidManifest.xml 
	 *               -> android:icon ;;;;; 
	 *                   dev	: ic_enrique_dev
	 *                   star	: ic_starclub
	 *                   Tyrese	: ic_tyrese
	 *                   Nicole : ic_nicole
	 *                   photogenics: ic_photogenics
	 *                   scott	: ic_scott
	 *                   marvel	: ic_marvel
	 *               -> package name
	 *               com.starclub.enrique
	 *               com.starclub.tyrese
	 *               com.starclub.marvel
	 *               
	 *               -> android:scheme ;;;;  // deep link
	 *               <data android:scheme="com.myapp.starclub.enrique" />
	 *               <data android:scheme="com.myapp.starclub.tyrese" />
	 *               <data android:scheme="com.myapp.starclub.nicole" />
	 *               <data android:scheme="com.myapp.starclub.stardemo" />
	 *               <data android:scheme="com.myapp.starclub.photogenics" />
	 *               <data android:scheme="com.myapp.starclub.suavesays" />
	 *               <data android:scheme="com.myapp.starclub.football" />
	 *               <data android:scheme="com.myapp.starclub.geek" />
	 *               <data android:scheme="com.myapp.starclub.marvel" />
	 *               <data android:scheme="com.myapp.starclub.scottdisick" />
	 *               
	 */
	
	public static String MENU_MAIN_FEED	= "All Access";
	public static String MENU_DRAFT		= "Draft View";
	public static String MENU_COMMUNITY	= "Community";
	public static String MENU_TOUR		= "Tour Dates";
	public static String MENU_QUIZ		= "Polls & Contests";
	public static String MENU_MUSIC		= "Music Player";

	public static final int APP_ENRIQUE_DEV 	= 0;
	public static final int APP_ENRIQUE 		= 7;
	public static final int APP_STARCLUB 		= 1;
	public static final int APP_TYRESE 			= 2;
	public static final int APP_NICOLE			= 3;
	public static final int APP_PHOTOGENICS		= 4;
	public static final int APP_SCOTT			= 5;
	public static final int APP_MARVEL			= 6;
	
	
	
	public static final int GOOGLE_ADMOB_NOR		 = 0;
	public static final int GOOGLE_ADMOB_DFP		 = 1;
	
	public static int ADMOB_MODE	= GOOGLE_ADMOB_DFP;
	
/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        Enrique Dev 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_ENRIQUE_DEV;
	
	public static final String SERVER 	= "http://dev.cms.enrique.starsite.com";
	public static int CID = 7;
	public static String DEEPLINK_URL = "starclub.enrique";
    public static String SHARE_APP_LOGO		= "http://dev.cms.enrique.starsite.com/channel-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_enrique;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_enrique;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	// in app purchase
	public final static String InAppPurchase_ID = "com.starclub.enriqueiglesias.purchased1";
	public final static String InAppPurchase_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgQmg0NUkVbtvpOMfk2yybVrGGDhdM1wLqY2hdOOXGIySR0Ke6SplN/fLPjVm+9Y+a8KbOOpd9ntWogW8rRYVDoEqjLCUkqq7p9UvhcFQzQcfMSM8qQg1vkB97PnedrCoSq8/sD/FiLSmhpXhucS1TFEOq9yLrhd0pDBu61aFvcdpf1hk8KR2IbZI0Dkap/eImJQ3vbGI11jQ6XI3Jf1vOHGpU3DHRoNfkNK0kuOqrXN4MUnviKCoh1VHVybDizAPaez4uL66AvAOHbwvOKb2qOianoQGhWL//eVhO78lBi1YIOFIAn2roiwNfk6s+z3Bbn+6x9YgD1hjOwgKrx+lgwIDAQAB";
	
	// push notification
	public final static String GCM_Project_Number = "699837137341";

	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";

	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "EI App";

	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/

	/////////////////////////////////////////////////////////////////////////
	/////////////        Enrique 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_ENRIQUE;
	
	public static final String SERVER 	= "http://cms.enrique.starsite.com";
	public static int CID = 7;
	public static String DEEPLINK_URL = "com.myapp.starclub.enrique";
    public static String SHARE_APP_LOGO		= "http://cms.enrique.starsite.com/assets/enrique-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_enrique;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_enrique;
	
	public final static String FACEBOOK_ID = "1440671026187879";
	
	// in app purchase
	public final static String InAppPurchase_ID = "com.starclub.enriqueiglesias.purchased1";
	public final static String InAppPurchase_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgQmg0NUkVbtvpOMfk2yybVrGGDhdM1wLqY2hdOOXGIySR0Ke6SplN/fLPjVm+9Y+a8KbOOpd9ntWogW8rRYVDoEqjLCUkqq7p9UvhcFQzQcfMSM8qQg1vkB97PnedrCoSq8/sD/FiLSmhpXhucS1TFEOq9yLrhd0pDBu61aFvcdpf1hk8KR2IbZI0Dkap/eImJQ3vbGI11jQ6XI3Jf1vOHGpU3DHRoNfkNK0kuOqrXN4MUnviKCoh1VHVybDizAPaez4uL66AvAOHbwvOKb2qOianoQGhWL//eVhO78lBi1YIOFIAn2roiwNfk6s+z3Bbn+6x9YgD1hjOwgKrx+lgwIDAQAB";
	
	// push notification
	public final static String GCM_Project_Number = "699837137341";
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-0q3dsg2cb72n34y9";
	public static String QUANTCAST_ID			= "EI App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "68ed531242ca912a";
	

/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        StarClub 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_STARCLUB;
	
	public static final String SERVER 	= "http://cms.stardemo.starsite.com";
	public static int CID = 17;
	public static String DEEPLINK_URL = "starclub.enrique";
    public static String SHARE_APP_LOGO		= "http://cms.tyrese.starsite.com/channel-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_starclub;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_starclub;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "StarDemo App";

	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
	
*/

/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        TYRESE 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_TYRESE;
	
	public static final String SERVER 	= "http://cms.tyrese.starsite.com";
	public static int CID = 1;
	public static String DEEPLINK_URL = "com.myapp.starclub.tyrese";
    public static String SHARE_APP_LOGO		= "http://cms.tyrese.starsite.com/assets/tyrese-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_tyrese;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_tyrese;
	
	public final static String FACEBOOK_ID = "1440671026187879";
	
	// in app purchase
	public final static String InAppPurchase_ID = "com.starclub.tyrese.purchased1";
	public final static String InAppPurchase_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmtKF0r+pk6D4VLK3igI3lHV48xZRQW+uXm/h71BU4v68+WkJO12uB+40khwe8n93y05vp5dLq2Ly/UhVs5nzi2i6Yiolpx2cEdla9xNAm5pvx5nPSRNFU6wInnOaYIQKuPn1Sq1QG//GWI2ytHs87XiGHHTRhPXoAboEV1DTwoLevljQE91hlUX4AMBLxr62/IMxzbnxiX6YFayLEqkw0fl2njaIz93t7UrhBPJeJ7Dg+VC8qk47rckqkFdnEhSiz3jKoxIMwugFmoDt4iYg9w0/vc9lkO7PNe1nzgkbENdCwAyJD0/Ujr2VtPpHQ0lNwZqhhf7KbDClBuBVAJOuewIDAQAB";
	
	// push notification
	public final static String GCM_Project_Number = "95372322933";
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-8";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-0q3dsg2cb72n34y9";
	public static String QUANTCAST_ID			= "Tyrese App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/

/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        NICOLE 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_NICOLE;
	
	public static final String SERVER 	= "http://cms.nicole.starsite.com";
	public static int CID = 3;
	public static String DEEPLINK_URL = "starclub.enrique";
    public static String SHARE_APP_LOGO		= "http://cms.nicole.starsite.com/channel-logo.jpg";

	public static String  LOGINVIDEO_NAME = "intro_nicole";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_nicole;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_nicole;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "Nicole App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/
/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        PHOTOGENIC 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_PHOTOGENICS;
	
	public static final String SERVER 	= "http://sccmsstaging.starsite.com";
	public static int CID = 17;
	public static String DEEPLINK_URL = "com.myapp.starclub.photogenics";
    public static String SHARE_APP_LOGO		= "http://cms.photogenics.starsite.com/channel-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_photogenics;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_photogenics;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "Photogenics App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/	
/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        SCOTT 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_SCOTT;
	
	public static final String SERVER 	= "http://cms.lorddisick.starsite.com";
	public static int CID = 1;
	public static String DEEPLINK_URL = "com.myapp.starclub.scottdisick";
    public static String SHARE_APP_LOGO		= "http://cms.lorddisick.starsite.com/channel-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_scott;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_scott;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "Scott App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/
	
/*	
	/////////////////////////////////////////////////////////////////////////
	/////////////        MARVEL 
	//////////////////////////////////////////////////////////////////////
	
	public static final int APP_MODE = APP_MARVEL;
	
	public static final String SERVER 	= "http://cms.marvel.starsite.com";
	public static int CID = 18;
	public static String DEEPLINK_URL = "com.myapp.starclub.marvel";
    public static String SHARE_APP_LOGO		= "http://cms.marvel.starsite.com/channel-logo.jpg";
	
	public static String  LOGINVIDEO_NAME = "intro_marvel";
	
	public static int RES_LOGIN_LOGO 		= R.drawable.login_logo_marvel;
	public static int RES_ALLACCESS_LOGO 	= R.drawable.allaccess_logo_marvel;
	
	public final static String FACEBOOK_ID = "1437276809848602";
	
	
	public static String ADMOB_COMMUNITY_ID_1 		= "ca-app-pub-7963321383486508/7860883674";
	public static String ADMOB_COMMUNITY_ID_2 		= "ca-app-pub-7963321383486508/1001702873";
	public static String ADMOB_COMMUNITY_ID_3 		= "ca-app-pub-7963321383486508/2478436077";
	public static String ADMOB_HOME_ID_1 			= "ca-app-pub-7963321383486508/6523751270";
	public static String ADMOB_HOME_ID_2 			= "ca-app-pub-7963321383486508/7048236476";
	public static String ADMOB_HOME_ID_3 			= "ca-app-pub-7963321383486508/8524969671";
	public static String ADMOB_PHOTO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/4972966074";
	public static String ADMOB_PHOTO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/3955169275";
	public static String ADMOB_PHOTO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/5431902476";
	public static String ADMOB_PHOTO_GRID_ID_1 		= "ca-app-pub-7963321383486508/3496232870";
	public static String ADMOB_PHOTO_GRID_ID_2 		= "ca-app-pub-7963321383486508/6908635670";
	public static String ADMOB_PHOTO_GRID_ID_3 		= "ca-app-pub-7963321383486508/8385368878";
	public static String ADMOB_VIDEO_GALLARY_ID_1 	= "ca-app-pub-7963321383486508/6449699270";
	public static String ADMOB_VIDEO_GALLARY_ID_2 	= "ca-app-pub-7963321383486508/2338835278";
	public static String ADMOB_VIDEO_GALLARY_ID_3 	= "ca-app-pub-7963321383486508/3815568470";

	public static String GA_PROPERTY_ID 			= "UA-46624794-3";
	
	public static String QUANTCAST_KEY 			= "0no4ke096ler55tf-yvv900r2h8855hk4";
	public static String QUANTCAST_ID			= "Marvel App";
	
	public static String AVIARY_API_KEY         = "2af581758300d42b";
	public static String AVIARY_SECRET_KEY      = "735294c0a232973e";
*/	
	
	/////////////////////////////////////////////////////////////////////////
	/////////////        The END 
	//////////////////////////////////////////////////////////////////////
	
	
	public static final String SERVER_URL = SERVER + "/index.php/api?";

	
}
