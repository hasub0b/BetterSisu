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
 *
 * @author Jyri
 */
public class StudentWriter {

    private static final String DEFAULT_PATH = "saveddata/students/";

    public void write(String directory, Student student) throws IOException {
        // make sure directory exists
        Path dir = Paths.get(directory);
        Files.createDirectories(dir);

        Path studentFile = dir.resolve(student.getStudentId() + ".json");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try ( BufferedWriter bw = Files.newBufferedWriter(studentFile)) {
            gson.toJson(student, bw);
        }
    }

    public void write(Student student) throws IOException {
        write(DEFAULT_PATH, student);
    }

    public void writeAll(Collection<Student> students) throws IOException {
        for (Student student : students) {
            write(DEFAULT_PATH, student);
        }
    }

    public void writeAll(String directory, Collection<Student> students) throws IOException {
        for (Student student : students) {
            write(directory, student);
        }
    }
}
