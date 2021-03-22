package com.example.h9;

import android.graphics.Movie;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MovieTheaterHandler {
    public ArrayList<String> movies = new ArrayList<String>();
    ArrayList<MovieTheater> movietheaters = new ArrayList<MovieTheater>();
    int mtAreas[] = {1014, 1015, 1016, 1017, 1018, 1019, 1021, 1022, 1041};
    public MovieTheaterHandler(){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String url = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            MovieTheater mt = null;
            for (int i = 0; i < nList.getLength(); i++){
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getElementsByTagName("ID").item(0).getTextContent();

                    String name = element.getElementsByTagName("Name").item(0).getTextContent();
                    mt = new MovieTheater(id, name);
                    movietheaters.add(mt);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("##########DONE1##########");
        }
    }
    public ArrayList<MovieTheater> getArray(){
        return movietheaters;
    }

    public void getMovieInfo(String date, String mt, String timeAfter, String timeBefore){
        try {
            String title;
            String url;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter localTime_format = DateTimeFormatter.ofPattern("HH:mm");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            url = "https://www.finnkino.fi/xml/Schedule/?area=" + mt + "&dt=" + date;
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Movie date formatting
                    String Date = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    LocalDateTime dateTime = LocalDateTime.parse(Date, formatter);
                    LocalTime movie_LocalTime = LocalTime.parse(dateTime.format(localTime_format));

                    // Jos ei ole annettu aikaväliä
                    if(timeAfter.equals("") || timeBefore.equals("")){
                        title = element.getElementsByTagName("Title").item(0).getTextContent();
                        movies.add(title);
                    }else{
                        LocalTime start_LocalTime = LocalTime.parse(timeAfter, localTime_format);
                        LocalTime end_LocalTime = LocalTime.parse(timeBefore, localTime_format);
                        System.out.println("aiak on " + timeAfter);
                        System.out.println("aiak on " + timeBefore);
                        if(movie_LocalTime.isAfter(start_LocalTime) && movie_LocalTime.isBefore(end_LocalTime)){
                            title = element.getElementsByTagName("Title").item(0).getTextContent();
                            movies.add(title);
                        }
                    }
                }
            }
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(SAXException e){
            e.printStackTrace();
        }finally{
            System.out.println("##########DONE2##########");
        }
    }

    public void getMovietheatersWithMovie(String date, String mtID, String timeAfter, String timeBefore, String nameOfTheMovie){
        try {
            String title = null, time = null, place = null, timeAndPlace = null;
            System.out.println("Etsitään elokuvaa: " + nameOfTheMovie);
            String url;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter disp_format = DateTimeFormatter.ofPattern("dd.MM.yyyy 'Klo:' HH:mm");
            DateTimeFormatter localTime_format = DateTimeFormatter.ofPattern("HH:mm");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (mtID.equals("1029")) {
                for (int mt : mtAreas) {
                    url = "https://www.finnkino.fi/xml/Schedule/?area=" + mt + "&dt=" + date;
                    Document doc = builder.parse(url);
                    doc.getDocumentElement().normalize();
                    NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
                    for (int i = 0; i < nList.getLength(); i++) {
                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            title = element.getElementsByTagName("Title").item(0).getTextContent();
                            if (title.equals(nameOfTheMovie)) {
                                // Movie date formatting
                                String Date = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                                LocalDateTime dateTime = LocalDateTime.parse(Date, formatter);
                                LocalTime movie_LocalTime = LocalTime.parse(dateTime.format(localTime_format));

                                // Jos ei ole annettu aikaväliä

                                if(timeAfter.equals("") || timeBefore.equals("")){
                                    time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                                    place = element.getElementsByTagName("Theatre").item(0).getTextContent();
                                    LocalDateTime movieTime = LocalDateTime.parse(time, formatter);
                                    String formatted_time = movieTime.format(disp_format);
                                    timeAndPlace = place + " " + formatted_time;
                                    System.out.println(timeAndPlace);
                                    movies.add(timeAndPlace);
                                }else{
                                    LocalTime start_LocalTime = LocalTime.parse(timeAfter, localTime_format);
                                    LocalTime end_LocalTime = LocalTime.parse(timeBefore, localTime_format);
                                    System.out.println("aiak on " + timeAfter);
                                    System.out.println("aiak on " + timeBefore);
                                    if(movie_LocalTime.isAfter(start_LocalTime) && movie_LocalTime.isBefore(end_LocalTime)){
                                        time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                                        place = element.getElementsByTagName("Theatre").item(0).getTextContent();
                                        LocalDateTime movieTime = LocalDateTime.parse(time, formatter);
                                        String formatted_time = movieTime.format(disp_format);
                                        timeAndPlace = place + " " + formatted_time;
                                        System.out.println(timeAndPlace);
                                        movies.add(timeAndPlace);
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                url = "https://www.finnkino.fi/xml/Schedule/?area=" + mtID + "&dt=" + date;
                Document doc = builder.parse(url);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        title = element.getElementsByTagName("Title").item(0).getTextContent();
                        if (title.equals(nameOfTheMovie)) {
                            // Movie date formatting
                            String Date = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                            LocalDateTime dateTime = LocalDateTime.parse(Date, formatter);
                            LocalTime movie_LocalTime = LocalTime.parse(dateTime.format(localTime_format));

                            // Jos ei ole annettu aikaväliä
                            if(timeAfter.equals("") || timeBefore.equals("")){
                                time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                                place = element.getElementsByTagName("Theatre").item(0).getTextContent();
                                LocalDateTime movieTime = LocalDateTime.parse(time, formatter);
                                String formatted_time = movieTime.format(disp_format);
                                timeAndPlace = place + " " + formatted_time;
                                movies.add(timeAndPlace);
                            }else {
                                LocalTime start_LocalTime = LocalTime.parse(timeAfter, localTime_format);
                                LocalTime end_LocalTime = LocalTime.parse(timeBefore, localTime_format);
                                if (movie_LocalTime.isAfter(start_LocalTime) && movie_LocalTime.isBefore(end_LocalTime)) {
                                    time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                                    place = element.getElementsByTagName("Theatre").item(0).getTextContent();
                                    LocalDateTime movieTime = LocalDateTime.parse(time, formatter);
                                    String formatted_time = movieTime.format(disp_format);
                                    timeAndPlace = place + " " + formatted_time;
                                    System.out.println(timeAndPlace);
                                    movies.add(timeAndPlace);
                                }
                            }
                        }
                    }
                }
            }
        for (String i : movies){
            System.out.println(i);
        }
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(SAXException e){
            e.printStackTrace();
        }finally{
            System.out.println("##########DONE3##########");
        }
    }

        public String getMovieTheaterId(String movieTheaterName){
        for (MovieTheater mt : movietheaters){
            if(mt.getName().equals(movieTheaterName)){
                return mt.getId();
            }
        }
        return null;
    }
}

