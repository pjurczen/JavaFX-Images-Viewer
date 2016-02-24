package pl.imagesViewer.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import pl.imagesViewer.dataProvider.DataProvider;
import pl.imagesViewer.dataProvider.data.ImageVO;
import pl.imagesViewer.model.FileModel;
import javafx.scene.layout.GridPane;

public class ImagesViewerController {
    
    private final DataProvider dataProvider = DataProvider.INSTANCE;
    private FileModel model = new FileModel();
    
    @FXML
    private ImageView imageView;

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
    private ListView<ImageVO> imagesList;
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private void initialize() {
        imageView.fitWidthProperty().bind(gridPane.widthProperty());
        installKeyHandlers();
        initializeImagesList();
        imagesList.itemsProperty().bind(model.resultProperty());
    }

    @FXML
    public void browseButtonAction() throws IOException {
        imageView.setImage(null);
        selectDirectory();
        updateDirectoryLabel();
        updateImagesList();
    }

    @FXML
    public void nextButtonAction() {
        imagesList.getSelectionModel().selectNext();
    }

    @FXML
    public void playButtonAction() {

    }

    @FXML
    public void previousButtonAction() {
        imagesList.getSelectionModel().selectPrevious();
    }

    private void installKeyHandlers() {
        gridPane.setFocusTraversable(true);
        final EventHandler<KeyEvent> previousKeyHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.LEFT) {
                    previousButtonAction();
                    keyEvent.consume();
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    nextButtonAction();
                    keyEvent.consume();
                }
            }
        };
        gridPane.setOnKeyPressed(previousKeyHandler);
    }

    private void initializeImagesList() {
        imagesList.setCellFactory(listView -> new ListCell<ImageVO>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(80);
                imageView.setFitWidth(160);
                imageView.setPreserveRatio(true);
            }
            @Override
            public void updateItem(ImageVO image, boolean empty) {
                super.updateItem(image, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image("file:" + File.separator + image.getFullPath()));
                    setGraphic(imageView);
                }
            }
        });
        imagesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageVO>() {
            @Override
            public void changed(ObservableValue<? extends ImageVO> observable, ImageVO oldValue, ImageVO newValue) {
                if (newValue == null)
                    return;
                Image image = new Image("file:" + File.separator + newValue.getFullPath());
                imageView.setImage(image);
            }
        });
    }

    private void updateImagesList() {
        if(model.getDirectory() != null) {
            Task<Collection<ImageVO>> getImagesTask = new Task<Collection<ImageVO>>() {

                @Override
                protected Collection<ImageVO> call() throws Exception {
                    return dataProvider.getImages(model.getDirectory());
                }

                @Override
                protected void succeeded() {
                    model.setResult(getValue());
                    imagesList.setVisible(true);
                }
            };
            new Thread(getImagesTask).start();
        }
    }

    private void updateDirectoryLabel() {
        if(model.getDirectory() != null) {
            directoryLabel.setText(model.getDirectory());
            directoryLabel.setTooltip(new Tooltip(model.getDirectory()));
            directoryLabel.setVisible(true);
        }
    }

    private void selectDirectory() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(".").getCanonicalFile());
        model.setDirectory(directoryChooser.showDialog(gridPane.getScene().getWindow()).getAbsolutePath());
    }
}
