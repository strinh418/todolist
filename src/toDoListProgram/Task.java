package toDoListProgram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Task implements Serializable {
	private String taskTitle;
	private int priority;
	private String description;
	private boolean completed;
	private List<Task> subTasks;
	private int level;
	private Calendar deadline;
	private Calendar dateCreated;
	private Calendar dateCompleted;
	private Task parentTask;
	
	// Constructor for creating a base level 1 task
	public Task(String _taskTitle, int _priority, String _description, Calendar _deadline) {
		this(_taskTitle, _priority, _description, _deadline, null);
	}
	
	// Constructor for creating a SubTask
	public Task(String _taskTitle, int _priority, String _description, Calendar _deadline, Task _parentTask) {
		taskTitle = _taskTitle;
		priority = _priority;
		description = _description;
		completed = false;
		subTasks = new ArrayList<Task>();
		deadline = _deadline;
		dateCreated = Calendar.getInstance();
		dateCompleted = null;
		parentTask = _parentTask;
		if(_parentTask == null) {
			level = 1;
		} else {
			level = _parentTask.level + 1;
		}
	}
	
	// Method to create a subtask for this Task, then returns the Task object created (if it was created)
	public Task createSubTask(String _taskTitle, int _priority, String _description, Calendar _deadline) {
		if(level >= 3 || _priority < 0 || _priority > 5) {
			return null; // no Task is returned when unable to create the SubTask
		}
		Task subTask = new Task(_taskTitle, _priority, _description, _deadline, this);
		subTasks.add(subTask);
		return subTask;
	}
	
	// Method to remove a subtask from this Task's subTasks list
	public void removeSubTask(Task _subTask) {
		subTasks.remove(_subTask);
	}
	
	// Method to set a Task, and if applicable all its subTasks, to completed status
	public void completeTask() {
		for(Task task : subTasks) {
			task.completeTask();
		}
		completed = true;
		dateCompleted = Calendar.getInstance();
	}
	
	// Method to set a Task, and if applicable all its parent task, to an incomplete status
	public void undoCompleteTask() {
		completed = false;
		dateCompleted = null;
		if(parentTask != null) {
			parentTask.undoCompleteTask();
		}
	}
	
	// Override toString method
	public String toString() {
		String info = taskTitle + ": ";
		if(completed) {
			info += "Completed on " + (dateCompleted.get(Calendar.MONTH)+1) + "/" + dateCompleted.get(Calendar.DATE)
					+ "/" + dateCompleted.get(Calendar.YEAR);
		} else {
			info += "incomplete|Priority: " + priority + "|Subtasks: " + subTasks.size() + "|Date Created: "
					+ (dateCreated.get(Calendar.MONTH)+1) + "/" + dateCreated.get(Calendar.DATE) + "/" + 
					dateCreated.get(Calendar.YEAR);
			if(deadline != null) {
				info += "|Deadline: " + (deadline.get(Calendar.MONTH)+1) + "/" + deadline.get(Calendar.DATE) + "/"
						+ deadline.get(Calendar.YEAR);
			}
		}
		return info;
		
	}
	
	// Getters and Setters
	public void setTaskTitle(String _taskTitle) {
		taskTitle = _taskTitle;
	}
	public String getTaskTitle() {
		return taskTitle;
	}

	public void setPriority(int _priority) {
		priority = _priority;
	}
	public int getPriority() {
		return priority;
	}

	public void setDescription(String _description) {
		description = _description;
	}
	public String getDescription() {
		return description;
	}

	public boolean getCompleted() {
		return completed;
	}
	
	public List<Task> getSubTasks(){
		return subTasks;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setDeadline(Calendar _deadline) {
		deadline = _deadline;
	}
	public Calendar getDeadline() {
		return deadline;
	}
	
	public Calendar getDateCreated() {
		return dateCreated;
	}

	public Calendar getDateCompleted() {
		return dateCompleted;
	}
}
