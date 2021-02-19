package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSeller() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
		controller.setSellerService(new SellerService());
		controller.updateTableView();
	});
	}

	@FXML
	public void onMenuItemDepartment() { 
			loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	public void onMenuItemAbout() {
		loadView("/gui/About.fxml", x ->{});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	public synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getmainScene();
			VBox mainBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainBox.getChildren().get(0);
			mainBox.getChildren().clear();
			mainBox.getChildren().add(mainMenu);
			mainBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IOException", "ERROR", e.getMessage(), AlertType.ERROR);
		}
	}

	public synchronized void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getmainScene();
			VBox mainBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainBox.getChildren().get(0);
			mainBox.getChildren().clear();
			mainBox.getChildren().add(mainMenu);
			mainBox.getChildren().addAll(newVBox.getChildren());

			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();

		} catch (IOException e) {
			Alerts.showAlert("IOException", "ERROR", e.getMessage(), AlertType.ERROR);
		}
	}

}
