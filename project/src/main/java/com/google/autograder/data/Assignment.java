package com.google.autograder.data;

import java.util.ArrayList;
import java.util.List;

/** Class containing server statistics. */
public class Assignment {

  public String name;
  public int points;
  public String status;

  public Assignment(String name, int points, String status) {
    this.name = name;
    this.points = points;
    this.status = status;
  }

}