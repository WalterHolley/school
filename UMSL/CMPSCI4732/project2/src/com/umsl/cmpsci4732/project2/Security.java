package com.umsl.cmpsci4732.project2;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;


public class Security {

    private final String KEYSTORE_FILE_NAME = "projectKeystore.jceks";
    private final String KEYSTORE_TYPE = "JCEKS";
    private final String KEYGEN_INSTANCE = "DESede";
    private final String CIPHER_INSTANCE = "DESede/ECB/PKCS5Padding";
    private final String KEYSTORE_PASSWORD = "changeit";
    private final String KEYSTORE_ALIAS = "project2alias";
    private final Integer BIT_SIZE = 168;

    public Security(){

    }

    public byte[] encrypt(byte[] plainBytes){
        byte[] result = null;

        result = translate(plainBytes,false);
        return  result;
    }

    /**
     * Decrypts a byte array
     * @param cipherBytes the encrypted bytes
     * @return byte array ofthe decrypted bytes
     */
    public byte[] decrypt(byte[] cipherBytes){
        byte[] result = null;
        result = translate(cipherBytes, true);
        return  result;
    }

    /**
     * Retrieves keystore
     * @return
     */
    private KeyStore getKeystore(){

        KeyStore ks = null;
        File file  = new File(KEYSTORE_FILE_NAME);

        try{
            if(!file.exists()){
                //create keystore
                ks = KeyStore.getInstance(KEYSTORE_TYPE);
                ks.load(null, null);
                addKey(ks, createKey());
                ks.store(new FileOutputStream(KEYSTORE_FILE_NAME), KEYSTORE_PASSWORD.toCharArray());
            }
            else{
                ks.load(new FileInputStream(KEYSTORE_FILE_NAME), KEYSTORE_PASSWORD.toCharArray());
            }

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return  ks;
    }

    /**
     * Creates a secret private key for secure messages
     * @return Secret key representation of key
     */
    private SecretKey createKey(){

        SecretKey key = null;
        try{
            //create key
            KeyGenerator generator = KeyGenerator.getInstance(KEYGEN_INSTANCE);
            SecureRandom random = new SecureRandom();
            generator.init(BIT_SIZE, random);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return key;


    }

    /**
     * Retrieves the key from the existing keystore
     * @return
     */
    private SecretKey getKey(){
        SecretKeySpec key = null;

        try{

            Key ksKey = getKeystore().getKey(KEYSTORE_ALIAS, KEYSTORE_PASSWORD.toCharArray());
            key = new SecretKeySpec(ksKey.getEncoded(), ksKey.getAlgorithm());

        } catch (UnrecoverableKeyException e) {
            //TODO: create better exception message
            e.printStackTrace();
        } catch (KeyStoreException e) {
            //TODO: create better exception message
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            //TODO: create better exception message
            e.printStackTrace();
        }


        return key;
    }

    /**
     * Adds a key to the given keystore
     * @param ks
     * @param key
     */
    private void addKey(KeyStore ks, SecretKey key){

        try{
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            KeyStore.ProtectionParameter pwd = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
            ks.setEntry(KEYSTORE_ALIAS, entry, pwd);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    /**
     * Translates a given byte array into an encrypted or decrypted state
     * @param byteArray they byte array to translate
     * @param doDecrypt decrypts if true, encrypts if false
     * @return the translated byte array
     */
    private byte[] translate(byte[] byteArray, boolean doDecrypt){
        byte[] result = null;

        try{

            //setup cipher
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            if(doDecrypt)
                cipher.init(Cipher.DECRYPT_MODE, getKey());
            else
                cipher.init(Cipher.ENCRYPT_MODE, getKey());

            //translate
            result = cipher.doFinal(byteArray);
        }
        catch (NoSuchPaddingException e) {
            //TODO: provide exception message
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            //TODO: provide exception message
            e.printStackTrace();
        }  catch (InvalidKeyException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (BadPaddingException e) {
            //TODO: provide exception message
            e.printStackTrace();
        }

        return result;
    }
}
