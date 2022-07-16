package com.umsl.cmpsci4732.project2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;

public class Settings {
    private static final String CONFIG_FILE_NAME = "settings.cfg";
    private HashMap<String, Object> configMap;


    public Settings(){
        initSettings();
    }

    public void setDogName(String dogName){
        configMap.put("DogName", dogName);
    }

    public void setAge(int age){
        configMap.put("Age", age);
    }

    public void setWeight(float weight){
        configMap.put("Weight", weight);
    }

    public String getDogName(){
        return (String) configMap.get("DogName");
    }

    public Integer getAge(){
        return (Integer) configMap.get("Age");
    }

    /**
     * Returns the weight of the dod
     * @return float of the dog's weight
     */
    public float getWeight(){
        return (float) configMap.get("Weight");
    }

    //TODO: load settings
    private void loadConfig(){
        File file = new File(CONFIG_FILE_NAME);
        try{
            if(file.exists()){

                byte[] configFile = Files.readAllBytes(file.toPath());
                byte[] configObject = decrypt(configFile);

                ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(configObject));
                configMap = (HashMap<String, Object>) objectStream.readObject();



            }
        } catch (FileNotFoundException e) {
            //TODO: handle exception message
            e.printStackTrace();
        } catch (IOException e) {
            //TODO: Handle Exception message
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the configuration file to disk
     */
    private void saveConfig() {

        if(!configMap.isEmpty() && configMap != null){

            try {
                //convert object to byte array
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
                outputStream.writeObject(configMap);

                //save to file
                 FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE_NAME);
                 fileOutputStream.write(encrypt(byteStream.toByteArray()));
                 fileOutputStream.close();


            } catch (IOException e) {
                //TODO: provide exception message
                e.printStackTrace();
            }


        }
    }

    /**
     * Decrypts a DESede encrypted object
     * @param encryptedByteArray The encrypted byte array
     * @return the decrytped byte array
     */
    private byte[] decrypt(byte[] encryptedByteArray){
        byte[] result = null;

        try{
            //load keystore
            KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
            ks.load(new FileInputStream(Constants.KEYSTORE_FILE_NAME), Constants.KEYSTORE_PASSWORD.toCharArray());

            //get key
            KeyStore.ProtectionParameter pwd = new KeyStore.PasswordProtection(Constants.KEYSTORE_PASSWORD.toCharArray());
            Key ksKey = ks.getKey(Constants.KEYSTORE_ALIAS, Constants.KEYSTORE_PASSWORD.toCharArray());
            SecretKeySpec key = new SecretKeySpec(ksKey.getEncoded(), ksKey.getAlgorithm());

            //setup cipher
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, key);

            result = cipher.doFinal(encryptedByteArray);

        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Encrypt the given content
     * @param byteArray An array of bytes to encrypt
     * @return the encrypyted bytes
     */
    private byte[] encrypt(byte[] byteArray){

        byte[] result = null;

        try{
            //load keystore
            KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
            ks.load(new FileInputStream(Constants.KEYSTORE_FILE_NAME), Constants.KEYSTORE_PASSWORD.toCharArray());

            //get key
            KeyStore.ProtectionParameter pwd = new KeyStore.PasswordProtection(Constants.KEYSTORE_PASSWORD.toCharArray());
            Key ksKey = ks.getKey(Constants.KEYSTORE_ALIAS, Constants.KEYSTORE_PASSWORD.toCharArray());
            SecretKeySpec key = new SecretKeySpec(ksKey.getEncoded(), ksKey.getAlgorithm());

            //setup cipher
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            //encrypt
            result = cipher.doFinal(byteArray);
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (CertificateException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (KeyStoreException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (IOException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            //TODO: provide exception message
            e.printStackTrace();
        } catch (InvalidKeyException e) {
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

    /**
     * setup and loading of settings into the class
     */
    private void initSettings(){
        //check if settings file exists
        File f  = new File(CONFIG_FILE_NAME);

        //load settings
        if(f.exists()){
            loadConfig();

        }//create settings
        else{
            configMap = new HashMap<>();
            configMap.put("DogName", "Kookie");
            configMap.put("Age", 4);
            configMap.put("Weight", 50.2);

            saveConfig();
        }


    }



}
