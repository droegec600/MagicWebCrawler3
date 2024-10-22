package com.droegec.mwc;

public interface ProgressListener {

	/**
	 * Indica el progreso actual.

	 */
	public void setProgress(float percent, String status);
	
	/**
	 *
	 * @param unknown
	 */
	public void setUnknownProgress(boolean unknown, String status);
	
	
}
