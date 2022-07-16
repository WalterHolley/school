package com.umsl.cmpsci4732.project2;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;

public class Main {

    private static Settings settings = new Settings();

    public static void main(String[] args) {
	    if(init()){

        }
        else {
            System.out.println("The program stopped unexpectedly");
        }

        System.out.println("Exiting Program");
    }

    /**
     * Initializes the application.
     * Creates files and object neccessary
     * for program to funtion
     * @return true if initialization completed
     * successfully.  otherwise false.
     */
    private static boolean init(){
        File f = new File(Constants.KEYSTORE_FILE_NAME);
        boolean result = true;

        //check for keystore
        if(!f.exists()){
            try{
                //create keystore
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                SecureRandom random = new SecureRandom();

                //save keystore
                FileOutputStream fo = new FileOutputStream(Constants.KEYSTORE_FILE_NAME);
                ks.store(fo, Constants.KEYSTORE_PASSWORD.toCharArray());
                ks.load(new FileInputStream(Constants.KEYSTORE_FILE_NAME), Constants.KEYSTORE_PASSWORD.toCharArray());


                //create key
                KeyGenerator generator = KeyGenerator.getInstance("AES");
                generator.init(256, random);
                SecretKey key = generator.generateKey();

                KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
                KeyStore.ProtectionParameter pwd = new KeyStore.PasswordProtection(Constants.KEYSTORE_PASSWORD.toCharArray());
                ks.setEntry(Constants.KEYSTORE_ALIAS, entry, pwd);
                ks.store(new FileOutputStream(Constants.KEYSTORE_FILE_NAME), Constants.KEYSTORE_PASSWORD.toCharArray());

            }
            catch (KeyStoreException ex){
                System.out.println("There was a problem initializing security for the program");
                result = false;
            }
            catch (FileNotFoundException ex){
                System.out.println("There was a problem creating the keystore  file");
                result = false;
            }
            catch (Exception ex){
                System.out.println("There was a problem initializing the program");
                result = false;
            }
        }

        settings = new Settings();

        return result;

    }

    //UX for file reading
    //UX for updating settings

}
