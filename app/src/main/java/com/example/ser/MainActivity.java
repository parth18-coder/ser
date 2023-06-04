package com.example.ser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_mic;
    private TextView tv_Speech_to_text;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private TextView emotion_textview;
    private Button predict_button;
    private ArrayList<String> result;

    String url="https://ser.herokuapp.com/predict";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_mic = findViewById(R.id.iv_mic);
        tv_Speech_to_text = findViewById(R.id.tv_speech_to_text);
        emotion_textview=(TextView) findViewById(R.id.id_emotion);
        predict_button=(Button) findViewById(R.id.id_predict);

        predict_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Hit the API
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    String resulted_emotion=jsonObject.getString("emotion");

                                    if(resulted_emotion.equals("joy")){
                                        emotion_textview.setText(R.string.joy);
                                    }
                                    else if(resulted_emotion.equals("sadness")){
                                        emotion_textview.setText(R.string.sadness);
                                    }
                                    else if(resulted_emotion.equals("fear")){
                                        emotion_textview.setText(R.string.fear);
                                    }
                                    else if(resulted_emotion.equals("anger")){
                                        emotion_textview.setText(R.string.anger);
                                    }
                                    else if(resulted_emotion.equals("surprise")){
                                        emotion_textview.setText(R.string.surprise);
                                    }
                                    else if(resulted_emotion.equals("neutral")){
                                        emotion_textview.setText(R.string.neutral);
                                    }
                                    else if(resulted_emotion.equals("disgust")){
                                        emotion_textview.setText(R.string.disgust);
                                    }
                                    else if(resulted_emotion.equals("shame")){
                                        emotion_textview.setText(R.string.shame);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }){

                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params=new HashMap<String,String>();
                        params.put("audio",Objects.requireNonNull(result).get(0));

                        return params;
                    }
                };

                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);

            }
        });



        iv_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(MainActivity.this, " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                tv_Speech_to_text.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }
}