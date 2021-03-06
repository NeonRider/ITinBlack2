package Fys.Controllers;

import Fys.Models.Customer;
import Fys.Models.User;
import Fys.Tools.LogTools;
import Fys.Tools.Screen;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class. This class controls the Customer Edit screen including it's
 * features.
 *
 * @author Jeffrey van der Lingen, IS106-2
 */
public class CustomerEditController implements Initializable {
    private static Screen screen;
    private static User currentUser;
    private static Customer editCustomer;    
    
    @FXML private Label lblUsername, lblErrorMessage, lblRegisterDate, lblRegisterEmployee;
    @FXML private TextField firstName, lastName, phone, address, email;
    @FXML private MenuButton ddwnGender;
    
    /**
     * void setScreen(Screen newScreen) sets the Screen element for the
     * Controller. This element contains the Stage object, used for switching
     * scenes.
     *
     * @param newScreen is the new Screen object that needs to be set in this
     * class.
     */
    public static void setScreen(Screen newScreen) {
        screen = newScreen;
    }
    
    /**
     * void setUser(User user) sets the user for the Controller. This is the
     * curent User that is used to log into the application, and is being used
     * for tracking of actions.
     *
     * @param user is the user that needs to be set in this class.
     */
    public static void setUser(User user) {
        currentUser = user;
    }
    
    /**
     * void setCustomer(Customer customer)  sets the customer for the Controller.
     * This sets the customer information that is needed for this controller.
     *
     * @param customer is the user that needs to be set in this class.
     */
    public static void setCustomer(Customer customer)   {
        editCustomer = customer;
    }
    
    /**
     * void initialize(URL url, ResourceBundel rb) is the automatic
     * initialization of the Controller when it's being fired due to a load of
     * it's connected scene.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstName.setText(editCustomer.getFirstName());
        lastName.setText(editCustomer.getLastName());
        ddwnGender.setText(editCustomer.getGender());
        phone.setText(editCustomer.getPhone());
        address.setText(editCustomer.getAddress());
        email.setText(editCustomer.getEmail());
        lblUsername.setText(currentUser.getUsername());
        try {
            lblRegisterDate.setText((
                    new LogTools().getCustomerRegisterDate(editCustomer.getId())).toString());
            lblRegisterEmployee.setText((
                    new LogTools().getCustomerRegisterEmployee(editCustomer.getEmployeeId())));
        } catch (SQLException | ParseException | ClassNotFoundException ex) {
            Logger.getLogger(CustomerEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * void ddwnGenderMaleEvent(ActionEvent event) fills in the MenuButton
     * with the text "Male" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnGenderMaleEvent(ActionEvent event) {
        ddwnGender.setText("Male");
        ddwnGender.setPrefWidth(200);
    }
    
    /**
     * void ddwnGenderMaleEvent(ActionEvent event) fills in the MenuButton
     * with the text "Female" and sets the size of the MenuButton to always
     * keep it the same. 
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void ddwnGenderFemaleEvent(ActionEvent event) {
        ddwnGender.setText("Female");
        ddwnGender.setPrefWidth(200);
    }
    
    /**
     * void btnSaveChangesEvent(ActionEvent event) saves the changes made to
     * editCustomer and returns to the Customer Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws ClassNotFoundException when the class could not be found.
     * @throws SQLException when no connection with the Database could be
     * established.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnSaveChangesEvent(ActionEvent event) 
            throws IOException, ClassNotFoundException, SQLException {
        if (!(firstName.getText().equals("") || lastName.getText().equals("") || 
                phone.getText().equals(""))) {
            lblErrorMessage.setText("");
            firstName.setStyle("-fx-border-width: 0px;");
            lastName.setStyle("-fx-border-width: 0px;");
            phone.setStyle("-fx-border-width: 0px;");
            
            LogTools logTools = new LogTools();
            editCustomer.setFirstName(firstName.getText());
            if (logTools.checkCustomerChanged(editCustomer, "firstName")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "firstName");
            }
            editCustomer.setLastName(lastName.getText());
            if (logTools.checkCustomerChanged(editCustomer, "lastName")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "lastName");
            }
            editCustomer.setGender(ddwnGender.getText());
            if (logTools.checkCustomerChanged(editCustomer, "gender")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "gender");
            }
            editCustomer.setPhone(phone.getText());
            if (logTools.checkCustomerChanged(editCustomer, "phone")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "phone");
            }
            editCustomer.setAddress(address.getText());
            if (logTools.checkCustomerChanged(editCustomer, "address")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "address");
            }
            editCustomer.setEmail(email.getText());
            if (logTools.checkCustomerChanged(editCustomer, "email")) {
                logTools.logCustomerChanged(editCustomer, currentUser, "email");
            }
            editCustomer.updateCustomer(editCustomer);
            
            CustomerOverviewController.setUser(currentUser);
            CustomerOverviewController.setScreen(screen);
            screen.change("CustomerOverview");
        } else {
            lblErrorMessage.setText("The highlighted fields can't be empty");
            firstName.setStyle("-fx-text-box-border: red;");
            lastName.setStyle("-fx-text-box-border: red;");
            phone.setStyle("-fx-text-box-border: red;");
        }
    }
    
    /**
     * void btnHistoryEvent(ActionEvent event) changes the scene of the Stage
     * to the history log scene. This scene is being used to add new customers into
     * the database so that they can be notified when their luggage is found.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws Exception when no connection with the Database could be
     * established.
     */
    @FXML
    private void btnHistoryEvent(ActionEvent event) throws Exception {
        LogCustomerController.setCustomer(editCustomer);
        Stage logStage = new Stage();
        LogCustomerController.setStage(logStage);
        Scene logScene = new Scene(FXMLLoader.load(getClass().getResource("/Fys/Views/LogCustomer.fxml")));
        logScene.getStylesheets().add("/Fys/Content/Css/stylesheet.css");
        logStage.setTitle("Corendon Lost Luggage System");
        logStage.getIcons().add(new javafx.scene.image.Image("/Fys/Content/Image/corendonicon.png"));
        logStage.setScene(logScene);
        logStage.show();
    }
    
    /**
     * void btnPrintProofEvent(ActionEvent event) opens the file explorer to save the proof as pdf.
     * The saved document can be printed from the computer location.
     *
     * @param event The event that is being fired by clicking the button.
     */
    @FXML
    private void btnPrintProofEvent(ActionEvent event) {
        try {
            /* Create new FileChooser */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(
                    "Proof of Registration - customer" + editCustomer.getId());
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf"));
            
            /* Create a new File by selecting a save directory */
            File pdfFile = fileChooser.showSaveDialog(new Stage());
            
            /* Check if a directory has been selected. If not, return out of the method */
            if (pdfFile == null) {
                return;
            }
            
            /* Create new Document */
            Document document = new Document();
            
            /* Create the FileOutputStream */
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            
            /* Initialize the image and the bold font */
            Image corendonLogo = Image.getInstance("src/Fys/Content/Image/corendonlogo.jpg");
            Font fontbold = FontFactory.getFont("Arial", 18, Font.BOLD);
            
            /* Write to the Document */
            document.open();
            corendonLogo.scalePercent(20f);
            document.add(corendonLogo);
            document.add(new Paragraph("Proof of registration: Customer", fontbold));
            document.add(new Paragraph("Name: " + editCustomer.getFullName()));
            document.add(new Paragraph("Gender: " + editCustomer.getGender()));
            document.add(new Paragraph("Phone: " + editCustomer.getPhone()));
            document.add(new Paragraph("Address: " + editCustomer.getAddress()));
            document.add(new Paragraph("Email: " + editCustomer.getEmail()));
            document.add(new Paragraph("Register date: " + editCustomer.getRegisterDate()));
            document.close();
            
            /* Display success of file save */
            lblErrorMessage.setText("Successfully saved the Proof of Registration document");
        } catch(IOException | DocumentException ex) {
            Logger.getLogger(LuggageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * void btnBackToOverviewEvent(ActionEvent event) returns the user back to
     * the Customer Overview screen. This is the button next to the "Save
     * Changes" button.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnBackToOverviewEvent(ActionEvent event) throws IOException {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
    }
    
    /**
     * void btnLuggageEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Luggage Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLuggageEvent(ActionEvent event) throws Exception {
        LuggageOverviewController.setUser(currentUser);
        LuggageOverviewController.setScreen(screen);
        screen.change("LuggageOverview");
    }
    
    /**
     * void btnCustomerEvent(ActionEvent event) is the button on the left of the
     * screen inside the red bar that returns to the Customer Overview scene.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnCustomerEvent(ActionEvent event) throws Exception {
        CustomerOverviewController.setUser(currentUser);
        CustomerOverviewController.setScreen(screen);
        screen.change("CustomerOverview");
    }
    
    /**
     * void btnLogoutEvent(ActionEvent event) logs the current User out of the
     * application and displays the Login screen.
     *
     * @param event The event that is being fired by clicking the button.
     * @throws IOException when the FXML file could not be loaded.
     */
    @FXML
    private void btnLogoutEvent(ActionEvent event) throws IOException {
        LoginController.setScreen(screen);
        ((Node) event.getSource()).getScene().getWindow().hide();
        screen.change("Login");
    }
    
}
