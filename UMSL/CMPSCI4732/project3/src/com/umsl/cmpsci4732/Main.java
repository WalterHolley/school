package com.umsl.cmpsci4732;

import java.io.*;
import java.lang.System;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String BIT256_FILE = ".//testfiles//file-32b";
    private static final String KB1_FILE = ".//testfiles//kbfile-1.bin";
    private static final String BIT128_FILE = ".//testfiles//smallfile-1.bin";
    private static final String BIT64_FILE = ".//testfiles//smallfile-2.bin";
    private static final String BIRTHDAY_VANITY = "0227";

    public static void main(String[] args) {
	// write your code here
        //task1();
        task2();


    }

    private static void task1(){
        int iterations = 0;
        System.out.println("TASK 1");
        iterations = timeTest(new File(BIT64_FILE));
        System.out.println("Iterations at 64 bits per file: " + iterations);
        iterations = timeTest(new File(BIT128_FILE));
        System.out.println("Iterations at 128 bits per file: " + iterations);
        iterations = timeTest(new File(BIT256_FILE));
        System.out.println("Iterations at 256 bits per file: " + iterations);
        iterations = timeTest(new File(KB1_FILE));
        System.out.println("Iterations at 1 kilobyte per file: " + iterations);
    }

    private static void task2(){
        int iterations = 0;


        //get Iterations with one value
        iterations = getVanityIterations(BIRTHDAY_VANITY.substring(0,1),resetBytes());
        System.out.println("For one character: " + iterations);
        //get iterations with two values
        iterations = getVanityIterations(BIRTHDAY_VANITY.substring(0,2), resetBytes());
        System.out.println("For two characters: " + iterations);
        //get iterations with three values
        iterations = getVanityIterations(BIRTHDAY_VANITY.substring(0,3), resetBytes());
        System.out.println("For three characters: " + iterations);
        //get iterations with four values
        iterations = getVanityIterations(BIRTHDAY_VANITY, resetBytes());
        System.out.println("For four characters: " + iterations);



    }

    private static byte[] resetBytes(){
        byte[] resetBytes = new byte[32];
        for(int i = 0; i < resetBytes.length; i++)
            resetBytes[i] = Byte.MIN_VALUE;

        return resetBytes;
    }
    private static int getVanityIterations(String vanityString, byte[] seedBytes){
        int iterations = 0;
        boolean maxReached = false;
        boolean matchFound = false;
        String hexValue;

        while(!maxReached && !matchFound){

            try{
                byte[] hash = hashString(seedBytes);
                hexValue = bytesToHex(hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                iterations = -1;
                break;
            }

            if(seedBytes[0] == Byte.MAX_VALUE){
                System.out. println("We could not find a match");
                maxReached = true;
            }


            //check for vanity match
            matchFound = hexValue.startsWith(vanityString);
            iterations++;
            if(!matchFound)
                seedBytes = iterateBytes(seedBytes);
        }


        return  iterations;
    }

    private static byte[] iterateBytes(byte[] bytesToIterate){
        for(int i = bytesToIterate.length - 1; i >= 0; i--){

            if(bytesToIterate[i] == Byte.MAX_VALUE){

                if(i == 0)
                    break;
                else{
                    bytesToIterate[i] = Byte.MIN_VALUE;
                    continue;
                }

            }
            else{
                bytesToIterate[i]++;
                break;
            }

        }

        return bytesToIterate;
    }

    private static int timeTest(File fileName) {
        long totalTime = 0;
        int iterations = 0;
        boolean secondHasPassed = false;

        if(fileName.exists()){
            while(!secondHasPassed){
                try{
                    totalTime += hashTime(fileName);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    iterations = -1;
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    iterations = -1;
                    break;
                }

                //check for time and count iteration
                long seconds = TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS);

                if( seconds >= 1)
                    secondHasPassed = true;
                else
                    iterations++;
            }
        }
        else
            System.out.println(fileName + "Does not exist");

        return iterations;
    }

    private static long hashTime(File fileName) throws NoSuchAlgorithmException, IOException {
        long timeElapsed = 0;
        if(fileName.exists()){

            byte[] fileBytes = Files.readAllBytes(fileName.toPath());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            //hash file
            timeElapsed = System.nanoTime();
            byte[] encoded = digest.digest(fileBytes);
            timeElapsed = System.nanoTime() - timeElapsed;

        }
        else
            System.out.println(fileName + " does not exist");

        return  timeElapsed;
    }

  private static byte[] hashString(byte[] bytesToHash) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(bytesToHash);

        return hash;

  }

    private static String bytesToHex(byte[] encodedHash){
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);

        for(int i = 0; i < encodedHash.length; i++){
            String hex = Integer.toHexString(0xfff & encodedHash[i]);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
