package com.example.h9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> dates_Disp = new ArrayList<String>();
    private Spinner movie_date;
    private Spinner movie_place;
    Button button;
    EditText timeStart;
    EditText timeEnd;
    EditText movieName;
    int t1hour, t1minute, t2hour, t2minute;
    String timeAfter = "", timeBefore = "", nameOfTheMovie = "";
    String mt_ID, new_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        // Määritellään tarvittavat objectit
        // Spinnerit
        movie_place = (Spinner) findViewById(R.id.spinner);
        movie_date = (Spinner) findViewById(R.id.spinner2);

        // Hae näppäin
        button = (Button) findViewById(R.id.button);

        // Jälkeen ja ennen kellonajat
        timeStart = (EditText) findViewById(R.id.date_time_input_start);
        timeEnd = (EditText) findViewById(R.id.date_time_input_end);
        timeStart.setInputType(InputType.TYPE_NULL);
        timeEnd.setInputType(InputType.TYPE_NULL);

        // ELokuvan nimi
        movieName = (EditText) findViewById(R.id.moviename);

        // Lisätään päivämäärät spinneriin
        addDates();
        new_date = dates_Disp.get(0).substring(dates_Disp.get(0).length() - 10);
        MovieTheaterHandler mtHandler = new MovieTheaterHandler();

        // Elokuvateatterin valinta listener
        movie_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String movieTheater = parent.getItemAtPosition(pos).toString();
                mt_ID = mtHandler.getMovieTheaterId(movieTheater);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Päivämäärän valinta listener
        movie_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = parent.getItemAtPosition(pos).toString();
                new_date = selected.substring(selected.length() - 10);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Aloita jälkeen listener
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeInput_start(timeStart);
            }
        });

        // Aloita ennen listener
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeInput_end(timeEnd);
            }
        });

        // Hae-painikkeen listener
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nameOfTheMovie = movieName.getText().toString();
                if ( nameOfTheMovie.isEmpty()){
                    mtHandler.getMovieInfo(new_date, mt_ID, timeAfter, timeBefore);
                }else{
                    mtHandler.getMovietheatersWithMovie(new_date, mt_ID, timeAfter, timeBefore, nameOfTheMovie);
                }
                System.out.println("setOnClickListene ");
                timeAfter = ""; timeBefore = "";
                showMovies(mtHandler.movies, nameOfTheMovie);
                mtHandler.movies.clear();
                movieName.getText().clear();
                timeStart.getText().clear();
                timeEnd.getText().clear();
            }
        });
    }

    private void showDateTimeInput_start(EditText date_time_in) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t1hour = hourOfDay;
                t1minute = minute;
                String time = t1hour + ":" + t1minute;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                try{
                    Date date = timeFormat.parse(time);
                    timeAfter = timeFormat.format(date);
                    date_time_in.setText(timeFormat.format(date));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }, 24, 0, true
        );
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(t1hour,t1minute);
        timePickerDialog.show();
    }

    private void showDateTimeInput_end(EditText date_time_in) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                t2hour = hourOfDay;
                t2minute = minute;
                String time = t2hour + ":" + t2minute;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                try{
                    Date date = timeFormat.parse(time);
                    timeBefore = timeFormat.format(date);
                    date_time_in.setText(timeFormat.format(date));
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }, 24, 0, true
        );
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(t1hour,t1minute);
        timePickerDialog.show();
    }

    public void addDates(){
        LocalDateTime pvm = LocalDate.now().atStartOfDay();
        Resources res = getResources();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String[] dates_xml = res.getStringArray(R.array.Dates);
        for(String d : dates_xml){
            LocalDateTime date = LocalDateTime.parse(d);
            if(date.isBefore(pvm)){
                continue;
            }
            if(date.equals(pvm)) {
                String s = "Tänään, " + date.format(myFormatObj);
                dates_Disp.add(s);
            }else if(date.equals(pvm.plusDays(1))){
                String s = "Huomenna, "+ date.format(myFormatObj);
                dates_Disp.add(s);
            }else {
                if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                    dates_Disp.add("Ma, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
                    dates_Disp.add("Ti, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
                    dates_Disp.add("Ke, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.THURSDAY)){
                    dates_Disp.add("To, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                    dates_Disp.add("Pe, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                    dates_Disp.add("La, " + date.format(myFormatObj));
                }else if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                    dates_Disp.add("Su, " + date.format(myFormatObj));
                }else{
                    System.out.println("How did you end up here???" +
                            "Anyway addDates() is broken");
                }
            }
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, dates_Disp);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        movie_date.setAdapter(adapter);
    }

    public void showMovies(ArrayList<String> movies, String  nameOfTheMovie){
        Intent intent = new Intent(this,ActivitySecond.class);
        intent.putStringArrayListExtra("movies", movies);
        System.out.println("showMovies ");
        intent.putExtra("movieName", nameOfTheMovie);
        startActivity(intent);
    }

}