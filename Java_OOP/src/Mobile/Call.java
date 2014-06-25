package Mobile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Call {

	public static void main(String[] args) {
		GSM gsmNokia = new GSM();
		
		System.out.println("Enter gsm specs:");
		
		gsmNokia.setButtons("test");
		gsmNokia.setDisplay("display");
		gsmNokia.setModel("Nokia 3310 Classic");
		
		String str = gsmNokia.print();
		System.out.println( GSMCall() + " " + gsmNokia.getModel() );
		
	}
	
	public static String GSMCall(){
		
		return "Person call from" ;
	}
	
}
