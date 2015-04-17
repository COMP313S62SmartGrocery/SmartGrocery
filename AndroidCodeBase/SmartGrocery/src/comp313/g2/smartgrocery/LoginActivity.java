package comp313.g2.smartgrocery;

import comp313.g2.smartgrocery.helpers.GeneralHelpers;
import comp313.g2.smartgrocery.helpers.PreferenceHelper;
import comp313.g2.smartgrocery.helpers.ServiceHelper;
import comp313.g2.smartgrocery.models.User;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private SharedPreferences prefs;

	private LinearLayout loginView, registerView, connectingView;

	// fields for login view
	private EditText etUsername, etPassword;
	private Button btnSignIn;
	private TextView tvSignUp;

	// fields for signup view
	private EditText etSignUpUsername, etSignUpPassword, etConfirmPassword;
	private Button btnSignUp;
	private TextView tvSignIn;

	// fields for connecting view
	private TextView tvUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// initializing components
		InitializeComponents();
	}

	private void InitializeComponents() {
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		connectingView = (LinearLayout) findViewById(R.id.llConnectingView);
		loginView = (LinearLayout) findViewById(R.id.llSignInView);
		registerView = (LinearLayout) findViewById(R.id.llSignUpView);

		tvUsername = (TextView) findViewById(R.id.tvUsername);
		
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etSignUpUsername = (EditText) findViewById(R.id.etSignUpUsername);
		etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
		etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignIn.setOnClickListener(this);

		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUp.setOnClickListener(this);

		tvSignIn = (TextView) findViewById(R.id.tvSignIn);
		tvSignIn.setOnClickListener(this);
		tvSignUp = (TextView) findViewById(R.id.tvSignUp);
		tvSignUp.setOnClickListener(this);
		
		if (prefs.contains(PreferenceHelper.KEY_USERNAME)
				&& prefs.contains(PreferenceHelper.KEY_AUTH)) {
			// getting details from preferences
			final String username = prefs.getString(
					PreferenceHelper.KEY_USERNAME, "");
			final String authKey = prefs.getString(PreferenceHelper.KEY_AUTH,
					"");

			tvUsername.setText(username);
			
			// showing connecting view
			connectingView.setVisibility(View.VISIBLE);
			loginView.setVisibility(View.GONE);
			registerView.setVisibility(View.GONE);

			// connecting
			authenticate();

		} else {

			// switching to login view
			loginView.setVisibility(View.VISIBLE);
		}
	}

	private void authenticate() {
		final String authKey = prefs.getString(PreferenceHelper.KEY_AUTH, "");
		if (authKey.length() != 36) {
			// showing login view
			connectingView.setVisibility(View.GONE);
			loginView.setVisibility(View.VISIBLE);
			registerView.setVisibility(View.GONE);
		} else {
			if (GeneralHelpers.IsConnected(getApplicationContext())) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							ServiceHelper helper = new ServiceHelper();
							final String sessKey = helper
									.AuthenticateUser(authKey).replace("\"", "");

							if (sessKey != null && sessKey.length() == 36) {

								runOnUiThread(new Runnable() {

									@Override
									public void run() {

										Editor editor = prefs.edit();
										editor.putString(
												PreferenceHelper.KEY_SESS,
												sessKey);
										editor.commit();

										//starting main activity
										startMainActivity();
									}
								});
							} else {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(LoginActivity.this,
												"Unable to connect!",
												Toast.LENGTH_LONG).show();

										// showing login view
										connectingView.setVisibility(View.GONE);
										loginView.setVisibility(View.VISIBLE);
										registerView.setVisibility(View.GONE);
									}
								});
							}

						} catch (final Exception ex) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(LoginActivity.this,
											ex.getMessage(), Toast.LENGTH_LONG)
											.show();
								}
							});
						}
					}
				}).start();
			} else {
				Toast.makeText(getApplicationContext(), "Unable to connect!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view == tvSignIn) {
			// switching to login view
			loginView.setVisibility(View.VISIBLE);
			registerView.setVisibility(View.GONE);
			connectingView.setVisibility(View.GONE);
		} else if (view == tvSignUp) {
			// switching to register view
			registerView.setVisibility(View.VISIBLE);
			loginView.setVisibility(View.GONE);
			connectingView.setVisibility(View.GONE);
		} else if (view == btnSignIn) {
			// validating input
			final String username = etUsername.getText().toString().trim();
			final String password = etPassword.getText().toString().trim();

			if (username.length() <= 0) {
				etUsername.setError("Enter Username!");
			}else if(!GeneralHelpers.IsValidEmailId(username)){
				etUsername.setError("Invalid Email Id!");
			}
			else if (password.length() <= 0) {
				etPassword.setError("Enter Password!");
			} else {
				// signing in
				performSignIn(username, password);
			}
		} else if (view == btnSignUp) {
			// validating input
			final String username = etSignUpUsername.getText().toString()
					.trim();
			final String password = etSignUpPassword.getText().toString()
					.trim();
			final String confirmPassword = etConfirmPassword.getText()
					.toString().trim();

			if (username.length() <= 0) {
				etSignUpUsername.setError("Enter Username");
			}else if(!GeneralHelpers.IsValidEmailId(username)){
				etSignUpUsername.setError("Invalid Email Id!");
			}
			else if (password.length() <= 0) {
				etSignUpPassword.setError("Enter Password!");
			} else if (!password.equals(confirmPassword)) {
				etConfirmPassword.setError("Password do not match!");
			} else {
				performSignUp(username, password);
			}
		}
	}

	private void performSignUp(final String username, final String password) {
		btnSignUp.setEnabled(false);
		if (GeneralHelpers.IsConnected(getApplicationContext())) {
			// signing up
			new Thread(new Runnable() {

				@Override
				public void run() {
					User user = new User();
					user.Username = username;
					user.Password = password;

					final String message, authKey, sessKey;

					try {
						ServiceHelper serviceHelper = new ServiceHelper();
						authKey = serviceHelper.RegisterUser(user);
						sessKey = serviceHelper
								.AuthenticateUser(authKey);

						if (authKey.equals("-1")) {
							message = "User already exists!";
						} else if (authKey.equals("")) {
							message = "Unable to connect!";
						} else {
							message = "";
						}

						// performing post run operation
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								btnSignUp.setEnabled(true);

								if (message.equals("")) {
									if (sessKey != null
											&& !sessKey.equals("")
											&& sessKey.length() == 36) {
										Editor editor = prefs.edit();
										editor.putString(
												PreferenceHelper.KEY_USERNAME,
												username);
										editor.putString(
												PreferenceHelper.KEY_AUTH,
												authKey);
										editor.putString(
												PreferenceHelper.KEY_SESS,
												sessKey);
										editor.commit();

										// starting main Activity
										startMainActivity();

									} else {
										Toast.makeText(
												LoginActivity.this,
												"Unable to connect!",
												Toast.LENGTH_LONG)
												.show();
									}
								} else {
									Toast.makeText(LoginActivity.this,
											message, Toast.LENGTH_LONG)
											.show();
								}
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(LoginActivity.this,
										e.getMessage(),
										Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}).start();
		} else {
			Toast.makeText(getApplicationContext(),
					"Unable to connect!", Toast.LENGTH_LONG).show();
		}
		
	}

	private void performSignIn(final String username,final String password) {
		btnSignIn.setEnabled(false);
		if (GeneralHelpers.IsConnected(getApplicationContext())) {
			// signing up
			new Thread(new Runnable() {

				@Override
				public void run() {
					User user = new User();
					user.Username = username;
					user.Password = password;

					final String message, authKey, sessKey;

					try {
						ServiceHelper serviceHelper = new ServiceHelper();
						authKey = serviceHelper.GetAuthKey(user).replace("\"", "");
						sessKey = serviceHelper.AuthenticateUser(authKey).replace("\"", "");

						if (authKey.equals("") || authKey.length()!=36) {
							message = "Invalid Username or Password!";
						} else {
							message = "";
						}

						// performing post run operation
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								btnSignIn.setEnabled(true);

								if (message.equals("")) {
									if (sessKey != null
											&& !sessKey.equals("")
											&& sessKey.length() == 36) {
										Editor editor = prefs.edit();
										editor.putString(
												PreferenceHelper.KEY_USERNAME,
												username);
										editor.putString(
												PreferenceHelper.KEY_AUTH,
												authKey);
										editor.putString(
												PreferenceHelper.KEY_SESS,
												sessKey);
										editor.commit();

										// starting main Activity
										startMainActivity();

									} else {
										Toast.makeText(
												LoginActivity.this,
												"Unable to connect!",
												Toast.LENGTH_LONG)
												.show();
									}
								} else {
									Toast.makeText(LoginActivity.this,
											message, Toast.LENGTH_LONG)
											.show();
								}
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(LoginActivity.this,
										e.getMessage(),
										Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}).start();
		} else {
			Toast.makeText(getApplicationContext(),
					"Unable to connect!", Toast.LENGTH_LONG).show();
		}
	}

	private void startMainActivity() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
		startActivity(intent);
	}
}
