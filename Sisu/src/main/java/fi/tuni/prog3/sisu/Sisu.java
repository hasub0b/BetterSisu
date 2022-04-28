package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 *
 *
 * A Program used to investigate the degree structures of
 * study programmes in Tampere University.
 *
 * @author Hasu
 */

public class Sisu extends Application {

    // ModuleReader to get data from Sisu
    ModuleReader mr = new ModuleReader(new UrlJsonFetcher());
    TreeMap<String, String> degreeGroupIdPairs = new TreeMap<>();

    //StudentReader to handle Student data
    StudentReader sr = new StudentReader();
    boolean existingProgramme = false;

    // Student used to save user status
    Student student;

    // These are UI objects that are updated at different parts of the code,
    // It could clear up the code to have Tabs as their own classes
    TabPane tabPane = new TabPane();
    Tab studentTab = new Tab();
    Tab studiesTab = new Tab();
    ComboBox fieldOfStudyBox = new ComboBox();
    ComboBox degreeBox = new ComboBox();
    TreeView treeView = new TreeView();
    VBox checkBoxes = new VBox();
    TextField searchbar = new TextField();
    Button saveButton = new Button("SAVE");


    // Save all courses of selected TreeItem
    List<CourseUnit> courses = new ArrayList<>();

    // Handle user inputs
    EventHandler eh = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof CheckBox) {
                CheckBox chk = (CheckBox) event.getSource();
                TreeItem selected = (TreeItem) treeView.getSelectionModel().getSelectedItem();
                for (CourseUnit course : courses) {
                    if (Objects.equals(chk.getText(), course.getName())) {
                        if (chk.isSelected()) {
                            TreeItem treeItem = new TreeItem<>(course);
                            selected.getChildren().add(treeItem);
                            handleCredits(selected,"add",course.getMaxCredits());
                            course.setSelected(true);
                        } else {
                            for (Object ob : selected.getChildren()) {
                                TreeItem treeItem = (TreeItem) ob;
                                try {
                                    CourseUnit cu = (CourseUnit) treeItem.getValue();
                                    if (Objects.equals(chk.getText(), cu.getName())) {
                                        selected.getChildren().remove(ob);
                                        handleCredits(selected,"remove", cu.getMaxCredits());
                                        course.setSelected(false);
                                        break;
                                    }
                                } catch (ClassCastException ignored){}
                            }
                        }
                    }
                }
            } else if (event.getSource() instanceof ComboBox) {
                if (event.getSource() == degreeBox) {
                    // Using SwingWorker multithreading to implement loading screen
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

                        @Override
                        protected Void doInBackground() throws Exception {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    SmallLoadingScreen smallLoadingScreen = new SmallLoadingScreen();
                                    smallLoadingScreen.setVisible(true);
                                    smallLoadingScreen.setAlwaysOnTop(true);
                                    createFieldOfStudyOptions2(degreeBox.getSelectionModel().getSelectedItem().toString());
                                    smallLoadingScreen.dispose();

                                }
                            });
                            return null;
                        }
                    };
                    worker.execute();
                } else if (event.getSource() == fieldOfStudyBox) {
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

                        @Override
                        protected Void doInBackground() throws Exception {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // --- Loading screen on ---
                                    publish();
                                    createStudiesTab((Module) fieldOfStudyBox.getSelectionModel().getSelectedItem());
                                    // --- Loading screen off ---
                                }
                            });

                            return null;
                        }
                    };
                    worker.execute();
                }
            } else if(event.getSource() instanceof TextField){
                // Searchbar listener defined in createStudiesTab
            }
            // Save button
            else if (event.getSource() instanceof Button){
                save();
            }

            else {
                // If this error message comes, ActionEvent for that UI object is yet to be implemented
                System.err.println("EVENT NOT RECOGNIZED: " + event.getSource().toString());
            }
        }
    };

    /**
     *
     * Update the first tab where degree and field of study can be chosen by the user, also display info about current Student
     */
    public void createStudentTab(){

        // Set fonts
        javafx.scene.text.Font font = new javafx.scene.text.Font(30f);
        javafx.scene.text.Font font1 = new javafx.scene.text.Font(20f);

        // fields for Students info
        VBox studentInfo = new VBox();
        studentInfo.setAlignment(Pos.BOTTOM_CENTER);
        Label firstName = new Label("First Name: " + student.getFirstName());
        Label lastName = new Label("Last Name: " + student.getLastName());
        Label studentID = new Label("Student ID: " + student.getStudentId());
        studentInfo.getChildren().addAll(firstName,lastName,studentID);
        VBox vBox = new VBox();
        Label studentLabel = new Label("Hey, " + student.getFirstName() + "!");
        studentLabel.setFont(font);

        // Degrees
        Label degreeField = new Label("Choose a degree program:");
        degreeField.setFont(font1);

        // Get all the degrees from Sisu with ModuleReader
        ArrayList<String> degrees = new ArrayList<>();
        degreeGroupIdPairs = mr.getDegreeGroupIdPairs();
        for (Map.Entry<String, String> entry : degreeGroupIdPairs.entrySet()){
            degrees.add(entry.getKey());
        }
        // Transfer Arraylist to ObservableList and add it to the ComboBox
        ObservableList<String> oDegrees = FXCollections.observableArrayList(degrees);
        degreeBox = new ComboBox(oDegrees);
        if (existingProgramme){
            for (Map.Entry<String, String> entry : degreeGroupIdPairs.entrySet()){
                if (Objects.equals(entry.getValue(), student.getDegreeId())){
                    degreeBox.getSelectionModel().select(entry.getKey());
                }
            }
        } else {
            degreeBox.getSelectionModel().selectFirst();
        }
        degreeBox.setOnAction(eh);

        // Fields of study
        Label fieldOfStudyLabel = new Label("Choose field:");
        fieldOfStudyLabel.setFont(font1);
        createFieldOfStudyOptions2(degreeBox.getSelectionModel().getSelectedItem().toString());

        // Add everything to studentTab
        vBox.getChildren().addAll(studentLabel,degreeField,degreeBox,fieldOfStudyLabel,fieldOfStudyBox,studentInfo);
        studentTab.setText("STUDENT INFORMATION");
        studentTab.setContent(vBox);
        vBox.setAlignment(Pos.CENTER);
    }

    /**
     * Update fieldsOfStudyBox with given degree name
     *
     * @param name Name of the currently selected DegreeProgramme
     */
    public void createFieldOfStudyOptions2(String name){
        // Get groupId with given name
        String groupId = degreeGroupIdPairs.get(name);
        Module degree = mr.buildModule(groupId);

        // Create ArrayList of possible fields under the given degree
        ArrayList<Module> options = new ArrayList<>();
        for (Module subModule : degree.getSubModules()){

            // Assuming field of study have same max credits as the degree
            // and all StudyModules have lower max credits than the degree
            if ((subModule instanceof DegreeProgramme) && (degree instanceof DegreeProgramme)){
                if (((DegreeProgramme) subModule).getMaxCredits() == ((DegreeProgramme) degree).getMaxCredits()){
                    options.add(subModule);
                }
            }
        }

        // Degree had no fields
        if (options.isEmpty()){
            options.add(degree);
        }
        // ArrayList to ObservableList and add it to ComboBox
        ObservableList<Module> observableListOptions = FXCollections.observableList(options);
        fieldOfStudyBox.setItems(observableListOptions);
        fieldOfStudyBox.setOnAction(eh);

        // Choose witch option is selected by default
        if (existingProgramme){
            fieldOfStudyBox.getSelectionModel().select(student.getProgramme());
        } else {
            fieldOfStudyBox.getSelectionModel().selectFirst();
        }
    }

    /**
     *
     * Update the second tab containing the TreeView and course selection
     *
     * @param degreeProgramme The DegreeProgramme chosen on the fieldsOfStudyBox
     */
    public void createStudiesTab(Module degreeProgramme){
        HBox studiesTabHBox = new HBox();

        // create TreeView
        treeView = createTreeView((DegreeProgramme) degreeProgramme);
        treeView.setMinWidth(700);

        // searchbar
        VBox studiesTabRightSide = new VBox();
        searchbar = new TextField();
        // Add listener to the TextField
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Update shown CheckBoxes depending on the text entered to the TextField
            checkBoxes.getChildren().clear();
            for (CourseUnit course : courses){
                if (course.getName().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))){
                    addCheckBox(course);
                }
            }
        });
        studiesTabRightSide.getChildren().add(searchbar);

        // Checkboxes
        checkBoxes.getChildren().clear();
        studiesTabRightSide.getChildren().add(checkBoxes);

        // Add everything to studiesTab
        studiesTabHBox.getChildren().addAll(treeView,studiesTabRightSide);
        studiesTab.setText("DEGREE STRUCTURE");
        studiesTab.setContent(studiesTabHBox);
    }

    /**
     * Create the TreeView component
     *
     * @param dp DegreeProgramme chosen on the fieldsOfStudyBox
     * @return TreeView of the degree structure
     */
    public TreeView createTreeView(DegreeProgramme dp){

        // Set DegreeProgramme as root and get rest of the tree
        TreeView treeView = new TreeView();
        TreeItem rootItem = getTree(dp);
        treeView.setRoot(rootItem);

        // Listener to update shown CheckBoxes based on selected TreeItem
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            courses.clear();
            checkBoxes.getChildren().clear();
            getCourses();
            for (CourseUnit course : courses){
                addCheckBox(course);
            }
        });

        return treeView;
    }

    /**
     * Recursive function used to get TreeItem and it's children
     *
     * @param md Module to be transfer to TreeItem and whose submodules and subunits will be added as TreeItems under it
     * @return TreeItem of the given Module
     */
    public TreeItem getTree(Module md){
        TreeItem rootItem = new TreeItem(md);

        // Add all SubModules
        for (Module subModule : md.getSubModules()){
            rootItem.getChildren().add(getTree(subModule));
        }

        // Add all CourseUnits
        for (CourseUnit course : md.getSubUnits()){
            if (course.getSelected()){
                TreeItem treeItem = new TreeItem<>(course);
                rootItem.getChildren().add(treeItem);
            }
        }
        return rootItem;
    }

    /**
     * Get CourseUnits of currently selected StudyModule
     * And update them to courses
     *
     */
    public void getCourses(){
        courses.clear();
        try {
            TreeItem module = (TreeItem) treeView.getSelectionModel().getSelectedItem();
            Module md = (Module) module.getValue();
            courses.addAll(md.getSubUnits());
        } catch (ClassCastException ignored){
        }
    }

    /**
     * Add CheckBox of the given course
     *
     * @param course CourseUnit
     */
    public void addCheckBox(CourseUnit course){
        // Add CheckBox and ToolTip
        CheckBox checkBox = new CheckBox(course.getName());
        Tooltip courseInfo = new Tooltip(
                String.format("Name: %s | Code: %s | Credits: %s",
                        course.getName(),course.getCode(),course.getMaxCredits()));
        checkBox.setTooltip(courseInfo);

        // Set CheckBox selection based on if the course was previously selected
        if (course.getSelected()){
            checkBox.setSelected(true);
        }

        checkBox.setOnAction(eh);
        checkBoxes.getChildren().add(checkBox);
    }

    /**
     * Update DegreePrograms currentCredits when CourseUnit under it was selected
     *
     * @param treeItem TreeItem of the DegreeProgramme to be updated
     * @param operand String, "add" if course was selected and credits added, "remove" if course was deselected and credits removed
     * @param credits int credits of the course
     */
    public void handleCredits(TreeItem treeItem, String operand, int credits){
        switch (operand){
            case "add":
                try {
                    DegreeProgramme dp = (DegreeProgramme) treeItem.getValue();
                    dp.addCredits(credits);
                    if (treeItem.getParent() != null){
                        handleCredits(treeItem.getParent(),operand,credits);
                    }
                } catch (ClassCastException cce){
                    // TreeItem is not a DegreeProgramme
                    if (treeItem.getParent() != null){
                        handleCredits(treeItem.getParent(),operand,credits);
                    }
                }
                break;
            case "remove":
                try {
                    DegreeProgramme dp = (DegreeProgramme) treeItem.getValue();
                    dp.removeCredits(credits);
                    if (treeItem.getParent() != null){
                        handleCredits(treeItem.getParent(),operand,credits);
                    }
                } catch (ClassCastException cce){
                    // TreeItem is not a DegreeProgramme
                    if (treeItem.getParent() != null){
                        handleCredits(treeItem.getParent(),operand,credits);
                    }
                }
                break;
        }
    }

    /**
     * Starts the main displayed stage
     *
     * @param stage Stage to be displayed
     * @throws InterruptedException thrown if a Thread is interrupted
     * @throws IOException thrown if there is a problem while reading or writing JSON-files
     */
    @Override
    public void start(Stage stage) throws InterruptedException, IOException {

        // Set action to be performed when user tries to close the program
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                save();
                Platform.exit();
                System.exit(0);
            }
        });

        // Show login screen
        LoginScreen login = new LoginScreen();
        while (!login.isLoginEntered()){
            Thread.sleep(50);
        }

        // Login entered, create a Student or get a saved one
        if (sr.exists(login.getStudentID())){
            student = sr.read(login.getStudentID());
        } else {
            student = new Student(login.getFirstName(), login.getLastName(), login.getStudentID());

        }
        if (student.hasProgramme()){
            existingProgramme = true;
        }

        // SwingWorker used to MultiThread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        // Show LoadingScreen
                        LoadingScreen loadingScreen = new LoadingScreen();

                        // Start to prepare main program window

                        // Using AnchorPane to set the save button to always be on bottom right of the screen
                        AnchorPane root = new AnchorPane();
                        AnchorPane.setTopAnchor(tabPane,0.0);
                        AnchorPane.setLeftAnchor(tabPane,0.0);
                        AnchorPane.setRightAnchor(tabPane,0.0);
                        AnchorPane.setBottomAnchor(saveButton,20.0);
                        AnchorPane.setRightAnchor(saveButton, 20.0);

                        // Setup Save-button
                        saveButton.setTooltip(new Tooltip("Save current status"));
                        saveButton.setPrefSize(100,50);
                        saveButton.setOnAction(eh);

                        // Setup TabPane and all the Tabs
                        tabPane = new TabPane();
                        tabPane.setMinSize(1500,800);
                        createStudentTab();
                        createStudiesTab((Module)fieldOfStudyBox.getSelectionModel().getSelectedItem());
                        studentTab.setClosable(false);
                        studiesTab.setClosable(false);
                        tabPane.getTabs().add(studentTab);
                        tabPane.getTabs().add(studiesTab);

                        // Add all to AnchorPane, Create a new scene and add it to the stage
                        root.getChildren().addAll(tabPane,saveButton);
                        Scene scene = new Scene(root);
                        stage.setTitle("SISU");
                        stage.setScene(scene);

                        // Close the LoadingScreen
                        loadingScreen.dispose();

                        // Finally, show the stage
                        stage.show();
                    }
                });
                return null;
            }
        };
        worker.execute();

    }

    //

    /**
     * Save current degree and selected courses using StudentWriter to a JSON-file
     */
    public void save(){
        StudentWriter sw = new StudentWriter();
        try {
            // The DegreeProgramme chosen on the fieldsOfStudyBox
            DegreeProgramme dp = (DegreeProgramme) treeView.getRoot().getValue();
            // Save the DegreeProgramme on the degreeBox in a case there were multiple options on the fieldsOfStudyBox
            String groupId = degreeGroupIdPairs.get(degreeBox.getSelectionModel().getSelectedItem().toString());

            // Set all to the current Student and write the Student to a file
            student.setDegreeId(groupId);
            student.setProgramme(dp);
            sw.write(student);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}