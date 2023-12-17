package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText et;
    double previousResult = Double.NaN;
    boolean isPreviousResult = false;

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
        et = findViewById(R.id.etCalc);
        int buttonId = view.getId();
        String buttonIdString = getResources().getResourceEntryName(buttonId);

        //aina jotain nappia painattaessa ohjelma tarkistaa onko olemassa aiempi tulos
        //if-lausekkeen tarkoitus on pyyhkiä aiempi laskutoimitus-lauseke pois ja jatkaa laskemista aiemman tuloksen kanssa
        if (isPreviousResult) {
            et.setText(String.valueOf(previousResult));
            isPreviousResult = false; //muutetaan isPreviousResult falseksi, jotta aina nappia painaessa ei tulla if-lausekkeeseen
        }

        switch (buttonIdString) {
            case "button0":
                et.setText(et.getText() + "0");
                break;
            case "button1":
                et.setText(et.getText() + "1");
                break;
            case "button2":
                et.setText(et.getText() + "2");
                break;
            case "button3":
                et.setText(et.getText() + "3");
                break;
            case "button4":
                et.setText(et.getText() + "4");
                break;
            case "button5":
                et.setText(et.getText() + "5");
                break;
            case "button6":
                et.setText(et.getText() + "6");
                break;
            case "button7":
                et.setText(et.getText() + "7");
                break;
            case "button8":
                et.setText(et.getText() + "8");
                break;
            case "button9":
                et.setText(et.getText() + "9");
                break;
            case "buttonKeno":
                et.setText(et.getText() + "/");
                break;
            case "buttonPlus":
                et.setText(et.getText() + "+");
                break;
            case "buttonMiinus":
                et.setText(et.getText() + "-");
                break;
            case "buttonPiste":
                et.setText(et.getText() + ".");
                break;
            case "buttonX":
                et.setText(et.getText() + "*");
                break;
            case "buttonTyhjenna":
                et.setText("");
                break;
            case "buttonSulut":
                et.setText(et.getText() + "(");
                break;
            case "buttonProsentti":
                et.setText(et.getText() + "%");
                break;
            case "buttonPlusMiinus":
                et.setText(et.getText() + "-");
                break;
            case "buttonYht":
                try {
                    String expression = et.getText().toString();
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

                    et.setText(expression + "\n" + String.valueOf(result));
                    previousResult = result;
                    isPreviousResult = true;

                    /*String expression = et.getText().toString();
                    double result = evaluateExpression(expression); //viedään laskutoimitus evaluateExpression-metodiin

                    et.setText(expression + "\n" + String.valueOf(result));

                    previousResult = result;
                    isPreviousResult = true;*/ //asetetaan isPreviousResult trueksi, jotta seuraavan kerran mitä tahansa nappia painaessa mennään ylempänä esitettyyn if-lausekkeeseen
                } catch (Exception e) {
                    et.setText("Virheellinen laskutoimitus");
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
            throw new IllegalArgumentException("Virheellinen laskutoimitus");
        }
    }
}