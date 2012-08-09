package edu.txstate.cs4398.vc.mobile;
/**
 * Listener interface
 * @author Rudolph Newball
 *
 * @param <T> the type the task listening for
 */
public interface Listener<T>{
	public abstract void onEvent(TaskEvent<T> task);
}
