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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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


    Tab studentTab = new Tab();
    Tab studiesTab = new Tab();
    ComboBox fieldOfStudyBox = new ComboBox();
    ComboBox degreeBox = new ComboBox();
    TreeView treeView = new TreeView();
    VBox checkBoxes = new VBox();
    List<TreeItem> courses = new ArrayList<>();


    EventHandler eh = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof CheckBox) {
                CheckBox chk = (CheckBox) event.getSource();
                System.out.println("action on "+chk.toString());
                TreeItem selected = (TreeItem) treeView.getSelectionModel().getSelectedItem();
                for (TreeItem course : courses){
                    if (Objects.equals(chk.getText(), course.getValue())){
                        if (chk.isSelected()){
                            selected.getChildren().add(course);
                        } else {
                            System.out.println("remove");
                            for (Object ob : selected.getChildren())
                            {
                                TreeItem treeOb = (TreeItem) ob;
                                System.out.println(treeOb.getValue());
                                System.out.println(chk.getText());
                                if (Objects.equals(chk.getText(), treeOb.getValue())){
                                    selected.getChildren().remove(ob);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    };





    @Override
    public void start(Stage stage) {

        TabPane tabPane = new TabPane();
        createStudentTab();
        createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString());
        tabPane.getTabs().add(studentTab);
        tabPane.getTabs().add(studiesTab);


        stage.setTitle("SISU");
        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.show();

        degreeBox.setOnAction((e) -> {
            createFieldOfStudyOptions(degreeBox.getSelectionModel().getSelectedItem().toString());
        });
        fieldOfStudyBox.setOnAction((e) -> {
            createStudiesTab(fieldOfStudyBox.getSelectionModel().getSelectedItem().toString());
        });

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


    }

    // only for demo
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

        vBox.getChildren().add(degreeBox);

        Label fieldOfStudyLabel = new Label("Valitse opintosuuntaus:");
        vBox.getChildren().add(fieldOfStudyLabel);

        createFieldOfStudyOptions(degreeBox.getValue().toString());
        vBox.getChildren().add(fieldOfStudyBox);

        studentTab.setText("Opiskelijan tiedot");
        studentTab.setContent(vBox);
    }

    // only  for demo
    public void createFieldOfStudyOptions(String option){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        option+" a",
                        option+" b"
                );
        fieldOfStudyBox.setItems(options);
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

        // searchbar and checkbox menu
        VBox studiesTabRightSide = new VBox();
        TextField searchbar = new TextField();
        studiesTabRightSide.getChildren().add(searchbar);

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

    // only for demo
    public VBox createCheckbox(String module){
        checkBoxes = new VBox();
        for (int i = 0; i < 6; ++i){
            CheckBox checkBox = new CheckBox(module+i);
            checkBoxes.getChildren().add(checkBox);

        }
        return checkBoxes;
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

    public Tab createStudentTab(Module module){
        Tab studentTab = new Tab("Opiskelijan teidot");

        VBox vBox = new VBox();
        Label studentLabel = new Label("OPISKELIJA");
        vBox.getChildren().add(studentLabel);

        Label degreeField = new Label("Valitse tutkinto-ohjelma:");
        vBox.getChildren().add(degreeField);
        ObservableList<Module> options = getOptions(module);
        ComboBox degreeBox = new ComboBox(options);
        vBox.getChildren().add(degreeBox);

        Label fieldOfStudyLabel = new Label("Valitse opintosuuntaus:");
        vBox.getChildren().add(fieldOfStudyLabel);
        ComboBox fieldOfStudyBox = new ComboBox();
        vBox.getChildren().add(fieldOfStudyBox);

        studentTab.setContent(vBox);

        return studentTab;
    }

    public ObservableList<Module> getOptions(Module module){
        ObservableList<Module> options = FXCollections.observableArrayList(module.getSubModules());

        return options;
    }

    public Tab createStudiesTab(DegreeProgramme degreeProgramme){
        Tab studiesTab = new Tab("Opintojen rakenne");
        VBox studiesTabLabelVBox = new VBox();
        Label studiesLabel = new Label("OPINTOJEN RAKENNE");
        studiesTabLabelVBox.getChildren().add(studiesLabel);
        HBox studiesTabHBox = new HBox();
        studiesTabLabelVBox.getChildren().add(studiesTabHBox);

        // create TreeView
        TreeView treeView = createTreeView(degreeProgramme);
        treeView.setPrefSize(300,500);
        studiesTabHBox.getChildren().add(treeView);

        // searchbar and checkbox menu
        VBox studiesTabRightSide = new VBox();
        TextField searchbar = new TextField();
        studiesTabRightSide.getChildren().add(searchbar);

        VBox checkBox = createCheckbox(degreeProgramme);
        studiesTabRightSide.getChildren().add(checkBox);

        studiesTabHBox.getChildren().add(studiesTabRightSide);
        studiesTab.setContent(studiesTabHBox);

        return studiesTab;
    }

    public TreeView createTreeView(DegreeProgramme degreeProgramme){

        TreeView treeView = new TreeView();
        treeView.setPrefSize(250,500);
        TreeItem rootItem = createTreeItem(degreeProgramme);
        treeView.setRoot(rootItem);

        return treeView;
    }

    public TreeItem createTreeItem(Module module){
        TreeItem treeItem = new TreeItem(module.getName());

        if (!module.getSubModules().isEmpty()){
            for(Module subModule : module.getSubModules()){
                createTreeItem(subModule);
            }
        }
        if (!module.getSubUnits().isEmpty()){
            for (CourseUnit courseUnit : module.getSubUnits()) {
                treeItem.getChildren().add(new TreeItem(courseUnit.getName()));
            }
        }
        return treeItem;
    }

    public VBox createCheckbox(Module module){
        VBox vBox = new VBox();
        for (CourseUnit course : module.getSubUnits()){
            CheckBox checkBox = new CheckBox(course.getName());
            vBox.getChildren().add(checkBox);
        }
        return vBox;
    }

    public static void main(String[] args) {
        launch();
    }

}