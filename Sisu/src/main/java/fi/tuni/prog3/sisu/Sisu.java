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

    public void createStudentTab(){

        javafx.scene.text.Font font = new javafx.scene.text.Font(30f);
        javafx.scene.text.Font font1 = new javafx.scene.text.Font(20f);
        // fields for students info
        VBox studentInfo = new VBox();
        studentInfo.setAlignment(Pos.BOTTOM_CENTER);
        Label firstName = new Label("First Name: " + student.getFirstName());
        Label lastName = new Label("Last Name: " + student.getLastName());
        Label studentID = new Label("Student ID: " + student.getStudentId());
        studentInfo.getChildren().addAll(firstName,lastName,studentID);

        VBox vBox = new VBox();
        Label studentLabel = new Label("Hey, " + student.getFirstName() + "!");
        studentLabel.setFont(font);
        Label degreeField = new Label("Choose a degree program:");
        degreeField.setFont(font1);

        ArrayList<String> degrees = new ArrayList<>();

        degreeGroupIdPairs = mr.getDegreeGroupIdPairs();
        for (Map.Entry<String, String> entry : degreeGroupIdPairs.entrySet()){
            degrees.add(entry.getKey());
        }

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

        Label fieldOfStudyLabel = new Label("Choose field:");
        fieldOfStudyLabel.setFont(font1);

        createFieldOfStudyOptions2(degreeBox.getSelectionModel().getSelectedItem().toString());

        vBox.getChildren().addAll(studentLabel,degreeField,degreeBox,fieldOfStudyLabel,fieldOfStudyBox,studentInfo);
        studentTab.setText("STUDENT INFORMATION");
        studentTab.setContent(vBox);
        vBox.setAlignment(Pos.CENTER);
    }

    public void createFieldOfStudyOptions2(String name){
        String groupId = degreeGroupIdPairs.get(name);
        Module degree = mr.buildModule(groupId);
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
        ObservableList<Module> observableListOptions = FXCollections.observableList(options);
        fieldOfStudyBox.setItems(observableListOptions);
        fieldOfStudyBox.setOnAction(eh);

        if (existingProgramme){
            fieldOfStudyBox.getSelectionModel().select(student.getProgramme());
        } else {
            fieldOfStudyBox.getSelectionModel().selectFirst();
        }
    }

    public void createStudiesTab(Module degreeProgramme){
        HBox studiesTabHBox = new HBox();

        // create TreeView
        treeView = createTreeView((DegreeProgramme) degreeProgramme);
        treeView.setMinWidth(700);

        // searchbar
        VBox studiesTabRightSide = new VBox();
        searchbar = new TextField();
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            checkBoxes.getChildren().clear();
            for (CourseUnit course : courses){
                if (course.getName().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))){
                    addCheckBox(course);
                }
            }
        });
        studiesTabRightSide.getChildren().add(searchbar);

        // checkboxes
        checkBoxes.getChildren().clear();
        studiesTabRightSide.getChildren().add(checkBoxes);

        studiesTabHBox.getChildren().addAll(treeView,studiesTabRightSide);
        studiesTab.setText("DEGREE STRUCTURE");
        studiesTab.setContent(studiesTabHBox);
    }

    public TreeView createTreeView(DegreeProgramme dp){
        TreeView treeView = new TreeView();
        TreeItem rootItem = getTree(dp);
        treeView.setRoot(rootItem);

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

    // recursive function used to get TreeItem and it's children
    public TreeItem getTree(Module md){
        TreeItem rootItem = new TreeItem(md);

        for (Module subModule : md.getSubModules()){
            rootItem.getChildren().add(getTree(subModule));
        }

        for (CourseUnit course : md.getSubUnits()){
            if (course.getSelected()){
                TreeItem treeItem = new TreeItem<>(course);
                rootItem.getChildren().add(treeItem);
            }
        }
        return rootItem;
    }

    // Get CourseUnits of currently selected StudyModule
    public void getCourses(){
        courses.clear();
        try {
            TreeItem module = (TreeItem) treeView.getSelectionModel().getSelectedItem();
            Module md = (Module) module.getValue();
            courses.addAll(md.getSubUnits());
        } catch (ClassCastException ignored){
        }
    }

    public void addCheckBox(CourseUnit course){
        CheckBox checkBox = new CheckBox(course.getName());
        Tooltip courseInfo = new Tooltip(
                String.format("Name: %s | Code: %s | Credits: %s",
                        course.getName(),course.getCode(),course.getMaxCredits()));
        checkBox.setTooltip(courseInfo);

        if (course.getSelected()){
            checkBox.setSelected(true);
        }

        // check if checkbox was previously selected
        /*
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        for (Object object : item.getChildren()){
            TreeItem treeItem = (TreeItem) object;
            if (Objects.equals(course, treeItem.getValue())){
                checkBox.setSelected(true);
            }
        }

         */
        checkBox.setOnAction(eh);
        checkBoxes.getChildren().add(checkBox);

    }

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

    @Override
    public void start(Stage stage) throws InterruptedException, IOException {

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                save();
                Platform.exit();
                System.exit(0);
            }
        });

        LoginScreen login = new LoginScreen();

        while (!login.isLoginEntered()){
            Thread.sleep(50);
        }

        if (sr.exists(login.getStudentID())){
            student = sr.read(login.getStudentID());
        } else {
            student = new Student(login.getFirstName(), login.getLastName(), login.getStudentID());

        }
        if (student.hasProgramme()){
            existingProgramme = true;
        }


        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
            @Override
            protected Void doInBackground() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LoadingScreen loadingScreen = new LoadingScreen();

                        // Using AnchorPane to set the save button to always be on bottom right of the screen
                        AnchorPane root = new AnchorPane();
                        AnchorPane.setTopAnchor(tabPane,0.0);
                        AnchorPane.setLeftAnchor(tabPane,0.0);
                        AnchorPane.setRightAnchor(tabPane,0.0);
                        AnchorPane.setBottomAnchor(saveButton,20.0);
                        AnchorPane.setRightAnchor(saveButton, 20.0);


                        saveButton.setTooltip(new Tooltip("Save current status"));
                        saveButton.setPrefSize(100,50);
                        saveButton.setOnAction(eh);
                        tabPane = new TabPane();
                        tabPane.setMinSize(1500,800);
                        createStudentTab();
                        createStudiesTab((Module)fieldOfStudyBox.getSelectionModel().getSelectedItem());
                        studentTab.setClosable(false);
                        studiesTab.setClosable(false);
                        tabPane.getTabs().add(studentTab);
                        tabPane.getTabs().add(studiesTab);


                        stage.setTitle("SISU");

                        root.getChildren().addAll(tabPane,saveButton);
                        Scene scene = new Scene(root);
                        stage.setScene(scene);

                        loadingScreen.dispose();
                        stage.show();
                    }
                });
                return null;
            }
        };
        worker.execute();

    }

    // save current progress
    public void save(){
        StudentWriter sw = new StudentWriter();
        try {
            DegreeProgramme dp = (DegreeProgramme) treeView.getRoot().getValue();
            String groupId = degreeGroupIdPairs.get(degreeBox.getSelectionModel().getSelectedItem().toString());
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