package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	private DepartmentService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label lblMessageErrorId;

	@FXML
	private Label lblMessagErrorName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void onActionBtnSave(ActionEvent event) {
		if (entity == null || service == null) {
			throw new IllegalStateException("ENTITY or SERVICE was NULL!!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyChangeListener();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	public void onActionBtnCancel(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private Department getFormData() {
		Department obj = new Department();

		ValidationException exception = new ValidationException("Validation Error");

		obj.setId(Utils.tryParsetoint(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}

		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializaNodes();
	}

	private void initializaNodes() {
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldInteger(txtId);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Illegal error");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			lblMessagErrorName.setText(errors.get("name"));
		}

	}

}
