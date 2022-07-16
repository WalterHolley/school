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



    public static void main(String[] args) {
	    init();

        System.out.println("Exiting Program");
    }

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
            }
            catch (FileNotFoundException ex){
                System.out.println("There was a problem creating the keystore  file");
            }
            catch (Exception ex){
                System.out.println("There was a problem initializing the program");
            }


        }

        return result;

    }

    //UX for file reading
    //UX for updating settings
    //UX for adding settings
    //UX for removing settings
}
