package net.mentalpad.observing;

public class FlickrImage {
	
	public long id;
	public int farm;
	public String server;
	public String secret;
	public String owner;
	public String title;

	public String imageUrl(){
		return String.format("http://farm%s.static.flickr.com/%s/%s_%s_m.jpg", farm, server, id, secret);	
	}
	
}
