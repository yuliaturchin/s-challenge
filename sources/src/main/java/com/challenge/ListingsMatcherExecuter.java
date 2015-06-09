package com.challenge;

import com.challenge.entities.Result;
import com.challenge.match.impl.ListingsMatcherImpl;
import com.challenge.utils.json.JSONUtils;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ListingsMatcherExecuter {

    /**
     * This program executes match between Listings and the provided Products.
     * The program expects for 3 arguments representing three file paths:
     * 1 Products file,
     * 2 Listings file,
     * 3 Results file to which to fetch the results
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args)  {
        try {

            long startTime =System.currentTimeMillis();
            if (args.length<3)
            {
                System.out.println("Please provide input and output files paths: " +
                        "java -jar [jar] [products] [listings] [results]");
                return;
            }

            String productsFile = args[0];
            String listingsFile = args[1];
            String outputFile = args[2];

            BufferedReader productsReader = getReader (productsFile);
            BufferedReader listingsReader =  getReader (listingsFile);

            Map<String, Result> product2Results = new ListingsMatcherImpl().executeMatch(productsReader, listingsReader);
            writeResults(product2Results, outputFile);

            System.out.println("********************************");
            System.out.println("Finished all...total time:  " + (System.currentTimeMillis()-startTime) + " msec");

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static BufferedReader getReader(String file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        if (fileReader == null)
        {
            System.err.println("Error: didn't find file on the path " + file);
            System.exit(0);
        }

        BufferedReader productsReader = new BufferedReader(fileReader);
        return productsReader;
    }


    private static void writeResults(Map<String, Result> product2Results, String fileout) throws IOException {
        String url = new File(fileout).getAbsolutePath();
        BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(url));

        for (Result result: product2Results.values())
        {
            String resultString = JSONUtils.convertToJsonString(result, Result.class);
            bufferedWriter.write(resultString, 0, resultString.length());
            bufferedWriter.newLine();
        }
    }



}