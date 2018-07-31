
package jdz.NZXN.authentication;

import org.passay.*;


public class PasswordStrengthChecker {
	private static PasswordValidator validator = new PasswordValidator(
			  new LengthRule(6, 32),
			  new CharacterRule(EnglishCharacterData.UpperCase, 1),
			  new CharacterRule(EnglishCharacterData.LowerCase, 1),
			  new CharacterRule(EnglishCharacterData.Digit, 1),
			  new WhitespaceRule());
	
	public static boolean isStrong(String password) {
		return validator.validate(new PasswordData(password)).isValid();
	}

}
