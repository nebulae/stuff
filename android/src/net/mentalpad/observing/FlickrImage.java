package net.mentalpad.observing;

public class FlickrImage {
	
	public long id;
	public int farm;
	public String server;
	public String secret;
	public String owner;
	public String title;

	public String imageUrl(){
		return "http://farm" + farm +".static.flickr.com/" + server + "/" + id + "_" + secret + "_" + "m.jpg";
	}
	
}
