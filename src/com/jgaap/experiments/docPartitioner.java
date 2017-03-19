package com.jgaap.experiments;

import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;

/**
 * Separates the given document into specified smaller parts, making multiple
 * smaller documents from the original. Assumes .txt
 * 
 * @author Derek S. Prijatelj
 */
protected class docPartioner{
    
    /**
     * Strict partition of documents. Every partition has exactly the words
     * specified, otherwise is discarded from partition set.
     *
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath relative path to export documents
     */
    protected static void strict(int wordCount, String dirOfDocs,
            String exportPath){
        partition(wordCount, dirOfDocs, exportPath, true);
    }

    /**
     * Strict partition of documents. Every partition has exactly the words
     * specified, otherwise is discarded from partition set.
     *
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath relative path to export documents
     */
    protected static void lenient(int wordCount, String dirOfDocs,
            String exportPath){
        partition(wordCount, dirOfDocs, exportPath, false);
    }

    /**
     * Find and access directory of documents
     * 
     * @param dirOfDocs relative path to directory of documents for partitioning
     * @return File object of directory of documents, null if path is not to a
     * directory.
     */
    private static File accessDir(String dirOfDocs){
        try{
            File dir = new File(dirOfDocs);
            
            if (!dir.isDirectory()){
                throw new NotADirectory(
                    "The submitted path is not a directory. Must be given a "
                    + "directory containing the plain text documents to be "
                    + "partitioned."
                    );
                dir = null;
            }
            
            return dir;
        } catch (IOException | NotADirectory e) {
            e.printStackTrace();
        }
    }

    /**
     * Partitions the documents in the provided relative file path based on the
     * indicated word count per partiton and stores the resulting partitions
     * into the desired relative file export path.
     * 
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath path to export documents
     * @param strict if true, strict partitioning occurs where every partition
     * has exactly the words specified and all partitions that have less words
     * are discarded. If false, documents are partitioned the same, but if a
     * partition has less words than indicated it will still be saved.
     */
    private static void partition(int wordCount, String dirOfDocs,
            String exportPath, boolean strict){
        File dir = accessDir(dirOfDocs);

        // loop through all files in dir, and execute partitionFile() if .txt
        for (final File file : dir.listFiles()){
            if (!file.isFile() || !file.getName().endsWith(".txt")){
                continue;
            }
            partitionFile(wordCount, file, exportPath, strict);
        }
    }

    // save sub-docs in export dir w/ corresponding sub-dir and naming
    /**
     * partitions the given plain text file into multipl plain text files with
     * at maximum as many words in the file as specified
     *
     * @param wordCount max number of words in plain text file
     * @param file the file being partitioned
     * @param exportPath relative path to export the partitioned files
     * @param strict if strict, only files with exactly the specified amount of
     * words will be created.
     */
    private static void partitionFile(int wordCount, File file,
            String exportPath, boolean strict){
        Scanner sc = new Scanner(file);

        File subDir = createSubDir(file, exportPath);
        int numWords = 0, subDocNum = 1;
        File subDoc = new File(subDir.getPath() + "_" + subDocNum + ".txt");
        boolean makingWord = false;
        char c;

        try {
            PrintWriter writer = new PrintWriter(subDoc, "UTF-8");

            while(sc.hasNext()){
                c = sc.nextChar(); // TODO read char by char, Scanner does not do this. Buffered Reader pls.
                writer.write(c);

                if (isAlphaNum(c) && !makingWord){
                    makingWord = true;
                } else if (!isAlphaNum(c) && makingWord){
                    numWords++;
                    makingWord = false;
                }

                if (numWords >= wordCount){
                    writer.close();
                    subDocNum++;
                    subDoc = new File(subDir.getPath() + "_" + subDocNum
                        + ".txt");
                    writer = new PrintWriter(subDoc, "UTF-8");
                    numWords = 0;
                }
            }
            
            writer.close();

            if (numWords != 0 && strict){
                subDoc.delete();
            } 
        } catch (IOException e){
            e.printStackTrace();
        }
        sc.close();
    }

    // What about hyphenated words? and other special cases?
    private static boolean isAlphaNum(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
            ||c >= '0' && c <= '9';
    }

    /**
     * Creates the sub directory and it's parent directory, if they do not
     * already exist.
     */
    private static File createSubDir(File file, String exportPath){
        File export = new File(exportPath);

        if (!export.isDirectory()){
            export.mkdir();
        }
        
        String name = file.getName();
        File subDir = new File(export, name.substring(0, name.length()-4));

        if (!subDir.isDirectory()){
            file.mkdir();
        }

        return subDir;
    }
    
    /**
     * Command Line Interface
     */
    protected static void cli(String[] args){
    
    }

    public static void main(String[] args){
       cli(args); 
    }
}
