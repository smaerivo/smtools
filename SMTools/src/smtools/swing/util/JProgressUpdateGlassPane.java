// ---------------------------------------------
// Filename      : JProgressUpdateGlassPane.java
// Author        : Sven Maerivoet
// Last modified : 21/02/2013
// Target        : Java VM (1.6)
// ---------------------------------------------

/**
 * Copyright 2003-2013 Sven Maerivoet
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

package smtools.swing.util;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import smtools.math.*;
import smtools.miscellaneous.*;

/**
 * The <CODE>JProgressUpdateGlassPane</CODE> class provides the basic functionality for a progress updating glasspane.
 * <P>
 * In order to meaningfully use the progress updater, a user first has to set the total number of expected progress updates via
 * {@link JProgressUpdateGlassPane#setTotalNrOfProgressUpdates(int)}. Each time an update is done, the user should call {@link JProgressUpdateGlassPane#signalProgressUpdate()}
 * which repaints the glasspane.
 * <P>
 * The following visualisations are supported:
 * <P>
 * <UL>
 *   <B>Bar:</B><BR />
 *   <IMG src="doc-files/progress-update-bar.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Circles:</B><BR />
 *   <IMG src="doc-files/progress-update-circles.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Sector (fixed):</B><BR />
 *   <IMG src="doc-files/progress-update-sector-integral-fixed.png">
 * </UL>
 * <P>
 * <UL>
 *   <B>Sector (rotating):</B><BR />
 *   <IMG src="doc-files/progress-update-sector-fractional-rotating.png">
 * </UL>
 * <P>
 *
 * @author  Sven Maerivoet
 * @version 21/02/2013
 */
public class JProgressUpdateGlassPane extends JPanel implements MouseListener, MouseMotionListener
{
	/**
	 * The different types of progress update visualisations.
	 */
	public static enum EVisualisationType {kBar, kCircles, kFixedSector, kRotatingSector};

	// the number of circles to draw
	private static final int kTotalNrOfCircles = 16;

	// internal datastructures
	private EVisualisationType fVisualisationType;
	private boolean fShowFractions;
	private int fTotalNrOfProgressUpdates;
	private int fNrOfProgressUpdatesCompleted;
	private double fPercentageCompleted;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs an unblocking <CODE>JProgressUpdateGlassPane</CODE> object and resets it.
	 * <P>
	 * A bar is used as the type of visualisation, fractions are not shown in the percentage completed.
	 */
	public JProgressUpdateGlassPane()
	{
		this(EVisualisationType.kBar,false);
	}

	/**
	 * Constructs an unblocking <CODE>JProgressUpdateGlassPane</CODE> object and resets it.
	 *
	 * @param visualisationType the type of the visualisation to show
	 * @param showFractions a <CODE>boolean</CODE> indicating whether or not fractions are shown in the percentage completed
	 */
	public JProgressUpdateGlassPane(EVisualisationType visualisationType, boolean showFractions)
	{
		super();
		setVisualisationType(visualisationType);
		setShowFractions(showFractions);
		reset();
		setBlocking(false);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the visualisation type to use.
	 *
	 * @param visualisationType the type of the visualisation to use
	 */
	public final void setVisualisationType(EVisualisationType visualisationType)
	{
		fVisualisationType = visualisationType;
	}

	/**
	 * Returns the visualisation type to use.
	 *
	 * @return the type of the visualisation to use
	 */
	public final EVisualisationType getVisualisationType()
	{
		return fVisualisationType;
	}

	/**
	 * Sets whether or not fractions are shown in the percentage completed
	 *
	 * @param showFractions a <CODE>boolean</CODE> indicating whether or not fractions are shown in the percentage completed
	 */
	public final void setShowFractions(boolean showFractions)
	{
		fShowFractions = showFractions;
	}

	/**
	 * Returns whether or not fractions are shown in the percentage completed
	 *
	 * @return a <CODE>boolean</CODE> indicating whether or not fractions are shown in the percentage completed
	 */
	public final boolean getShowFractions()
	{
		return fShowFractions;
	}

	/**
	 * Resets the progress update glasspane, by setting the percentage completed to zero and making the glasspane visible.
	 */
	public final void reset()
	{
		fNrOfProgressUpdatesCompleted = 0;
		setPercentageCompleted(0.0);
		setVisible(true);
	}

	/**
	 * Sets the total number of progress updates expected, corresponding to 100% completion.
	 *
	 * @param totalNrOfProgressUpdates the total number of progress updates expected
	 */
	public final void setTotalNrOfProgressUpdates(int totalNrOfProgressUpdates)
	{
		fTotalNrOfProgressUpdates = totalNrOfProgressUpdates;
	}

	/**
	 * Increases the number of progress updates already completed and repaints the glasspane.
	 */
	public final void signalProgressUpdate()
	{
		++fNrOfProgressUpdatesCompleted;
		setPercentageCompleted(((double) fNrOfProgressUpdatesCompleted / (double) fTotalNrOfProgressUpdates) * 100.0);
	}

	/**
	 * Directly sets the percentage completed.
	 *
	 * @param percentageCompleted the percentage completed
	 */
	public final void setPercentageCompleted(double percentageCompleted)
	{
		fPercentageCompleted = MathTools.clip(percentageCompleted,0.0,100.0);
		repaint();
		if (fPercentageCompleted >= 100.0) {
			done();
		}
	}

	/**
	 * Returns the percentage completed.
	 *
	 * @return the percentage completed
	 */
	public final double getPercentageCompleted()
	{
		return fPercentageCompleted;
	}

	/**
	 * Resets the progress update glasspane and makes the glasspane invisible.
	 */
	public final void done()
	{
		reset();
		setVisible(false);
	}

	/**
	 * Sets whether or not the glasspane should block all user mouse-input.
	 *
	 * @param blocking a <CODE>boolean</CODE> that indicates whether or not the glasspane should block all user mouse-input
	 */
	public final void setBlocking(boolean blocking)
	{
		if (blocking) {
			if (getMouseListeners().length == 0) {
				addMouseListener(this);
				addMouseMotionListener(this);
			}
		}
		else {
			if (getMouseListeners().length > 0) {
				removeMouseListener(this);
				removeMouseMotionListener(this);
			}
		}
	}

	/**
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		int width = getWidth();
		int height = getHeight();
		double percentageCompleted = getPercentageCompleted();

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		// paint a dark blue translucent veil, fade in at the beginning and out at the end
		double alphaLevel = 0.5;
		if (percentageCompleted < 15.0) {
			alphaLevel *= (percentageCompleted / 15.0);
		}
		else if (percentageCompleted > 75.0) {
			alphaLevel *= ((100.0 - percentageCompleted) / 25.0);
		}
		int alpha = (int) (alphaLevel * 255);
		Rectangle2D r = new Rectangle2D.Double(0,0,width,height);
		Color veilC1 = new Color(0,0,64,alpha);
		Color veilC2 = new Color(0,0,255,alpha);
		GradientPaint veilGP = new GradientPaint(0,0,veilC1,0,height,veilC2);
		g2.setPaint(veilGP);
		g2.fill(r);
		g2.draw(r);

		// determine the general diameter
		double diameter = (double) width / 1.5;
		if (width > height) {
			diameter = (double) height / 1.5;
		}

		// show the percentage completed
		int kTextOffset = 2;
		int nrOfDecimals = 0;
		if (fShowFractions) {
			nrOfDecimals = 1;
		}
		String percentageCompletedDesc = StringTools.convertDoubleToString(percentageCompleted,nrOfDecimals) + "%";
		Font font = new Font(getFont().getFontName(),Font.BOLD,(int) Math.round(diameter / 10.0));
		g2.setFont(font);
		FontMetrics fontMetrics = g2.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(percentageCompletedDesc);
		int textHeight = fontMetrics.getHeight();
		// create offset text
		g2.setColor(Color.BLACK);
		g2.drawString(percentageCompletedDesc,(width / 2) - (textWidth / 2) + kTextOffset,(height / 2) - (textHeight / 2) + fontMetrics.getAscent() + kTextOffset);
		g2.setColor(Color.WHITE);
		g2.drawString(percentageCompletedDesc,(width / 2) - (textWidth / 2),(height / 2) - (textHeight / 2) + fontMetrics.getAscent());

		if (fVisualisationType == EVisualisationType.kBar) {
			// determine bar dimensions
			int totalWidth = (int) Math.round(width / 1.5);
			int totalHeight = height / 10;

			int x1 = (width / 2) - (totalWidth / 2);
			int y1 = (height / 2) - (totalHeight / 2) + textHeight + fontMetrics.getAscent();
			int barWidth = totalWidth;
			int currentBarWidth = (int) Math.round((double) totalWidth * (percentageCompleted / 100.0));
			int barHeight = totalHeight;
			int corner = barHeight / 2;

			// draw the outer bar
			RoundRectangle2D outerBar = new RoundRectangle2D.Double(x1,y1,barWidth,barHeight,corner,corner);
			Color outerC1 = Color.BLACK;
			Color outerC2 = Color.BLUE;
			GradientPaint outerGP = new GradientPaint(0,y1,outerC1,0,y1 + (barHeight / 2),outerC2,true);
			g2.setPaint(outerGP);
			g2.fill(outerBar);

			// inset the progress bar
			int kBarOffset = 10;
			RoundRectangle2D bar = new RoundRectangle2D.Double(x1 + kBarOffset,y1 + kBarOffset,currentBarWidth - (2 * kBarOffset),barHeight - (2 * kBarOffset),corner,corner);
			Color barC1 = Color.BLUE;
			Color barC2 = Color.WHITE;
			GradientPaint barGP = new GradientPaint(x1,0,barC1,x1 + currentBarWidth,0,barC2,true);
			g2.setPaint(barGP);
			g2.fill(bar);
		}
		else if (fVisualisationType == EVisualisationType.kCircles) {
			double circleDiameter = diameter / 10.0;
			double spacingAngle = 360.0 / (double) kTotalNrOfCircles;

			// determine the current circle {0,...,total - 1}
			int currentCircle = (int) MathTools.clip(Math.floor((percentageCompleted / 100.0) * (double) kTotalNrOfCircles),0,kTotalNrOfCircles - 1);

			for (int circleNr = 0; circleNr < kTotalNrOfCircles; ++circleNr) {

				// determine position of circle
				double circleAngle = (double) circleNr * spacingAngle;
				double circleCenterX = (width / 2) + ((diameter / 2.0) * Math.cos(MathTools.deg2rad(circleAngle - 90)));
				double circleCenterY = (height / 2) + ((diameter / 2.0) * Math.sin(MathTools.deg2rad(circleAngle - 90)));
				double x1 = circleCenterX - (circleDiameter / 2.0);
				double y1 = circleCenterY - (circleDiameter / 2.0);

				// render unprocessed circles in blue shades
				// highlight all processed circles
				// gradually increase the brightness of the current circle
				Color circleC1 = Color.BLACK;
				Color circleC2 = Color.BLUE;
				if (circleNr < currentCircle) {
					circleC2 = Color.WHITE;
				}
				else if (circleNr == currentCircle) {
					float fraction = (float) MathTools.clip(((percentageCompleted / 100.0) - ((float) currentCircle * (1.0f / (float) kTotalNrOfCircles))) / (1.0f / (float) kTotalNrOfCircles),0.0,1.0);
					circleC2 = new Color(fraction,fraction,1.0f);
				}
				GradientPaint circleGP = new GradientPaint(0,(int) y1,circleC1,0,(int) (y1 + circleDiameter),circleC2);
				g2.setPaint(circleGP);

				g2.fillOval((int) x1,(int) y1,(int) circleDiameter,(int) circleDiameter);
			}
		}
		else if ((fVisualisationType == EVisualisationType.kFixedSector) || (fVisualisationType == EVisualisationType.kRotatingSector)) {
			// determine the angles
			double angle = (percentageCompleted / 100.0) * 360.0;
			double startAngle = (360.0 - angle) + 90.0;

			// draw the outer arc
			double x1 = (width / 2.0) - (diameter / 2.0);
			double y1 = (height / 2.0) - (diameter / 2.0);
			float strokeWidth = (float) (diameter / 10.0f);
			Arc2D outerArc = new Arc2D.Double(x1,y1,diameter,diameter,0,360,Arc2D.OPEN);
			g2.setStroke(new BasicStroke(strokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
			Color outerArcC1 = Color.BLACK;
			Color outerArcC2 = Color.WHITE;
			GradientPaint outerArcGP = new GradientPaint(0,(int) y1,outerArcC1,0,(int) (y1 + diameter),outerArcC2);
			g2.setPaint(outerArcGP);
			g2.draw(outerArc);

			// draw the inner arc
			diameter -= (strokeWidth * 1.9);
			x1 = (width / 2.0) - (diameter / 2.0);
			y1 = (height / 2.0) - (diameter / 2.0);
			Arc2D innerArc = new Arc2D.Double(x1,y1,diameter,diameter,0,360,Arc2D.OPEN);
			g2.setStroke(new BasicStroke(strokeWidth,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
			g2.setColor(Color.LIGHT_GRAY);
			g2.draw(innerArc);

			if (fVisualisationType == EVisualisationType.kRotatingSector) {
/*
				// position the start of the circle according to the fraction
				// the extent corresponds to the total percentage completed
				startAngle = MathTools.frac(percentageCompleted) * 360.0;
				angle = (percentageCompleted / 100.0) * 360.0;
				startAngle = (360.0 - startAngle) + 90.0;
*/
				// position the start of the circle so that it has turned a full circle upon completion
				startAngle = ((percentageCompleted * 2.0) / 100.0) * 360.0;
				angle = (percentageCompleted / 100.0) * 360.0;
				startAngle = (360.0 - startAngle) + 90.0;
			}

			// overlay the progress circle on the inner arc
			strokeWidth *= 1.05f;
			Arc2D progressCircle = new Arc2D.Double(x1,y1,diameter,diameter,startAngle,angle,Arc2D.OPEN);
			g2.setStroke(new BasicStroke(strokeWidth,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
			JGradientColorRamp gcr = new JGradientColorRamp(JGradientColorRamp.EColorMap.kBlue);
			g2.setColor(gcr.interpolate(0.25 + (percentageCompleted / 75.0)));
			g2.draw(progressCircle);
		}
	}

	// the mouse-listener
	/**
	 */
	@Override
	public final void mouseClicked(MouseEvent e)
	{
		// block event
		e.consume();
	}

	/**
	 */
	@Override
	public final void mousePressed(MouseEvent e)
	{
		// block event
		e.consume();
	}

	/**
	 */
	@Override
	public final void mouseReleased(MouseEvent e)
	{
		// block event
		e.consume();
	}

	/**
	 */
	@Override
	public final void mouseEntered(MouseEvent e)
	{
		// block event
		e.consume();
	}

	/**
	 */
	@Override
	public final void mouseExited(MouseEvent e)
	{
		// block event
		e.consume();
	}

	// the mouse-motion-listener
	/**
	 */
	@Override
	public final void mouseMoved(MouseEvent e)
	{
		// block event
		e.consume();
	}

	/**
	 */
	@Override
	public final void mouseDragged(MouseEvent e)
	{
		// block event
		e.consume();
	}
}
