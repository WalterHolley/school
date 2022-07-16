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

    private static Settings settings;

    public static void main(String[] args) {
	    if(init()){
            settings = new Settings();
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
                KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE);
                SecureRandom random = new SecureRandom();
                ks.load(null, null);

                //create key
                KeyGenerator generator = KeyGenerator.getInstance(Constants.KEYGEN_INSTANCE);
                generator.init(168, random);
                SecretKey key = generator.generateKey();

                KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
                KeyStore.ProtectionParameter pwd = new KeyStore.PasswordProtection(Constants.KEYSTORE_PASSWORD.toCharArray());
                ks.setEntry(Constants.KEYSTORE_ALIAS, entry, pwd);

                //save keystore
                ks.store(new FileOutputStream(Constants.KEYSTORE_FILE_NAME), Constants.KEYSTORE_PASSWORD.toCharArray());
                settings = new Settings();


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



        return result;

    }

    //UX for file reading
    //UX for updating settings

}
