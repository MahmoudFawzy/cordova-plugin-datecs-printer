package com.m3printer;


import com.nbbse.mobiprint3.*;
import org.apache.cordova.*;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.util.Xml.Encoding;
import android.util.Base64;
import java.util.ArrayList;
import java.util.List;


public class M3Printer extends CordovaPlugin {
	public static com.nbbse.mobiprint3.Printer print;
	  
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);	 
		print =Printer.getInstance();
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
			 
			print.printBitmap(bitmap);

			print.printEndLine();
			callbackContext.success("1");
            return true; 
		} 
		else { 
            return false; 
		} 
	}

	private static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
    
		if (width > w) {
		    float scaleWidth = ((float) w) / width;
		    float scaleHeight = ((float) h) / height + 24;
		    Matrix matrix = new Matrix();
		    matrix.postScale(scaleWidth, scaleWidth);
		    Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		    return resizedBitmap;
		} else {
		    Bitmap resizedBitmap = Bitmap.createBitmap(w, height + 24, Config.RGB_565);
		    Canvas canvas = new Canvas(resizedBitmap);
		    Paint paint = new Paint();
		    canvas.drawColor(Color.WHITE);
		    canvas.drawBitmap(bitmap, (w - width) / 2, 0, paint);
		    return resizedBitmap;
		}
	  }
}
