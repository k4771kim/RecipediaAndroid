package seop.gyun.recipedia;

public class RecipediaConstant {

	public static final String PACKAGE_NAME = "seop.gyun.recipedia";
	public static final String RECIPE_DB_BASIC_INFO = "basic.db";
	
	public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
        ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
        ".LOCATION_DATA_EXTRA";
    
    public static final int GALLERY_REQUEST_CODE = 1132;
    public static final int GALLERY_REQUEST_CODE_KITKAT = 1135;
    public static final String LOGIN_RESPONSE_EXTRA = "login_response";
    public static final String OPEN_API_KEY = "Your MAFRA API KEY";
    
    public static final String GET_PRICE_URL = "MAFRA DATA URL WITH YOUR MAFRA KEY"
    
    public static final String SERVER = "YOUR SERVER";
    public static final String SERVER_GRID = SERVER + "/gridlist";
    public static final String SERVER_LOGIN = SERVER + "/memberinfo";
    public static final String SERVER_COLUMN = SERVER + "/columnrecipe";
    public static final String SERVER_REPLY = SERVER + "/replylist";
    public static final String SERVER_REPLY_POST = SERVER + "/insertreply";
    public static final String SERVER_TRADITIONAL = SERVER + "/traditional";
    public static final String SERVER_IMG_UPLOAD = SERVER + "/imgupload2";
}
