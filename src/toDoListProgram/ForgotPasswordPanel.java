package toDoListProgram;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ForgotPasswordPanel extends JPanel {
	private final static DatabaseAccessor da = new DatabaseAccessor();
	
	private ToDoListGUI parentFrame; 
	private JPanel parentPanel;
	private JPanel previousPanel;
	private JLabel lblTitle;
	private JTextArea lblSecurityQuestion;
	private JTextField txtSecurityResponse;
	private JLabel lblNewPassword;
	private JPasswordField txtNewPassword;
	private JLabel lblConfirmNewPassword;
	private JPasswordField txtConfirmNewPassword;
	private JButton btnChangePassword;
	private JButton btnCancel;
	
	public ForgotPasswordPanel(ToDoListGUI parentFrame) {
		// Initial set up (layout and name)
		setLayout(null);
		setName("forgotPassword");
		setBounds(100, 100, 600, 350);
		JPanel frame = this; // need this for the JOptionPane
		
		// Assigning necessary frame and panels
		this.parentFrame = parentFrame;
		parentPanel = null;
		previousPanel = null;
		
		// Adding the title label to the forgot password panel
		lblTitle = new JLabel("Answer security question to change password:");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblTitle.setBounds(0, 11, 588, 60);
		add(lblTitle);
		
		// Adding the security question to the forgot password panel
		lblSecurityQuestion = new JTextArea();
		lblSecurityQuestion.setLineWrap(true);
		lblSecurityQuestion.setEditable(false);;
		lblSecurityQuestion.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblSecurityQuestion.setBackground(UIManager.getColor("Button.background"));
		lblSecurityQuestion.setBounds(58, 82, 221, 75);
		add(lblSecurityQuestion);
		
		// Adding text field for security response input
		txtSecurityResponse = new JTextField();
		txtSecurityResponse.setText("");
		txtSecurityResponse.setColumns(10);
		txtSecurityResponse.setBounds(289, 82, 239, 20);
		add(txtSecurityResponse);
		
		// Adding new password label to forgot password panel
		lblNewPassword = new JLabel("New Password:");
		lblNewPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewPassword.setBounds(132, 168, 147, 14);
		add(lblNewPassword);
		
		// Adding password field for inputting new password
		txtNewPassword = new JPasswordField();
		txtNewPassword.setBounds(289, 166, 239, 20);
		add(txtNewPassword);
		
		// Adding confirm password label to forgot password panel
		lblConfirmNewPassword = new JLabel("Confirm New Password:");
		lblConfirmNewPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblConfirmNewPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblConfirmNewPassword.setBounds(85, 193, 194, 14);
		add(lblConfirmNewPassword);
		
		// Adding password field for inputting confirmed password
		txtConfirmNewPassword = new JPasswordField();
		txtConfirmNewPassword.setBounds(289, 191, 239, 20);
		add(txtConfirmNewPassword);
		
		// Adding the change password button to forgot password panel
		btnChangePassword = new JButton("Change Password");
		btnChangePassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnChangePassword.setBounds(132, 278, 147, 23);
		btnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!da.checkSecurityAnswer(txtSecurityResponse.getText(), parentFrame.getCurrentUsername())) {
						JOptionPane.showMessageDialog(frame, "Incorrect response to security question.", "Unable to change password", JOptionPane.ERROR_MESSAGE);
					} else if(!PasswordUtils.validPassword(txtNewPassword.getPassword())) {
						// Error if invalid password
						JOptionPane.showMessageDialog(frame, "Passwords must be 8-15 characters long and contain at least 1 lowercase letter, uppercase letter, number, and special symbol.",
								"Unable to change password", JOptionPane.ERROR_MESSAGE);
					} else if(!java.util.Arrays.equals(txtNewPassword.getPassword(), txtConfirmNewPassword.getPassword())) {
						// Error if passwords don't match
						JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Unable to change password", JOptionPane.ERROR_MESSAGE);
					} else {
						da.setNewPassword(txtNewPassword.getPassword(), parentFrame.getCurrentUsername());
						parentFrame.setCurrentUsername("");
						((CardLayout) parentPanel.getLayout()).show(parentPanel, previousPanel.getName());
						resetAllFields();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		add(btnChangePassword);
		
		// Adding the cancel button to forgot password panel
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		btnCancel.setBounds(318, 279, 147, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentFrame.setCurrentUsername("");
				((CardLayout) parentPanel.getLayout()).show(parentPanel, previousPanel.getName());
				resetAllFields();
			}
		});
		add(btnCancel);
		
	}
	private void resetAllFields() {
		lblSecurityQuestion.setText("");
		txtSecurityResponse.setText("");
		txtNewPassword.setText("");
		txtConfirmNewPassword.setText("");
	}
	
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	
	public void setPreviousPanel(JPanel previousPanel) {
		this.previousPanel = previousPanel;
	}
	
	public void setSecurityQuestionText(String question) {
		lblSecurityQuestion.setText(question);
	}
}


