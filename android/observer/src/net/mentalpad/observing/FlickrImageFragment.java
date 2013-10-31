package net.mentalpad.observing;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class FlickrImageFragment extends Fragment implements Observer {
	
	private static final String TAG = FlickrImageFragment.class.getSimpleName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.flickr_image, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ImageDataSource.getInstance(getActivity()).addObserver(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageDataSource.getInstance(getActivity()).deleteObserver(this);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		
		final Animation anim_out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out); 
	    final Animation anim_in  = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in); 
		final ImageView imageView = (ImageView)getView().findViewById(R.id.image);


		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				Bitmap b = null;
				try {
					URL url = new URL(params[0]);
					InputStream is = url.openConnection().getInputStream();
					b = BitmapFactory.decodeStream(is);
				} catch (MalformedURLException e) {
					Log.e(TAG, "oops, you made a bad url. ", e);
				} catch (IOException e) {
					Log.e(TAG, "oops, io no worky. ", e);
				}
				return b;
			};
			
			
			protected void onPostExecute(final Bitmap result) {

				anim_out.setAnimationListener(new AnimationListener() {
			        @Override public void onAnimationStart(Animation animation) {} // don't care
			        @Override public void onAnimationRepeat(Animation animation) {} // don't care
			        @Override public void onAnimationEnd(Animation animation) // care
			        {
			        	imageView.setImageBitmap(result); 
			            anim_in.setAnimationListener(new AnimationListener() {
			                @Override public void onAnimationStart(Animation animation) {}
			                @Override public void onAnimationRepeat(Animation animation) {}
			                @Override public void onAnimationEnd(Animation animation) {}
			            });
			            imageView.startAnimation(anim_in);
			        }
			    });
				
			    imageView.startAnimation(anim_out);
			};
			
		}.execute(new String[]{((FlickrImage)data).imageUrl()});
		
	}

}
