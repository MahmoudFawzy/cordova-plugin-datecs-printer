package com.nbbse.printer;

import com.nbbse.printapi.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class DatecsPrinter extends CordovaPlugin {
	public static Printer print;
	 

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		print = Printer.getInstance();
		print.printText("Printer testing!!!");
	 
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		  
		if (action == "printText") { 
		// String text = args.getString(0);
				// String charset = args.getString(1);
				// printer.printTaggedText(text, charset);
		}
		return true;
	}
}
