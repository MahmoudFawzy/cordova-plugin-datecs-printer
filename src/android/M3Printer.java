package com.m3printer;


import com.nbbse.mobiprint3.*;


import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class M3Printer extends CordovaPlugin {
	public static Printer print;
	  
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);	 
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException { 
		if (action.equals("printText")) { 
            String name = args.getString(0);
            String message = "Hello, " + name;
			print = Printer.getInstance();
			print.printText("Printer testing!!!");
			callbackContext.success(message);
            return true; 
        } else { 
            return false; 
		} 
	}
}
