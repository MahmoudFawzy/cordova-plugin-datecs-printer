package com.m3printer;


import com.nbbse.mobiprint3.*;
import org.apache.cordova.*;



import java.io.*; 
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
import android.graphics.Bitmap.CompressFormat;
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
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			bitmap.recycle();


			//byte[] bt = decodeBitmap(bitmap); 

			print.printBitmap(is); 
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

	  ////////////////////
	  public static byte[] decodeBitmap(Bitmap bmp) {
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		List<String> list = new ArrayList<String>(); //binaryString list
		StringBuffer sb;
		int bitLen = bmpWidth / 8;
		int zeroCount = bmpWidth % 8;
		String zeroStr = "";
		if (zeroCount > 0) {
		    bitLen = bmpWidth / 8 + 1;
		    for (int i = 0; i < (8 - zeroCount); i++) {
			  zeroStr = zeroStr + "0";
		    }
		}
    
		for (int i = 0; i < bmpHeight; i++) {
		    sb = new StringBuffer();
		    for (int j = 0; j < bmpWidth; j++) {
			  int color = bmp.getPixel(j, i);
    
			  int r = (color >> 16) & 0xff;
			  int g = (color >> 8) & 0xff;
			  int b = color & 0xff;
			  // if color close to white，bit='0', else bit='1'
			  if (r > 160 && g > 160 && b > 160) {
				sb.append("0");
			  } else {
				sb.append("1");
			  }
		    }
		    if (zeroCount > 0) {
			  sb.append(zeroStr);
		    }
		    list.add(sb.toString());
		}
    
		List<String> bmpHexList = binaryListToHexStringList(list);
		String commandHexString = "1D763000";
		
		//construct xL and xH
		//there are 8 pixels per byte. In case of modulo: add 1 to compensate.
		bmpWidth = bmpWidth % 8 == 0 ? bmpWidth / 8 : (bmpWidth / 8 + 1);
		int xL = bmpWidth % 256;
		int xH = (bmpWidth - xL) / 256;
    
		String xLHex = Integer.toHexString(xL);
		String xHHex = Integer.toHexString(xH);
		if(xLHex.length() == 1){
		    xLHex = "0" + xLHex;
		}
		if(xHHex.length() == 1){
		    xHHex = "0" + xHHex;
		}
		String widthHexString = xLHex + xHHex;
    
    
		//construct yL and yH
		int yL = bmpHeight % 256;
		int yH = (bmpHeight - yL) / 256;
    
		String yLHex = Integer.toHexString(yL);
		String yHHex = Integer.toHexString(yH);
		if(yLHex.length() == 1){
		    yLHex = "0" + yLHex;
		}
		if(yHHex.length() == 1){
		    yHHex = "0" + yHHex;
		}
		String heightHexString = yLHex + yHHex;
    
		List<String> commandList = new ArrayList<String>();
		commandList.add(commandHexString + widthHexString + heightHexString);
		commandList.addAll(bmpHexList);
		
		return hexList2Byte(commandList);
	  }
    
	  public static List<String> binaryListToHexStringList(List<String> list) {
		List<String> hexList = new ArrayList<String>();
		for (String binaryStr : list) {
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < binaryStr.length(); i += 8) {
			  String str = binaryStr.substring(i, i + 8);
    
			  String hexString = myBinaryStrToHexString(str);
			  sb.append(hexString);
		    }
		    hexList.add(sb.toString());
		}
		return hexList;
    
	  }
    
	  public static String myBinaryStrToHexString(String binaryStr) {
		String hex = "";
		String f4 = binaryStr.substring(0, 4);
		String b4 = binaryStr.substring(4, 8);
		for (int i = 0; i < binaryArray.length; i++) {
		    if (f4.equals(binaryArray[i])) {
			  hex += hexStr.substring(i, i + 1);
		    }
		}
		for (int i = 0; i < binaryArray.length; i++) {
		    if (b4.equals(binaryArray[i])) {
			  hex += hexStr.substring(i, i + 1);
		    }
		}
    
		return hex;
	  }
    
	  private static String hexStr = "0123456789ABCDEF";
    
	  private static String[] binaryArray = {"0000", "0001", "0010", "0011",
		"0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
		"1100", "1101", "1110", "1111"};
    
	  public static byte[] hexList2Byte(List<String> list) {
		List<byte[]> commandList = new ArrayList<byte[]>();
    
		for (String hexStr : list) {
		    commandList.add(hexStringToBytes(hexStr));
		}
		byte[] bytes = sysCopy(commandList);
		return bytes;
	  }
    
	  //New implementation, change old
	  public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
		    return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
		    int pos = i * 2;
		    d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	  }
    
	  private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	  }
    
	  public static byte[] sysCopy(List<byte[]> srcArrays) {
		int len = 0;
		for (byte[] srcArray : srcArrays) {
		    len += srcArray.length;
		}
		byte[] destArray = new byte[len];
		int destLen = 0;
		for (byte[] srcArray : srcArrays) {
		    System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
		    destLen += srcArray.length;
		}
		return destArray;
	  }
}
