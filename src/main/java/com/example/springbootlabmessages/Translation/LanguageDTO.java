package com.example.springbootlabmessages.Translation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageDTO {

    private String langCode;


    public LanguageDTO() {
    }
    public LanguageDTO(String langCode) {
        this.langCode = langCode;
    }


    @Override
    public String toString() {
        return "Language{" +
                "langCode='" + langCode + '\'' +
                '}';
    }
}
