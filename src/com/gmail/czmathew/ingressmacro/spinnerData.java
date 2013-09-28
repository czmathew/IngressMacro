package com.gmail.czmathew.ingressmacro;

public class spinnerData {
	String spinnerText;
    String value;
	
	 public spinnerData( String spinnerText, String value ) {
         this.spinnerText = spinnerText;
         this.value = value;
     }
	 
	 public spinnerData() {
         
     }

     public String getSpinnerText() {
         return spinnerText;
     }

     public String getValue() {
         return value;
     }

     public String toString() {
         return spinnerText;
     }
     
     public void setSpinnerText (String text) {
    	 spinnerText = text;
     }
     
     public void setValue (String val) {
    	 value = val;
     }
     
     
     
}
