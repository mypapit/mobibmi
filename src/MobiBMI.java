/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as published by
 *  the Free Software Foundation
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * MobiMoon 1.0 <info@mypapit.net>
 * Copyright 2007 Mohammad Hafiz bin Ismail. All rights reserved.
 * 
 * MobiBMI.java 
 * Calculates and display Body Mass Index.
 */

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import javax.wireless.messaging.*;
import javax.microedition.io.*;
import javax.microedition.rms.*;
import java.io.*;

public class MobiBMI extends MIDlet implements CommandListener {

private Command cmdExit,cmdAbout,cmdCalculate,cmdResult;
private Display display;
public Form form;
private ChoiceGroup cgGender,cgEthnic,cgAge;
public  TextField tfHeight,tfWeight;
public StringItem status;
private String[] strGender= {"Male","Female"};
private String[] strEthnic = {"Asian","European"};
private String[] strAge = {"Below 16","16 or Above"};
private AboutForm frmAbout;
private RecordStore rs = null;
public SendResult sr;

public MobiBMI () {
	display = Display.getDisplay(this);
	
	//init Command items
	cmdExit = new Command("Exit",Command.EXIT,99);
	cmdCalculate = new Command("Calculate",Command.SCREEN,1);
	cmdAbout = new Command("About",Command.HELP,10);
	//cmdBack = new Command("Back",Command.BACK,99);
	cmdResult = new Command("Send Result",Command.SCREEN,9);	
	
	tfHeight = new TextField("Height (cm)","",10,TextField.NUMERIC);
	tfWeight = new TextField("Mass (kg)","",10,TextField.DECIMAL);

	SendResult sr = new SendResult();
	sr.setCommandListener(this);

	form = new Form("MobiBMI 1.0");
	form.setTicker(new Ticker("MobiBMI 1.0 - Calculate Body Mass Index Copyright 2007 Mohammad Hafiz bin Ismail (info@mypapit.net)"));
	
	cgGender = new ChoiceGroup("Gender",ChoiceGroup.POPUP,strGender,null);
	cgEthnic = new ChoiceGroup("Ethnic",ChoiceGroup.POPUP,strEthnic,null);
	cgAge = new ChoiceGroup("Age",ChoiceGroup.POPUP,strAge,null);
	cgAge.setSelectedIndex(1,true);
	
	status = new StringItem("\nStatus","");
	
		
	form.append(cgGender);
	form.append(cgEthnic);
	form.append(cgAge);
	
	form.append(tfHeight);
	form.append(tfWeight);
	form.append(status);
	
	form.addCommand(cmdAbout);
	form.addCommand(cmdExit);
	form.addCommand(cmdCalculate);

	
	
	form.setCommandListener(this);
	


}

public void startApp() {
	this.getSettings();
	display.setCurrent(form);
}

public void pauseApp() {

}

public void destroyApp(boolean f){
	this.saveSettings();
	
	try {
		rs.closeRecordStore();
		rs = null;
	} catch (Exception e)
	{
	
	}
	notifyDestroyed();

}

public void commandAction(Command c, Displayable d)
{
	if (c == cmdExit) {
		destroyApp(false);
	} else if (c == cmdAbout) {
		AboutForm frmAbout =new AboutForm("About","MobiBMI 1.0","/bmi.png");
		frmAbout.setCopyright("Mohammad Hafiz","2007");
		frmAbout.setHyperlink("http://java.mobilepit.com",this);
		frmAbout.append("Calculates Body Mass Index (BMI) and display the result on mobile devices\n\nThis program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 2.0");
		//frmAbout.addCommand(cmdBack);
		frmAbout.setCommandListener(this);
		display.setCurrent(frmAbout);
	} else if (c == frmAbout.DISMISS_COMMAND) {
		display.setCurrent(form);
	} else if (c == cmdCalculate) {
	
		this.GetBMI();
	} else if (c == cmdResult) {
		sr = new SendResult();
		sr.setCommandListener(this);		
		display.setCurrent(sr);
		//System.out.println("wtf?");
	} else if ( c == sr.cmdBack) {
		display.setCurrent(form);
	} else if ( c == sr.cmdSMS) {
		this.sendSMS();
	}
}


private void GetBMI() {
double h = Double.parseDouble(tfHeight.getString());
double w = Double.parseDouble(tfWeight.getString());
 
if (h < 60.0) {
	this.showAlert("Height too low");
} else if ( w < 5.0) {  
	this.showAlert("Weight too low");
} else if ( h > 225.0) {
	this.showAlert("Height too high");
} else if (w > 350.0) {
	this.showAlert("Weight too high");
} else {
	this.calculate(h,w);
}




}


private void calculate(double h, double w)
{
	double disp=0.0;
	String status="";
	double c = h/100.0;
		
	double bmi = w/(c*c);
	
	if (cgEthnic.getSelectedIndex() ==0) {
		disp+= 3.2;
	}
	
	if (cgAge.getSelectedIndex() == 0) {
		disp+=2.7;
	}
	
	
	if (bmi < 18.5-disp) {
		status = "Underweight";
	}else if (bmi < 24.9-disp) {
		status = "Normal";
	} else if (bmi < 29.9-disp) {
		status = "Overweight";
	} else if (bmi > 30-disp) {
		status = "Obese";
	}
	
	bmi = (double)(int)((bmi+0.00005)*10000.0)/10000.0;
	
	this.status.setText("\n"+status+"\nBMI: "+bmi+"\n\n");
	form.addCommand(cmdResult);	

	
	
	
}

public void showAlert(String text)
{
	Alert a = new Alert("Error",text,null, AlertType.ERROR);
	a.setTimeout(Alert.FOREVER);
	display.setCurrent(a,form);
	
}

private void sendSMS() {
	SendSMS s = new SendSMS(this);
	s.start();

}

public void saveSettings()
{
	
	try { 
		//rs = Recordstore.openRecordStore("settings",true);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		
		try {
		
			dout.writeInt(cgGender.getSelectedIndex());
			dout.writeInt(cgAge.getSelectedIndex());
			dout.writeInt(cgEthnic.getSelectedIndex());
			//dout.close();
			
			byte[] data = bout.toByteArray();
			rs.setRecord(1,data,0,data.length);
			dout.close();
		} catch (Exception ex) {
			showAlert(ex.toString());
		}
		
	} catch (Exception ex2) {
			showAlert(ex2.toString());
	}
		

}

public void getSettings()
{
	ByteArrayInputStream bin;
	DataInputStream din;
	try {
			rs = RecordStore.openRecordStore("settings",true);
			
			if (rs.getNumRecords() == 0) {
				this.saveSettings();
			}
			
			byte [] data = rs.getRecord(1);
			bin =  new ByteArrayInputStream(data);
			din = new DataInputStream(bin);
			
			cgGender.setSelectedIndex(din.readInt(),true);
			cgAge.setSelectedIndex(din.readInt(),true);
			cgEthnic.setSelectedIndex(din.readInt(),true);
			din.close();
			bin.close();
			
			
	
	} catch (RecordStoreException rse) 
	{
		showAlert("Error saving data");
	} catch (Exception ex) {
		showAlert(ex.toString());
	}
 

}

}



class SendSMS implements Runnable {
private MobiBMI midlet;
private Display display;
private Gauge g;
private Form formRunning;

public SendSMS(MobiBMI midlet) 
{
this.midlet = midlet;
display = Display.getDisplay(this.midlet);
formRunning = new Form("Sending Result");
g = new Gauge("Processing...",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
formRunning.append(g);
display.setCurrent(formRunning);

}

public void start() {
       Thread t = new Thread(this);
       t.start();
}

public void run() {

StringBuffer sb = new StringBuffer("");

	try {
		String addr = "sms://" + midlet.sr.getPhoneNo();
		MessageConnection conn = (MessageConnection) Connector.open(addr);
		TextMessage msg =
		(TextMessage)conn.newMessage(MessageConnection.TEXT_MESSAGE);
		sb.append("BMI Information for "+ midlet.sr.getName() + "\n\n" );
		sb.append("Weight : " + midlet.tfWeight.getString() + " kg\n");		
		sb.append("Height : " + midlet.tfHeight.getString() + " cm\n");
		sb.append("[Status]  " + midlet.status.getText());
		msg.setPayloadText(sb.toString());
		sb = null;
		conn.send(msg);
		display.setCurrent(midlet.form);
		} catch (IllegalArgumentException iae) {
			midlet.showAlert("Please fill in the form");
		
		} catch (Exception e) {
			midlet.showAlert("Send Result failed :" + e.toString());
		} 
			
	}


}
