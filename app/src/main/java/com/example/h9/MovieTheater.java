package com.example.h9;

public class MovieTheater {
    private String Id;
    private String Name;
    
    public MovieTheater(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;

    }

    public String getId(){
        return Id;
    }
    public String getName(){
        return Name;
    }
}
