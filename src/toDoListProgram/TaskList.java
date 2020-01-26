package toDoListProgram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskList implements Serializable {
	private List<Task> completed;
	private List<Task> incomplete;
	private boolean archived;
	private String title;
	private Calendar dateCreated;
	private Calendar dateArchived;
	private String orderedBy; // can be by priority, deadline, dateCreated, or alphabetical
	
	// Constructor for a TaskList
	public TaskList(String _title) {
		completed = new ArrayList<Task>();
		incomplete = new ArrayList<Task>();
		archived = false;
		title = _title;
		dateCreated = Calendar.getInstance();
		dateArchived = null;
		orderedBy = "dateCreated";
	}
	
	// Method to create a new base Task in this list; returns the Task if one was created
	public Task createNewTask(String _taskTitle, int _priority, String _description, Calendar _deadline) {
		if(archived || _priority > 5 || _priority < 0) {
			return null;
		}
		Task task = new Task(_taskTitle, _priority, _description, _deadline);
		incomplete.add(task);
		return task;
	}
	
	// Method to add an existing task into the Task List
	public void add(Task _task) {
		if(!archived) {
			if(_task.getCompleted()) {
				completed.add(_task);
			} else {
				incomplete.add(_task);
			}
		}
	}
	
	// Method to remove an existing task from a Task List
	public boolean remove(Task _task) {
		if(!archived) {
			completed.remove(_task);
			incomplete.remove(_task);
			return true;
		}
		return false;
	}

	// Method to archive a task list
 	public void archiveTaskList(){
		archived = true;
		dateArchived = Calendar.getInstance();
	}
	
 	// Method to unarchive a task list
 	public void unArchiveTaskList() {
 		archived = false;
 		dateArchived = null;
 	}

 	// Method to complete a task in the incomplete task list
	public boolean completeTask(Task _task) {
		if(!archived && incomplete.remove(_task)) {
			_task.completeTask();
			completed.add(_task);
			return true;
		}
		return false;
	}
	
	// Method to undo a completed task in the completed task list
	public boolean undoCompletedTask(Task _task) {
		if(!archived && completed.remove(_task)) {
			_task.undoCompleteTask();
			incomplete.add(_task);
			return true;
		}
		return false;
	}
	
	// Method to order the incomplete task list by priority
	public void orderByPriority() {
		for(Task task : incomplete) {
			if(task.getPriority() == 1) {
				incomplete.remove(task);
				incomplete.add(task);
			}
		}
		for(Task task : incomplete) {
			if(task.getPriority() == 2) {
				incomplete.remove(task);
				incomplete.add(task);
			}
		}
		for(Task task : incomplete) {
			if(task.getPriority() == 3) {
				incomplete.remove(task);
				incomplete.add(task);
			}
		}
	}
	
	public String toString() {
		String info = title + ": ";
		if(archived) {
			info += "Archived on " + (dateArchived.get(Calendar.MONTH)+1) + "/" + dateArchived.get(Calendar.DATE)
					+ "/" + dateArchived.get(Calendar.YEAR);
		} else {
			info += "Created on " + (dateCreated.get(Calendar.MONTH)+1) + "/" + dateCreated.get(Calendar.DATE)
			+ "/" + dateCreated.get(Calendar.YEAR) + "|Completed: " + completed.size() + "|Incomplete: " +
					incomplete.size() + "|Ordered by: " + orderedBy;
		}
		return info;
	}
	
	// Getters and Setters
	public List<Task> getCompleted(){
		return completed;
	}
	
	public List<Task> getIncomplete(){
		return incomplete;
	}
	
	public boolean getArchived() {
		return archived;
	}
	
	public void setTitle(String _title) {
		title = _title;
	}
	public String getTitle() {
		return title;
	}
	
	public Calendar getDateCreated() {
		return dateCreated;
	}
	
	public Calendar getDateArchived() {
		return dateArchived;
	}
	
	public String getOrderedBy() {
		return orderedBy;
	}
}

