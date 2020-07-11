package com.example.projectworkv02.database;

public class Film {

    private long id;
    private String name;
    private String description;
    private String imgCardboard;
    private String imgLarge;
    private float vote;
    private float personalVote;
    private String releaseDate;
    private int watch;
    private int watched;
    private long film_id;
    private int update;

    public Film() {}

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

    public long getFilm_id() {
        return film_id;
    }

    public void setFilm_id(long film_id) {
        this.film_id = film_id;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public float getVote() {
        return vote;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPersonalVote() {
        return personalVote;
    }

    public void setPersonalVote(float personalVote) {
        this.personalVote = personalVote;
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }
}
