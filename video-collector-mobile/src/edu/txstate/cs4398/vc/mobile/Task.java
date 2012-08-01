package edu.txstate.cs4398.vc.mobile;

public class Task {
	private String taskType;
	private String taskResult;
	/**
	 * Constructor for Task object
	 * @param type the type of Task this is
	 */
	public Task(String type){
		taskType = type;
	}
	
	public String getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

}
