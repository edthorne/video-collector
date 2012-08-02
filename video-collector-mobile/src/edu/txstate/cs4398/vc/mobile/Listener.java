package edu.txstate.cs4398.vc.mobile;

public interface Listener<T>{
	public abstract void onEvent(TaskEvent<T> task);
}
