package toDoListProgram;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TaskTest {
	
	// Testing the createSubTask method
	@Test
	void createSubTask_ProperFormat_Pass() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		// act
		Task testSubTask = testTask.createSubTask("test", 3, "a test subtask", null);
		// assert
		assert testSubTask != null : "A Task object should have been returned.";
		assert testTask.getSubTasks().size() == 1 : "A Task should've been added to the subtasks list.";
		assert testSubTask.getLevel() == 2 : "The level of the subtask should be 2.";
	}
	
	@Test
	void createSubTask_ImproprerPriority_Fail() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		// act
		Task testSubTask = testTask.createSubTask("test", 6, "an improper subtask", null);
		// assert
		assert testSubTask == null : "An object should not have been returned.";
		assert testTask.getSubTasks().size() == 0 : "A task shouldn't have been added to the subtasks list.";
	}
	
	@Test
	void createSubTask_AboveMaxLevel_Fail() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		Task testSubTask = testTask.createSubTask("test", 1, "a test subtask at level 2", null);
		Task testSubTask2 = testSubTask.createSubTask("test", 3, "a test subtask at level 3", null);
		// act
		Task failedTask = testSubTask2.createSubTask("test", 3, "a failed subtask", null);
		// assert
		assert failedTask == null : "An object should not have been returned.";
		assert testSubTask2.getSubTasks().size() == 0 : "A task shouldn't have been added to the subtasks list.";
	}

	// Testing the removeSubTask method
	@Test
	void removeSubTask_ProperFormat_Removed() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		Task testSubTask = testTask.createSubTask("test", 3, "a test subtask", null);
		// act
		testTask.removeSubTask(testSubTask);
		// assert
		assert testTask.getSubTasks().size() == 0 : "No subtasks should exist.";
	}
	
	@Test
	void removeSubTask_null_NothingRemoved() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		Task testSubTask = testTask.createSubTask("test", 3, "a test subtask", null);
		// act
		testTask.removeSubTask(null);
		// assert
		assert testTask.getSubTasks().contains(testSubTask) : "Subtask shouldn't have been removed.";
	}
	
	@Test
	void removeSubTask_UnownedSubtask_NothingRemoved() {
		// arrange
		Task testTask1 = new Task("test1", 0, "a test task", null);
		Task testTask2 = new Task("test2", 0, "a test task", null);
		Task testSubTask1 = testTask1.createSubTask("test", 0, "a test subtask", null);
		Task testSubTask2 = testTask2.createSubTask("test", 0, "a test task", null);
		// act
		testTask1.removeSubTask(testSubTask2);
		// assert
		assert testTask1.getSubTasks().contains(testSubTask1) : "Subtask should not have been removed.";
		assert testTask2.getSubTasks().contains(testSubTask2) : "Subtask should not have been removed.";
	}

	void removeSubTask_ImproperLevel_NothingRemoved() {
		// arrange
		Task testTask = new Task("test", 0, "a test task", null);
		Task testSubTask = testTask.createSubTask("test", 3, "a test subtask", null);
		Task testSubTask2 = testSubTask.createSubTask("test", 0, "a test subtask", null);
		// act
		testTask.removeSubTask(testSubTask2);
		// assert
		assert testSubTask.getSubTasks().contains(testSubTask2) : "Subtask shouldn't have been removed";
		assert testTask.getSubTasks().contains(testSubTask) : "Subtask shouldn't have been removed";
	}
	
	// Testing the completeTask method
	@Test
	void completeTask_ManySubTasks_CompletesAll() {
		// arrange
		Task baseTask = new Task("test", 0, "a test task", null);
		Task subTask = baseTask.createSubTask("test", 0, "test", null);
		Task subTask2 = baseTask.createSubTask("test", 0, "test", null);
		Task subTask3 = subTask.createSubTask("test", 0, "test", null);
		// act
		baseTask.completeTask();
		// assert
		assert baseTask.getCompleted() == true : "All tasts should be set to complete";
		assert subTask.getCompleted() == true : "All tasts should be set to complete";
		assert subTask2.getCompleted() == true : "All tasts should be set to complete";
		assert subTask3.getCompleted() == true : "All tasts should be set to complete";
	}
	
	@Test
	void completeTask_ManySubTasks_CompletesSome() {
		// arrange
		Task baseTask = new Task("test", 0, "a test task", null);
		Task subTask = baseTask.createSubTask("test", 0, "test", null);
		Task subTask2 = baseTask.createSubTask("test", 0, "test", null);
		Task subTask3 = subTask.createSubTask("test", 0, "test", null);
		// act
		subTask.completeTask();
		// assert
		assert baseTask.getCompleted() == false : "Level 1 task should not be completed.";
		assert subTask.getCompleted() == true : "Subtask should be complete.";
		assert subTask2.getCompleted() == false : "Second subtask shouldn't be complete.";
		assert subTask3.getCompleted() == true : "Level 3 subtask should be complete.";
	}
	
	// Testing the undoCompleteTask method
	@Test
	void undoCompleteTask_ManySubTasks_UndoSome() {
		// arrange
		Task baseTask = new Task("test", 0, "a test task", null);
		Task subTask = baseTask.createSubTask("test", 0, "test", null);
		Task subTask2 = baseTask.createSubTask("test", 0, "test", null);
		Task subTask3 = subTask.createSubTask("test", 0, "test", null);
		baseTask.completeTask();
		// act
		subTask3.undoCompleteTask();
		// assert
		assert baseTask.getCompleted() == false : "Level 1 task should be incomplete";
		assert subTask.getCompleted() == false : "Should be incomplete.";
		assert subTask2.getCompleted() == true : "Shoudld be completed.";
		assert subTask3.getCompleted() == false : "Should be incomplete.";
	}
}
