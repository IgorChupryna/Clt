package chup.com.ua;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	
	private static final Messages singleton = new Messages();
	
	private List<String> list = new ArrayList<String>();
	
	public static Messages getInstance() {
		return singleton;
	}
	
	private Messages() {} // private constructor
	
	public void add(String s) {
		list.add(s);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (String s : list)
			sb.append("<br>").append(s);
		
		return sb.toString();
	}
}
