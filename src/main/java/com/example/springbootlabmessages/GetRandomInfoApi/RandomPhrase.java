package com.example.springbootlabmessages.GetRandomInfoApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RandomPhrase {
    String text;

    public RandomPhrase(String text) {
        this.text = text;
    }

    public RandomPhrase() {
    }
}


