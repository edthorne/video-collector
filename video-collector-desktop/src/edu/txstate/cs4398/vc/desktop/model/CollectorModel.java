package edu.txstate.cs4398.vc.desktop.model;

import java.io.File;

import edu.txstate.cs4398.vc.model.AbstractModel;
import edu.txstate.cs4398.vc.model.Collection;

/**
 * The desktop application model.
 * 
 * @author Ed
 */
public class CollectorModel extends AbstractModel {
	/**
	 * The video collection held by the GUI.
	 */
	private Collection collection;
	/**
	 * A flag to indicate if the collection is dirty.
	 */
	private boolean dirty;
	/**
	 * The file where the collection is stored.
	 */
	private File file;

	/**
	 * @return the collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
}
