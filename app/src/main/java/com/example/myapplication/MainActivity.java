package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.Gravity;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    CheckBox a, b, c, d;
    Button btn;
    RadioGroup rg;
    ImageView img;
    int qnumber, score;
    String[] aS, bS, cS, dS, qS, imgS;
    boolean returned = false;
    List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONObject jsonObject = JsonParser.parseJsonFromAsset(this, "questions.json");

        if (jsonObject != null) {
            try {
                JSONArray value = (JSONArray) jsonObject.get("questions");
                aS = new String[value.length() + 1];
                bS = new String[value.length() + 1];
                cS = new String[value.length() + 1];
                dS = new String[value.length() + 1];
                qS = new String[value.length() + 1];
                imgS = new String[value.length() + 1];
                for (int i = 0; i < value.length(); i++) {
                    JSONObject obiekt = new JSONObject(value.getString(i));
                    JSONArray correctIndicesArray = obiekt.getJSONArray("ans");
                    List<Integer> correctIndices = new ArrayList<>();
                    for (int j = 0; j < correctIndicesArray.length(); j++) {
                        correctIndices.add(correctIndicesArray.getInt(j));
                    }
                    System.out.println(correctIndices);
                    aS[i] = obiekt.getString("q_ans_1");
                    bS[i] = obiekt.getString("q_ans_2");
                    cS[i] = obiekt.getString("q_ans_3");
                    dS[i] = obiekt.getString("q_ans_4");
                    qS[i] = obiekt.getString("q");
                    imgS[i] = obiekt.getString("img");
                    questions.add(new Question(qS[i], Arrays.asList(aS[i], bS[i], cS[i], dS[i]), correctIndices, imgS[i]));
                    System.out.println(aS[i]);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        tv1 = (TextView) findViewById(R.id.question);
        tv2 = (TextView) findViewById(R.id.response);
        img = (ImageView) findViewById(R.id.imageView);
        rg = (RadioGroup) findViewById(R.id.optionGroup);
        a = (CheckBox) findViewById(R.id.option1);
        b = (CheckBox) findViewById(R.id.option2);
        c = (CheckBox) findViewById(R.id.option3);
        d = (CheckBox) findViewById(R.id.option4);
        btn = (Button) findViewById(R.id.next);
        rg.setVisibility(View.INVISIBLE);
        img.setVisibility(View.GONE);
        qnumber = 0;
        score = 0;
    }

    private List<Integer> getSelectedOptionIndices() {
        List<Integer> selectedIndices = new ArrayList<>();
        if (a.isChecked()) {
            selectedIndices.add(0);
        }
        if (b.isChecked()) {
            selectedIndices.add(1);
        }
        if (c.isChecked()) {
            selectedIndices.add(2);
        }
        if (d.isChecked()) {
            selectedIndices.add(3);
        }
        System.out.println(selectedIndices);
        return selectedIndices;
    }
    @SuppressLint("SetTextI18n")
    public void quiz(View v) {
        qnumber = 0;
        img.setVisibility(View.VISIBLE);
        rg.setVisibility(View.VISIBLE);
        a.setChecked(false);
        b.setChecked(false);
        c.setChecked(false);
        d.setChecked(false);
        tv2.setText("");
        a.setEnabled(true);
        b.setEnabled(true);
        c.setEnabled(true);
        d.setEnabled(true);
        btn.setText("Next");
        score = 0;
        tv1.setText(qS[0]);
        setImg(img, imgS[0]);
        a.setText(aS[0]);
        b.setText(bS[0]);
        c.setText(cS[0]);
        d.setText(dS[0]);
        btn.setOnClickListener(g -> {
            tv2.setText("");
            rg.setVisibility(View.VISIBLE);
            Question currentQuestion = questions.get(qnumber);

            List<Integer> selectedOptionIndices = getSelectedOptionIndices();
            boolean isCorrect = true;
            for (int optionIndex : selectedOptionIndices) {
                if (!currentQuestion.correctOptionIndices.contains(optionIndex)) {
                    isCorrect = false;
                    break;
                }
            }
            if (isCorrect) {
                score++;
                System.out.println("AAAAAAAAAAAAAAAAAAAAAA" + score);
            }
            qnumber++;
            if (returned) {
                qnumber--;
                returned = false;
            }
            if (qnumber < questions.size()) {
                a.setEnabled(true);
                b.setEnabled(true);
                c.setEnabled(true);
                d.setEnabled(true);
                String imageFileName = currentQuestion.img;
                setImg(img, imageFileName);
                Question nextQuestion = questions.get(qnumber);
                tv1.setText(nextQuestion.question);
                String nextImageFileName = nextQuestion.img;
                setImg(img, nextImageFileName);
                a.setText(nextQuestion.options.get(0));
                b.setText(nextQuestion.options.get(1));
                c.setText(nextQuestion.options.get(2));
                d.setText(nextQuestion.options.get(3));
                a.setChecked(false);
                b.setChecked(false);
                c.setChecked(false);
                d.setChecked(false);
                btn.setText("Next");
            } else {
                rg.setVisibility(View.INVISIBLE);
                tv1.setText("");
                tv2.setText("Wynik testu: " + score + "/" + (qS.length-1));
                returned = true;
                qnumber = 0;
                score = 0;
                btn.setText("Restart");
                rg.clearCheck();
            }
        });
    }
    public void setImg(ImageView img, String imageFileName) {
        try {
            InputStream inputStream = getAssets().open(imageFileName);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            img.setImageDrawable(drawable);
            img.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            img.setVisibility(View.GONE);
        }
    }
}


