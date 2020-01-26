package toDoListProgram;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JList;

public class ToDoListGUIOld extends JFrame {

	private static final DatabaseAccessor da = new DatabaseAccessor();
	private String currentUsername;
	private User currentUser;
	private JPanel contentPane;
	private JPanel login;
	private JPanel createAccount;
	private JPanel toDoList;
	private JPanel updateAccount;
	private JPanel forgotPassword;
	private JPanel inputUsername;
	private JPanel answerSecurity;
	private JLabel lblLoginTitle;
	private JTextField txtUsername;
	private JPasswordField pwField;
	private JLabel lblCreateAccount;
	private JLabel lblChooseUsername;
	private JTextField txtChooseUsername;
	private JLabel lblChoosePassword;
	private JPasswordField pwFieldChoose;
	private JButton btnCreateAccount;
	private JLabel lblConfirmPassword;
	private JPasswordField pwFieldConfirm;
	private JLabel lblFirstName;
	private JTextField txtFirstName;
	private JLabel lblSecurityQuestion;
	private JTextField txtQuestion;
	private JLabel lblResponse;
	private JTextField txtResponse;
	private JTextField txtLastName;
	private final JButton btnCancel = new JButton("Cancel");
	private JLabel lblPasswordInfo;
	private JLabel lblRetrieveTitle;
	private JLabel lblUsernameRetrieve;
	private JTextField txtUsernameRetrieve;
	private JButton btnVerifyAccount;
	private JTextField txtSecurityResponse;
	private JLabel lblNewPassword;
	private JLabel lblConfirmNewPassword;
	private JPasswordField pwFieldNewPassword;
	private JPasswordField pwFieldConfirmNewPassword;
	private JButton btnCancelUsernameRetrieve;
	private JButton btnCancelChangePassword;
	private JList listTaskLists;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToDoListGUIOld frame = new ToDoListGUIOld();
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
	public ToDoListGUIOld() {
		currentUsername = "";
		currentUser = null;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		// Creating all card frames: 
		login = new JPanel();
		createAccount = new JPanel();
		toDoList = new JPanel();
		updateAccount = new JPanel();
		forgotPassword = new JPanel();
		CardLayout cl = (CardLayout) contentPane.getLayout();
		
		// Creating the login frame
		setTitle("To Do List - Login");
		/*setBounds(100, 100, 700, 500);*/ setBounds(100, 100, 600, 350);
		contentPane.add("login", login);
		login.setLayout(null);
		
		lblLoginTitle = new JLabel("Login");
		lblLoginTitle.setBounds(0, 11, 588, 60);
		lblLoginTitle.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblLoginTitle.setHorizontalAlignment(SwingConstants.CENTER);
		login.add(lblLoginTitle);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblUsername.setBounds(112, 82, 99, 14);
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		login.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblPassword.setBounds(112, 130, 99, 14);
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setForeground(new Color(0, 0, 0));
		login.add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(221, 82, 250, 20);
		txtUsername.setText("");
		txtUsername.setColumns(10);
		login.add(txtUsername);
		
		pwField = new JPasswordField();
		pwField.setBounds(221, 129, 250, 20);
		login.add(pwField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() { // Login button pressed on the login page
			public void actionPerformed(ActionEvent e) {
				try {
					if(txtUsername.getText().equals("") || !da.usernameExists(txtUsername.getText())) {
						// a username isn't given or the username doesn't exist in the database
						JOptionPane.showMessageDialog(login, "Incorrect username.");
					} else if(pwField.getPassword() == null || !da.checkUsernamePassword(txtUsername.getText(), pwField.getPassword())) {
						// a password isn't given or the password doesn't match the username's secured password
						JOptionPane.showMessageDialog(login, "Incorrect password.");
					} else {
						// successful login and need to reset the frame
						currentUsername = txtUsername.getText();
						txtUsername.setText("");
						pwField.setText("");
						currentUser = da.getUserObject(currentUsername);
						if(currentUser == null) { // first time login
							currentUser = new User(currentUsername);
						} 
						setBounds(100, 100, 700, 500);
						cl.show(contentPane, "toDoList");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnLogin.setBounds(244, 197, 90, 33);
		login.add(btnLogin);
		
		JLabel lblNewUser = new JLabel("New to the To Do List?");
		lblNewUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewUser.setForeground(new Color(0, 0, 255));
		lblNewUser.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		lblNewUser.setBounds(112, 254, 155, 14);
		login.add(lblNewUser);
		
		JButton btnCreateNewAccount = new JButton("Create new account");
		btnCreateNewAccount.addActionListener(new ActionListener() { // Create account button pressed on the login page
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText("");
				pwField.setText("");
				setTitle("To Do List - Create an Account");
				setBounds(100, 100, 600, 400);
				cl.show(contentPane, "createAccount");
			}
		});
		btnCreateNewAccount.setBounds(112, 266, 155, 23);
		login.add(btnCreateNewAccount);
		
		JLabel lblForgotPassword = new JLabel("Forgot Password?");
		lblForgotPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblForgotPassword.setForeground(Color.BLUE);
		lblForgotPassword.setFont(new Font("Times New Roman", Font.ITALIC, 12));
		lblForgotPassword.setBounds(356, 254, 127, 14);
		login.add(lblForgotPassword);
		
		JButton btnResetPassword = new JButton("Reset Password");
		btnResetPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // Reset password button pressed on login frame
				txtUsername.setText("");
				pwField.setText("");
				setTitle("To Do List - Forgot Password");
				cl.show(contentPane, "forgotPassword");
			}
		});
		btnResetPassword.setBounds(356, 266, 127, 23);
		login.add(btnResetPassword);
		
		// Creating the createAccount frame
		contentPane.add("createAccount", createAccount);
		createAccount.setLayout(null);
		
		lblCreateAccount = new JLabel("Create Account");
		lblCreateAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateAccount.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblCreateAccount.setBounds(0, 11, 588, 60);
		createAccount.add(lblCreateAccount);
		
		lblChooseUsername = new JLabel("Username:");
		lblChooseUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChooseUsername.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblChooseUsername.setBounds(135, 123, 99, 14);
		createAccount.add(lblChooseUsername);
		
		txtChooseUsername = new JTextField();
		txtChooseUsername.setText("");
		txtChooseUsername.setColumns(10);
		txtChooseUsername.setBounds(244, 122, 250, 20);
		createAccount.add(txtChooseUsername);
		
		lblChoosePassword = new JLabel("Choose Password:");
		lblChoosePassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChoosePassword.setForeground(Color.BLACK);
		lblChoosePassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblChoosePassword.setBounds(69, 148, 165, 14);
		createAccount.add(lblChoosePassword);
		
		pwFieldChoose = new JPasswordField();
		pwFieldChoose.setBounds(244, 147, 250, 20);
		createAccount.add(pwFieldChoose);
		
		lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblConfirmPassword.setForeground(Color.BLACK);
		lblConfirmPassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblConfirmPassword.setBounds(69, 173, 165, 14);
		createAccount.add(lblConfirmPassword);
		
		pwFieldConfirm = new JPasswordField();
		pwFieldConfirm.setBounds(244, 172, 250, 20);
		createAccount.add(pwFieldConfirm);
		
		lblFirstName = new JLabel("First Name:");
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblFirstName.setBounds(135, 73, 99, 14);
		createAccount.add(lblFirstName);
		
		txtFirstName = new JTextField();
		txtFirstName.setText("");
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(244, 72, 250, 20);
		createAccount.add(txtFirstName);
		
		lblSecurityQuestion = new JLabel("Security Question:");
		lblSecurityQuestion.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSecurityQuestion.setForeground(Color.BLACK);
		lblSecurityQuestion.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblSecurityQuestion.setBounds(69, 198, 165, 14);
		createAccount.add(lblSecurityQuestion);
		
		txtQuestion = new JTextField();
		txtQuestion.setText("");
		txtQuestion.setColumns(10);
		txtQuestion.setBounds(244, 197, 250, 20);
		createAccount.add(txtQuestion);
		
		lblResponse = new JLabel("Response:");
		lblResponse.setHorizontalAlignment(SwingConstants.TRAILING);
		lblResponse.setForeground(Color.BLACK);
		lblResponse.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblResponse.setBounds(69, 223, 165, 14);
		createAccount.add(lblResponse);
		
		txtResponse = new JTextField();
		txtResponse.setText("");
		txtResponse.setColumns(10);
		txtResponse.setBounds(244, 222, 250, 20);
		createAccount.add(txtResponse);
		
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() { // Create account button on create new account page
			public void actionPerformed(ActionEvent e) {
				try {
					if(txtFirstName.getText().equals("") || txtLastName.getText().equals("") || txtChooseUsername.getText().equals("") || 
							txtQuestion.getText().equals("") || txtResponse.getText().equals("") || pwFieldChoose.getPassword() == null ||
							pwFieldConfirm.getPassword() == null) {
						// Error if any field is empty
						JOptionPane.showMessageDialog(createAccount, "Please fill in all fields.");
					} else if(da.usernameExists(txtChooseUsername.getText())) {
						// Error if username already exists
						JOptionPane.showMessageDialog(createAccount, "Username already exists.");
					} else if(!PasswordUtils.validPassword(pwFieldChoose.getPassword())) {
						// Error if invalid password
						JOptionPane.showMessageDialog(createAccount, "Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.");
					} else if(!java.util.Arrays.equals(pwFieldChoose.getPassword(), pwFieldConfirm.getPassword())) {
						// Error if passwords don't match
						JOptionPane.showMessageDialog(createAccount, "Passwords do not match.");
					} else if(!da.createNewUser(txtFirstName.getText(), txtLastName.getText(), txtChooseUsername.getText(), pwFieldChoose.getPassword(), 
								txtQuestion.getText(), txtResponse.getText())) {
						JOptionPane.showMessageDialog(createAccount, "There was an error while creating your account.");
					} else {
						txtFirstName.setText("");
						txtLastName.setText("");
						txtChooseUsername.setText("");
						pwFieldChoose.setText("");
						pwFieldConfirm.setText("");
						txtQuestion.setText("");
						txtResponse.setText("");
						cl.show(contentPane, "login");
						setBounds(100, 100, 600, 350);
						setTitle("To Do List");
						setTitle("To Do List - Login");
					}
				} catch (Exception e1) {
					
				}
			}
		});
		btnCreateAccount.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCreateAccount.setBounds(121, 302, 178, 33);
		createAccount.add(btnCreateAccount);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblLastName.setBounds(135, 98, 99, 14);
		createAccount.add(lblLastName);
		
		txtLastName = new JTextField();
		txtLastName.setText("");
		txtLastName.setColumns(10);
		txtLastName.setBounds(244, 97, 250, 20);
		createAccount.add(txtLastName);
		
		btnCancel.addActionListener(new ActionListener() { // Cancel button on create account page
			public void actionPerformed(ActionEvent e) {
				txtFirstName.setText("");
				txtLastName.setText("");
				txtChooseUsername.setText("");
				pwFieldChoose.setText("");
				pwFieldConfirm.setText("");
				txtQuestion.setText("");
				txtResponse.setText("");
				setTitle("To Do List - Login");
				cl.show(contentPane, "login");
				setBounds(100, 100, 600, 350);
				setTitle("To Do List - Login");
			}
		});
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCancel.setBounds(328, 302, 121, 32);
		createAccount.add(btnCancel);
		
		lblPasswordInfo = new JLabel("Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.");
		lblPasswordInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordInfo.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		lblPasswordInfo.setBounds(0, 277, 588, 14);
		createAccount.add(lblPasswordInfo);
		
		// Creating Forgot Password frame
		contentPane.add("forgotPassword", forgotPassword);
		forgotPassword.setLayout(new CardLayout(0, 0));
		CardLayout passwdCL = (CardLayout) forgotPassword.getLayout();
		inputUsername = new JPanel();
		answerSecurity = new JPanel();
		forgotPassword.add("inputUsername", inputUsername);
		inputUsername.setLayout(null);
		
		lblRetrieveTitle = new JLabel("Username of the account to retrieve:");
		lblRetrieveTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblRetrieveTitle.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblRetrieveTitle.setBounds(0, 11, 588, 60);
		inputUsername.add(lblRetrieveTitle);
		
		lblUsernameRetrieve = new JLabel("Username:");
		lblUsernameRetrieve.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsernameRetrieve.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lblUsernameRetrieve.setBounds(111, 109, 99, 14);
		inputUsername.add(lblUsernameRetrieve);
		
		txtUsernameRetrieve = new JTextField();
		txtUsernameRetrieve.setText("");
		txtUsernameRetrieve.setColumns(10);
		txtUsernameRetrieve.setBounds(220, 109, 250, 20);
		inputUsername.add(txtUsernameRetrieve);
		
		JTextArea lblSecurity = new JTextArea();
		lblSecurity.setLineWrap(true);
		lblSecurity.setEditable(false);
		lblSecurity.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSecurity.setBackground(UIManager.getColor("Button.background"));
		lblSecurity.setBounds(58, 82, 221, 75);
		answerSecurity.add(lblSecurity);
		
		btnVerifyAccount = new JButton("Verify Account");
		btnVerifyAccount.addActionListener(new ActionListener() { // Verify account button on Input Username page
			public void actionPerformed(ActionEvent e) {
				if(txtUsernameRetrieve.getText().equals("")) {
					JOptionPane.showMessageDialog(inputUsername, "Please enter your username.");
				} else
					try {
						if(!da.usernameExists(txtUsernameRetrieve.getText())) {
							JOptionPane.showMessageDialog(inputUsername, "Unable to find username in system.");
						} else {
							currentUsername = txtUsernameRetrieve.getText();
							lblSecurity.setText(da.getSecurityQuestion(currentUsername));
							txtUsernameRetrieve.setText("");
							passwdCL.show(forgotPassword, "answerSecurity");
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		btnVerifyAccount.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnVerifyAccount.setBounds(224, 213, 147, 23);
		inputUsername.add(btnVerifyAccount);
		
		btnCancelUsernameRetrieve = new JButton("Cancel");
		btnCancelUsernameRetrieve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsernameRetrieve.setText("");
				cl.show(contentPane, "login");
			}
		});
		btnCancelUsernameRetrieve.setBounds(255, 263, 89, 23);
		inputUsername.add(btnCancelUsernameRetrieve);
		
		forgotPassword.add("answerSecurity", answerSecurity);
		answerSecurity.setLayout(null);
		
		txtSecurityResponse = new JTextField();
		txtSecurityResponse.setText("");
		txtSecurityResponse.setColumns(10);
		txtSecurityResponse.setBounds(289, 82, 239, 20);
		answerSecurity.add(txtSecurityResponse);
		
		JButton btnChangePassword = new JButton("Change Password");
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!da.checkSecurityAnswer(txtSecurityResponse.getText(), currentUsername)) {
						JOptionPane.showMessageDialog(answerSecurity, "Incorrect response to security question.");
					} else if(!PasswordUtils.validPassword(pwFieldNewPassword.getPassword())) {
						// Error if invalid password
						JOptionPane.showMessageDialog(answerSecurity, "Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.");
					} else if(!java.util.Arrays.equals(pwFieldNewPassword.getPassword(), pwFieldConfirmNewPassword.getPassword())) {
						// Error if passwords don't match
						JOptionPane.showMessageDialog(answerSecurity, "Passwords do not match.");
					} else {
						da.setNewPassword(pwFieldNewPassword.getPassword(), currentUsername);
						currentUsername = "";
						passwdCL.show(forgotPassword, "inputUsername");
						cl.show(contentPane, "login");
						lblSecurity.setText("");
						txtSecurityResponse.setText("");
						pwFieldNewPassword.setText("");
						pwFieldConfirmNewPassword.setText("");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnChangePassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnChangePassword.setBounds(132, 278, 147, 23);
		answerSecurity.add(btnChangePassword);
		
		JLabel lblAnswerQuestionTitle = new JLabel("Answer security question to change password:");
		lblAnswerQuestionTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnswerQuestionTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblAnswerQuestionTitle.setBounds(0, 11, 588, 60);
		answerSecurity.add(lblAnswerQuestionTitle);
		
		lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewPassword.setBounds(132, 168, 147, 14);
		answerSecurity.add(lblNewPassword);
		
		lblConfirmNewPassword = new JLabel("Confirm New Password:");
		lblConfirmNewPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblConfirmNewPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblConfirmNewPassword.setBounds(85, 193, 194, 14);
		answerSecurity.add(lblConfirmNewPassword);
		
		JLabel lblPasswordInfoSecurity = new JLabel("Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.");
		lblPasswordInfoSecurity.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordInfoSecurity.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		lblPasswordInfoSecurity.setBounds(0, 253, 588, 14);
		answerSecurity.add(lblPasswordInfoSecurity);
		
		pwFieldNewPassword = new JPasswordField();
		pwFieldNewPassword.setBounds(289, 166, 239, 20);
		answerSecurity.add(pwFieldNewPassword);
		
		pwFieldConfirmNewPassword = new JPasswordField();
		pwFieldConfirmNewPassword.setBounds(289, 191, 239, 20);
		answerSecurity.add(pwFieldConfirmNewPassword);
		
		btnCancelChangePassword = new JButton("Cancel");
		btnCancelChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentUsername = "";
				passwdCL.show(forgotPassword, "inputUsername");
				cl.show(contentPane, "login");
				lblSecurity.setText("");
				txtSecurityResponse.setText("");
				pwFieldNewPassword.setText("");
				pwFieldConfirmNewPassword.setText("");
			}
		});
		btnCancelChangePassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCancelChangePassword.setBounds(318, 279, 147, 23);
		answerSecurity.add(btnCancelChangePassword);
		
		// Creating the toDoList frame
		contentPane.add("toDoList", toDoList);
		toDoList.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 23, 109, 442);
		toDoList.add(scrollPane);
		
		String[] data = {"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a"};
		listTaskLists = new JList(data);
		scrollPane.setViewportView(listTaskLists);
		
		contentPane.add("updateAccount", updateAccount);
		
		
	}
}
