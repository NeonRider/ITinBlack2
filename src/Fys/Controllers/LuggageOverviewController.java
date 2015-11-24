package Fys.Controllers;

import Fys.Models.Luggage;
import Fys.Models.User;
import Fys.Tools.Screen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Fys.Views.ViewModels.LuggageTabelView;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * FXML Controller class. This class controls the Luggage Overview screen
 * including it's features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class LuggageOverviewController implements Initializable {

    @FXML private Label lblUsername, lblErrorMessage;
    @FXML private TableView tblLuggage;
    @FXML private TableColumn colType, colBrand, colMaterial, colColor, colComment, colStatus, colAction;
    @FXML private MenuButton ddwnLuggageType;
    @FXML private TextField lblSearch;
    
    private final Screen SCREEN = new Screen();
    private static User currentUser;
    
    public static void getUser(User user) {
        currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(currentUser.getUsername());
        colType.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("type"));
        colBrand.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("brand"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("material"));
        colColor.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("color"));
        colComment.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("comment"));
        colStatus.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("status"));
        colAction.setCellValueFactory(new PropertyValueFactory<LuggageTabelView, String>("action"));
        Callback<TableColumn<LuggageTabelView, String>, TableCell<LuggageTabelView, String>> printColumnCellFactory
                = new Callback<TableColumn<LuggageTabelView, String>, TableCell<LuggageTabelView, String>>() {

                    @Override
                    public TableCell call(final TableColumn param) {
                        final TableCell cell = new TableCell() {

                            @Override
                            public void updateItem(Object item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    final Button btnPrint = new Button("Edit");
                                    btnPrint.setOnAction(new EventHandler<ActionEvent>() {

                                        @Override
                                        public void handle(ActionEvent event) {
                                            param.getTableView().getSelectionModel().select(getIndex());
                                            LuggageTabelView item = (LuggageTabelView) tblLuggage.getSelectionModel().getSelectedItem();
                                            if (item != null) {
                                                try {
                                                    Luggage editLuggage = new Luggage().getLuggageById(item.getId());
                                                    LuggageEditController.getUser(currentUser);
                                                    LuggageEditController.setLuggage(editLuggage);
                                                    ((Node) event.getSource()).getScene().getWindow().hide();
                                                    SCREEN.change("LuggageEdit", "Edit Luggage");
                                                } catch (Exception ex) {
                                                    Logger.getLogger(AccountOverviewController.class.getName()).log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        }
                                    });
                                    setGraphic(btnPrint);
                                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                }
                            }
                        };
                        return cell;
                    }
                };
        colAction.setCellFactory(printColumnCellFactory);
        try {
            tblLuggage.setItems(getLuggageList());
        } catch (Exception ex) {
            Logger.getLogger(LuggageOverviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ObservableList<LuggageTabelView> getLuggageList() throws Exception{
        ObservableList<LuggageTabelView> luggageList= new LuggageTabelView().getLuggageList();
        return luggageList;
    }

    @FXML
    private void ddwnLuggageLostEvent(ActionEvent event) {
        ddwnLuggageType.setText("Lost");
        ddwnLuggageType.setPrefWidth(110);
    }

    @FXML
    private void ddwnLuggageFoundEvent(ActionEvent event) {
        ddwnLuggageType.setText("Found");
        ddwnLuggageType.setPrefWidth(110);
    }

    @FXML
    private void ddwnLuggageConnectedEvent(ActionEvent event) {
        ddwnLuggageType.setText("Connected");
        ddwnLuggageType.setPrefWidth(110);
    }
    
    @FXML
    private void ddwnLuggageAllEvent(ActionEvent event) {
        ddwnLuggageType.setText("All");
        ddwnLuggageType.setPrefWidth(110);
    }

    @FXML
    private void btnSearchLuggageEvent(ActionEvent event) throws Exception {
        if (!ddwnLuggageType.getText().equals("Luggage Type")) {
            lblErrorMessage.setText("");
            ObservableList<LuggageTabelView> luggageList = new LuggageTabelView().getLuggageList(lblSearch.getText(), ddwnLuggageType.getText());
            tblLuggage.setItems(luggageList);
        } else {
            lblErrorMessage.setText("Select a luggage type");
        }
    }

    //-- DO NOT TOUCH THESE BUTTONS BELOW, THEY ARE THE DEFAULT MENU ITEMS --
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("CustomerOverview", "Customer Overview");
    }

    @FXML
    private void btnAddLuggageEvent(ActionEvent event) throws IOException {
        LuggageAddController.getUser(currentUser);
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("LuggageAdd", "Add Luggage");
    }

    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        SCREEN.change("Login", "Login");
    }

}
