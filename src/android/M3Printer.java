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
		print = Printer.getInstance();
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException { 
		if (action.equals("printText")) { 
            String txt = args.getString(0);  
			print.printText(txt);
			callbackContext.success("1");
            return true; 
		} 
		else if (action.equals("printBase64")) { 
            String txt = args.getString(0); 
			print.printText(txt);
			callbackContext.success("1");
            return true; 
		} 
		else { 
            return false; 
		} 
	}
}
