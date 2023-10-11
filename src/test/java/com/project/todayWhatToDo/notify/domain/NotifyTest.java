package com.project.todayWhatToDo.notify.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotifyTest {

    @DisplayName("notify 최조 생성시 확인 필드는 false 이다.")
    @Test
    public void defaultValue() {
        //given
        var notify = Notify.builder()
                .userId(1L)
                .content("alert")
                .build();
        //when //then
        assertThat(notify.getIsChecked()).isFalse();
    }

    @DisplayName("entity 값은 dto 값과 동일하다.")
    @Test
    public void toDto() {
        //given
        var notify = Notify.builder()
                .userId(1L)
                .content("alert")
                .build();

        //when
        var dto = notify.toDto();
        //then
        assertThat(dto).extracting("content")
                .isEqualTo("alert");
    }

    @DisplayName("check 호출하면 메시지 확인 상태로 변경된다.")
    @Test
    public void check() {
        //given
        var notify = Notify.builder()
                .userId(1L)
                .content("alert")
                .build();
        //when
        notify.check();
        //then
        assertThat(notify.getIsChecked()).isTrue();
    }
}
