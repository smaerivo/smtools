// --------------------------------------
// Filename      : JNumberInputField.java
// Author        : Sven Maerivoet
// Last modified : 05/09/2014
// Target        : Java VM (1.8)
// --------------------------------------

/**
 * Copyright 2003-2014 Sven Maerivoet
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sm.smtools.swing.util;

import java.awt.event.*;
import javax.swing.*;

/**
 * The <CODE>JNumberInputField</CODE> class provides a input textfield for <CODE>int</CODE> and <CODE>double</CODE> datatypes.
 * <P>
 * Validation of the input is done via a {@link ANumberFilter} class and the {@link JNumberInputField#setNumberFilter(ANumberFilter)} method.
 *
 * @author  Sven Maerivoet
 * @version 05/09/2014
 */
public class JNumberInputField extends JTextField implements ActionListener
{
	// the different supported numbers-types
	private static final int kInteger = 0;
	private static final int kDouble = 1;

	// internal datastructures
	private int fNumberType;
	private int fIntegerValue;
	private double fDoubleValue;
	private boolean fTransferFocusAfterEntering;
	private ANumberFilter fNumberFilter;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JNumberInputField</CODE> for editing an <CODE>int</CODE> value.
	 *
	 * @param integerValue                the initial <CODE>int</CODE> value in the input textfield
	 * @param transferFocusAfterEntering  a <CODE>boolean</CODE> to indicate whether or not the focus should be transferred
	 *                                    to the next component in the GUI after pressing the &lt;ENTER&gt; key in the textfield
	 * @see                               JNumberInputField#JNumberInputField(double,boolean)
	 */
	public JNumberInputField(int integerValue, boolean transferFocusAfterEntering)
	{
		fNumberType = kInteger;
		fIntegerValue = integerValue;
		fTransferFocusAfterEntering = transferFocusAfterEntering;

		setHorizontalAlignment(JTextField.RIGHT);
		update();
		addActionListener(this);
	}

	/**
	 * Constructs a <CODE>JNumberInputField</CODE> for editing a <CODE>double</CODE> value.
	 *
	 * @param doubleValue                 the initial <CODE>double</CODE> value in the input textfield
	 * @param transferFocusAfterEntering  a <CODE>boolean</CODE> to indicate whether or not the focus should be transferred
	 *                                    to the next component in the GUI after pressing the &lt;ENTER&gt; key in the textfield
	 * @see                               JNumberInputField#JNumberInputField(int,boolean)
	 */
	public JNumberInputField(double doubleValue, boolean transferFocusAfterEntering)
	{
		fNumberType = kDouble;
		fDoubleValue = doubleValue;
		fTransferFocusAfterEntering = transferFocusAfterEntering;

		setHorizontalAlignment(JTextField.RIGHT);
		update();
		addActionListener(this);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the filter to use for validation of the input.
	 *
	 * @param numberFilter a reference to the filter to use for validation of the input
	 */
	public void setNumberFilter(ANumberFilter numberFilter)
	{
		fNumberFilter = numberFilter;
	}

	// the action-listener
	/**
	 * The <CODE>JNumberInputField</CODE>'s <B>action listener</B>.
	 * <P>
	 * Note that when an invalid number is entered in the textfield, this method automatically
	 * reverts to the textfield's previous value.
	 * <P>
	 * If the &lt;ENTER&gt; key is pressed in the textfield, the focus may transfer to the next
	 * GUI component (if this behaviour is specified).
	 *
	 * @param e  the <CODE>ActionEvent</CODE> that is received.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int dummyInteger = 0;
		double dummyDouble = 0.0;

		try {
			if (fNumberType == kInteger) {
				dummyInteger = (int) Math.round((new Double(e.getActionCommand())).doubleValue());
			}
			else if (fNumberType == kDouble) {
				dummyDouble = (new Double(e.getActionCommand())).doubleValue();
			}
		}
		catch (NumberFormatException exc) {
			if (fNumberType == kInteger) {
				dummyInteger = fIntegerValue;
			}
			else if (fNumberType == kDouble) {
				dummyDouble = fDoubleValue;
			}
		}

		if (fNumberType == kInteger) {
			if (validateInteger(dummyInteger)) {
				fIntegerValue = dummyInteger;
			}
		}
		else if (fNumberType == kDouble) {
			if (validateDouble(dummyDouble)) {
				fDoubleValue = dummyDouble;
			}
		}

		update();

		if (fTransferFocusAfterEntering) {
			transferFocus();
		}
	}

	/**
	 * Updates the value of the textfield.
	 */
	public final void update()
	{
		if (fNumberType == kInteger) {
			setText(String.valueOf(fIntegerValue));
		}
		else if (fNumberType == kDouble) {
			setText(String.valueOf(fDoubleValue));
		}
	}

	/**
	 * Changes the numerical <CODE>int</CODE> value of the textfield.
	 * <P>
	 * Note that this method has no effect when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>double</CODE>.
	 *
	 * @param newIntegerValue  the new <CODE>int</CODE> value for the textfield
	 */
	public final void setIntegerValue(int newIntegerValue)
	{
		fIntegerValue = newIntegerValue;
		update();
	}

	/**
	 * Retrieves the numerical <CODE>int</CODE> value of the textfield.
	 * <P>
	 * Note that this method returns 0 when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>double</CODE>.
	 *
	 * @return the <CODE>int</CODE> value of the textfield
	 */
	public final int getIntegerValue()
	{
		// obtain the current value from the textfield
		fireActionPerformed();

		return fIntegerValue;
	}

	/**
	 * Changes the numerical <CODE>double</CODE> value of the textfield.
	 * <P>
	 * Note that this method has no effect when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>int</CODE>.
	 *
	 * @param newDoubleValue  the new <CODE>double</CODE> value for the textfield
	 */
	public final void setDoubleValue(double newDoubleValue)
	{
		fDoubleValue = newDoubleValue;
		update();
	}

	/**
	 * Retrieves the numerical <CODE>double</CODE> value of the textfield.
	 * <P>
	 * Note that this method returns 0.0 when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>int</CODE>.
	 *
	 * @return the <CODE>double</CODE> value of the textfield
	 */
	public final double getDoubleValue()
	{
		// obtain the current value from the textfield
		fireActionPerformed();

		return fDoubleValue;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 * @param i  -
	 * @return   -
	 */
	private boolean validateInteger(int i)
	{
		if (fNumberFilter == null) {
			return true;
		}
		else {
			return fNumberFilter.validateInteger(i);
		}
	}

	/**
	 * @param d  -
	 * @return   -
	 */
	private boolean validateDouble(double d)
	{
		if (fNumberFilter == null) {
			return true;
		}
		else {
			return fNumberFilter.validateDouble(d);
		}
	}
}
