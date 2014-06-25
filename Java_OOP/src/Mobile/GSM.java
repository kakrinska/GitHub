package Mobile;

public class GSM {
	
	//fields
   private String display;
   private String buttons;
   private String model;
   
   //method
   public String print(){
	   
	   String buttons  = getButtons();
	   String display  = getDisplay();
	   String model  = getModel();
	   String str  = buttons + "\n" + display + "\n" + model;
	   
	   return str;
   }
   
	   /* constructor
		public GSM(String display, String buttons, String model) {
		super();
		this.display = display;
		this.buttons = buttons;
		this.model = model;
	}*/
   
	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
	/**
	 * @return the buttons
	 */
	public String getButtons() {
		return buttons;
	}
	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(String buttons) {
		this.buttons = buttons;
	}
	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

}
