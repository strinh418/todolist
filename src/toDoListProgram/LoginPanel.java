package toDoListProgram;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {
	private static final DatabaseAccessor da = new DatabaseAccessor();
	
	private JLabel lblTitle;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JLabel lblNewUser;
	private JButton btnCreateAccount;
	private JLabel lblForgotPassword;
	private JButton btnResetPassword;
	
	private ToDoListGUI parentFrame; // Later change GUIRedone to ToDoListGUI
	private JPanel parentPanel;
	private ToDoListPanel afterLoginPanel;
	private JPanel createAccountPanel;
	private ForgotPasswordPanel manageAccountPanel;
	
	public LoginPanel(ToDoListGUI parentFrame) {
		// Initial set up (layout and name)
		setLayout(null);
		setName("login");
		setBounds(100, 100, 600, 350);
		JPanel frame = this; // need this for the JOptionPane
		
		// Assigning necessary frame and panels
		this.parentFrame = parentFrame;
		parentPanel = null;
		afterLoginPanel = null;
		createAccountPanel = null;
		manageAccountPanel = null;
		
		// Adding a title to the login panel
		lblTitle = new JLabel("Login");
		lblTitle.setBounds(0, 11, 588, 60);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle);
		
		// Adding the username label to the login panel
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblUsername.setBounds(112, 82, 99, 14);
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		add(lblUsername);
		
		// Adding the password label to the login panel
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPassword.setBounds(112, 130, 99, 14);
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setForeground(new Color(0, 0, 0));
		add(lblPassword);
		
		// Adding the text field for username input
		txtUsername = new JTextField();
		txtUsername.setBounds(221, 82, 250, 20);
		txtUsername.setText("");
		txtUsername.setColumns(10);
		add(txtUsername);
		
		// Adding the password field for password input
		txtPassword = new JPasswordField();
		txtPassword.setBounds(221, 129, 250, 20);
		add(txtPassword);
		
		// Adding the login button to the login panel
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnLogin.setBounds(244, 197, 90, 33);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(txtUsername.getText().equals("") || !da.usernameExists(txtUsername.getText())) {
						// a username isn't given or the username doesn't exist in the database
						JOptionPane.showMessageDialog(frame, "Incorrect username", "Unable to Login", JOptionPane.ERROR_MESSAGE);
					} else if(txtPassword.getPassword() == null || !da.checkUsernamePassword(txtUsername.getText(), txtPassword.getPassword())) {
						// a password isn't given or the password doesn't match the username's secured password
						JOptionPane.showMessageDialog(frame, "Incorret password", "Unable to Login", JOptionPane.ERROR_MESSAGE);
					} else {
						// successful login storing current user information
						parentFrame.setCurrentUsername(txtUsername.getText());
						parentFrame.setCurrentUser(da.getUserObject(txtUsername.getText()));
						if(parentFrame.getCurrentUser() == null) { // first time login
							parentFrame.setCurrentUser(new User(txtUsername.getText())); 
						} 
						// resetting all fields on the panel
						txtUsername.setText("");
						txtPassword.setText("");
						// setting up the new panel
						afterLoginPanel.initializePanel();
						// changing the panel to the main panel
						parentFrame.setTitle("To Do List - Welcome " + parentFrame.getCurrentUsername());
						parentFrame.setBounds(100, 100, 700, 500);
						((CardLayout) parentPanel.getLayout()).show(parentPanel, afterLoginPanel.getName());
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		add(btnLogin);
		
		// Adding the New User label to the login panel
		lblNewUser = new JLabel("New to the To Do List?");
		lblNewUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewUser.setForeground(new Color(0, 0, 255));
		lblNewUser.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		lblNewUser.setBounds(112, 254, 155, 14);
		add(lblNewUser);
		
		// Adding the create account button to the login panel
		btnCreateAccount = new JButton("Create new account");
		btnCreateAccount.setBounds(112, 266, 155, 23);
		btnCreateAccount.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				// Resetting all fields on this panel
				txtUsername.setText("");
				txtPassword.setText("");
				// Changing to the create account panel
				parentFrame.setTitle("To Do List - Create an Account");
				parentFrame.setBounds(100, 100, 600, 400);
				((CardLayout) parentPanel.getLayout()).show(parentPanel, createAccountPanel.getName());
			}
		});
		add(btnCreateAccount);
		
		// Adding the Forgot Password label to the login panel
		lblForgotPassword = new JLabel("Forgot Password?");
		lblForgotPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblForgotPassword.setForeground(Color.BLUE);
		lblForgotPassword.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		lblForgotPassword.setBounds(356, 254, 127, 14);
		add(lblForgotPassword);
		
		// Adding the Reset Password button to the login panel
		btnResetPassword = new JButton("Reset Password");
		btnResetPassword.setBounds(356, 266, 127, 23);
		btnResetPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				// Trying to get the username of the account
				String input = JOptionPane.showInputDialog(frame, "Type the username of the account you would like to retrieve.");
				try {
					if(input == null || !da.usernameExists(input)) {
						// Unable to validate the username
						JOptionPane.showMessageDialog(frame, "Unable to find username in the system.", "Unable to retrieve.", JOptionPane.ERROR_MESSAGE);
					} else {
						parentFrame.setCurrentUsername(input);
						// Resetting all fields on the current panel
						txtPassword.setText("");
						txtUsername.setText("");
						// Changing to the manage account panel
						parentFrame.setTitle("To Do List - Forgot Password");
						manageAccountPanel.setSecurityQuestionText(da.getSecurityQuestion(parentFrame.getCurrentUsername()));
						((CardLayout) parentPanel.getLayout()).show(parentPanel, manageAccountPanel.getName());
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		add(btnResetPassword);
	}

	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	
	public void setAfterLoginPanel(ToDoListPanel afterLoginPanel) {
		this.afterLoginPanel = afterLoginPanel;
	}
	
	public void setCreateAccountPanel(JPanel createAccountPanel) {
		this.createAccountPanel = createAccountPanel;
	}
	
	public void setManageAccountPanel(ForgotPasswordPanel manageAccountPanel) {
		this.manageAccountPanel = manageAccountPanel;
	}

}
