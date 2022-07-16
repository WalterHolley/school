package com.umsl.cmpsci4732.project2;

import java.io.File;
import java.util.HashMap;

public class Settings {
    private static final String CONFIG_FILE_NAME = "settings.cfg";
    private HashMap<String, Object> configFile;


    public Settings(){
        initSettings();
    }

    //TODO: update settings

    //TODO: load settings

    //TODO: save settings

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
        }


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



}
