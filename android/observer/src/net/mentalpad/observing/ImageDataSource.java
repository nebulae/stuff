package net.mentalpad.observing;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;


public class ImageDataSource extends Observable {

	// we are going to manage our own observers.
	private List<Observer> observers;
	
	// the singleton instance of this
	private static ImageDataSource instance;
	
	// the collection of flickr images to be passed to the observers
	private FlickrImageCollection fic;
	
	private static final long REFRESH_MS = 10000; // refresh every 10 seconds
	
	// the handler to call the runnable every x seconds
	private Handler newImageHandler = new Handler();
	
	// the runnable to send the update 
	private Runnable newImageRunnable = new Runnable() {
		
		@Override
		public void run() {
		     notifyObservers(); // Tell everyone who cares
		     clearChanged(); // k done changing. 
		     // kick off the next refresh... 
		     newImageHandler.postDelayed(newImageRunnable, REFRESH_MS);
		}
	};
	
	// private because we want the access to go through the singleton instance
	private ImageDataSource(Context context){
		
		fic = new FlickrImageCollection(context);
		observers = new ArrayList<Observer>();
		
	}
	
	// return the one and only instance... 
	public static ImageDataSource getInstance(Context context) {
		if(instance == null){
			instance = new ImageDataSource(context);
		}
		return instance;
	}
	
	public void startUpdating(){
		// just in case... 
		newImageHandler.removeCallbacks(newImageRunnable);
		newImageHandler.post(newImageRunnable);
	}
	
	@Override
	protected void setChanged() {
		super.setChanged();
	}
	
	// we are managing our own observers, so override all the observer-y methods from the extended Observable object.
	@Override
	public int countObservers() {
		return this.observers.size();
	}
	
	@Override
	public synchronized void deleteObservers() {
		this.observers.clear();
	}
	
	@Override
	public synchronized void deleteObserver(Observer observer) {
		this.observers.remove(observer);
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	// we want to send each observer a different image.  loop through send the update. 
	@Override
	public void notifyObservers() {
		for(Observer o : observers){
			o.update(this, fic.getRandomImage());
		}
	}
}
