package net.mentalpad.observing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class FlickrImageCollection extends ArrayList<FlickrImage> {
	
	private static final String TAG = FlickrImageCollection.class.getSimpleName();

	private static final long serialVersionUID = 378341827411865971L;
	private Random rand;

	public FlickrImageCollection(Context context) {
		rand = new Random();
		try {
			parseFromJson(context);
		} catch (IOException e) {
			Log.e(TAG, "parsing the json didn't work. ", e);
		}
	}
	
	public FlickrImage getRandomImage(){
		return get(rand.nextInt(this.size()));
	}
	
	private void parseFromJson(Context context) throws IOException{

		InputStream is = context.getResources().openRawResource(R.raw.interesting_images);
		Writer writer = new StringWriter();
		char[] buffer = new char[1024];
		try {
		    Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		    int n;
		    while ((n = reader.read(buffer)) != -1) {
		        writer.write(buffer, 0, n);
		    }
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Encoding needs some attention", e);
		} catch (IOException e) {
			Log.e(TAG, "IO no worky.", e);
		} finally {
		    is.close();
		}
		
		Gson gson= new Gson();
		FlickrImage[] images = gson.fromJson( writer.toString(),  FlickrImage[].class);
		addAll(Arrays.asList(images));
		
	}
	

}
