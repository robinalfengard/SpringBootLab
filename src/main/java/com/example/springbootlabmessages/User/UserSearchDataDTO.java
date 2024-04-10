package com.example.springbootlabmessages.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchDataDTO {

    private String searchText;



    @Override
    public String toString() {
        return "UserSearchDataDTO{" +
                "searchText='" + searchText + '\'' +
                '}';
    }
}
