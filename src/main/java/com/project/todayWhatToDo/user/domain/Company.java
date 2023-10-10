package com.project.todayWhatToDo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Company {
    @Column(name = "company_name")
    private String name;
    @Column
    private String address;

    @Builder
    private Company(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
