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
 * MobiMoon 1.2 <info@mypapit.net>
 * Copyright 2008 Mohammad Hafiz bin Ismail. All rights reserved.
 *
 * SendResult.java
 * A Generic Send SMS form
 *
 * http://mobibmi.googlecode.com
 * http://mobilepit.com
 * http://blog.mypapit.net
 *
 */

import javax.microedition.lcdui.*;


public class SendResult extends Form
{

public Command cmdSMS;
public Command cmdBack;

private TextField tfName, tfPhoneNo;

public SendResult() {
	super("Send BMI Result");
	cmdSMS = new Command("Send",Command.SCREEN,1);
	cmdBack = new Command("Back",Command.BACK,99);

	this.addCommand(cmdSMS);
	this.addCommand(cmdBack);

	tfName = new TextField("Name","",48,TextField.ANY|TextField.INITIAL_CAPS_WORD);
	tfPhoneNo = new TextField("Send to (Phone No)","",48,TextField.PHONENUMBER);

	this.append(tfName);
	this.append(tfPhoneNo);


}

public String getName()
{

	return tfName.getString();
}

public String getPhoneNo()
{

	return tfPhoneNo.getString();
}


}
