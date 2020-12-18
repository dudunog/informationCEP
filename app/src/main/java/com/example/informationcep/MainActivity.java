package com.example.informationcep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editText_cep;
    private Button btnEnviar;
    private TextView textView_infoCep;
    Cep classcep = new Cep();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_cep = (EditText) findViewById(R.id.editText_cep);
        btnEnviar = (Button) findViewById(R.id.button_enviar);
        textView_infoCep = (TextView) findViewById(R.id.textView_infoCep);

        listenersButtons();
    }

    public void listenersButtons() {
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number_cep = editText_cep.getText().toString();

                if (number_cep.length() == 8) {
                    progress = new ProgressDialog(MainActivity.this);
                    progress.setTitle("Buscando cep...");
                    progress.show();

                    enviarCep(number_cep);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    Toast.makeText(MainActivity.this, "Insira o cep corretamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setValues() {
        textView_infoCep.setText(classcep.toString());
    }

    public void enviarCep(String cep) {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<Cep> call = service.enviarCep(cep);

        call.enqueue(new Callback<Cep>() {
            @Override
            public void onResponse(Call<Cep> call, Response<Cep> response) {
                if (response.isSuccessful()) {
                    Cep info_cep = response.body();

                    if (info_cep != null) {
                        classcep.setCep(info_cep.getCep());
                        classcep.setLogradouro(info_cep.getLogradouro());
                        classcep.setComplemento(info_cep.getComplemento());
                        classcep.setBairro(info_cep.getBairro());
                        classcep.setLocalidade(info_cep.getLocalidade());
                        classcep.setUf(info_cep.getUf());
                        classcep.setDdd(info_cep.getDdd());

                        progress.dismiss();
                        setValues();
                    } else {
                        Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "A respota não foi um sucesso", Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Cep> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro na chamada do servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}