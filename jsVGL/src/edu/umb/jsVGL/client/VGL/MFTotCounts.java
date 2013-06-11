package edu.umb.jsVGL.client.VGL;
/**
 * Brian White Summer 2008
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * @author Brian White
 * @version 1.0 $Id: MFTotCounts.java,v 1.2 2008-06-24 14:13:47 brian Exp $
 */

public class MFTotCounts {
	private int males;
	private int females;
	private int total;
	
	public MFTotCounts(int males, int females) {
		this.males = males;
		this.females = females;
		total = males + females;
	}

	public int getMales() {
		return males;
	}

	public int getFemales() {
		return females;
	}

	public int getTotal() {
		return total;
	}
	
	public MFTotCounts add(MFTotCounts x) {
		return new MFTotCounts(
				males + x.getMales(), 
				females + x.getFemales());
	}
	
	public String toString() {
		return "M: " + males + " F: " + females + " T: " + total;
	}
	
}
