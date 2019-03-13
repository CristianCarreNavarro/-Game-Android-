package com.stucom.flx.dam2project.model;

import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Image")
    private String image;
  @SerializedName("data")
  private String data;
  @SerializedName("totalScore")
  private String totalScore;
  @SerializedName("lastLevel")
  private String lastLevel;
  @SerializedName("lastScore")
  private String lastScore;
  @SerializedName("score")
  private String score;

  public Player(){
  }
    public Player(int id, String name, String image, String data, String totalScore,String lastLevel,String lastScore,String score ) {
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

  public String getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(String totalScore) {
    this.totalScore = totalScore;
  }

  public String getLastLevel() {
    return lastLevel;
  }

  public void setLastLevel(String lastLevel) {
    this.lastLevel = lastLevel;
  }

  public String getLastScore() {
    return lastScore;
  }

  public void setLastScore(String lastScore) {
    this.lastScore = lastScore;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }


}


