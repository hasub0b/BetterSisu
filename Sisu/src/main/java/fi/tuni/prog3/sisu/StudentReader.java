package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Jyri
 */
public class StudentReader {

    private static final String DEFAULT_PATH = "saveddata/students/";

    public Student read(String directory, String studentId) throws IOException {
        // make sure directory exists
        Path dir = Paths.get(directory);
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Not a valid path: " + directory);
        }

        Path studentFile = dir;

        try ( DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (path.getFileName().toString().startsWith(studentId)) {
                    studentFile = path;
                }
            }
        }

        if (!Files.isRegularFile(studentFile)) {
            throw new IllegalArgumentException("Not a valid student id: " + studentId);
        }
        
        Student student;

        try ( BufferedReader br = Files.newBufferedReader(studentFile)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Module.class, new ModuleAdapter())
                    .create();
            student = gson.fromJson(br, Student.class);
        }

        return student;

    }

    public Student read(String studentId) throws IOException {
        Student s = read(DEFAULT_PATH, studentId);
        return s;
    }

    public Collection<Student> readAll(String directory) throws IOException {
        // make sure directory exists
        Path dir = Paths.get(directory);
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Not a valid path: " + directory);
        }

        List<Path> studentFiles = new ArrayList<>();

        try ( DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (path.getFileName().toString().endsWith(".json")) {
                    studentFiles.add(path);
                }
            }
        }

        Collection<Student> students = new ArrayList<>();
        Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Module.class, new ModuleAdapter())
                    .create();

        for (Path studentFile : studentFiles) {
            try ( BufferedReader br = Files.newBufferedReader(studentFile)) {
                Student student = gson.fromJson(br, Student.class);
                students.add(student);
            }
        }

        return students;
    }

    public Collection<Student> readAll() throws IOException {
        Collection<Student> students = readAll(DEFAULT_PATH);
        return students;
    }
}
