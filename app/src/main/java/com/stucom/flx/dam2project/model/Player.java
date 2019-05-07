package com.stucom.flx.dam2project.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Player implements Comparable<Player>{

    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Image")
    private String image;
  @SerializedName("data")
  private String data;
  @SerializedName("totalScore")
  private int totalScore;
  @SerializedName("lastLevel")
  private String lastLevel;
  @SerializedName("lastScore")
  private int lastScore;
  @SerializedName("score")
  private String score;

  public Player(){
  }
    public Player(int id, String name, String image, String data, int totalScore,String lastLevel,int lastScore,String score ) {
      this.id = id;
      this.name = name;
      this.image = image;
      this.data = data;
      this.totalScore = totalScore;
      this.lastLevel = lastLevel;
      this.totalScore = lastScore;
      this.score = score;
    }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public String getLastLevel() {
    return lastLevel;
  }

  public void setLastLevel(String lastLevel) {
    this.lastLevel = lastLevel;
  }

  public int getLastScore() {
    return lastScore;
  }

  public void setLastScore(int lastScore) {
    this.lastScore = lastScore;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }


  public int getId() {
    return id;
  }
  @Override
  public int compareTo(@NonNull Player o) {
    if(getTotalScore() == o.getTotalScore()){
      return 0;
    } else if (getTotalScore() < o.getTotalScore()) {
      return 1;
    } else {
      return -1;
    }
  }
}


