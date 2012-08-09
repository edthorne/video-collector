package edu.txstate.cs4398.vc.mobile;

import android.os.AsyncTask;
/**
 * Abstract parent class to all other tasks
 * @author Rudolph Newball
 *
 * @param <Params> The type of the parameter passed in to the override function doInBackground()
 * @param <Progress> The return type of the override function onPostExecute() 
 * @param <Result> The return type of the override function doInBackground()
 */
public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	protected static final String NAMESPACE = "http://services.desktop.vc.cs4398.txstate.edu/";
	protected EventHandler event;

	/**
	 * Constructor that takes a listener and adds it to the EventHandler pool
	 * @param listener listener to be notified of any changes made in task
	 */
	protected BaseTask(Listener listener){
		event = new EventHandler();
		event.addEventListener(listener);
	}
}
