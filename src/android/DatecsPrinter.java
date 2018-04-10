package com.giorgiofellipe.datecsprinter;

import com.nbbse.printapi.*;


import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class DatecsPrinter extends CordovaPlugin {
	public static Printer print;
	 

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		print = Printer.getInstance();
	 
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		if (action.equals("printText")) {

            String name = args.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);
			print.printText("Printer testing!!!");
            return true;

        } else {
            
            return false;

		}
		
	  
	}
}
