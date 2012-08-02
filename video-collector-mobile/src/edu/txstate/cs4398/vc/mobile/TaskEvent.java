package edu.txstate.cs4398.vc.mobile;

public class TaskEvent<T> {
	private String task;
	private Status status;
	private T result;
	
	public enum Status{
		FAIL, SUCCESS
	}
	/**
	 * Constructor for Task object
	 * @param task the task being
	 */
	public TaskEvent(String task){
		this.task = task;
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
