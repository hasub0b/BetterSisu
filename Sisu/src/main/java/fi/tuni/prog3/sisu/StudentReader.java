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
 * Creates Students from JSON files.
 *
 * @author Jyri
 */
public class StudentReader {

    private static final String DEFAULT_PATH = "saveddata/students/";

    /**
     * Reads a Student with given id from directory.
     *
     * @param directory directory to read from
     * @param studentId id for Student to read
     * @return Student with attributes specified in JSON file
     * @throws IOException when unable to perform read operation
     */
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Module.class, new ModuleAdapter())
                .create();

        try ( BufferedReader br = Files.newBufferedReader(studentFile)) {
            student = gson.fromJson(br, Student.class);
        }

        return student;

    }

    /**
     * Reads a Student with given id from saveddata/students/.
     *
     * @param studentId id for Student to read
     * @return Student with attributes specified in JSON file
     * @throws IOException when unable to perform read operation
     */
    public Student read(String studentId) throws IOException {
        Student s = read(DEFAULT_PATH, studentId);
        return s;
    }

    /**
     * Reads all Students found in directory to a collection.
     *
     * @param directory directory to read from
     * @return a Collection of Students
     * @throws IOException when unable to perform read operation
     */
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

    /**
     * Reads all Students found in saveddata/students/ to a collection.
     *
     * @return a Collection of Students
     * @throws IOException when unable to perform read operation
     */
    public Collection<Student> readAll() throws IOException {
        Collection<Student> students = readAll(DEFAULT_PATH);
        return students;
    }

    /**
     * Checks whether a Student with given id exists under saveddata/students/.
     *
     * @param studentId id for Student to find
     * @return true if found, false otherwise
     * @throws IOException when unable to perform read operation
     */
    public Boolean exists(String studentId) throws IOException {
        return exists(DEFAULT_PATH, studentId);
    }

    /**
     * Checks whether Student exists in specified directory.
     *
     * @param directory directory to search
     * @param studentId id for Student to find
     * @return true if found, false otherwise
     * @throws IOException when unable to perform read operation
     */
    public Boolean exists(String directory, String studentId) throws IOException {
        Collection<Student> students = readAll(directory);
        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                return true;
            }
        }
        return false;
    }
}
