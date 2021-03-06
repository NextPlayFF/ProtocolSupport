package protocolsupport.protocol.utils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.spigotmc.SneakyThrow;

public class MinecraftEncryption {

	public static Cipher getDecrypter(SecretKey key) {
		try {
			Cipher instance = Cipher.getInstance("AES/CFB8/NoPadding");
			instance.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
			return instance;
		} catch (GeneralSecurityException ex) {
			SneakyThrow.sneaky(ex);
			return null;
		}
	}

	public static byte[] createHash(final PublicKey publicKey, final SecretKey secretKey) {
		return createHash("SHA-1", new byte[][] { secretKey.getEncoded(), publicKey.getEncoded() });
	}

	private static byte[] createHash(final String hashAlgoName, final byte[]... data) {
		try {
			MessageDigest instance = MessageDigest.getInstance(hashAlgoName);
			for (int length = data.length, i = 0; i < length; ++i) {
				instance.update(data[i]);
			}
			return instance.digest();
		} catch (NoSuchAlgorithmException ex) {
			SneakyThrow.sneaky(ex);
			return null;
		}
	}

}
