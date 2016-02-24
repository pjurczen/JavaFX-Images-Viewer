package pl.imagesViewer.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import pl.imagesViewer.dataProvider.DataProvider;
import pl.imagesViewer.model.FileModel;

public class ImagesViewerController {
    
    private final DataProvider dataProvider = DataProvider.INSTANCE;
    private FileModel model = new FileModel();
    
    @FXML
    private ImageView image;

    @FXML
    private Button previousButton;

    @FXML
    private Button playButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button browseButton;

    @FXML
    private Label directoryLabel;
    
    @FXML
    private void initialize() {
        
    }

    @FXML
    void browseButtonAction() {

    }

    @FXML
    void nextButtonAction() {

    }

    @FXML
    void playButtonAction() {

    }

    @FXML
    void previousButtonAction() {

    }

}
