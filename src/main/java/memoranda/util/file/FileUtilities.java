package memoranda.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ryan Dinaro
 * @version 7/3/2023
 * This Class holds useful file methods
 */
public class FileUtilities {
    /**
     * Retrieves a saved list and returns the List
     * @param filePath where to save
     * @param classType makes the method generic
     * @return the saved list
     * @param <T> the type of the saved data
     */
    public static <T> List<T> populateList(String filePath, Class<T> classType) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        File studentFile = new File(filePath);
        try {
            fileInputStream = new FileInputStream(studentFile);
            objectInputStream = new ObjectInputStream(fileInputStream);

        } catch (FileNotFoundException e) {
            //File not found
            try {
                //Create a file and return no need to populate list
                if(studentFile.createNewFile()) {
                    return null;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch(EOFException e1) {
            return new ArrayList<T>(); //EmptyFile
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return (ArrayList<T>) Objects.requireNonNull(objectInputStream).readObject();
        } catch (EOFException e) {
            //End of stream
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Saves the list to the declared File path
     * @param filePath the path to be saved
     * @param list the list to be saved
     * @param <T> the type of list to save
     */
    public static <T> void saveList(String filePath, List<T> list) {
        while (true) {
            try {
                FileOutputStream studentWriter = new FileOutputStream(filePath, false);
                ObjectOutputStream obj = new ObjectOutputStream(studentWriter);

                // Write the entire studentList to the file
                obj.writeObject(list);

                obj.close();
            } catch (IOException e) {
                try {
                    File file = new File(filePath);
                    if(file.createNewFile()) {
                        continue;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            break;

        }
    }
}
