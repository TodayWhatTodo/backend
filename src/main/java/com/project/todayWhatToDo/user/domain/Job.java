package com.project.todayWhatToDo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Job {
    @Column(name = "company_name", nullable = false)
    private String companyName;
    @Column(name = "company_address", nullable = false)
    private String address;
    @Column(name = "company_position", nullable = false)
    private String position;

    @Builder
    private Job(String companyName, String address, String position) {
        this.companyName = companyName;
        this.address = address;
        this.position = position;
    }

    public void setCompanyName(String companyName) {
        if (companyName != null) this.companyName = companyName;
    }

    public void setAddress(String address) {
        if (address != null) this.address = address;
    }

    public void setPosition(String position) {
        if (position != null) this.position = position;
    }
}
