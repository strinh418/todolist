package toDoListProgram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class User implements Serializable {
	private static final long serialVersionUID = 3318859846849304096L;
	private TaskList mainTaskList;
	private List<TaskList> createdTaskLists;
	private List<TaskList> archivedTaskLists;
	private TaskList allTasks;
	private String username;
	
	// Constructor for creating a user of the To Do List Program
	public User(String _username) {
		mainTaskList = new TaskList("Main To Do List");
		createdTaskLists = new ArrayList<TaskList>();
		archivedTaskLists = new ArrayList<TaskList>();
		allTasks = new TaskList("All Tasks");
		username = _username;
	}
	
	// Method to create a new task list (all lists must have a unique title)
	public TaskList createNewTaskList(String _title) {
		boolean nameExists = false;
		for(TaskList list : createdTaskLists) {
			if(list.getTitle().equals(_title)) {
				nameExists = true;
			}
		}
		if(_title.equals("Main To Do List")) {
			nameExists = true;
		}
		if(!nameExists) {
			TaskList list = new TaskList(_title);
			createdTaskLists.add(list);
			return list;
		}
		return null;
	}
	
	// Method to remove a task list from the createdTaskLists
	public void removeTaskList(TaskList _taskList) {
		if(createdTaskLists.contains(_taskList)) {
			for(Task task : _taskList.getCompleted()) {
				allTasks.remove(task);
			}
			for(Task task : _taskList.getIncomplete()) {
				allTasks.remove(task);
			}
			createdTaskLists.remove(_taskList);
		}
	}

	// Method to create a base task in any task list
	public Task createNewTask(TaskList _taskList, String _taskTitle, int _priority, String _description, Calendar _deadline) {
		Task task = _taskList.createNewTask(_taskTitle, _priority, _description, _deadline);
		allTasks.add(task);
		return task;
	}
	
	// Method to remove a task from any task list
	public void removeTask(TaskList _taskList, Task _task) {
		if(_taskList.remove(_task)) {
			allTasks.remove(_task);
		}
	}
	
	// Method to complete a task in one of the Task Lists
	public void completeTask(TaskList _taskList, Task _task) {
		if(_taskList.completeTask(_task)) {
			allTasks.completeTask(_task);
		}
	}
	
	// Method to undo a completed task in one of the Task Lists
	public void undoCompletedTask(TaskList _taskList, Task _task) {
		if(_taskList.undoCompletedTask(_task)) {
			allTasks.undoCompletedTask(_task);
		}
	}

	// Method to archive a task list in createdTaskLists
	public void archiveTaskList(TaskList _taskList) {
		if(createdTaskLists.contains(_taskList)) {
			for(Task task : _taskList.getCompleted()) {
				allTasks.remove(task);
			}
			for(Task task : _taskList.getIncomplete()) {
				allTasks.remove(task);
			}
			_taskList.archiveTaskList();
			createdTaskLists.remove(_taskList);
			archivedTaskLists.add(_taskList);
			
		}
	}
	
	// Method to unarchive a task list in archivedTaskLists
	public void unArchiveTaskList(TaskList _taskList) {
		if(archivedTaskLists.contains(_taskList)) {
			_taskList.unArchiveTaskList();
			createdTaskLists.add(_taskList);
			archivedTaskLists.remove(_taskList);
			for(Task task : _taskList.getCompleted()) {
				allTasks.add(task);
			}
			for(Task task : _taskList.getIncomplete()) {
				allTasks.add(task);
			}
		}
	}

	// Method to deserialize a user from a stream to a user
	public static User deserialize(InputStream stream) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(stream);
		try {
			return (User)ois.readObject();
		} finally {
			ois.close();
		}
	}
	
	// Method to serialize a user from a user to a byte array
	public static byte[] serialize(User object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.close();
		return baos.toByteArray();
	}
	
	// Getters and setters
	public List<TaskList> getCreatedTaskLists() {
		return createdTaskLists;
	}
	
	public TaskList getMainTaskList() {
		return mainTaskList;
	}
	public String getUsername() {
		return username;
	}

}
