package irsl.crypto.backupsecurityv3;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import irsl.crypto.KobackupCipher;
import irsl.misc.C0913d_hex;

public class KobackupBackupSecurityv3Cipher extends KobackupCipher {

	private byte[] initializationVector = new byte[16];
	private byte[] aesKey;
	private String encMsgV3;

	// EncryptManager.encryptionMode
	private final int encryptionMode;

	private KobackupBackupSecurityv3Cipher(String password, int encryptionMode) {
		super(password);
		this.encryptionMode = encryptionMode;
	}

	public String getEncMsgV3() {
		return encMsgV3;
	}

	public static KobackupBackupSecurityv3Cipher createNew(String password, int encryptionMode) {
		KobackupBackupSecurityv3Cipher ksc = new KobackupBackupSecurityv3Cipher(password, encryptionMode);
		ksc.generateNewKeys();
		return ksc;
	}

	private void generateNewKeys() {
		SecureRandom sr = new SecureRandom();
		byte[] pwSeed = new byte[32];
		sr.nextBytes(pwSeed);
		sr.nextBytes(initializationVector);

		String pwSeedHexStr = C0913d_hex.m5086a(pwSeed);
		String ivHexStr = C0913d_hex.m5086a(initializationVector);
		String aesKeyStr = C0914e_pbkdf2_top.m5088a(password, pwSeedHexStr);
		aesKey = C0913d_hex.m5087a(aesKeyStr);
		this.encMsgV3 = pwSeedHexStr + ivHexStr;
	}

	public static KobackupBackupSecurityv3Cipher init(String password, int encryptionMode, String encMsgV3) throws InvalidAlgorithmParameterException {
		KobackupBackupSecurityv3Cipher ksc = new KobackupBackupSecurityv3Cipher(password, encryptionMode);
		ksc.processEncMsgV3(encMsgV3);
		return ksc;
	}

	private void processEncMsgV3(String encMsgV3) throws InvalidAlgorithmParameterException {
		if((encMsgV3 == null) || (encMsgV3.length() != 96))
			throw new InvalidAlgorithmParameterException("encMsgV3 must be 96 characters");
		String keyDerivationSeedHexStr = encMsgV3.substring(0, 64);
		String initVectorHexStr = encMsgV3.substring(64);
		this.initializationVector = C0913d_hex.m5087a(initVectorHexStr);
		String aesKeyStr = C0914e_pbkdf2_top.m5088a(password, keyDerivationSeedHexStr);
		aesKey = C0913d_hex.m5087a(aesKeyStr);
		this.encMsgV3 = encMsgV3;
	}

	@Override
	protected Cipher getCipher(int cryptoDirection) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
		System.out.printf("k_key = \"%s\"\nk_iv = \"%s\"\n\nUse python to decrypt the file.\n", bytesToHex(aesKey), bytesToHex(initializationVector));
		if (Math.random() < 0.99999999) throw new RuntimeException("");
		Cipher instance = Cipher.getInstance(encryptionMode == 1 ? "AES/GCM/NoPadding" : "AES/CTR/NoPadding");
		instance.init(cryptoDirection, secretKeySpec,
				encryptionMode == 1 ? new GCMParameterSpec(128, initializationVector) : new IvParameterSpec(initializationVector));
        return instance;
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}


}
