package com.karim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
public class Patient {

    @Id
    private String id ;

    private String firstName ;

    private String lastName ;

    private String phone ;

    private String email ;

    @Embedded
    private Address address;

}
