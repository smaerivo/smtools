// ----------------------------------
// Filename      : Quadrilateral.java
// Author        : Sven Maerivoet
// Last modified : 09/12/2004
// Target        : Java VM (1.6)
// ----------------------------------

/**
 * Copyright 2003-2011 Sven Maerivoet
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
 * The <CODE>Quadrilateral</CODE> class provides a container for a 2D quadrilateral.
 * <P>
 * Note that a <I>counter clockwise</I> specified quadrilateral is preferred:
 * <P>
 * <UL>
 *   <IMG src="doc-files/quadrilateral.png">
 * </UL>
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 09/12/2004
 * @see     Quadrilateral
 * @see     QuadMapping
 * @see     QuadToQuadMapping
 */
public final class Quadrilateral
{
	// the quadrilateral's corner points
	/**
	 * The quadrilateral's first point.
	 */
	public Point2D.Double fP1;

	/**
	 * The quadrilateral's second point.
	 */
	public Point2D.Double fP2;

	/**
	 * The quadrilateral's third point.
	 */
	public Point2D.Double fP3;

	/**
	 * The quadrilateral's fourth point.
	 */
	public Point2D.Double fP4;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>Quadrilateral</CODE> object with all four 2D points equal to (0,0).
	 *
	 * @see Quadrilateral#Quadrilateral(Point2D.Double,Point2D.Double,Point2D.Double,Point2D.Double)
	 * @see Quadrilateral#Quadrilateral(Quadrilateral)
	 * @see Quadrilateral#clear()
	 */
	public Quadrilateral()
	{
		clear();
	}

	/**
	 * Constructs a <CODE>Quadrilateral</CODE> object with the four specified 2D points.
	 * <P>
	 * Note that internally, the four specified points are referenced; this means that any
	 * changes to the specified points are reflected in the internal <CODE>Point2D.Double</CODE> objects.
	 *
	 * @param p1 the quadrilateral's first point (<B>shallow copy</B>)
	 * @param p2 the quadrilateral's second point (<B>shallow copy</B>)
	 * @param p3 the quadrilateral's third point (<B>shallow copy</B>)
	 * @param p4 the quadrilateral's fourth point (<B>shallow copy</B>)
	 * @see   Quadrilateral#Quadrilateral()
	 * @see   Quadrilateral#Quadrilateral(Quadrilateral)
	 * @see   Quadrilateral#set(Point2D.Double,Point2D.Double,Point2D.Double,Point2D.Double)
	 */
	public Quadrilateral(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double p4)
	{
		set(p1,p2,p3,p4);
	}

	/**
	 * Constructs a <CODE>Quadrilateral</CODE> object as a copy of another <CODE>Quadrilateral</CODE> object.
	 * <P>
	 * This is the <B>copy constructor</B>.
	 * <P>
	 * Note that internally, the four specified points are referenced; this means that any
	 * changes to the specified points are reflected in the internal <CODE>Point2D.Double</CODE> objects.
	 *
	 * @param q the quadrilateral's 2D-datapoints to <B>shallow copy</B>
	 * @see   Quadrilateral#Quadrilateral()
	 * @see   Quadrilateral#Quadrilateral(Point2D.Double,Point2D.Double,Point2D.Double,Point2D.Double)
	 * @see   Quadrilateral#set(Quadrilateral)
	 */
	public Quadrilateral(Quadrilateral q)
	{
		set(q);
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets all four 2D-points of this <CODE>Quadrilateral</CODE> object equal to (0,0).
	 */
	public void clear()
	{
		fP1 = new Point2D.Double();
		fP2 = new Point2D.Double();
		fP3 = new Point2D.Double();
		fP4 = new Point2D.Double();
	}

	/**
	 * Sets all four 2D points of this <CODE>Quadrilateral</CODE> object equal to the specified points.
	 * <P>
	 * Note that internally, the four specified points are referenced; this means that any
	 * changes to the specified points are reflected in the internal <CODE>Point2D</CODE> objects.
	 *
	 * @param p1 the quadrilateral's first point (<B>shallow copy</B>)
	 * @param p2 the quadrilateral's second point (<B>shallow copy</B>)
	 * @param p3 the quadrilateral's third point (<B>shallow copy</B>)
	 * @param p4 the quadrilateral's fourth point (<B>shallow copy</B>)
	 * @see   Quadrilateral#set(Quadrilateral)
	 */
	public void set(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double p4)
	{
		fP1 = p1;
		fP2 = p2;
		fP3 = p3;
		fP4 = p4;
	}

	/**
	 * Sets all four 2D points of this <CODE>Quadrilateral</CODE> object equal to those of the specified <CODE>Quadrilateral</CODE> object.
	 * <P>
	 * Note that internally, the four specified points are referenced; this means that any
	 * changes to the specified points are reflected in the internal <CODE>Point2D.Double</CODE> objects.
	 *
	 * @param q the quadrilateral's 2D-data points to <B>shallow copy</B>
	 * @see   Quadrilateral#set(Point2D.Double,Point2D.Double,Point2D.Double,Point2D.Double)
	 */
	public void set(Quadrilateral q)
	{
		set(q.fP1,q.fP2,q.fP3,q.fP4);
	}

	/**
	 * Permutes all four 2D points of this <CODE>Quadrilateral</CODE> object counter clockwise.
	 * <P>
	 * After the operation, the 2D points have changed as follows:
	 * <P>
	 * <UL>
	 *   <LI>fP1 -> fP2</LI>
	 *   <LI>fP2 -> fP3</LI>
	 *   <LI>fP3 -> fP4</LI>
	 *   <LI>fP4 -> fP1</LI>
	 * </UL>
	 *
	 * @see Quadrilateral#permuteVertical()
	 */
	public void permuteCounterClockwise()
	{
		Point2D.Double dummy = fP1;
		fP1 = fP2;
		fP2 = fP3;
		fP3 = fP4;
		fP4 = dummy;
	}

	/**
	 * Permutes all four 2D points of this <CODE>Quadrilateral</CODE> object vertically.
	 * <P>
	 * After the operation, the 2D points have changed as follows:
	 * <P>
	 * <UL>
	 *   <LI>fP1 -> fP3</LI>
	 *   <LI>fP2 -> fP4</LI>
	 *   <LI>fP3 -> fP1</LI>
	 *   <LI>fP4 -> fP2</LI>
	 * </UL>
	 * <P>
	 * This corresponds to two consecutive counter clockwise permutations.
	 *
	 * @see Quadrilateral#permuteCounterClockwise()
	 */
	public void permuteVertical()
	{
		permuteCounterClockwise();
		permuteCounterClockwise();
	}

	/**
	 * Returns whether or not this <CODE>Quadrilateral</CODE> object is convex shaped.
	 * <P>
	 * Note: it is assumed that the X axis points to the right and the Y axis points upwards.
	 *
	 * @return <CODE>true</CODE> if this <CODE>Quadrilateral</CODE> object is convex shaped, <CODE>false</CODE> otherwise
	 * @see    Quadrilateral#isScreenConvex()
	 */
	public boolean isConvex()
	{
		if (!isLeftOn(fP1,fP2,fP3) || !isLeftOn(fP2,fP3,fP4) || !isLeftOn(fP3,fP4,fP1)
				|| !isLeftOn(fP4,fP1,fP2)) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Returns whether or not this <CODE>Quadrilateral</CODE> object is convex shaped <I>on the screen</I>.
	 * <P>
	 * Note: it is assumed that the X axis points to the right and the Y axis points <B>downwards</B>.
	 *
	 * @return <CODE>true</CODE> if this <CODE>Quadrilateral</CODE> object is convex shaped on the screen, <CODE>false</CODE> otherwise
	 * @see    Quadrilateral#isConvex()
	 */
	public boolean isScreenConvex()
	{
		if (isLeftOn(fP1,fP2,fP3) || isLeftOn(fP2,fP3,fP4) || isLeftOn(fP3,fP4,fP1)
				|| isLeftOn(fP4,fP1,fP2)) {
			return false;
		}
		else {
			return true;
		}
	}

	/*******************
	 * PRIVATE METHODS *
	 *******************/

	private boolean isLeftOn(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3)
	{
		return (computeTwiceTriangleArea(p1,p2,p3) >= 0.0);
	}

	private double computeTwiceTriangleArea(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3)
	{
		return (((p1.x * p2.y) - (p1.y * p2.x)) + ((p1.y * p3.x) - (p1.x * p3.y)) + ((p2.x * p3.y) - (p3.x * p2.y)));
	}
}
