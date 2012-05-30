/**
 * 
 */
package edu.jhu.coe.PCFGLA.smoothing;

import java.io.Serializable;

import edu.jhu.coe.PCFGLA.BinaryCounterTable;
import edu.jhu.coe.PCFGLA.UnaryCounterTable;


/**
 * @author leon
 *
 */
public class NoSmoothing implements Smoother, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see edu.jhu.coe.PCFGLA.smoothing.Smoother#smooth(edu.jhu.coe.util.UnaryCounterTable, edu.jhu.coe.util.BinaryCounterTable)
	 */
	public void smooth(UnaryCounterTable unaryCounter,
			BinaryCounterTable binaryCounter) {
		// perform no smoothing at all
	}

	/* (non-Javadoc)
	 * @see edu.jhu.coe.PCFGLA.smoothing.Smoother#smooth(short, float[])
	 */
	public void smooth(short tag, double[] ruleScores) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see edu.jhu.coe.PCFGLA.smoothing.Smoother#copy()
	 */
	public Smoother copy() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.jhu.coe.PCFGLA.smoothing.Smoother#updateWeights(int[][])
	 */
	public void updateWeights(int[][] toSubstateMapping) {
		// TODO Auto-generated method stub
		
	}
	
}
