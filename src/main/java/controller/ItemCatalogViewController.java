package controller;

import bo.BoFactory;
import bo.custom.ItemCatalogBo;
import bo.util.BoType;
import dto.CatalogItemDto;
import dto.StaffDto;
import dto.tm.CatalogItemTm;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class ItemCatalogViewController {

    @FXML
    private Label lblStaffId;

    @FXML
    private MFXTableView<CatalogItemTm> tblItems;

    @FXML
    private ImageView imgView;

    @FXML
    private MFXTextField txtItemCode;

    @FXML
    private MFXTextField txtItemName;

    @FXML
    private MFXComboBox<String> cmbxCategory;

    @FXML
    private MFXButton btnAddImage;

    @FXML
    private MFXButton btnAddItem;

    @FXML
    private MFXButton btnUpdateItem;

    private StaffDto staffDto;

    private CatalogItemTm selectedItem;

    private final ItemCatalogBo itemCatalogBo = BoFactory.getInstance().getBo(BoType.CATALOG_ITEM);
    private File tmpSelectedFile;

    public void initialize() throws SQLException, ClassNotFoundException {

        // Disable edit on ItemCode
        txtItemCode.setAllowEdit(false);

        // Set categories for combobox
        cmbxCategory.getItems().add("Electronic");
        cmbxCategory.getItems().add("Electrical");

        // Button Events
        btnAddImage.setOnAction(actionEvent -> selectImageFile());

        btnAddItem.setOnAction(actionEvent -> {
            if (txtItemName.getText().isEmpty() || cmbxCategory.getText().isEmpty() || tmpSelectedFile == null) {
                operationErrorAlert("Operation Failed!", "Invalid Form Data!");
                return;
            }

            String destinationPath = createFolder(tmpSelectedFile);

            try {
                itemCatalogBo.saveItem(new CatalogItemDto(
                        null,
                        txtItemName.getText(),
                        cmbxCategory.getSelectedItem(),
                        destinationPath,
                        staffDto.getStaffId()
                ));
                saveImage(tmpSelectedFile, destinationPath);
                clearFields();
                tblItems.setItems(getUsers());
                operationSuccessAlert("Operation Success!", "Item Added Successfully");
            } catch (SQLException e) {
                operationErrorAlert("Operation Failed!", "Something Went Wrong!");
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        btnUpdateItem.setOnAction(actionEvent -> {
            if (txtItemName.getText().isEmpty() || cmbxCategory.getText().isEmpty()) {
                operationErrorAlert("Operation Failed!", "Invalid Form Data");
                return;
            }

            boolean isImageChanged = tmpSelectedFile != null;
            String destinationPath = "";

            if (isImageChanged) {
                destinationPath = createFolder(tmpSelectedFile);
            }

            if (isImageChanged ||
                    !selectedItem.getItemName().equals(txtItemName.getText()) ||
                    !selectedItem.getCategory().equals(cmbxCategory.getSelectedItem())) {
                try {
                    itemCatalogBo.updateItem(new CatalogItemDto(
                            selectedItem.getCatalogItemId(),
                            txtItemName.getText(),
                            cmbxCategory.getText(),
                            isImageChanged ? destinationPath : selectedItem.getImgLink(),
                            selectedItem.getAddedByUser()
                    ));
                    tblItems.setItems(getUsers());
                } catch (SQLException e) {
                    operationErrorAlert("Operation Error!", "Update Failed!");
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if(isImageChanged){
                    saveImage(tmpSelectedFile, destinationPath);
                    deleteFile(selectedItem.getImgLink());
                }

                clearFields();
                operationSuccessAlert("Operation Success!", "Item Updated!");
                return;
            }
            operationErrorAlert("Operation Error!", "Update Failed!");
        });

        //Load Items table
        loadTableData();
        tblItems.getSelectionModel().selectionProperty().addListener((observableValue, oldValue, newValue) -> setData(newValue));
    }

    private void setData(ObservableMap<Integer, CatalogItemTm> newValue) {
        Object[] array = newValue.keySet().toArray();
        if (array.length == 1) {
            selectedItem = newValue.get(array[0]);
            try {
                imgView.setImage(new Image(selectedItem.getImgLink()));
            }catch (Exception e){
                operationErrorAlert("Failed to Load the Image","Image might be deleted or renamed");
            }

            txtItemCode.setText(selectedItem.getCatalogItemId());
            txtItemName.setText(selectedItem.getItemName());
            cmbxCategory.setText(selectedItem.getCategory());
        }
    }

    private void clearFields() {
        imgView.setImage(null);
        txtItemCode.clear();
        txtItemName.clear();
        cmbxCategory.clear();
        tmpSelectedFile = null;
    }

    private void selectImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(lblStaffId.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.getAbsolutePath());
            imgView.setImage(image);
            tmpSelectedFile = selectedFile;
        }
    }

    private void saveImage(File selectedFile, String destinationPath) {
        if (selectedFile != null) {
            try {
                Path sourcePath = selectedFile.toPath();
                Path destination = new File(destinationPath).toPath();
                Files.copy(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            operationErrorAlert("No Image Selected", "");
        }
    }

    private String createFolder(File selectedFile) {
        File folder = new File("images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath() + File.separator + selectedFile.getName();
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {

        MFXTableColumn<CatalogItemTm> catalogItemId =
                new MFXTableColumn<>("Catalog Item ID", false, Comparator.comparing(CatalogItemTm::getCatalogItemId));
        MFXTableColumn<CatalogItemTm> itemName =
                new MFXTableColumn<>("Item Name", false, Comparator.comparing(CatalogItemTm::getItemName));
        MFXTableColumn<CatalogItemTm> category =
                new MFXTableColumn<>("Category", false, Comparator.comparing(CatalogItemTm::getCategory));
        MFXTableColumn<CatalogItemTm> user =
                new MFXTableColumn<>("Added By", false, Comparator.comparing(CatalogItemTm::getAddedByUser));
        MFXTableColumn<CatalogItemTm> action =
                new MFXTableColumn<>("Action", false);

        catalogItemId.setRowCellFactory(item -> new MFXTableRowCell<>(CatalogItemTm::getCatalogItemId));
        itemName.setRowCellFactory(item -> new MFXTableRowCell<>(CatalogItemTm::getItemName));
        category.setRowCellFactory(item -> new MFXTableRowCell<>(CatalogItemTm::getCategory));
        user.setRowCellFactory(item -> new MFXTableRowCell<>(CatalogItemTm::getAddedByUser));

        action.setRowCellFactory(item -> {
            MFXTableRowCell<CatalogItemTm, String> mfxTableRowCell = new MFXTableRowCell<>(CatalogItemTm::getAction);
            MFXButton btnDelete = new MFXButton("❌");
            btnDelete.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                try {
                    deleteCustomer(item.getCatalogItemId(), item.getImgLink());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            mfxTableRowCell.setLeadingGraphic(btnDelete);
            mfxTableRowCell.setAlignment(Pos.CENTER);
            mfxTableRowCell.mouseTransparentProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    mfxTableRowCell.setMouseTransparent(false);
                }
            });
            return mfxTableRowCell;
        });

        catalogItemId.setPrefWidth(200);
        itemName.setPrefWidth(300.0);
        category.setPrefWidth(200.0);
        user.setPrefWidth(100.0);


        tblItems.getTableColumns().addAll(catalogItemId, itemName, category, user, action);

        tblItems.getFilters().addAll(
                new StringFilter<>("Catalog Item ID", CatalogItemTm::getCatalogItemId),
                new StringFilter<>("Item Name", CatalogItemTm::getItemName),
                new StringFilter<>("Category", CatalogItemTm::getCategory)
        );

        tblItems.setItems(getUsers());
    }

    private void deleteCustomer(String catalogItemId, String absolutePath) throws SQLException, ClassNotFoundException {
        Alert confirmAlert = new Alert
                (Alert.AlertType.CONFIRMATION,
                        "Do you want to delete this item?",
                        ButtonType.YES, ButtonType.NO);

        confirmAlert.showAndWait();

        if (confirmAlert.getResult() == ButtonType.YES) {
            if (itemCatalogBo.deleteItem(catalogItemId)) {
                deleteFile(absolutePath);
                operationSuccessAlert("Deleted!", "Item Deleted Successfully!");
            }
            confirmAlert.close();
            clearFields();
            tblItems.setItems(getUsers());
            return;
        }

        confirmAlert.close();
    }

    private boolean deleteFile(String absolutePath) {
        try {
            Path filePath = Paths.get(absolutePath);
            Files.delete(filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ObservableList<CatalogItemTm> getUsers() throws SQLException, ClassNotFoundException {
        List<CatalogItemDto> dtoList = itemCatalogBo.getAllItem();
        ObservableList<CatalogItemTm> itemTm = FXCollections.observableArrayList();

        for (CatalogItemDto dto : dtoList) {
            itemTm.add(new CatalogItemTm(
                    dto.getCatalogItemId(),
                    dto.getName(),
                    dto.getCategory(),
                    dto.getImgLink(),
                    dto.getStaffId(),
                    ""
            ));
        }
        return itemTm;
    }

    public void initLoggedUser(StaffDto staff) {
        staffDto = staff;
        // Set staff ID
        lblStaffId.setText(staffDto.getStaffId());
    }

    void operationSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    void operationErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}

