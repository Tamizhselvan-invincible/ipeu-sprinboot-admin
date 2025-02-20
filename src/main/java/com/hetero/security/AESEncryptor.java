package com.hetero.security;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;


@Converter
public class AESEncryptor implements AttributeConverter<Object, String> {


    private final String encryptionKey;

    private final String encryptionCipher = "AES";

    private Key key;
    private Cipher cipher;

    public AESEncryptor(@Value("${application.security.aes.encryption.key}") String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    private Key getKey () {
        if (key == null) {
            key = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);
        }
        return key;
    }

    private Cipher getCipher () throws GeneralSecurityException {
        if (cipher == null) {
            cipher = Cipher.getInstance(encryptionCipher);
        }
        return cipher;
    }

    private void initCipher(int encryptionMode) throws GeneralSecurityException {
        getCipher().init(encryptionMode,getKey());
    }


    @SneakyThrows
    @Override
    public String convertToDatabaseColumn (Object attribute) {

        if(attribute == null) {
            return null;
        }

        try{
            initCipher(Cipher.ENCRYPT_MODE);
            byte[] bytes = SerializationUtils.serialize(attribute);
            assert bytes != null;
            return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data ",e);
        }

    }

    @SneakyThrows
    @Override
    public Object convertToEntityAttribute (String dbData) {
        if (dbData == null) {
            return null;
        }
        try{
            initCipher(Cipher.DECRYPT_MODE);
            byte[] bytes = getCipher().doFinal(Base64.getDecoder().decode(dbData));
            return SerializationUtils.deserialize(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data ",e);
        }
    }
}
