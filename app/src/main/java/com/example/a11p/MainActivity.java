package com.example.a11p;





import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerConversionType, spinnerFrom, spinnerTo;
    EditText editTextInput;
    Button buttonConvert;
    TextView textViewResult;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI components
        spinnerConversionType = findViewById(R.id.spinnerConversionType);
        spinnerFrom = findViewById(R.id.spinnerFromUnit);
        spinnerTo = findViewById(R.id.spinnerToUnit);
        editTextInput = findViewById(R.id.editTextInput);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        // Conversion type options
        String[] conversionTypes = {"Length", "Weight", "Temperature"};

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, conversionTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConversionType.setAdapter(typeAdapter);

        // Set up listener to update unit spinners
        spinnerConversionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                String[] units;

                switch (selectedType) {
                    case "Length":
                        units = new String[]{"Meters", "Kilometers", "Miles", "Feet"};
                        break;
                    case "Weight":
                        units = new String[]{"Kilograms", "Pounds"};
                        break;
                    case "Temperature":
                        units = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
                        break;
                    default:
                        units = new String[]{};
                }

                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_spinner_item, units);
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFrom.setAdapter(unitAdapter);
                spinnerTo.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Placeholder conversion logic
        buttonConvert.setOnClickListener(v -> {
            String inputStr = editTextInput.getText().toString();
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();
            String conversionType = spinnerConversionType.getSelectedItem().toString();

            if (inputStr.isEmpty()) {
                textViewResult.setText("Please enter a value.");
                return;
            }

            try {
                double inputValue = Double.parseDouble(inputStr);
                double result = convert(conversionType, fromUnit, toUnit, inputValue);
                textViewResult.setText("Result: " + result + " " + toUnit);
            } catch (NumberFormatException e) {
                textViewResult.setText("Invalid input!");
            }



        });


    }

    private double convert(String conversionType, String fromUnit, String toUnit, double inputValue)
    {
        // If units are the same, no conversion needed
        if (fromUnit.equals(toUnit)) return inputValue;

        switch (conversionType) {

            case "Length":
                // Convert to meters first
                double valueInMeters = 0;
                switch (fromUnit) {
                    case "Meters":
                        valueInMeters = inputValue;
                        break;
                    case "Kilometers":
                        valueInMeters = inputValue * 1000;
                        break;
                    case "Miles":
                        valueInMeters = inputValue * 1609.34;
                        break;
                    case "Feet":
                        valueInMeters = inputValue * 0.3048;
                        break;
                }

                // Convert from meters to target unit
                switch (toUnit) {
                    case "Meters":
                        return valueInMeters;
                    case "Kilometers":
                        return valueInMeters / 1000;
                    case "Miles":
                        return valueInMeters / 1609.34;
                    case "Feet":
                        return valueInMeters / 0.3048;
                }
                break;

            case "Weight":
                // Convert to grams first
                double valueInGrams = 0;
                switch (fromUnit) {
                    case "Grams":
                        valueInGrams = inputValue;
                        break;
                    case "Kilograms":
                        valueInGrams = inputValue * 1000;
                        break;
                    case "Pounds":
                        valueInGrams = inputValue * 453.592;
                        break;
                    case "Ounces":
                        valueInGrams = inputValue * 28.3495;
                        break;
                }

                // Convert from grams to target unit
                switch (toUnit) {
                    case "Grams":
                        return valueInGrams;
                    case "Kilograms":
                        return valueInGrams / 1000;
                    case "Pounds":
                        return valueInGrams / 453.592;
                    case "Ounces":
                        return valueInGrams / 28.3495;
                }
                break;

            case "Temperature":
                // Use direct conversion formulas
                switch (fromUnit) {
                    case "Celsius":
                        if (toUnit.equals("Fahrenheit")) return (inputValue * 9 / 5) + 32;
                        else if (toUnit.equals("Kelvin")) return inputValue + 273.15;
                        break;
                    case "Fahrenheit":
                        if (toUnit.equals("Celsius")) return (inputValue - 32) * 5 / 9;
                        else if (toUnit.equals("Kelvin")) return (inputValue - 32) * 5 / 9 + 273.15;
                        break;
                    case "Kelvin":
                        if (toUnit.equals("Celsius")) return inputValue - 273.15;
                        else if (toUnit.equals("Fahrenheit"))
                            return (inputValue - 273.15) * 9 / 5 + 32;
                        break;
                }
                break;
        }

        // Fallback for unknown combinations
        return inputValue;
    }
}
