package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
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
                        } else {
                            for (Object ob : selected.getChildren()) {
                                TreeItem treeItem = (TreeItem) ob;
                                try {
                                    CourseUnit cu = (CourseUnit) treeItem.getValue();

                                    if (Objects.equals(chk.getText(), cu.getName())) {
                                        selected.getChildren().remove(ob);
                                        handleCredits(selected,"remove", cu.getMaxCredits());
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

                                    // commented out VERY aesthetic loading screen implementation
                                    /*
                                    StackPane pane = new StackPane();
                                    pane.setBackground(new Background(bI));
                                    Stage s = new Stage();
                                    s.setTitle("LOADING");
                                    Scene scene = new Scene(pane);
                                    s.setScene(scene);
                                    s.show();
                                    */

                                    publish();
                                    createFieldOfStudyOptions2(degreeBox.getSelectionModel().getSelectedItem().toString());
                                    //createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString());

                                    // s.close();
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

            else {
                // If this error message comes, ActionEvent for that UI object is yet to be implemented
                System.err.println("EVENT NOT RECOGNIZED: " + event.getSource().toString());
            }
        }
    };

    public void createStudentTab2(){

        VBox vBox = new VBox();
        Label studentLabel = new Label("OPISKELIJA");
        vBox.getChildren().add(studentLabel);
        Label degreeField = new Label("Valitse tutkinto-ohjelma:");
        vBox.getChildren().add(degreeField);

        ArrayList<String> degrees = new ArrayList<>();

        degreeGroupIdPairs = mr.getDegreeGroupIdPairs();
        for (Map.Entry<String, String> entry : degreeGroupIdPairs.entrySet()){
            degrees.add(entry.getKey());
        }

        ObservableList<String> oDegrees = FXCollections.observableArrayList(degrees);
        degreeBox = new ComboBox(oDegrees);
        degreeBox.getSelectionModel().selectFirst();
        degreeBox.setOnAction(eh);

        vBox.getChildren().add(degreeBox);

        Label fieldOfStudyLabel = new Label("Valitse opintosuuntaus:");
        vBox.getChildren().add(fieldOfStudyLabel);

        createFieldOfStudyOptions2(degreeBox.getValue().toString());
        vBox.getChildren().add(fieldOfStudyBox);

        studentTab.setText("Opiskelijan tiedot");
        studentTab.setContent(vBox);
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
        fieldOfStudyBox.getSelectionModel().selectFirst();
    }

    public void createStudiesTab(Module degreeProgramme){

        VBox studiesTabLabelVBox = new VBox();
        Label studiesLabel = new Label("OPINTOJEN RAKENNE");
        studiesTabLabelVBox.getChildren().add(studiesLabel);
        HBox studiesTabHBox = new HBox();
        studiesTabLabelVBox.getChildren().add(studiesTabHBox);

        // create TreeView
        treeView = createTreeView((DegreeProgramme) degreeProgramme);
        treeView.setMinWidth(700);
        studiesTabHBox.getChildren().add(treeView);

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

        studiesTabHBox.getChildren().add(studiesTabRightSide);
        studiesTab.setText("Opintojen rakenne");
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
        return rootItem;
    }

    // Get CourseUnits of currently selected StudyModule
    public void getCourses(){
        courses.clear();
        TreeItem module = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        Module md = (Module) module.getValue();
        courses.addAll(md.getSubUnits());

    }

    public void addCheckBox(CourseUnit course){
        CheckBox checkBox = new CheckBox(course.getName());

        // check if checkbox was previously selected
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        for (Object object : item.getChildren()){
            TreeItem treeItem = (TreeItem) object;
            if (Objects.equals(course, treeItem.getValue())){
                checkBox.setSelected(true);
            }
        }
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
    public void start(Stage stage) {

        tabPane = new TabPane();
        tabPane.setMinSize(1500,800);
        createStudentTab2();
        createStudiesTab((Module)fieldOfStudyBox.getSelectionModel().getSelectedItem());
        studentTab.setClosable(false);
        studiesTab.setClosable(false);
        tabPane.getTabs().add(studentTab);
        tabPane.getTabs().add(studiesTab);

        stage.setTitle("SISU");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}