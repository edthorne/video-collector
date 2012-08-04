package edu.txstate.cs4398.vc.desktop.model;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import edu.txstate.cs4398.vc.desktop.utils.NetworkUtils;
import edu.txstate.cs4398.vc.model.AbstractModel;
import edu.txstate.cs4398.vc.model.Collection;
import edu.txstate.cs4398.vc.model.ModelEvent;
import edu.txstate.cs4398.vc.model.ModelListener;

/**
 * The desktop application model.
 * 
 * @author Ed
 */
@XmlRootElement(name = "videoCollectorConfig")
public class CollectorModel extends AbstractModel implements ModelListener {
	private static final int PROPERTY_CHANGED = 1;
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
	 * Creates a new application model.
	 */
	public CollectorModel() {
		// start with a fresh collection
		this.collection = new Collection();
	}

	/**
	 * @return the collection
	 */
	@XmlElement
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(Collection collection) {
		if (this.collection != null) {
			// stop listening to the old collection
			this.collection.removeModelListener(this);
		}
		this.collection = collection;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "collection"));
		this.collection.addModelListener(this);
	}

	/**
	 * @return the dirty
	 */
	@XmlTransient
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "dirty"));
	}

	/**
	 * @return the file
	 */
	@XmlTransient
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
		notifyChanged(new ModelEvent(this, PROPERTY_CHANGED, "file"));
	}

	/**
	 * @return the IP address of the computer
	 */
	@XmlTransient
	public String getIPAddress() {
		return NetworkUtils.getIPAddress();
	}

	@Override
	public void modelChanged(ModelEvent event) {
		// we're listening for any changes to the model that would cause it to
		// be dirty and setting the model state accordingly
		setDirty(true);
	}
}
