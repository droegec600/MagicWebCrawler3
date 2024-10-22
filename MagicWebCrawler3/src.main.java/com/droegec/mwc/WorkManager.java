package com.droegec.mwc;

/**
 * Interfaz para el control del progreso del crawler.
 * Los métodos deben ser thread safe.
 * @author leandro
 *
 */
public interface WorkManager extends ProgressListener {

	/**
	 * @return true si quien llama debe seguir trabajando
	 */
	public boolean keepWorking();

	public void addLinkCount(int size);

	public void setOverallProgress(float f);
	
}
