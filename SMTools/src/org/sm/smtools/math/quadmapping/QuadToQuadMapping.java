// --------------------------------------
// Filename      : QuadToQuadMapping.java
// Author        : Sven Maerivoet
// Last modified : 09/12/2004
// Target        : Java VM (1.6)
// --------------------------------------

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

package org.sm.smtools.math.quadmapping;

import java.awt.geom.*;

/**
 * The <CODE>QuadToQuadMapping</CODE> class provides a perspective mapping between two convex quadrilaterals.
 * <P>
 * This class maps 2D-data points between two convex quadrilaterals:
 * <P>
 * <IMG src="doc-files/quad-quad-mapping.png" alt="">
 * <P>
 * Note: it is assumed that the X axis points to the right and the Y axis points upwards.
 * <P>
 * <B>Note that this class cannot be subclassed!</B>
 * 
 * @author  Sven Maerivoet
 * @version 09/12/2004
 * @see     Quadrilateral
 * @see     QuadMapping
 */
public final class QuadToQuadMapping
{
	// internal datastructures
	private QuadMapping fQuad1Mapping;
	private QuadMapping fQuad2Mapping;

	/****************
	 * CONSTRUCTORS *
	 ****************/

	/**
	 * Constructs a <CODE>QuadToQuadMapping</CODE> object based on the two perspective mappings between quadrilaterals.
	 *
	 * @param quad1Mapping  the first perspective mapping to use
	 * @param quad2Mapping  the second perspective mapping to use
	 * @see                 QuadToQuadMapping#QuadToQuadMapping(QuadMapping,QuadMapping)
	 * @see                 QuadToQuadMapping#set(QuadMapping,QuadMapping)
	 */
	public QuadToQuadMapping(QuadMapping quad1Mapping, QuadMapping quad2Mapping)
	{
		set(quad1Mapping,quad2Mapping);
	}

	/**
	 * Constructs a <CODE>QuadToQuadMapping</CODE> object based on the two specified quadrilaterals.
	 *
	 * @param q1  the first quadrilateral to use for the perspective mapping
	 * @param q2  the second quadrilateral to use for the perspective mapping
	 * @see       QuadToQuadMapping#QuadToQuadMapping(QuadMapping,QuadMapping)
	 * @see       QuadToQuadMapping#set(QuadMapping,QuadMapping)
	 */
	public QuadToQuadMapping(Quadrilateral q1, Quadrilateral q2)
	{
		set(new QuadMapping(q1),new QuadMapping(q2));
	}

	/******************
	 * PUBLIC METHODS *
	 ******************/

	/**
	 * Sets the perspective mapping based on the two perspective mappings between quadrilaterals.
	 *
	 * @param quad1Mapping  the first perspective mapping to use
	 * @param quad2Mapping  the second perspective mapping to use
	 */
	public void set(QuadMapping quad1Mapping, QuadMapping quad2Mapping)
	{
		fQuad1Mapping = quad1Mapping;
		fQuad2Mapping = quad2Mapping;
	}

	/**
	 * Maps a 2D-datapoint in the first quadrilateral to a 2D-data point in the second quadrilateral.
	 *
	 * @param p  the 2D-data point in the first quadrilateral
	 * @return   the corresponding 2D-data point in the second quadrilateral
	 */
	public Point2D.Double mapQuad1ToQuad2(Point2D.Double p)
	{
		return mapQuad1ToQuad2(p.x,p.y);
	}

	/**
	 * Maps a 2D-data point in the first quadrilateral to a 2D-data point in the second quadrilateral.
	 *
	 * @param x  the x coordinate of the 2D-data point in the first quadrilateral
	 * @param y  the y coordinate of the 2D-data point in the first quadrilateral
	 * @return   the corresponding 2D-data point in the second quadrilateral
	 */
	public Point2D.Double mapQuad1ToQuad2(double x, double y)
	{
		return fQuad2Mapping.mapSquareToQuadrilateral(fQuad1Mapping.mapQuadrilateralToSquare(x,y));
	}

	/**
	 * Maps a 2D-data point in the second quadrilateral to a 2D-data point in the first quadrilateral.
	 *
	 * @param p  the 2D-data point in the second quadrilateral
	 * @return   the corresponding 2D-data point in the first quadrilateral
	 */
	public Point2D.Double mapQuad2ToQuad1(Point2D.Double p)
	{
		return mapQuad2ToQuad1(p.x,p.y);
	}

	/**
	 * Maps a 2D-data point in the second quadrilateral to a 2D-data point in the first quadrilateral.
	 *
	 * @param x  the x coordinate of the 2D-data point in the second quadrilateral
	 * @param y  the y coordinate of the 2D-data point in the second quadrilateral
	 * @return   the corresponding 2D-data point in the first quadrilateral
	 */
	public Point2D.Double mapQuad2ToQuad1(double x, double y)
	{
		return fQuad1Mapping.mapSquareToQuadrilateral(fQuad2Mapping.mapQuadrilateralToSquare(x,y));
	}
}
