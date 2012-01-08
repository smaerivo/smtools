// -----------------------------------
// Filename      : JDefaultDialog.java
// Author        : Sven Maerivoet
// Last modified : 16/05/2011
// Target        : Java VM (1.6)
// -----------------------------------

/**
 * Copyright 2003-2012 Sven Maerivoet
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

package smtools.swing.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import smtools.application.util.*;
import smtools.miscellaneous.*;
import smtools.swing.util.*;

/**
 * The <CODE>JDefaultDialog</CODE> class is a baseclass for creating arbitrary dialog boxes.
 * <P>
 * Note that a valid {@link Messages} database must be available!
 * <P>
 * There are three types of dialog boxes available:
 * <P>
 * <UL>
 *   <B><U>"Ok" dialog box</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/ok-default-dialog-windows.png">
 * </UL>
 * <P>
 * <UL>
 *   <B><U>"Ok/Cancel" dialog box</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/ok-cancel-default-dialog-windows.png">
 * </UL>
 * <P>
 * <UL>
 *   <B><U>Custom dialog box</U></B><BR />
 *   <BR />
 *   <IMG src="doc-files/custom-default-dialog-windows.png">
 * </UL>
 * <P>
 * Typically, <CODE>JDefaultDialog</CODE> is subclassed, with several methods overridden.
 * These methods control both the visual layout of the dialog box (custom dialog title and
 * custom content area) and the actions that need to be taken upon user input. The dialog
 * boxes can be modal or modeless, fixed size or resizable, directly activated or postponed.
 * When the dialog box is shown, it is placed in the middle of the parent's frame.
 * <P>
 * The overridable methods are called in the following order:
 * <P>
 * <UL>
 *   <LI>{@link JDefaultDialog#initialiseClass(Object[])}</LI>
 *   <LI>{@link JDefaultDialog#getWindowTitle()}</LI>
 *   <LI>{@link JDefaultDialog#getInitialDialogSize()}</LI>
 *   <LI>{@link JDefaultDialog#constructMainPanel(JPanel)}</LI>
 *   <LI>{@link JDefaultDialog#actionPerformed(ActionEvent)}</LI>
 * </UL>
 * <P>
 * The result of an "Ok/Cancel" type dialog box should be queried using the
 * {@link JDefaultDialog#cancelled} method.
 * <P>
 * <B><U>Important remark</U></B>
 * <P>
 * Initialisation of member fields in the subclass is accomplished by overriding the
 * {@link JDefaultDialog#initialiseClass(Object[])} method; the parameters are specified as an array of
 * <CODE>Objects</CODE>. These parameters are passed to the subclass in the call to the
 * constructor, for example:
 * <P>
 * <CODE>
 * <PRE>
 *   MyDialogClass myDialogObject =
 *     new MyDialogClass(
 *       parentComponent,
 *       JDefaultDialog.EModality.kModal,
 *       JDefaultDialog.ESize.kResizable,
 *       JDefaultDialog.EType.kOkCancel,
 *       new Object[] {object1,object2},
 *       JDefaultDialog.EActivation.kImmediately);
 * </PRE>
 * </CODE>
 * So the <CODE>Object[]</CODE> array is constructed using <CODE>new Object[]
 * {object1,object2}</CODE>. If no parameters are to be passed, you should specify
 * <CODE>null</CODE> for the array.
 * <P>
 * If the dialog box's activation was postponed (using the {@link JDefaultDialog#JDefaultDialog(JFrame,EModality,ESize,EType,Object[],EActivation)}
 * constructor) for example for storing in the GUI's component cache, it should be reactivated using the {@link JDefaultDialog#activate} method. Any initialisation that needs to occur
 * during the reactivation process, can be performed by overriding the {@link JDefaultDialog#initialiseDuringActivation} method.
 *
 * @author  Sven Maerivoet
 * @version 16/05/2011
 */
public class JDefaultDialog extends JDialog implements ActionListener, WindowListener
{
	/**
	 * Useful constants to specify a modal or modelss dialog box.
	 */
	public static enum EModality {kModal, kModeless};

	/**
	 * Useful constants to specify a fixed or resizable dialog box.
	 */
	public static enum ESize {kResizable, kFixedSize};

	/**
	 * Useful constants to specify an "Ok", "Ok/Cancel" or custm type dialog box.
	 */
	public static enum EType {kOk, kOkCancel, Custom};

	/**
	 * Useful constants to specify that the dialog box should be opened at the end of the constructor or if it should be postponed.
	 */
	public static enum EActivation {kImmediately, kPostponed};

	// the offset used when the dialog is at the edge of the screen
	private static final int kDialogOffset = 50;

	// internal datastructures
	private JFrame fApplicationFrame;
	private EType fType;
	private boolean fCancelled;
	private EActivation fActivation;
	private boolean fIsShown;
	private boolean fAutoPositioning;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>JDefaultDialog</CODE> object with the specified characteristics.
	 * <P>
	 * Note that the dialog box is activated (i.e., made visible) at the end of the constructor.
	 *
	 * @param applicationFrame the frame in which this dialog box is to be displayed
	 * @param modality         an <CODE>EModality</CODE> flag to indicate if the dialog box should be modal or modeless
	 * @param size             an <CODE>ESize</CODE> flag indicating whether or not the dialog box should be resizable
	 * @param type             the type of the dialog box ("Ok", "Ok/Cancel" or custom)
	 * @param parameters       an array of objects which are passed to the {@link JDefaultDialog#initialiseClass(Object[])} method
	 * @see   JDefaultDialog#JDefaultDialog(JFrame,EModality,ESize,EType,Object[],EActivation)
	 * @see   JDefaultDialog.EModality
	 * @see   JDefaultDialog.ESize
	 * @see   JDefaultDialog.EType
	 */
	public JDefaultDialog(JFrame applicationFrame, EModality modality, ESize size, EType type, Object[] parameters)
	{
		this(applicationFrame,modality,size,type,parameters,EActivation.kImmediately);
	}

	/**
	 * Constructs a <CODE>JDefaultDialog</CODE> object with the specified characteristics.
	 * <P>
	 * If <CODE>immediateActivation</CODE> is <CODE>false</CODE>, the dialog box should be made
	 * visible by explicitly calling the {@link JDefaultDialog#activate} method.
	 *
	 * @param applicationFrame the frame in which this dialog box is to be displayed
	 * @param modality         an <CODE>EModality</CODE> flag to indicate if the dialog box should be modal or modeless
	 * @param size             an <CODE>ESize</CODE> flag indicating whether or not the dialog box should be resizable
	 * @param type             the type of the dialog box ("Ok", "Ok/Cancel" or custom)
	 * @param parameters       an array of objects which are passed to the {@link JDefaultDialog#initialiseClass(Object[])} method
	 * @param activation       an <CODE>EActivation</CODE> flag indicating whether or not the dialog box should be made visible at the end of the constructor
	 * @see   JDefaultDialog#JDefaultDialog(JFrame,EModality,ESize,EType,Object[])
	 * @see   JDefaultDialog.EModality
	 * @see   JDefaultDialog.ESize
	 * @see   JDefaultDialog.EType
	 */
	public JDefaultDialog(JFrame applicationFrame, EModality modality, ESize size, EType type, Object[] parameters, EActivation activation)
	{
		super(applicationFrame);

		fType = type;
		fCancelled = (fType == EType.kOkCancel);
		fActivation = activation;
		fIsShown = false;
		fAutoPositioning = true;

		initialiseClass(parameters);

		if (getWindowTitle() != null) {
			setTitle(getWindowTitle());
		}

		if (size == ESize.kResizable) {
			setResizable(true);
		}
		else if (size == ESize.kFixedSize) {
		}
		if (getInitialDialogSize() != null) {
			setSize(getInitialDialogSize());
		}

		fApplicationFrame = applicationFrame;

		JPanel contentPane = new JPanel();
		constructContentPane(contentPane);
		setContentPane(contentPane);

		addWindowListener(this);
		pack();

		if (modality == EModality.kModal) {
			setModal(true);
		}
		else if (modality == EModality.kModeless) {
			setModal(true);
		}

		if (fActivation == EActivation.kImmediately) {
			activate();
		}
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	// the action-listener
	/**
	 * The dialog box's <B>action listener</B>.
	 * <P>
	 * Note that when overriding this method in a subclass, its parent should
	 * explicitly be called in order to guarantee the correct processing of
	 * the user's input ("Ok" and "Ok/Cancel" type of dialog boxes):
	 * <P>
	 * <CODE>
	 * <PRE>
	 *   super.actionPerformed(e);
	 *   // rest of method's code
	 * </PRE>
	 * </CODE>
	 *
	 * @param e the <CODE>ActionEvent</CODE> that is received
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();

		if (command.equalsIgnoreCase(Messages.lookup("buttonOk"))) {
			MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSButton,MP3Player.EPlaying.kUnblocked);
			fCancelled = false;
			windowClosing(null);
		}
		else if (command.equalsIgnoreCase(Messages.lookup("buttonCancel"))) {
			MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSButton,MP3Player.EPlaying.kUnblocked);
			windowClosing(null);
		}
	}

	// the window-listener
	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowActivated(WindowEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowClosed(WindowEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowClosing(WindowEvent e)
	{
		fIsShown = false;
		dispose();
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowDeactivated(WindowEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowDeiconified(WindowEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowIconified(WindowEvent e)
	{
	}

	/**
	 * A method from the dialog box's <B>window listener</B>.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @param e the <CODE>WindowEvent</CODE> that is received
	 */
	@Override
	public final void windowOpened(WindowEvent e)
	{
	}

	/**
	 * Indicates whether or not the user has cancelled the dialog box.
	 * <P>
	 * For "Ok" and "Custom" type dialog boxes, this method always returns <CODE>false</CODE>;
	 * note that it can be overridden in a subclass if custom behavior is needed (this may
	 * desirable for "Custom" type dialog boxes).
	 * <P>
	 * For "Ok/Cancel" type dialog boxes, this method only returns <CODE>false</CODE> if the
	 * user has selected the "Ok" button.
	 *
	 * @return <CODE>false</CODE> for "Ok" and "Custom" type dialog boxes, and "Ok/Cancel"
	 *         type dialog boxes if the user has selected the "Ok" button
	 */
	public boolean cancelled()
	{
		return fCancelled;
	}

	/**
	 * Displays the dialog box on the screen, thereby 'activating' it.
	 * <P>
	 * This method only has an effect when the dialog box is not immediately activated
	 * at the time of the constructor.
	 * 
	 * @see JDefaultDialog#initialiseDuringActivation
	 */
	public final void activate()
	{
		if (fActivation == EActivation.kPostponed) {

			MP3Player.playSystemSound(MP3Player.kSoundFilenameLCARSMessageDialog);

			fIsShown = true;

			// allow custom initialisation during activation
			initialiseDuringActivation();

			// reset the dialog box's cancel status
			fCancelled = (fType == EType.kOkCancel);

			// resize the GUI
			pack();

			if (fAutoPositioning) {
				reposition();
			}

			// enable the dialog box
			setVisible(true);
		}
	}

	/**
	 * Returns whether or not the dialog is shown.
	 * <P>
	 * Note that this method cannot be overridden!
	 *
	 * @return <CODE>true</CODE> if the dialog is shown, <CODE>false</CODE> otherwise 
	 */
	public final boolean isShown()
	{
		return fIsShown;
	}

	public final void disableAutoPositioning()
	{
		fAutoPositioning = false;
	}

	/*********************
	 * PROTECTED METHODS *
	 *********************/

	/**
	 * Allows custom initialisation of a subclass's member fields.
	 * <P>
	 * Note that the caller should specify <CODE>null</CODE> if no parameters are specified.
	 *
	 * @param parameters an array of <CODE>Objects</CODE>
	 */
	protected void initialiseClass(Object[] parameters)
	{
	}

	/**
	 * Returns the window title of the dialog box.
	 * <P>
	 * In order to obtain a custom dialog title, the caller should override this method
	 * (it returns <CODE>null</CODE> in the baseclass).
	 *
	 * @return the window title of the dialog box
	 */
	protected String getWindowTitle()
	{
		return null;
	}

	/**
	 * Returns the initial screen size of the dialog box.
	 * <P>
	 * In order to obtain a custom size for the dialog box, the caller should override this
	 * method (it returns <CODE>null</CODE> in the baseclass).
	 *
	 * @return the initial screen size of the dialog box
	 */
	protected Dimension getInitialDialogSize()
	{
		return null;
	}

	/**
	 * Allows the creation of custom content in the dialog box.
	 * <P>
	 * A subclass should typically create labels, inputfields, ... in the dialog
	 * boxes main panel, by overriding this method.
	 * <P>
	 * <B>Note that the <CODE>mainPanel</CODE> object is already constructed!</B>
	 *
	 * @param mainPanel the area of the dialog box that is reserved for custom content
	 */
	protected void constructMainPanel(JPanel mainPanel)
	{
	}

	/**
	 * Allows custom initialisation to be performed during the dialog box's reactivation.
	 *
	 * @see JDefaultDialog#activate
	 */
	protected void initialiseDuringActivation()
	{
	}

	/**
	 * Returns a location that guarantees that a dialog is fully shown within the screen's boundaries.
	 *
	 * @param  requestedLocation the requested location to position the dialog
	 * @param  size the size of the dialog
	 * @return a location that guarantees that a dialog is fully shown within the screen's boundaries
	 */
	public static Point getOnScreenLocation(Point requestedLocation, Dimension size)
	{
		// leave a certain margin with respect to the screen's edges
		final int kMargin = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Point location = new Point(requestedLocation);

		if ((location.x + size.width) > (screenSize.width - kMargin)) {
			location.x = screenSize.width - kMargin - size.width;
		}
		if ((location.y + size.height) > (screenSize.height - kMargin)) {
			location.y = screenSize.height - kMargin - size.height;
		}

		return location;
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private void constructContentPane(JPanel contentPane)
	{
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10,10,5,10));

		JPanel panel = null;
		JPanel subPanel = null;
		JButton button = null;

		// construct the main panel
		JPanel mainPanel = new JPanel();
		constructMainPanel(mainPanel);
		contentPane.add(mainPanel,BorderLayout.CENTER);

		if ((fType == EType.kOk) || (fType == EType.kOkCancel)) {

			// construct the panel containing the control buttons
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
			panel.add(new JEtchedLine());
			subPanel = new JPanel();
			subPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

			button = new JButton(Messages.lookup("buttonOk"));
			button.setActionCommand(Messages.lookup("buttonOk"));
			button.addActionListener(this);
			subPanel.add(button);
			button.requestFocusInWindow();

			if (fType == EType.kOkCancel) {

				button = new JButton(Messages.lookup("buttonCancel"));
				button.setActionCommand(Messages.lookup("buttonCancel"));
				button.addActionListener(this);
				subPanel.add(button);
				button.requestFocusInWindow();
			}
			panel.add(subPanel);

			contentPane.add(panel,BorderLayout.SOUTH);
		}
	}

	private void reposition()
	{
		// try to position the dialog in the middle of its parent's window
		int windowWidth = fApplicationFrame.getContentPane().getSize().width;
		int windowHeight = fApplicationFrame.getContentPane().getSize().height;
		int dialogWidth = getPreferredSize().width;
		int dialogHeight = getPreferredSize().height;
		int xPos = fApplicationFrame.getContentPane().getLocationOnScreen().x + (windowWidth / 2)
				- (dialogWidth / 2);
		int yPos = fApplicationFrame.getContentPane().getLocationOnScreen().y + (windowHeight / 2)
				- (dialogHeight / 2);
		if (xPos < kDialogOffset) {
			xPos = kDialogOffset;
		}
		if (yPos < kDialogOffset) {
			yPos = kDialogOffset;
		}
		setLocation(xPos,yPos);
	}
}
