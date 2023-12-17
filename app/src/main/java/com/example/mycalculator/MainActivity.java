package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2;
    double previousResult = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //haetaan nappien idt array listasta
        TypedArray buttonIds = getResources().obtainTypedArray(R.array.button_ids);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButtonEvent(view);
            }
        };

        // Aseta OnClickListener jokaiselle napille
        for (int i = 0; i < buttonIds.length(); i++) {
            int buttonId = buttonIds.getResourceId(i, 0);
            Button button = findViewById(buttonId);
            if (button != null) {
                button.setOnClickListener(buttonClickListener);
            }
        }
        buttonIds.recycle();
    }

    public void clickedButtonEvent(View view) {
        tv1 = findViewById(R.id.etCalc);
        tv2 = findViewById(R.id.result);
        int buttonId = view.getId();
        String buttonIdString = getResources().getResourceEntryName(buttonId);

        //laskutoimituksen saadessa errorin, tyhjennetään tekstikenttä seuraavalla napin painalluksella
        if (tv1.getText().toString().contains("Error")) {
            tv1.setText("");
        }

        //yhtäkuin hakee result-tekstikentästä tuloksen
        if (buttonIdString.equals("buttonYht")) {
            String getResult = tv2.getText().toString();
            if (getResult.length() <= 0) {
                tv1.setText("Error");
            } else if (getResult.length() > 0) {
                tv1.setText("");
                appendTextWithColor(getResult);
                tv2.setText(" ");
            }
        }
        //muita nappeja painamalla aina lasketaan laskutoimitus ja näytetään result-tekstikentässä
        else {
            switch (buttonIdString) {
                case "button0":
                    appendTextWithColor("0");
                    break;
                case "button1":
                    appendTextWithColor("1");
                    break;
                case "button2":
                    appendTextWithColor("2");
                    break;
                case "button3":
                    appendTextWithColor("3");
                    break;
                case "button4":
                    appendTextWithColor("4");
                    break;
                case "button5":
                    appendTextWithColor("5");
                    break;
                case "button6":
                    appendTextWithColor("6");
                    break;
                case "button7":
                    appendTextWithColor("7");
                    break;
                case "button8":
                    appendTextWithColor("8");
                    break;
                case "button9":
                    appendTextWithColor("9");
                    break;
                case "buttonKeno":
                    appendTextWithColor("/");
                    break;
                case "buttonPlus":
                    appendTextWithColor("+");
                    break;
                case "buttonMiinus":
                    appendTextWithColor("-");
                    break;
                case "buttonPiste":
                    appendTextWithColor(".");
                    break;
                case "buttonX":
                    appendTextWithColor("*");
                    break;
                case "buttonTyhjenna":
                    tv1.setText("");
                    break;
                case "buttonSulkuAuki":
                    appendTextWithColor("(");
                    break;
                case "buttonSulkuKiinni":
                    appendTextWithColor(")");
                    break;
                case "buttonProsentti":
                    appendTextWithColor("%");
                    break;
                case "buttonPoista":
                    String originalText = tv1.getText().toString();
                    tv1.setText("");
                    appendTextWithColor(originalText.substring(0, originalText.length() - 1));
                    break;
            }
            String result = calculate();
            if (result != null) {
                tv2.setText(result);
            } else {
                tv2.setText("");
            }
        }
    }

    //laskee laskutoimituksen
    private double evaluateExpression(String expression) {
        try {
            Expression e = new ExpressionBuilder(expression)
                    .build();
            return e.evaluate();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Something went wrong");
        }
    }

    //asettaa tekstille värit arvon perusteella
    private void appendTextWithColor(String buttonText) {
        String currentText = tv1.getText().toString();
        SpannableString spannableString = new SpannableString(currentText + buttonText);

        int numberColor = Color.rgb(236, 169, 169);
        int operatorColor = Color.rgb(0, 102, 139);

        for (int i = 0; i < spannableString.length(); i++) {
            char currentChar = spannableString.charAt(i);
            int charColor;

            if (Character.isDigit(currentChar) || currentChar == '.') {
                charColor = numberColor;
            } else {
                charColor = operatorColor;
            }
            spannableString.setSpan(new ForegroundColorSpan(charColor), i, i + 1, 0);
        }
        tv1.setText(spannableString);
    }

    //tarkistaa laskutoimituksen ja lähettää sen evaluateExpression-metodille
    private String calculate() {
        try {
            String expression = tv1.getText().toString();
            double result;

            Pattern pattern = Pattern.compile("(\\d+)%");
            Matcher matcher = pattern.matcher(expression);

            if (matcher.find()) {
                String numberBeforePercent = matcher.group(1);
                double valueBeforePercent = Double.parseDouble(numberBeforePercent);
                String updatedExpression = expression.replace(matcher.group(), String.valueOf(valueBeforePercent / 100.0));
                result = evaluateExpression(updatedExpression);
            } else {
                result = evaluateExpression(expression);
            }

            previousResult = result;

            return String.valueOf(result);
        } catch (Exception e) {
            return null;
        }
    }
}