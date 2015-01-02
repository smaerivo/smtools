// -------------------------------------------
// Filename      : JCustomColorMapChooser.java
// Author        : Sven Maerivoet
// Last modified : 11/12/2014
// Target        : Java VM (1.8)
// -------------------------------------------

/**
 * Copyright 2003-2015 Sven Maerivoet
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

package org.sm.smtools.swing.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.sm.smtools.application.util.*;
import org.sm.smtools.exceptions.*;
import org.sm.smtools.math.*;
import org.sm.smtools.swing.util.*;
import org.sm.smtools.swing.util.JGradientColorMap.*;
import org.sm.smtools.util.*;

/**
 * The <CODE>JCustomColorMapChooser</CODE> class provides a dialog for creating a custom colour map:
 * <P>
 * <IMG src="doc-files/custom-color-map-chooser.png" alt="">
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 *
 * @author  Sven Maerivoet
 * @version 11/12/2014
 */
public final class JCustomColorMapChooser extends JDefaultDialog implements ActionListener, ChangeListener
{
	// the action commands
	private static final String kActionCommandColorButtonPrefix = "colorbutton-";
	private static final String kActionCommandColorCheckBoxPrefix = "colorcheckbox-";
	private static final String kActionCommandLoadCustomColorMap = "button.CustomColorMap.LoadFromFile";
	private static final String kActionCommandSaveCustomColorMap = "button.CustomColorMap.SaveToFile";
	private static final String kActionCommandClearCustomColorMap = "button.CustomColorMap.Clear";

	// internal datastructures
	private int fNrOfColors;
	private JGradientColorMap fColorMap;
	private JButton[] fColorButtons;
	private JSlider[] fColorSliders;
	private JCheckBox[] fColorCheckBoxes;
	private boolean fClearControls;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an <CODE>IterationRangeChooser</CODE> object.
	 *
	 * @param owner                    the owning frame
	 * @param nrOfColors               the number of colours
	 * @param customColorMapComponents the color map components
	 */
	public JCustomColorMapChooser(JFrame owner, int nrOfColors, TreeMap<Integer,JGradientColorMap.CustomColorMapComponent> customColorMapComponents)
	{
		super(owner,
			JDefaultDialog.EModality.kModal,
			JDefaultDialog.ESize.kFixedSize,
			JDefaultDialog.EType.kOkCancel,
			new Object[] {nrOfColors,customColorMapComponents},
			JDefaultDialog.EActivation.kImmediately);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	// the action listener
	/**
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		String command = e.getActionCommand();
		if (command.startsWith(kActionCommandColorCheckBoxPrefix)) {
			int colorIndex = Integer.parseInt(command.substring(command.indexOf("-") + 1));
			boolean selected = fColorCheckBoxes[colorIndex].isSelected();
			double level = (double) fColorSliders[colorIndex].getValue() / 100.0;

			if (selected) {
				fColorCheckBoxes[colorIndex].setText(I18NL10N.translate("text.CustomColorMap.Enabled"));
				fColorMap.setCustomColorMapComponent(colorIndex,level,fColorButtons[colorIndex].getBackground());
			}
			else {
				fColorCheckBoxes[colorIndex].setText(I18NL10N.translate("text.CustomColorMap.Disabled"));
				fColorMap.removeCustomColorMapComponent(colorIndex);
			}
			fColorButtons[colorIndex].setEnabled(selected);
			fColorSliders[colorIndex].setEnabled(selected);
			fColorMap.repaint();
		}
		else if (command.startsWith(kActionCommandColorButtonPrefix)) {
			int colorIndex = Integer.parseInt(command.substring(command.indexOf("-") + 1));
			Color color = JColorChooser.showDialog(this,I18NL10N.translate("text.CustomColorMap.SelectColor"),fColorButtons[colorIndex].getBackground());
			if (color != null) {
				fColorButtons[colorIndex].setBackground(color);				

				double level = (double) fColorSliders[colorIndex].getValue() / 100.0;
				fColorMap.setCustomColorMapComponent(colorIndex,level,color);
				fColorMap.repaint();
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandLoadCustomColorMap)) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setDialogTitle(I18NL10N.translate("text.CustomColorMap.LoadTitle"));
			fileChooser.setFileFilter(new JFileFilter("CSV",I18NL10N.translate("text.File.CSVDescription")));
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();

				try {
					clearControls();

					TextFileParser tfp = new TextFileParser(filename);
					fColorMap.loadCustomColorMapComponents(tfp);

					adjustControls();
				}
				catch (FileDoesNotExistException exc) {
					JWarningDialog.warn(this,I18NL10N.translate("error.CustomColorMap.ErrorLoadingCustomColorMap"));
				}
				catch (FileParseException exc) {
					JWarningDialog.warn(this,I18NL10N.translate("error.CustomColorMap.ErrorParsingCustomColorMap",String.valueOf(exc.getLineNr()),exc.getValue()));
				}
				catch (NumberFormatException exc) {
					JWarningDialog.warn(this,I18NL10N.translate("error.CustomColorMap.ErrorParsingCustomColorMapComponent",String.valueOf(exc.getMessage())));
				}
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandSaveCustomColorMap)) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setDialogTitle(I18NL10N.translate("text.CustomColorMap.SaveTitle"));
			fileChooser.setFileFilter(new JFileFilter("CSV",I18NL10N.translate("text.File.CSVDescription")));
			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				if (!filename.endsWith(".csv")) {
					filename += ".csv";
				}

				try {
					TextFileWriter tfw = new TextFileWriter(filename);
					fColorMap.saveCustomColorMapComponents(tfw);
					JMessageDialog.show(this,I18NL10N.translate("text.CustomColorMap.Saved"));
				}
				catch (FileCantBeCreatedException exc) {
					JWarningDialog.warn(this,I18NL10N.translate("error.CustomColorMap.ErrorSavingCustomColorMap"));
				}
				catch (FileWriteException exc) {
					JWarningDialog.warn(this,I18NL10N.translate("error.CustomColorMap.ErrorSavingCustomColorMap"));
				}
			}
		}
		else if (command.equalsIgnoreCase(kActionCommandClearCustomColorMap)) {
			clearControls();
		}
	}

	// the change-listener
	/**
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSlider colorSlider = (JSlider) e.getSource();

		if (!colorSlider.getValueIsAdjusting()) {
			// find colorIndex
			int colorIndex = 0;
			for (int i = 0; i < fNrOfColors; ++i) {
				if (colorSlider == fColorSliders[i]) {
					colorIndex = i;
				}
			}

			// extract value
			double level = (double) colorSlider.getValue() / 100.0;

			fColorMap.setCustomColorMapComponent(colorIndex,level,fColorButtons[colorIndex].getBackground());
			fColorMap.repaint();
		}
	}

	/**
	 * Returns the selected custom colour map components.
	 *
	 * @return the selected custom colour map components
	 */
	public TreeMap<Integer,JGradientColorMap.CustomColorMapComponent> getSelectedCustomColorMapComponents()
	{
		return fColorMap.getAllCustomColorMapComponents();
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Performs custom initialisation of the about box's member fields.
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void initialiseClass(Object[] parameters)
	{
		fClearControls = false;
		fNrOfColors = (int) parameters[0];
		fColorMap = new JGradientColorMap(JGradientColorMap.EOrientation.kVerticalTopToBottom,JGradientColorMap.EColorMap.kCustom,50,400);
		fColorMap.setAllCustomColorMapComponents((TreeMap<Integer,JGradientColorMap.CustomColorMapComponent>) parameters[1]);
	}

	/**
	 * Returns the dialog box's title.
	 */
	@Override
	protected java.lang.String setupWindowTitle()
	{
		return I18NL10N.translate("text.CustomColorMap.Title");
	}

	/**
	 * Creates the dialog box content area.
	 * <P>
	 * <B>Note that this method cannot be overridden!</B>
	 */
	@Override
	protected void setupMainPanel(JPanel mainPanel)
	{
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

			JPanel upperPanel = new JPanel();
			upperPanel.setLayout(new BoxLayout(upperPanel,BoxLayout.X_AXIS));

				JPanel leftPanel = new JPanel();
				leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
				leftPanel.add(Box.createVerticalStrut(40));
				leftPanel.add(fColorMap);
				leftPanel.add(Box.createVerticalStrut(30));
			upperPanel.add(leftPanel);

			upperPanel.add(Box.createHorizontalStrut(20));

			fColorButtons = new JButton[fNrOfColors];
			fColorSliders = new JSlider[fNrOfColors];
			fColorCheckBoxes = new JCheckBox[fNrOfColors];
			TreeMap<Integer,CustomColorMapComponent> customColorMapComponents = fColorMap.getAllCustomColorMapComponents();
			for (int colorIndex = 0; colorIndex < fNrOfColors; ++colorIndex) {
				JPanel colorPanel = new JPanel();
				colorPanel.setLayout(new BoxLayout(colorPanel,BoxLayout.Y_AXIS));
				boolean colorAvailable = customColorMapComponents.containsKey(colorIndex);

					fColorButtons[colorIndex] = new JButton(I18NL10N.translate("text.CustomColorMap.SetColor"));
					fColorButtons[colorIndex].setHorizontalAlignment(SwingConstants.LEFT);
					fColorButtons[colorIndex].setActionCommand(kActionCommandColorButtonPrefix + String.valueOf(colorIndex));
					fColorButtons[colorIndex].addActionListener(this);
					fColorButtons[colorIndex].setEnabled(colorAvailable);
					if (colorAvailable) {
						fColorButtons[colorIndex].setBackground(customColorMapComponents.get(colorIndex).fColor);
					}
				colorPanel.add(fColorButtons[colorIndex]);

				colorPanel.add(Box.createVerticalStrut(10));

					fColorSliders[colorIndex] = new JSlider(JSlider.VERTICAL);
					fColorSliders[colorIndex].setInverted(true);
					fColorSliders[colorIndex].setMinimum(0);
					fColorSliders[colorIndex].setMaximum(100);
					fColorSliders[colorIndex].setMinorTickSpacing(100 / 10);
					fColorSliders[colorIndex].setMajorTickSpacing(100 / 5);
					fColorSliders[colorIndex].setPaintTicks(true);
					fColorSliders[colorIndex].setPaintLabels(false);
					fColorSliders[colorIndex].setPaintTrack(true);
					fColorSliders[colorIndex].setEnabled(colorAvailable);
					if (colorAvailable) {
						fColorSliders[colorIndex].setValue((int) MathTools.clip(Math.round(customColorMapComponents.get(colorIndex).fLevel * 100.0),0.0,100.0));
					}
					else {
						fColorSliders[colorIndex].setValue(0);
					}
					fColorSliders[colorIndex].addChangeListener(this);
				colorPanel.add(fColorSliders[colorIndex]);

				colorPanel.add(Box.createVerticalStrut(10));

					fColorCheckBoxes[colorIndex] = new JCheckBox(I18NL10N.translate("text.CustomColorMap.Disabled"));
					fColorCheckBoxes[colorIndex].setHorizontalAlignment(SwingConstants.LEFT);
					fColorCheckBoxes[colorIndex].setSelected(colorAvailable);
					if (colorAvailable) {
						fColorCheckBoxes[colorIndex].setText(I18NL10N.translate("text.CustomColorMap.Enabled"));
					}
					fColorCheckBoxes[colorIndex].setActionCommand(kActionCommandColorCheckBoxPrefix + String.valueOf(colorIndex));
					fColorCheckBoxes[colorIndex].addActionListener(this);
				colorPanel.add(fColorCheckBoxes[colorIndex]);
			
				upperPanel.add(colorPanel);
				if (colorIndex < (fNrOfColors - 1)) {
					upperPanel.add(Box.createHorizontalStrut(10));
				}
			}
		mainPanel.add(upperPanel);

		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(new JEtchedLine(JEtchedLine.EOrientation.kHorizontal));
		mainPanel.add(Box.createVerticalStrut(10));

			JPanel lowerPanel = new JPanel();
			lowerPanel.setLayout(new BoxLayout(lowerPanel,BoxLayout.X_AXIS));
			lowerPanel.add(Box.createHorizontalGlue());

				JButton clearButton = new JButton(I18NL10N.translate(kActionCommandClearCustomColorMap));
				clearButton.setActionCommand(kActionCommandClearCustomColorMap);
				clearButton.addActionListener(this);
			lowerPanel.add(clearButton);

			lowerPanel.add(Box.createHorizontalStrut(10));

				JEtchedLine separator = new JEtchedLine(JEtchedLine.EOrientation.kVertical);
				separator.setMaximumSize(new Dimension(1,100));
			lowerPanel.add(separator);

			lowerPanel.add(Box.createHorizontalStrut(10));

				JButton loadButton = new JButton(I18NL10N.translate(kActionCommandLoadCustomColorMap));
				loadButton.setActionCommand(kActionCommandLoadCustomColorMap);
				loadButton.addActionListener(this);
			lowerPanel.add(loadButton);

			lowerPanel.add(Box.createHorizontalStrut(10));

				JButton saveButton = new JButton(I18NL10N.translate(kActionCommandSaveCustomColorMap));
				saveButton.setActionCommand(kActionCommandSaveCustomColorMap);
				saveButton.addActionListener(this);
			lowerPanel.add(saveButton);
		mainPanel.add(lowerPanel);

		mainPanel.add(Box.createVerticalStrut(10));
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	/**
	 */
	public void clearControls()
	{
		fClearControls = true;
		adjustControls();
	}

	/**
	 */
	private void adjustControls()
	{
		// reset all controls
		for (int colorIndex = 0; colorIndex < fNrOfColors; ++colorIndex) {
			fColorButtons[colorIndex].setBackground(getBackground());
			fColorButtons[colorIndex].setEnabled(false);

			fColorSliders[colorIndex].setValue(0);
			fColorSliders[colorIndex].setEnabled(false);

			fColorCheckBoxes[colorIndex].setSelected(false);
			fColorCheckBoxes[colorIndex].setText(I18NL10N.translate("text.CustomColorMap.Disabled"));
		}

		if (fClearControls) {
			fColorMap.clearAllCustomColorMapComponents();
			fClearControls = false;
		}

		TreeMap<Integer,CustomColorMapComponent> colorMapComponents = fColorMap.getAllCustomColorMapComponents();

		// prevent a ConcurrentModificationException
		Set<Integer> colorIndicesSet = fColorMap.getAllCustomColorMapComponents().keySet();
		ArrayList<Integer> colorIndices = new ArrayList<Integer>();
		for (int colorIndex : colorIndicesSet) {
			colorIndices.add(colorIndex);
		}

		for (int colorIndex : colorIndices) {
			CustomColorMapComponent colorMapComponent = colorMapComponents.get(colorIndex);

			// only adjust color map components within range
			if (colorIndex < fNrOfColors) {
				fColorButtons[colorIndex].setBackground(colorMapComponent.fColor);
				fColorButtons[colorIndex].setEnabled(true);

				fColorSliders[colorIndex].setValue((int) MathTools.clip(Math.round(colorMapComponent.fLevel * 100.0),0.0,100.0));
				fColorSliders[colorIndex].setEnabled(true);

				fColorCheckBoxes[colorIndex].setSelected(true);
				fColorCheckBoxes[colorIndex].setText(I18NL10N.translate("text.CustomColorMap.Enabled"));
			}
		}
	}
}
