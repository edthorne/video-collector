package edu.txstate.cs4398.vc.mobile;
/**
 * Class that holds the result of a given task.
 * It also holds whether this task failed or succeeded.
 * @author Rudolph Newball
 *
 * @param <T> the type of result the task will return
 */
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
	/**
	 * 
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * set the status of the task
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * 
	 * @return the task
	 */
	public String getTask() {
		return task;
	}
	/**
	 * set the task
	 * @param task
	 */
	public void setTask(String task) {
		this.task = task;
	}
	/**
	 * 
	 * @return the result
	 */
	public T getResult() {
		return result;
	}
	/**
	 * Sets the result
	 * @param result
	 */
	public void setResult(T result) {
		this.result = result;
	}

}
