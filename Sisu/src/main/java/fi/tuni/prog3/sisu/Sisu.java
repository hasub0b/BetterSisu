package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;


/**
 * DEMO
 * Any methods implemented for real use NOT guaranteed to work.
 *
 *
 * A Program used to investigate the degree structures of
 * study programmes in Tampere University.
 *
 * @author Hasu
 */


public class Sisu extends Application {

    ModuleReader mr = new ModuleReader(new UrlJsonFetcher());

    Tab studentTab = new Tab();
    Tab studiesTab = new Tab();
    ComboBox fieldOfStudyBox = new ComboBox();
    ComboBox degreeBox = new ComboBox();
    TreeView treeView = new TreeView();
    VBox checkBoxes = new VBox();
    TextField searchbar = new TextField();
    List<TreeItem> courses = new ArrayList<>();

    TreeMap<String, String> degreeGroupIdPairs = new TreeMap<>();

    EventHandler eh = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof CheckBox) {
                CheckBox chk = (CheckBox) event.getSource();
                TreeItem selected = (TreeItem) treeView.getSelectionModel().getSelectedItem();
                for (TreeItem course : courses) {
                    if (Objects.equals(chk.getText(), course.getValue())) {
                        if (chk.isSelected()) {
                            selected.getChildren().add(course);
                        } else {
                            for (Object ob : selected.getChildren()) {
                                TreeItem treeOb = (TreeItem) ob;
                                System.out.println(treeOb.getValue());
                                System.out.println(chk.getText());
                                if (Objects.equals(chk.getText(), treeOb.getValue())) {
                                    selected.getChildren().remove(ob);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (event.getSource() instanceof ComboBox) {
                if (event.getSource() == degreeBox) {
                    // createFieldOfStudyOptions(degreeBox.getSelectionModel().getSelectedItem().toString());
                    createFieldOfStudyOptions2(degreeBox.getSelectionModel().getSelectedItem().toString());
                } else if (event.getSource() == fieldOfStudyBox) {
                    createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString());
                }
            } else if (event.getSource() instanceof TextField){

            }

            else {
                System.out.println("EVENT NOT RECOGNIZED");
            }
        }
    };



    @Override
    public void start(Stage stage) {

        TabPane tabPane = new TabPane();
        //createStudentTab();
        createStudentTab2();
        createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString());
        tabPane.getTabs().add(studentTab);
        tabPane.getTabs().add(studiesTab);


        stage.setTitle("SISU");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();

        // degreeBox.setOnAction((e) -> createFieldOfStudyOptions(degreeBox.getSelectionModel().getSelectedItem().toString()));
        // fieldOfStudyBox.setOnAction((e) -> createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString()));

        // treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
        //    System.out.println("Action on "+ newValue);
        //    TreeItem selected = (TreeItem) newValue;
        //    checkBoxes.getChildren().clear();
        //    if (selected.toString().contains("StudyModule")){
        //        getCourses();
        //        for (TreeItem course : courses){
        //            addCheckBox(course.getValue().toString());
        //        }
        //    }
        // });


    }

    // only for demo
    /*
    public void createStudentTab(){

        VBox vBox = new VBox();
        Label studentLabel = new Label("OPISKELIJA");
        vBox.getChildren().add(studentLabel);

        Label degreeField = new Label("Valitse tutkinto-ohjelma:");
        vBox.getChildren().add(degreeField);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Option 1",
                        "Option 2",
                        "Option 3"
                );

        degreeBox = new ComboBox(options);
        degreeBox.getSelectionModel().selectFirst();
        degreeBox.setOnAction(eh);

        vBox.getChildren().add(degreeBox);

        Label fieldOfStudyLabel = new Label("Valitse opintosuuntaus:");
        vBox.getChildren().add(fieldOfStudyLabel);

        createFieldOfStudyOptions(degreeBox.getValue().toString());
        vBox.getChildren().add(fieldOfStudyBox);

        studentTab.setText("Opiskelijan tiedot");
        studentTab.setContent(vBox);
    }*/

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

    // only  for demo
    /*
    public void createFieldOfStudyOptions(String option){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        option+" a",
                        option+" b"
                );
        fieldOfStudyBox.setItems(options);
        fieldOfStudyBox.setOnAction(eh);
        fieldOfStudyBox.getSelectionModel().selectFirst();
    }*/
    public void createFieldOfStudyOptions2(String name){
        String groupId = degreeGroupIdPairs.get(name);
        Module degree = mr.buildModule(groupId);
        ArrayList<String> options = new ArrayList<>();
        for (Module subModule : degree.getSubModules()){
            options.add(subModule.getName());
        }
        ObservableList<String> observableListOptions = FXCollections.observableList(options);
        fieldOfStudyBox.setItems(observableListOptions);
        fieldOfStudyBox.setOnAction(eh);
        fieldOfStudyBox.getSelectionModel().selectFirst();

    }

    // only for demo
    public void createStudiesTab(String degreeProgramme){
        VBox studiesTabLabelVBox = new VBox();
        Label studiesLabel = new Label("OPINTOJEN RAKENNE");
        studiesTabLabelVBox.getChildren().add(studiesLabel);
        HBox studiesTabHBox = new HBox();
        studiesTabLabelVBox.getChildren().add(studiesTabHBox);

        // create TreeView
        treeView = createTreeView(degreeProgramme);
        studiesTabHBox.getChildren().add(treeView);

        // searchbar
        VBox studiesTabRightSide = new VBox();
        searchbar = new TextField();
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            checkBoxes.getChildren().clear();
            for (TreeItem course : courses){
                if (course.getValue().toString().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))){
                    addCheckBox(course.getValue().toString());
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

    // only for demo
    public TreeView createTreeView(String degreeProgramme){
        TreeView treeView = new TreeView();
        treeView.setPrefSize(250,500);
        TreeItem rootItem = new TreeItem(degreeProgramme);

        for (int i = 0; i < 4; i++) {
            TreeItem studyModule = new TreeItem("StudyModule "+i);
            rootItem.getChildren().add(studyModule);
        }
        treeView.setRoot(rootItem);
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            TreeItem selected = (TreeItem) newValue;
            checkBoxes.getChildren().clear();
            if (selected.toString().contains("StudyModule")){
                getCourses();
                for (TreeItem course : courses){
                    addCheckBox(course.getValue().toString());
                }
            }
        });
        return treeView;
    }

    public void getCourses(){
        courses.clear();

        TreeItem treeI = (TreeItem) treeView.getSelectionModel().selectedItemProperty().get();
        for (int j = 0; j < 9; j++) {
            TreeItem subModule = new TreeItem(  "/CourseUnit/"+j);
            courses.add(subModule);
            //treeI.getChildren().add(subModule);
        }
        /*
        for (Object child : treeItem.getChildren()){
            TreeItem treeChild = (TreeItem) child;
            if (treeChild.isLeaf()){
                courses.add(treeChild);
            }
        }*/
    }

    public void addCheckBox(String course){
        CheckBox checkBox = new CheckBox(course);
        checkBox.setText(course);

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




    public static void main(String[] args) {
        launch();
    }

}