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

public class CreateAccountPanel extends JPanel {
	private static final DatabaseAccessor da = new DatabaseAccessor();
	
	private ToDoListGUI parentFrame; 
	private JPanel parentPanel;
	private JPanel previousPanel;
	
	private JLabel lblTitle;
	private JLabel lblUsername;
	private JTextField txtUsername;
	private JLabel lblChoosePassword;
	private JPasswordField txtChoosePassword;
	private JLabel lblConfirmPassword;
	private JPasswordField txtConfirmPassword;
	private JLabel lblFirstName;
	private JTextField txtFirstName;
	private JLabel lblLastName;
	private JTextField txtLastName;
	private JLabel lblQuestion;
	private JTextField txtQuestion;
	private JLabel lblResponse;
	private JTextField txtResponse;
	private JButton btnCreateAccount;
	private JButton btnCancel;
	
	public CreateAccountPanel(ToDoListGUI parentFrame) {
		// Initial set up (layout and name)
		setLayout(null);
		setName("createAccount");
		setBounds(100, 100, 600, 400);
		JPanel frame = this; // need this for the JOptionPane
		
		// Assigning necessary frame and panels
		this.parentFrame = parentFrame;
		this.parentPanel = null;
		this.previousPanel = null;
				
		// Adding the title to the create account panel
		lblTitle = new JLabel("Create Account");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblTitle.setBounds(0, 11, 588, 60);
		add(lblTitle);
		
		// Adding the username label to the create account panel
		lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblUsername.setBounds(135, 123, 99, 14);
		add(lblUsername);
		
		// Adding the text field for username input
		txtUsername = new JTextField();
		txtUsername.setText("");
		txtUsername.setColumns(10);
		txtUsername.setBounds(244, 122, 250, 20);
		add(txtUsername);
		
		// Adding the choose password label to the create account panel
		lblChoosePassword = new JLabel("Choose Password:");
		lblChoosePassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChoosePassword.setForeground(Color.BLACK);
		lblChoosePassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblChoosePassword.setBounds(69, 148, 165, 14);
		add(lblChoosePassword);
		
		// Adding the text field for choosing a password 
		txtChoosePassword = new JPasswordField();
		txtChoosePassword.setBounds(244, 147, 250, 20);
		add(txtChoosePassword);
		
		// Adding the confirm password label to the create account panel
		lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblConfirmPassword.setForeground(Color.BLACK);
		lblConfirmPassword.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblConfirmPassword.setBounds(69, 173, 165, 14);
		add(lblConfirmPassword);
		
		// Adding the text field for confirming a password
		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setBounds(244, 172, 250, 20);
		add(txtConfirmPassword);
		
		// Adding the First Name label to the create account panel
		lblFirstName = new JLabel("First Name:");
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblFirstName.setBounds(135, 73, 99, 14);
		add(lblFirstName);
		
		// Adding the text field for inputting a first name
		txtFirstName = new JTextField();
		txtFirstName.setText("");
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(244, 72, 250, 20);
		add(txtFirstName);
		
		// Adding the Last Name label to the create account panel
		lblLastName = new JLabel("Last Name:");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblLastName.setBounds(135, 98, 99, 14);
		add(lblLastName);
		
		// Adding the text field for inputting a last name
		txtLastName = new JTextField();
		txtLastName.setText("");
		txtLastName.setColumns(10);
		txtLastName.setBounds(244, 97, 250, 20);
		add(txtLastName);
		
		// Adding the Security Question label to the create account panel
		lblQuestion = new JLabel("Security Question:");
		lblQuestion.setHorizontalAlignment(SwingConstants.TRAILING);
		lblQuestion.setForeground(Color.BLACK);
		lblQuestion.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblQuestion.setBounds(69, 198, 165, 14);
		add(lblQuestion);
		
		// Adding the text field for inputting a security question
		txtQuestion = new JTextField();
		txtQuestion.setText("");
		txtQuestion.setColumns(10);
		txtQuestion.setBounds(244, 197, 250, 20);
		add(txtQuestion);
	
		// Adding the Response label to the create account panel
		lblResponse = new JLabel("Response:");
		lblResponse.setHorizontalAlignment(SwingConstants.TRAILING);
		lblResponse.setForeground(Color.BLACK);
		lblResponse.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblResponse.setBounds(69, 223, 165, 14);
		add(lblResponse);
		
		// Adding the text field for inputting a response
		txtResponse = new JTextField();
		txtResponse.setText("");
		txtResponse.setColumns(10);
		txtResponse.setBounds(244, 222, 250, 20);
		add(txtResponse);
		
		// Adding the create account button to the create account panel
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCreateAccount.setBounds(121, 302, 178, 33);
		add(btnCreateAccount);
		btnCreateAccount.addActionListener(new ActionListener() { // Create account button on create new account page
			public void actionPerformed(ActionEvent e) {
				try {
					if(txtFirstName.getText().equals("") || txtLastName.getText().equals("") || txtUsername.getText().equals("") || 
							txtQuestion.getText().equals("") || txtResponse.getText().equals("") || txtChoosePassword.getPassword() == null ||
							txtConfirmPassword.getPassword() == null) {
						// Error if any field is empty
						JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Unable to create account", JOptionPane.ERROR_MESSAGE);
					} else if(da.usernameExists(txtUsername.getText())) {
						// Error if username already exists
						JOptionPane.showMessageDialog(frame, "Username already exists", "Unable to create account", JOptionPane.ERROR_MESSAGE);
					} else if(!PasswordUtils.validPassword(txtChoosePassword.getPassword())) {
						// Error if invalid password
						JOptionPane.showMessageDialog(frame, "Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.",
								"Unable to create account", JOptionPane.ERROR_MESSAGE);
					} else if(!java.util.Arrays.equals(txtChoosePassword.getPassword(), txtConfirmPassword.getPassword())) {
						// Error if passwords don't match
						JOptionPane.showMessageDialog(frame, "Passwords do not match", "Unable to create account", JOptionPane.ERROR_MESSAGE);
					} else if(!da.createNewUser(txtFirstName.getText(), txtLastName.getText(), txtUsername.getText(), txtChoosePassword.getPassword(), 
								txtQuestion.getText(), txtResponse.getText())) {
						JOptionPane.showMessageDialog(frame, "There was an error while creating your account", "Unable to create account", JOptionPane.ERROR_MESSAGE);
					} else {
						// Resetting all fields on this form
						resetTextFields();
						// Changing to new panel
						parentFrame.setBounds(100, 100, 600, 350);
						parentFrame.setTitle("To Do List - Login");
						((CardLayout) parentPanel.getLayout()).show(parentPanel, previousPanel.getName());
					}
				} catch (Exception e1) {
					
				}
			}
		});
		add(btnCreateAccount);
		
		// Adding the cancel button to the create account panel
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCancel.setBounds(328, 302, 121, 32);
		btnCancel.addActionListener(new ActionListener() { // Cancel button on create account page
			public void actionPerformed(ActionEvent e) {
				resetTextFields();
				parentFrame.setTitle("To Do List - Login");
				parentFrame.setBounds(100, 100, 600, 350);
				((CardLayout) parentPanel.getLayout()).show(parentPanel, previousPanel.getName());
			}
		});
		add(btnCancel);
	}
	
	public void resetTextFields() {
		txtFirstName.setText("");
		txtLastName.setText("");
		txtUsername.setText("");
		txtChoosePassword.setText("");
		txtConfirmPassword.setText("");
		txtQuestion.setText("");
		txtResponse.setText("");
	}
	
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	
	public void setPreviousPanel(JPanel previousPanel) {
		this.previousPanel = previousPanel;
	}
}
