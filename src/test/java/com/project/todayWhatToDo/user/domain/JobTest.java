package com.project.todayWhatToDo.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JobTest {

    @DisplayName("직책 변경시 반영된다.")
    @Test
    public void setPosition() {
        //given
        var job = Job.builder()
                .position("before")
                .build();
        //when
        job.setPosition("after");
        //then
        assertThat(job.getPosition()).isEqualTo("after");
    }

    @DisplayName("직책 변경시 null으로 변경되지 않는다.")
    @Test
    public void setPositionNull() {
        //given
        var job = Job.builder()
                .position("before")
                .build();
        //when
        job.setPosition(null);
        //then
        assertThat(job.getPosition()).isEqualTo("before");
    }

    @DisplayName("회사이름 변경시 반영된다.")
    @Test
    public void setCompanyName() {
        //given
        var job = Job.builder()
                .companyName("before")
                .build();
        //when
        job.setCompanyName("after");
        //then
        assertThat(job.getCompanyName()).isEqualTo("after");
    }

    @DisplayName("회사이름 변경시 null으로 변경되지 않는다.")
    @Test
    public void setCompanyNameNull() {
        //given
        var job = Job.builder()
                .companyName("before")
                .build();
        //when
        job.setCompanyName(null);
        //then
        assertThat(job.getCompanyName()).isEqualTo("before");
    }

    @DisplayName("주소 변경시 반영된다.")
    @Test
    public void setAddress() {
        //given
        var job = Job.builder()
                .address("before")
                .build();
        //when
        job.setAddress("after");
        //then
        assertThat(job.getAddress()).isEqualTo("after");
    }

    @DisplayName("주소 변경시 null으로 변경되지 않는다.")
    @Test
    public void setAddressNull() {
        //given
        var job = Job.builder()
                .address("before")
                .build();
        //when
        job.setAddress(null);
        //then
        assertThat(job.getAddress()).isEqualTo("before");
    }
}
