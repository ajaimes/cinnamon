package com.cinnamonframework.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The Password Encryption Service.
 * 
 * Based on the work of Jerry Orr, published on Java Code Geeks.
 * 
 * This implementation uses PBKDF2. Here's an excerpt from th web page.
 * 
 * Before I give show you some concrete code, let’s talk a little about why 
 * PBKDF2 is a good choice for encrypting passwords:
 * 
 * 1. Recommended by the NIST. Section 5.3 of Special Publication 800-132 
 * recommends PBKDF2 for encrypting passwords. Security officials will love 
 * that.
 * 
 * 2. Adjustable key stretching to defeat brute force attacks. The basic idea of 
 * key stretching is that after you apply your hashing algorithm to the 
 * password, you then continue to apply the same algorithm to the result many 
 * times (the iteration count). If hackers are trying to crack your passwords, 
 * this greatly increases the time it takes to try the billions of possible 
 * passwords. As mentioned previously, the slower, the better. PBKDF2 lets you 
 * specify the number of iterations to apply, allowing you to make it as slow as 
 * you like.
 * 
 * 3. A required salt to defeat rainbow table attacks and prevent collisions 
 * with other users. A salt is a randomly generated sequence of bits that is 
 * unique to each user and is added to the user’s password as part of the 
 * hashing. This prevents rainbow table attacks by making a precomputed list of 
 * results unfeasible. And since each user gets their own salt, even if two 
 * users have the same password, the encrypted values will be different. There 
 * is a lot of conflicting information out there on whether the salts should be 
 * stored someplace separate from the encrypted passwords. Since the key 
 * stretching in PBKDF2 already protects us from brute-force attacks, I feel it 
 * is unnecessary to try to hide the salt. Section 3.1 of NIST SP 800-132 also 
 * defines salt as a “non-secret binary value,” so that’s what I go with.
 * 
 * 4. Part of Java SE 6. No additional libraries necessary. This is particularly 
 * attractive to those working in environments with restrictive open-source 
 * policies.
 * 
 * The flow goes something like this:
 * 
 * 1. When adding a new user, call generateSalt(), then getEncryptedPassword(), 
 * and store both the encrypted password and the salt. Do not store the 
 * clear-text password. Don’t worry about keeping the salt in a separate table 
 * or location from the encrypted password; as discussed above, the salt is 
 * non-secret.
 * 
 * 2. When authenticating a user, retrieve the previously encrypted password and 
 * salt from the database, then send those and the clear-text password they 
 * entered to authenticate(). If it returns true, authentication succeeded.
 * 
 * 3. When a user changes their password, it’s safe to reuse their old salt; you 
 * can just call getEncryptedPassword() with the old salt.
 * 
 * @see http://www.javacodegeeks.com/2012/05/secure-password-storage-donts-dos-and.html
 */
public class PasswordEncryptionUtil {

	public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
		throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Encrypt the clear-text password using the same salt that was used to
		// encrypt the original password
		byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

		// Authentication succeeds if encrypted password that the user entered
		// is equal to the stored hash
		return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
	}

	public byte[] getEncryptedPassword(String password, byte[] salt)
		throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		// PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
		// specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
		String algorithm = "PBKDF2WithHmacSHA1";
		
		// SHA-1 generates 160 bit hashes, so that's what makes sense here
		int derivedKeyLength = 160;
		
		// Pick an iteration count that works for you. The NIST recommends at
		// least 1,000 iterations:
		// http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
		// iOS 4.x reportedly uses 10,000:
		// http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
		int iterations = 20000;

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

		return f.generateSecret(spec).getEncoded();
	}

	public byte[] generateSalt() throws NoSuchAlgorithmException {
		// VERY important to use SecureRandom instead of just Random
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		// Generate an 8 byte (64 bit) salt as recommended by RSA PKCS5
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		return salt;
	}
}
