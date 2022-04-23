package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Stores Students as JSON files in local storage.
 *
 * @author Jyri
 */
public class StudentWriter {

    private static final String DEFAULT_PATH = "saveddata/students/";

    /**
     * Saves a single Student as a JSON file to specified directory.
     *
     * @param directory directory to save to
     * @param student Student to save
     * @throws IOException when unable to perform write
     */
    public void write(String directory, Student student) throws IOException {
        // make sure directory exists
        Path dir = Paths.get(directory);
        Files.createDirectories(dir);

        Path studentFile = dir.resolve(student.getStudentId() + ".json");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Module.class, new ModuleAdapter())
                .create();

        try ( BufferedWriter bw = Files.newBufferedWriter(studentFile)) {
            gson.toJson(student, bw);
        }
    }

    /**
     * Uses a default path of saveddata/students/ to save Student file to.
     *
     * @param student Student to save
     * @throws IOException when unable to perform write
     */
    public void write(Student student) throws IOException {
        write(DEFAULT_PATH, student);
    }

    /**
     * Saves every Student in collection to separate JSON files in specified
     * directory.
     *
     * @param directory directory to save to
     * @param students Collection of Students to save
     * @throws IOException when unable to perform write
     */
    public void writeAll(String directory, Collection<Student> students) throws IOException {
        for (Student student : students) {
            write(directory, student);
        }
    }

    /**
     * Uses a default path of saveddata/students/ to save Student files to.
     *
     * @param students Collection of Students to save
     * @throws IOException when unable to perform write
     */
    public void writeAll(Collection<Student> students) throws IOException {
        for (Student student : students) {
            write(DEFAULT_PATH, student);
        }
    }
}
