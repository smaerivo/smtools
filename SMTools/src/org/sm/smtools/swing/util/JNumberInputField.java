// --------------------------------------
// Filename      : JNumberInputField.java
// Author        : Sven Maerivoet
// Last modified : 04/12/2014
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The <CODE>JNumberInputField</CODE> class provides a input textfield for <CODE>int</CODE> and <CODE>double</CODE> datatypes.
 * <P>
 * Validation of the input is done via a {@link ANumberFilter} class and the {@link JNumberInputField#setNumberFilter(ANumberFilter)} method.
 * <P>
 * Any time focus is transferred, the field is validated; an action listener can be defined to catch these events.
 *
 * @author  Sven Maerivoet
 * @version 04/12/2014
 */
public final class JNumberInputField extends JTextField implements ActionListener
{
	// the different supported numbers-types
	private static final int kInteger = 0;
	private static final int kDouble = 1;

	// internal datastructures
	private int fNumberType;
	private int fIntegerValue;
	private double fDoubleValue;
	private boolean fAutoCorrect;
	private String fErrorMessage;
	private ANumberFilter fNumberFilter;
	private boolean fInputIsValid = true;
	private FieldVerifier fFieldVerifier;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JNumberInputField</CODE> for editing an <CODE>int</CODE> value.
	 *
	 * @param integerValue  the initial <CODE>int</CODE> value in the input textfield
	 * @param fieldWidth    the field width
	 * @param autoCorrect   a <CODE>boolean</CODE> indicating whether or not the value should revert to the previous correct one in case of input error
	 * @param errorMessage  an optional error message that is displayed when an input error occurred
	 * @see                 JNumberInputField#JNumberInputField(double,int,boolean,String)
	 */
	public JNumberInputField(int integerValue, int fieldWidth, boolean autoCorrect, String errorMessage)
	{
		super(String.valueOf(integerValue),fieldWidth);
		fNumberType = kInteger;
		fIntegerValue = integerValue;
		fAutoCorrect = autoCorrect;
		fErrorMessage = errorMessage;
		fInputIsValid = true;

		setHorizontalAlignment(JTextField.RIGHT);
		fFieldVerifier = new FieldVerifier();
		setInputVerifier(fFieldVerifier);
		addActionListener(this);
	}

	/**
	 * Constructs a <CODE>JNumberInputField</CODE> for editing a <CODE>double</CODE> value.
	 *
	 * @param doubleValue  the initial <CODE>double</CODE> value in the input textfield
	 * @param fieldWidth    the field width
	 * @param autoCorrect   a <CODE>boolean</CODE> indicating whether or not the value should revert to the previous correct one in case of input error
	 * @param errorMessage  an optional error message that is displayed when an input error occurred
	 * @see                JNumberInputField#JNumberInputField(int,int,boolean,String)
	 */
	public JNumberInputField(double doubleValue, int fieldWidth, boolean autoCorrect, String errorMessage)
	{
		super(String.valueOf(doubleValue),fieldWidth);
		fNumberType = kDouble;
		fDoubleValue = doubleValue;
		fAutoCorrect = autoCorrect;
		fErrorMessage = errorMessage;
		fInputIsValid = true;

		setHorizontalAlignment(JTextField.RIGHT);
		fFieldVerifier = new FieldVerifier();
		setInputVerifier(fFieldVerifier);
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

	/**
	 * Retrieves the numerical <CODE>int</CODE> value of the textfield.
	 * <P>
	 * Note that this method returns 0 when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>double</CODE>.
	 *
	 * @return the <CODE>int</CODE> value of the textfield
	 */
	public int getIntegerValue()
	{
		return fIntegerValue;
	}

	/**
	 * Retrieves the numerical <CODE>double</CODE> value of the textfield.
	 * <P>
	 * Note that this method returns 0.0 when the <CODE>JNumberInputField</CODE> object
	 * operates on a <CODE>int</CODE>.
	 *
	 * @return the <CODE>double</CODE> value of the textfield
	 */
	public double getDoubleValue()
	{
		return fDoubleValue;
	}

	// the action-listener
	/**
	 * The <CODE>JNumberInputField</CODE>'s <B>action listener</B>.
	 * <P>
	 * This method always tries to transfer the focus to the next GUI component.
	 *
	 * @param e  the <CODE>ActionEvent</CODE> that is received.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		transferFocus();
	}

	/*****************
	 * INNER CLASSES *
	 *****************/

	/**
	 * @author  Sven Maerivoet
	 * @version 04/12/2014
	 */
	private class FieldVerifier extends InputVerifier
	{
		@Override
		public boolean verify(JComponent inputComponent)
		{
			JTextField inputTextField = (JTextField) inputComponent;
			try {
				if (fNumberType == kInteger) {
					fIntegerValue = Integer.parseInt(inputTextField.getText());
					if ((fNumberFilter != null) && (!fNumberFilter.validateInteger(fIntegerValue))) {
						throw (new NumberFormatException());
					}
				}
				else if (fNumberType == kDouble) {
					fDoubleValue = Double.parseDouble(inputTextField.getText());
					if ((fNumberFilter != null) && (!fNumberFilter.validateDouble(fDoubleValue))) {
						throw (new NumberFormatException());
					}
				}
				setBackground( UIManager.getColor("TextField.background"));
				fInputIsValid = true;
				fireActionPerformed();
			}
			catch(NumberFormatException e) {
				fInputIsValid = false;
				setBackground(new Color(255,128,128));
				if ((fErrorMessage != null) && (fErrorMessage.length() > 0)) {
					JOptionPane.showMessageDialog(null,fErrorMessage);
				}
				if (fAutoCorrect) {
					if (fNumberType == kInteger) {
						setText(String.valueOf(fIntegerValue));
					}
					else if (fNumberType == kDouble) {
						setText(String.valueOf(fDoubleValue));
					}
					setBackground( UIManager.getColor("TextField.background"));
				}
				else {
					setBackground(new Color(255,128,128));
				}
			}

			return fInputIsValid;
		}
	}
}
