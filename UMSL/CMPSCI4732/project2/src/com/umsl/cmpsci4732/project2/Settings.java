package com.umsl.cmpsci4732.project2;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class Settings {
    private static final String CONFIG_FILE_NAME = "settings.cfg";
    private HashMap<String, Object> configMap;
    private Security security;


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
                byte[] configObject = security.decrypt(configFile);

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
                 fileOutputStream.write(security.encrypt(byteStream.toByteArray()));
                 fileOutputStream.close();


            } catch (IOException e) {
                //TODO: provide exception message
                e.printStackTrace();
            }


        }
    }




    /**
     * setup and loading of settings into the class
     */
    private void initSettings(){
        //check if settings file exists
        File f  = new File(CONFIG_FILE_NAME);
        security = new Security();

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
