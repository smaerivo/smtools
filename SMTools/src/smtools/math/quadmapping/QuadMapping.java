// --------------------------------
// Filename      : QuadMapping.java
// Author        : Sven Maerivoet
// Last modified : 09/12/2004
// Target        : Java VM (1.6)
// --------------------------------

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

package smtools.math.quadmapping;

import java.awt.geom.*;

/**
 * The <CODE>QuadMapping</CODE> class provides a perspective mapping for a convex quadrilateral.
 * <P>
 * This class maps 2D-data points between a convex quadrilateral and a normalised square:
 * <P>
 * <UL>
 *   <IMG src="doc-files/quad-mapping.png">
 * </UL>
 * <P>
 * Note: it is assumed that the X axis points to the right and the Y axis points upwards.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 09/12/2004
 * @see     Quadrilateral
 * @see     QuadToQuadMapping
 */
public final class QuadMapping
{
	// internal datastructures
	private double a, b, c, d, e, f, g, h, i;
	private double A, B, C, D, E, F, G, H, I;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>QuadMapping</CODE> object based on the perspective mapping of the specified quadrilateral.
	 *
	 * @param q the quadrilateral to use for the perspective mapping
	 * @see   QuadMapping#setQuadrilateral(Quadrilateral)
	 */
	public QuadMapping(Quadrilateral q)
	{
		setQuadrilateral(q);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Calculates the perspective mapping of the specified quadrilateral.
	 *
	 * @param q the quadrilateral to use for the perspective mapping
	 */
	public void setQuadrilateral(Quadrilateral q)
	{
		constructQuadrilateralMapping(q);
	}

	/**
	 * Maps a 2D-data point in the normalised square to a 2D-data point in the quadrilateral.
	 *
	 * @param  p the 2D-data point in the normalised square to map
	 * @return   the corresponding 2D-data point in the quadrilateral
	 * @see    QuadMapping#mapSquareToQuadrilateral(double,double)
	 */
	public Point2D.Double mapSquareToQuadrilateral(Point2D.Double p)
	{
		return mapSquareToQuadrilateral(p.x,p.y);
	}

	/**
	 * Maps a 2D-data point in the normalised square to a 2D-data point in the quadrilateral.
	 *
	 * @param  u the x coordinate of the 2D-data point in the normalised square to map
	 * @param  v the y coordinate of the 2D-data point in the normalised square to map
	 * @return   the corresponding 2D-data point in the quadrilateral
	 * @see    QuadMapping#mapSquareToQuadrilateral(Point2D.Double)
	 */
	public Point2D.Double mapSquareToQuadrilateral(double u, double v)
	{
		return new Point2D.Double(((a * u) + (b * v) + c) / ((g * u) + (h * v) + i),((d * u) + (e * v) + f)
				/ ((g * u) + (h * v) + i));
	}

	/**
	 * Inversely maps a 2D-data point in the quadrilateral to a 2D-data point in the normalised square.
	 *
	 * @param  p the 2D-data point in the quadrilateral to map inversely
	 * @return   the corresponding 2D-data point in the normalised square
	 * @see    QuadMapping#mapQuadrilateralToSquare(double,double)
	 */
	public Point2D.Double mapQuadrilateralToSquare(Point2D.Double p)
	{
		return mapQuadrilateralToSquare(p.x,p.y);
	}

	/**
	 * Inversely maps a 2D-data point in the quadrilateral to a 2D-data point in the normalised square.
	 *
	 * @param  x the x coordinate of the 2D-data point in the quadrilateral to map inversely
	 * @param  y the y coordinate of the 2D-data point in the quadrilateral to map inversely
	 * @return   the corresponding 2D-data point in the normalised square
	 * @see    QuadMapping#mapQuadrilateralToSquare(Point2D.Double)
	 */
	public Point2D.Double mapQuadrilateralToSquare(double x, double y)
	{
		return new Point2D.Double(((A * x) + (B * y) + C) / ((G * x) + (H * y) + I),((D * x) + (E * y) + F)
				/ ((G * x) + (H * y) + I));
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private void constructQuadrilateralMapping(Quadrilateral q)
	{
		// construct mapping from square to quadriteral and vice versa
		double[] x = new double[4];
		double[] y = new double[4];

		x[0] = q.fP1.x;
		y[0] = q.fP1.y;
		x[1] = q.fP2.x;
		y[1] = q.fP2.y;
		x[2] = q.fP3.x;
		y[2] = q.fP3.y;
		x[3] = q.fP4.x;
		y[3] = q.fP4.y;

		double deltaX1 = x[1] - x[2];
		double deltaY1 = y[1] - y[2];
		double deltaX2 = x[3] - x[2];
		double deltaY2 = y[3] - y[2];
		double sumX = x[0] - x[1] + x[2] - x[3];
		double sumY = y[0] - y[1] + y[2] - y[3];

		// calculate matrix for transformation
		g = ((sumX * deltaY2) - (sumY * deltaX2)) / ((deltaX1 * deltaY2) - (deltaY1 * deltaX2));
		h = ((deltaX1 * sumY) - (deltaY1 * sumX)) / ((deltaX1 * deltaY2) - (deltaY1 * deltaX2));
		i = 1;
		a = x[1] - x[0] + (g * x[1]);
		b = x[3] - x[0] + (h * x[3]);
		c = x[0];
		d = y[1] - y[0] + (g * y[1]);
		e = y[3] - y[0] + (h * y[3]);
		f = y[0];

		// calculate adjoint matrix for inverse mapping from quadriteral to square
		A = e - (f * h);
		B = (c * h) - b;
		C = (b * f) - (e * c);
		D = (f * g) - d;
		E = a - (c * g);
		F = (c * d) - (a * f);
		G = (d * h) - (e * g);
		H = (b * g) - (a * h);
		I = (a * e) - (b * d);
	}
}
