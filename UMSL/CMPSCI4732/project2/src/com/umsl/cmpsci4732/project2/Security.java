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
        byte[] result;

        result = translate(plainBytes,false);
        return  result;
    }

    /**
     * Decrypts a byte array
     * @param cipherBytes the encrypted bytes
     * @return byte array ofthe decrypted bytes
     */
    public byte[] decrypt(byte[] cipherBytes){
        byte[] result;
        result = translate(cipherBytes, true);
        return  result;
    }

    /**
     * Retrieves keystore for the application,
     * otherwise creates a new one if it doesn't
     * exist
     * @return The keystore for the settings file
     */
    private KeyStore getKeystore(){

        KeyStore ks = null;
        File file  = new File(KEYSTORE_FILE_NAME);

        try{
            ks = KeyStore.getInstance(KEYSTORE_TYPE);

            if(!file.exists()){
                //create keystore

                ks.load(null, null);
                addKey(ks, createKey());
                ks.store(new FileOutputStream(KEYSTORE_FILE_NAME), KEYSTORE_PASSWORD.toCharArray());
            }
            else{
                ks.load(new FileInputStream(KEYSTORE_FILE_NAME), KEYSTORE_PASSWORD.toCharArray());
            }

        } catch (CertificateException e) {
            System.out.println("There was a problem: " + e.getMessage());
        } catch (KeyStoreException e) {
            System.out.println("There was a problem with the keystore: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("There was a problem with the keystore file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("There was a problem with the keystore algorithm: " + e.getMessage());
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
     * @return Secret key object containing the private
     * key
     */
    private SecretKey getKey(){
        SecretKeySpec key = null;

        try{

            Key ksKey = getKeystore().getKey(KEYSTORE_ALIAS, KEYSTORE_PASSWORD.toCharArray());
            key = new SecretKeySpec(ksKey.getEncoded(), ksKey.getAlgorithm());

        } catch (UnrecoverableKeyException e) {
            System.out.println("There was a problem with the key: " + e.getMessage());
        } catch (KeyStoreException e) {
            System.out.println("There was a problem with the keystore: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("There was a problem with the key algorithm: " + e.getMessage());
        }


        return key;
    }

    /**
     * Adds a key to the given keystore
     * @param ks The keystore to add the key to
     * @param key The secret key to be added
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
            System.out.println("There was a problem with the translation padding: " + e.getMessage());
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("There was a problem with the translation algorithm: " + e.getMessage());
        }  catch (InvalidKeyException e) {
            System.out.println("There was a problem with the message credentials: " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("There was a problem with the message size " + e.getMessage());
        } catch (BadPaddingException e) {
            System.out.println("There was a problem padding the message: " + e.getMessage());
        }

        return result;
    }
}
