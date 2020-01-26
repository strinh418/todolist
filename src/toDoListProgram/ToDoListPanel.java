package toDoListProgram;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class ToDoListPanel extends JPanel {
	private final static DatabaseAccessor da = new DatabaseAccessor();
	
	private ToDoListGUI parentFrame;
	private JPanel parentPanel;
	private JPanel afterLogoutPanel;
	
	private JLabel lblTitle;
	private JLabel lblTaskLists;
	private JButton btnAddTaskList;
	private JScrollPane scrollPaneLists;
	private JList<String> listTaskLists;
	private JButton btnLogout;
	private JLabel lblTaskTitle;
	private JButton btnDeleteTaskList;
	private JTable tblTasks;
	private JScrollPane scrollPaneTasks;
	
	private User currentUser;
	private DefaultListModel<String> taskListNames;
	private String listListener;
	private List<DefaultTableModel> tasksArrays;
	private TaskList currentTaskList;
	private int currentTaskListIndex;

	
	public ToDoListPanel(ToDoListGUI parentFrame) {
		// Initial set up (layout and name)
			setLayout(null);
			setName("toDoList");
			setBounds(100, 100, 700, 500);
			JPanel frame = this; // need this for the JOptionPane
			taskListNames = new DefaultListModel<String>();
			listListener = "select";
			tasksArrays = new ArrayList<DefaultTableModel>();
			currentTaskList = null;
			
			// Assigning necessary frame and panels
			this.parentFrame = parentFrame;
			parentPanel = null;
			afterLogoutPanel = null;
			
			// Adding the title label
			lblTitle = new JLabel("");
			lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle.setBounds(10, 11, 680, 29);
			add(lblTitle);
			
			// Adding the Your Task Lists label
			lblTaskLists = new JLabel("Your Task Lists");
			lblTaskLists.setHorizontalAlignment(SwingConstants.CENTER);
			lblTaskLists.setFont(new Font("Times New Roman", Font.PLAIN, 13));
			lblTaskLists.setBounds(10, 51, 91, 26);
			add(lblTaskLists);
			
			// Setting up the scroll pane and list for displaying the task lists
			scrollPaneLists = new JScrollPane();
			scrollPaneLists.setBounds(10, 75, 177, 359);
			add(scrollPaneLists);
			listTaskLists = new JList<String>(taskListNames); 
			listTaskLists.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			listTaskLists.addListSelectionListener(new ListSelectionListener() {
			      public void valueChanged(ListSelectionEvent evt) {
			        if (evt.getValueIsAdjusting()) {
			        	if(listListener.equals("select")) {
			        		selectTaskList();
			        	} else if(listListener.equals("delete")) {
			        		listListener = "select";
			        		listTaskLists.setForeground(new Color(0,0,0));
			        		if(listTaskLists.getSelectedIndex() == 0) {
			        			listListener = "select";
			        			listTaskLists.setSelectedValue(null, false);
			        			JOptionPane.showMessageDialog(frame, "You cannot delete your main task list.", "Unable to delete task list", JOptionPane.ERROR_MESSAGE);
			        		} else {
			        			int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task list?", "Confirm", 2);
			        			if(choice == 0) {
			        				listListener = "select";
				        			TaskList deletedTaskList = currentUser.getCreatedTaskLists().get(listTaskLists.getSelectedIndex()-1);
				        			selectTaskList(); // just testing this... better if i could not have to use this
				        			lblTaskTitle.setText("Select a list to view tasks.");
				        			tblTasks.setModel(new DefaultTableModel());
				        			listTaskLists.setSelectedValue(null, false);
					        		currentUser.removeTaskList(deletedTaskList);
					        		updateTaskListNamesAndArrays(deletedTaskList);
					        		currentTaskList = null;
			        			}
			        		}
			        		listTaskLists.setSelectedValue(null, false);
			        	}
			        }   
			      }
			    });
			scrollPaneLists.setViewportView(listTaskLists);			
			
			// Adding the add task list button
			btnAddTaskList = new JButton("+");
			btnAddTaskList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String title = JOptionPane.showInputDialog(frame, "Title of your new task list");
					if(title != null && title.equals("")) {
						JOptionPane.showMessageDialog(frame, "Please provide a title for your list", "Unable to create task list", JOptionPane.ERROR_MESSAGE);
					} else if(title != null) {
						TaskList list = currentUser.createNewTaskList(title);
						if(list == null) {
							JOptionPane.showMessageDialog(frame, "Make sure your task list has a unique title", "Unable to create task list", JOptionPane.ERROR_MESSAGE);
						} else {
							updateTaskListNamesAndArrays(list);
						}
					}
				}
			});
			btnAddTaskList.setBounds(99, 51, 41, 26);
			add(btnAddTaskList);
			
			// Adding the delete task list button
			btnDeleteTaskList = new JButton("-");
			btnDeleteTaskList.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(listListener.equals("delete")) {
						listListener = "select";
						listTaskLists.setForeground(new Color(0,0,0));
					} else {
						listListener = "delete";
						listTaskLists.setForeground(Color.RED);
						listTaskLists.setSelectedValue(null, false);
					}
				}
			});
			btnDeleteTaskList.setFont(new Font("Times New Roman", Font.BOLD, 12));
			btnDeleteTaskList.setBounds(139, 51, 48, 26);
			add(btnDeleteTaskList);
			
			// Adding the logout button
			btnLogout = new JButton("Logout");
			btnLogout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						da.setUserObj(parentFrame.getCurrentUsername(), User.serialize(currentUser));
						
						// Resetting the fields
						resetFields();
						parentFrame.setCurrentUser(null);
						parentFrame.setCurrentUsername("");
						// Changing panels
						parentFrame.setTitle("To Do List - Login");
						parentFrame.setBounds(100, 100, 600, 350);
						((CardLayout) parentPanel.getLayout()).show(parentPanel, afterLogoutPanel.getName());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			});
			btnLogout.setBounds(10, 430, 656, 23);
			add(btnLogout);
			
			// Adding the task title label
			lblTaskTitle = new JLabel("Select a list to view tasks.");
			lblTaskTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
			lblTaskTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblTaskTitle.setBounds(197, 51, 378, 39);
			add(lblTaskTitle);
			
			// Setting up the scroll pane and table for displaying tasks
			scrollPaneTasks = new JScrollPane();
			scrollPaneTasks.setBounds(197, 89, 470, 330);
			add(scrollPaneTasks);
			tblTasks = new JTable();
			tblTasks.setBounds(197, 89, 470, 330);
			scrollPaneTasks.setViewportView(tblTasks);
			
			// Adding the add task button
			JButton btnAddTask = new JButton("Add Task");
			btnAddTask.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(currentTaskList == null) {
						JOptionPane.showMessageDialog(frame, "Please select as task list to put the task in.", "Unable to add task", JOptionPane.ERROR_MESSAGE);
					} else if(!listListener.equals("delete"))  {
						String taskTitle = JOptionPane.showInputDialog(frame, "Type the title of your new task");
						if((taskTitle != null && taskTitle.equals(""))) {
							JOptionPane.showMessageDialog(frame, "Please type a name for your task", "Unable to add task", JOptionPane.ERROR_MESSAGE);
						} else if(taskTitle != null) {
							tasksArrays.get(currentTaskListIndex).addRow(new Object[] {new JCheckBox(), taskTitle, "", new JButton("Delete")});
							currentUser.createNewTask(currentTaskList, taskTitle, 0, "", null);
						}
					}
				}
			});
			btnAddTask.setFont(new Font("Times New Roman", Font.BOLD, 12));
			btnAddTask.setBounds(574, 51, 91, 39);
			add(btnAddTask);
			
			
	}
	public void resetFields() {
		taskListNames.clear();
		lblTitle.setText("");
		lblTaskTitle.setText("Select a list to view tasks.");
		tasksArrays.clear();
		tblTasks.setModel(new DefaultTableModel());
	}
	
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	
	public void setAfterLogoutPanel(JPanel afterLogoutPanel) {
		this.afterLogoutPanel = afterLogoutPanel;
	}
	
	public void initializeTaskListNames() {
		if(currentUser != null) {
			taskListNames.addElement(currentUser.getMainTaskList().getTitle());
			for(TaskList list : currentUser.getCreatedTaskLists()) {
				taskListNames.addElement(list.getTitle());
			}
		}
	}
	public void updateTaskListNamesAndArrays(TaskList editedTaskList) {
		if(taskListNames.contains(editedTaskList.getTitle())) {
			taskListNames.removeElement(editedTaskList.getTitle());
			tasksArrays.remove(currentTaskListIndex);
		} else {
			taskListNames.addElement(editedTaskList.getTitle());
			tasksArrays.add(new DefaultTableModel(new Object[0][4], new String[] {"Done", "Task", "Priority", "Delete"}));
		}
	}
	
	
	public String visualizePriority(int priority) {
		String exclamations = "";
		for(int i=0; i<priority; i++) {
			exclamations += "!";
		}
		return exclamations;
	}
	
	public void initializeTasksArrays() {
		if(currentUser != null) {
			// Setting up the tasksarray for the Main To Do List
			DefaultTableModel mainToDoListTable = new DefaultTableModel(new Object[0][4], new String[] {"Done", "Task", "Priority", "Delete"});
			tasksArrays.add(mainToDoListTable);
			for(Task task : currentUser.getMainTaskList().getIncomplete()) {
				mainToDoListTable.addRow(new Object[] {new JCheckBox(), task.getTaskTitle(), visualizePriority(task.getPriority()), "Delete"});
			}
			for(Task task : currentUser.getMainTaskList().getCompleted()) {
				mainToDoListTable.addRow(new Object[] {new JCheckBox("", true), task.getTaskTitle(), visualizePriority(task.getPriority()), "Delete"});
			}
			// Setting up the tasksarray for the rest of the lists
			for(TaskList list : currentUser.getCreatedTaskLists()) {
				DefaultTableModel table = new DefaultTableModel(new Object[0][4], new String[] {"Done", "Task", "Priority", "Delete"});
				tasksArrays.add(table);
				for(Task task : list.getIncomplete()) {
					table.addRow(new Object[] {new JCheckBox(), task.getTaskTitle(), visualizePriority(task.getPriority()),  "Delete"});
				}
				for(Task task : currentUser.getMainTaskList().getCompleted()) {
					table.addRow(new Object[] {new JCheckBox("", true), task.getTaskTitle(), visualizePriority(task.getPriority()), "Delete"});
				}
			}
		}
	}
	
	public void initializePanel() throws Exception {
		currentUser = parentFrame.getCurrentUser();
		initializeTaskListNames();
		lblTitle.setText("Hello " + da.getFirstName(parentFrame.getCurrentUsername()) + "! Let's get stuff done!");
		listTaskLists.setForeground(new Color(0,0,0));
		initializeTasksArrays();
	}

	public void selectTaskList() {
		if(listTaskLists.getSelectedValue() != null) {
			lblTaskTitle.setText(listTaskLists.getSelectedValue());
			tblTasks.setModel(tasksArrays.get(listTaskLists.getSelectedIndex()));
			if(listTaskLists.getSelectedIndex() == 0) {
				currentTaskList = currentUser.getMainTaskList();
			} else {
				currentTaskList = currentUser.getCreatedTaskLists().get(listTaskLists.getSelectedIndex()-1);
			}
			currentTaskListIndex = listTaskLists.getSelectedIndex();
			tblTasks.getColumnModel().getColumn(0).setPreferredWidth(38);
			tblTasks.getColumnModel().getColumn(1).setPreferredWidth(327);
			tblTasks.getColumnModel().getColumn(2).setPreferredWidth(45);
			tblTasks.getColumnModel().getColumn(3).setPreferredWidth(50);
		}
	}
}
