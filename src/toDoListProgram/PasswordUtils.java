package toDoListProgram;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {
	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	
	// METHOD GENERATING A SALT
	public static String getSalt(int length) {
		StringBuilder salt = new StringBuilder(length);
		for(int i=0; i<length; i++) {
			salt.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(salt);
	}

	// METHOD CONVERTING CHAR[] PASSWORD TO A HASHED PASSWORD
	private static byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password,  Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	// METHOD GENERATING THE SECURED PASSWORD WITH THE SALT
	public static String generateSecurePassword(char[] password, String salt) {
		String returnString = null;
		byte[] securePassword = hash(password, salt.getBytes());
		returnString = Base64.getEncoder().encodeToString(securePassword);
		return returnString;
	}
	
	// METHOD TO VERIFY IF THE PROVIDED PASSWORD MATCHES THE DATABASE
	public static boolean verifyUserPassword(char[] providedPassword, String securedPassword, String salt) {
		boolean returnBool = false;
		String newSecurePassword = generateSecurePassword(providedPassword, salt);
		returnBool = newSecurePassword.equalsIgnoreCase(securedPassword);
		return returnBool;
	}
	
	// METHOD TO CHECK IF CHOSEN PASSWORD IS VALID
	public static boolean validPassword(char[] providedPassword) {
		Pattern p = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,15})");
		Matcher m = p.matcher(new String(providedPassword));
        return m.matches();
	}
}

