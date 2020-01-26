package toDoListProgram;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ToDoListGUI extends JFrame {

	private JPanel contentPane;
	private String currentUsername;
	private User currentUser;
	private LoginPanel login;
	private CreateAccountPanel createAccount;
	private ToDoListPanel toDoList;
	private ForgotPasswordPanel forgotPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToDoListGUI frame = new ToDoListGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ToDoListGUI() {
		setResizable(false);
		currentUsername = "";
		currentUser = null;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new CardLayout(0, 0));
		setContentPane(contentPane);
		
		// Creating all the card frames
		login = new LoginPanel(this);
		createAccount = new CreateAccountPanel(this);
		forgotPassword = new ForgotPasswordPanel(this);
		toDoList = new ToDoListPanel(this);
		
		// Displaying the login panel
		setTitle("To Do List - Login");
		setBounds(100, 100, 600, 350); // starting bounds 
		contentPane.add("login", login);
		
		// Setting up the create account panel
		contentPane.add("createAccount", createAccount);
		
		// Setting up the forgot password panel
		contentPane.add("forgotPassword", forgotPassword);
		
		// Setting up the to do list panel
		contentPane.add("toDoList", toDoList);
		
		// Assigning completed panels to panel fields
		login.setParentPanel(contentPane);
		login.setAfterLoginPanel(toDoList);
		login.setCreateAccountPanel(createAccount);
		login.setManageAccountPanel(forgotPassword);
		
		createAccount.setParentPanel(contentPane);
		createAccount.setPreviousPanel(login);
		
		forgotPassword.setParentPanel(contentPane);
		forgotPassword.setPreviousPanel(login);
		
		toDoList.setParentPanel(contentPane);
		toDoList.setAfterLogoutPanel(login);
	}
	
	public void setCurrentUsername(String username) {
		currentUsername = username;
	}
	public String getCurrentUsername() {
		return currentUsername;
	}
	public void setCurrentUser(User user) {
		currentUser = user;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	

}
