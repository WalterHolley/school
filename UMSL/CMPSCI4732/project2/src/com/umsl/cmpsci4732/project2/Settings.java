package com.umsl.cmpsci4732.project2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.FieldPosition;
import java.util.HashMap;

public class Settings {
    private static final String CONFIG_FILE_NAME = "settings.cfg";
    private HashMap<String, Object> configFile;


    public Settings(){
        initSettings();
    }

    public void setDogName(String dogName){
        configFile.put("DogName", dogName);
    }

    public void setAge(int age){
        configFile.put("Age", age);
    }

    public void setWeight(float weight){
        configFile.put("Weight", weight);
    }

    public String getDogName(){
        return (String)configFile.get("DogName");
    }

    public Integer getAge(){
        return (Integer) configFile.get("Age");
    }

    /**
     * Returns the weight of the dod
     * @return float of the dog's weight
     */
    public float getWeight(){
        return (float) configFile.get("Weight");
    }

    //TODO: load settings
    private void loadConfig(){

    }

    //TODO: save settings
    private void saveConfig() {

        if(!configFile.isEmpty() && configFile != null){

            try {
                //convert object to byte array
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(byteStream);
                outputStream.writeObject(configFile);

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

        }//create settings
        else{
            configFile = new HashMap<>();
            configFile.put("DogName", "Kookie");
            configFile.put("Age", 4);
            configFile.put("Weight", 50.2);

            saveConfig();
        }


    }



}
