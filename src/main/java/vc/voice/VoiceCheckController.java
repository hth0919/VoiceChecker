package vc.voice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;

import javax.sound.sampled.*;

public class VoiceCheckController extends AnchorPane implements Initializable {

	private static int rec_num = 1;
	private Boolean isLogin = false;
	private static Boolean recflag = false;

	private Boolean isStart = false; // �������� �Ǵ��� �ʵ�.
	private Timeline timeLine;
	private DoubleProperty timeSeconds = new SimpleDoubleProperty();
	private Duration time = Duration.ZERO;
	static VoiceRecorder recorder;
	private static boolean stopflag = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		timeLine = new Timeline(); // timeLine ��ü �ʱ�ȭ
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();

	}

	public VoiceCheckController() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/voicecheck.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void exitApplication(ActionEvent event) {
		recorder.finish();
		recorder.cancel();
		Platform.exit();
	}

	@FXML
	private Button recbutton; // Value injected by FXMLLoader

	@FXML
	private Button stopbutton; // Value injected by FXMLLoader

	@FXML
	private Button loginbutton; // Value injected by FXMLLoader

	@FXML
	private Button regbutton; // Value injected by FXMLLoader

	@FXML
	private Label idlabel; // Value injected by FXMLLoader

	@FXML
	private Label pwlabel; // Value injected by FXMLLoader

	@FXML
	private Label timelabel; // Value injected by FXMLLoader

	@FXML
	private Label statelabel; // Value injected by FXMLLoader

	@FXML
	private Label idshowlabel; // Value injected by FXMLLoader

	@FXML
	private Label welcomlabel; // Value injected by FXMLLoader

	@FXML
	private TextField idfield; // Value injected by FXMLLoader

	@FXML
	private PasswordField pwfield; // Value injected by FXMLLoader

	public String getid() {
		return idshowlabel.getText();
	}

	public void showstatelabel(String state) {
		statelabel.setText(state);
	}

	@FXML
	void handlerec(ActionEvent event) {
		if (isLogin) {
			if(SQLHandler.checkuserban(getid()))
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Banned User");
				alert.setHeaderText("You are forbidden to use the program because of frequent bad language");
				alert.setContentText("If you want to see the log, click the showlog button. If not, click the cancle button.");

				ButtonType buttonTypeOne = new ButtonType("Show log");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					Alert alert1 = new Alert(AlertType.ERROR);
					alert1.setTitle("Showing Log");
					alert1.setHeaderText("Your abusive log");
					alert1.setContentText("Click the arrow below to see the log. \nAnd if you have any questions, please contact our Customer Center.");
					UserLog ul = SQLHandler.showtext(getid());
					String[] text = ul.getid();
					String[] date = ul.getdate();
					String logtext = "";
					for(int i = 0;i<ul.getlength();i++)
					{
						logtext = logtext + "Date : "+ date[i] + "\nYou Said : " + text[i] + "\n"; 
					}

					// Create expandable Exception.// Create expandable Exception.
					
					Label label = new Label("Your Log:");

					TextArea textArea = new TextArea(logtext);
					
					textArea.setEditable(false);
					textArea.setWrapText(true);

					textArea.setMaxWidth(Double.MAX_VALUE);
					textArea.setMaxHeight(Double.MAX_VALUE);
					GridPane.setVgrow(textArea, Priority.ALWAYS);
					GridPane.setHgrow(textArea, Priority.ALWAYS);

					GridPane expContent = new GridPane();
					expContent.setMaxWidth(Double.MAX_VALUE);
					expContent.add(label, 0, 0);
					expContent.add(textArea, 0, 1);

					// Set expandable Exception into the dialog pane.// Set expandable Exception into the dialog pane.
					alert1.getDialogPane().setExpandableContent(expContent);

					alert1.showAndWait();
					System.exit(0);
				} else {
					System.exit(0);
				}
			}
			
			try {
				timeLine.stop(); // ���� �ð��� �����Ϸ��� timeLine�� �ʱ�ȭ�Ǿ� �ϹǷ� stop()
				time = Duration.ZERO; // time�� ���� ���� ���� �� ������ 0�̵Ǿ�� ��.
				timelabel.textProperty().bind(timeSeconds.asString()); // timeCheck �� timeSeconds �� ����
				showstatelabel("Recording...");
				isStart = true; // newButton�� Ŭ�������Ƿ� isStart �� true��
				if (isStart == true) {

					if (timeLine == null) {
						// ���� �� �� ����.
					} else {
						timeLine = new Timeline(new KeyFrame(Duration.millis(10), // 0.01�� ������ üũ
								new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent t) {
										Duration duration = ((KeyFrame) t.getSource()).getTime();
										time = time.add(duration);
										timeSeconds.set(time.toSeconds());
									}
								}));
						timeLine.setCycleCount(Timeline.INDEFINITE);
						timeLine.play();
					}

				}
			} catch (RuntimeException r) {
				r.printStackTrace();
			}
			recording(event);
			
		} else {
			ShowLoginAlert();
		}
	}

	@FXML
	void handlestop(ActionEvent event) {
		if (isLogin) {
			timeLine.stop(); // timeLine����
			time = Duration.ZERO; // time�� ���� ���� ���� �� ������ 0�̵Ǿ�� ��.
			timeSeconds.multiply(0);
			timelabel.textProperty().bind(timeSeconds.asString()); // timeCheck �� timeSeconds �� ����
			statelabel.setText("Stop");
			recorder.cancel();
			recording(event);

		} else {
			ShowLoginAlert();
		}
	}

	@FXML
	void handlelogin(ActionEvent event) {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login");
		dialog.setHeaderText("Login Please");

		// Set the icon (must be included in the project).
		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("ID");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("ID:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result1 = dialog.showAndWait();

		result1.ifPresent(usernamePassword -> {

			if (SQLHandler.checkuser(usernamePassword.getKey(), usernamePassword.getValue()) ) {
				isLogin = true;
				idshowlabel.setText(usernamePassword.getKey());
				welcomlabel.setText("Welcome!!!");
			} else {
				Alert notuser = new Alert(AlertType.WARNING);
				notuser.setTitle("Warning");
				notuser.setHeaderText("Not Registed User!!");
				notuser.setContentText("First, You have to regist!!");

				notuser.showAndWait();

			}
		});
	}

	@FXML
	void handleregister(ActionEvent event) {
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		dialog.setTitle("Resister");
		dialog.setHeaderText("Welcome new User!!");
		Boolean Isduplicate = false;
		// Set the icon (must be included in the project).
		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField name = new TextField();
		name.setPromptText("Name");
		TextField ID = new TextField();
		ID.setPromptText("ID");
		Button D_check = new Button();
		Label showstate = new Label("Please Check Duplication");
		D_check.setOnAction(e -> {
			if (SQLHandler.checkduplicate(ID.getText())) {
				showstate.setText("Already taken..");
			} else {
				showstate.setText("It's OK!!");
			}
		});
		D_check.setText("Duplication Chceck");

		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Name:"), 0, 0);
		grid.add(name, 1, 0);
		grid.add(new Label("ID:"), 0, 1);
		grid.add(ID, 1, 1);
		grid.add(D_check, 2, 1);
		grid.add(new Label("Password:"), 0, 2);
		grid.add(password, 1, 2);
		grid.add(showstate, 0, 3);

		// Enable/Disable login button depending on whether a username was entered.
		Node registerButton = dialog.getDialogPane().lookupButton(loginButtonType);
		registerButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		ID.textProperty().addListener((observable, oldValue, newValue) -> {
			registerButton.setDisable(newValue.trim().isEmpty());
			D_check.setDisable(newValue.trim().isEmpty());
		});
		name.textProperty().addListener((observable, oldValue, newValue) -> {
			registerButton.setDisable(newValue.trim().isEmpty());
		});
		password.textProperty().addListener((observable, oldValue, newValue) -> {
			registerButton.setDisable(newValue.trim().isEmpty());
		});
		showstate.textProperty().addListener((observable, oldValue, newValue) -> {
			registerButton.setDisable(newValue.trim() != "It's OK!!");
		});
		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> {
			name.requestFocus();
			ID.requestFocus();
			password.requestFocus();
		});

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				ArrayList<String> a = new ArrayList<String>();

				a.add(name.getText());
				a.add(ID.getText());
				a.add(password.getText());

				return a;
			}
			return null;
		});

		Optional<ArrayList<String>> result1 = dialog.showAndWait();
		result1.ifPresent(usernamePassword -> {

			SQLHandler.sqluserinsert(usernamePassword.get(0), usernamePassword.get(1), usernamePassword.get(2), "1");
		});
	}

	static void sysclose() {
		if (recflag) {
			recorder.finish();
			recorder.cancel();
			stopflag = true;
		}

	}

	void recording(ActionEvent e) {
		if (stopflag) {
			recflag = true;
			stopflag = false;
			recorder = new VoiceRecorder();
			recorder.set_id(idshowlabel.getText());
			Thread rthread = new Thread(recorder);
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					int i = 0;
					while (i < 1) {
						System.out.println("stopper sleep...");
						try {
							if (stopflag) {
								break;
							}
							Thread.sleep(10000);
							if (stopflag) {
								break;
							}
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
						System.out.println("stopper wake...");
						if (stopflag) {
							break;
						}
						recorder.finish();
						if (stopflag) {
							break;
						}
						recorder.cancel();
						if (stopflag) {
							break;
						}
					}
				}
			});
			Thread restarter = new Thread(new Runnable() {
				public void run() {
					while (!stopflag) {
						if (stopflag)
							break;
						Thread restopper = new Thread(new Runnable() {
							public void run() {
								System.out.println("stopper sleep...");
								try {
									Thread.sleep(10000);
								} catch (InterruptedException ex) {
									ex.printStackTrace();
								}
								System.out.println("stopper wake...");
								recorder.finish();
								recorder.cancel();
							}
						});
						if (stopflag)
							break;
						System.out.println("restarter sleep...");
						if (stopflag)
							break;
						try {
							if (stopflag)
								break;
							Thread.sleep(10001);
							if (stopflag)
								break;
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
						System.out.println("restarter wake...");
						if (stopflag)
							break;
						recorder = new VoiceRecorder();
						if (stopflag)
							break;
						recorder.set_id(idshowlabel.getText());
						if (stopflag)
							break;
						Thread thread = new Thread(recorder);
						if (stopflag)
							break;
						restopper.start();
						if (stopflag)
							break;
						thread.start();

					}
				}
			});

			stopper.start();
			restarter.start();
			rthread.start();

		} else {
			recorder.finish();
			recorder.cancel();

			stopflag = true;
		}
	}

	static boolean getstopflag() {
		return stopflag;
	}

	static void setstopflag(Boolean sf) {
		stopflag = sf;
	}

	void ShowLoginAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Warnig");
		alert.setHeaderText("First, You have to Log-In, If you're not our member, Please Resist First");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeLogin = new ButtonType("Login");
		ButtonType buttonTypeRegister = new ButtonType("Register");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeLogin, buttonTypeRegister, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeLogin) {
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Login");
			dialog.setHeaderText("Login Please");

			// Set the icon (must be included in the project).
			dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

			// Set the button types.
			ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			// Create the username and password labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			TextField username = new TextField();
			username.setPromptText("ID");
			PasswordField password = new PasswordField();
			password.setPromptText("Password");

			grid.add(new Label("ID:"), 0, 0);
			grid.add(username, 1, 0);
			grid.add(new Label("Password:"), 0, 1);
			grid.add(password, 1, 1);

			// Enable/Disable login button depending on whether a username was entered.
			Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
			loginButton.setDisable(true);

			// Do some validation (using the Java 8 lambda syntax).
			username.textProperty().addListener((observable, oldValue, newValue) -> {
				loginButton.setDisable(newValue.trim().isEmpty());
			});

			dialog.getDialogPane().setContent(grid);

			// Request focus on the username field by default.
			Platform.runLater(() -> username.requestFocus());

			// Convert the result to a username-password-pair when the login button is
			// clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == loginButtonType) {
					return new Pair<>(username.getText(), password.getText());
				}
				return null;
			});

			Optional<Pair<String, String>> result1 = dialog.showAndWait();

			result1.ifPresent(usernamePassword -> {

				if (SQLHandler.checkuser(usernamePassword.getKey(), usernamePassword.getValue())) {
					isLogin = true;
					idshowlabel.setText(usernamePassword.getKey());
					welcomlabel.setText("Welcome!!!");
				} else {
					Alert notuser = new Alert(AlertType.WARNING);
					notuser.setTitle("Warning");
					notuser.setHeaderText("Not Registed User!!");
					notuser.setContentText("First, You have to regist!!");

					notuser.showAndWait();

				}
			});
		} else if (result.get() == buttonTypeRegister) {
			Dialog<ArrayList<String>> dialog = new Dialog<>();
			dialog.setTitle("Resister");
			dialog.setHeaderText("Welcome new User!!");
			Boolean Isduplicate = false;
			// Set the icon (must be included in the project).
			dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

			// Set the button types.
			ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			// Create the username and password labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			TextField name = new TextField();
			name.setPromptText("Name");
			TextField ID = new TextField();
			ID.setPromptText("ID");
			Button D_check = new Button();
			Label showstate = new Label("Please Check Duplication");
			D_check.setOnAction(e -> {
				if (SQLHandler.checkduplicate(ID.getText())) {
					showstate.setText("Already taken..");
				} else {
					showstate.setText("It's OK!!");
				}
			});
			D_check.setText("Duplication Chceck");

			PasswordField password = new PasswordField();
			password.setPromptText("Password");

			grid.add(new Label("Name:"), 0, 0);
			grid.add(name, 1, 0);
			grid.add(new Label("ID:"), 0, 1);
			grid.add(ID, 1, 1);
			grid.add(D_check, 2, 1);
			grid.add(new Label("Password:"), 0, 2);
			grid.add(password, 1, 2);
			grid.add(showstate, 0, 3);

			// Enable/Disable login button depending on whether a username was entered.
			Node registerButton = dialog.getDialogPane().lookupButton(loginButtonType);
			registerButton.setDisable(true);

			// Do some validation (using the Java 8 lambda syntax).
			ID.textProperty().addListener((observable, oldValue, newValue) -> {
				registerButton.setDisable(newValue.trim().isEmpty());
				D_check.setDisable(newValue.trim().isEmpty());
			});
			name.textProperty().addListener((observable, oldValue, newValue) -> {
				registerButton.setDisable(newValue.trim().isEmpty());
			});
			password.textProperty().addListener((observable, oldValue, newValue) -> {
				registerButton.setDisable(newValue.trim().isEmpty());
			});
			showstate.textProperty().addListener((observable, oldValue, newValue) -> {
				registerButton.setDisable(newValue.trim() != "It's OK!!");
			});
			dialog.getDialogPane().setContent(grid);

			// Request focus on the username field by default.
			Platform.runLater(() -> {
				name.requestFocus();
				ID.requestFocus();
				password.requestFocus();
			});

			// Convert the result to a username-password-pair when the login button is
			// clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == loginButtonType) {
					ArrayList<String> a = new ArrayList<String>();

					a.add(name.getText());
					a.add(ID.getText());
					a.add(password.getText());

					return a;
				}
				return null;
			});

			Optional<ArrayList<String>> result1 = dialog.showAndWait();
			result1.ifPresent(usernamePassword -> {

				SQLHandler.sqluserinsert(usernamePassword.get(0), usernamePassword.get(1), usernamePassword.get(2),
						"1");
			});

		} else {
			/*
			 * 
			 */
		}
	}

	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}

}
