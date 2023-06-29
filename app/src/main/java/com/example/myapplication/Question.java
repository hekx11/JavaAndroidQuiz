package com.example.myapplication;

import java.util.List;

public class Question {
    String question;
    List<String> options;
    List<Integer> correctOptionIndices;
    String img;

    public Question(String question, List<String> options, List<Integer> correctOptionIndices, String img) {
        this.question = question;
        this.options = options;
        this.correctOptionIndices = correctOptionIndices;
        this.img = img;
    }
}
