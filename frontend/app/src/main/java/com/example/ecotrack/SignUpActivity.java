package com.example.ecotrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit, birthDateEdit;
    private RadioGroup sexGroup;

    private Button signUpBtn;
    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEdit = findViewById(R.id.email_input);
        passwordEdit = findViewById(R.id.password_input);
        sexGroup = findViewById(R.id.sex_input);
        birthDateEdit = findViewById(R.id.birth_date_input);
        signUpBtn = findViewById(R.id.signup_button);

        authApi = ApiClient.getRetrofit().create(AuthApi.class);
        birthDateEdit.setFocusable(false);
        birthDateEdit.setClickable(true);

        birthDateEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignUpActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" +
                                String.format("%02d", selectedMonth + 1) + "-" +
                                String.format("%02d", selectedDay);
                        birthDateEdit.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        signUpBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            int selectedSexId = sexGroup.getCheckedRadioButtonId();
            String birthDate = birthDateEdit.getText().toString();

            if (email.isEmpty() || password.isEmpty()|| selectedSexId == -1 || birthDate.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedSexButton = findViewById(selectedSexId);
            String sex = selectedSexButton.getText().toString();

            UserRequest userRequest = new UserRequest(email, password, sex, birthDate);
            Call<String> call = authApi.signUp(userRequest);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
