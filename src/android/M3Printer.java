package com.m3printer;


import com.nbbse.mobiprint3.*;


import org.apache.cordova.*;
import org.json.*; 
import java.io.*; 
import java.util.*;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.util.Xml.*; 


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



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
			print.printEndLine();
			callbackContext.success("1");
            return true; 
		} 
		else if (action.equals("printBase64")) { 
			String txt = args.getString(0); 
			 
			final byte[] decodedBytes = Base64.decode(txt, Base64.DEFAULT);

			Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

			 
			int mWidth = bitmap.getWidth();
			int mHeight = bitmap.getHeight();

			bitmap = resizeImage(bitmap, 48 * 8, mHeight);
			
			printer.printBitmap(bitmap);

			print.printEndLine();
			callbackContext.success("1");
            return true; 
		} 
		else { 
            return false; 
		} 
	}
}
