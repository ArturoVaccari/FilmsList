package com.example.projectworkv02.database;

public class Film {

    private long id;
    private String name;
    private String description;
    private String imgCardboard;
    private String imgLarge;
    private int watch;

    public Film() {}

    public Film(long id, String name, String description, String imgCardboard, String imgLarge, int watch) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgCardboard = imgCardboard;
        this.imgLarge = imgLarge;
        this.watch = watch;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgCardboard() {
        return imgCardboard;
    }

    public void setImgCardboard(String imgCardboard) {
        this.imgCardboard = imgCardboard;
    }

    public String getImgLarge() {
        return imgLarge;
    }

    public void setImgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }
}
