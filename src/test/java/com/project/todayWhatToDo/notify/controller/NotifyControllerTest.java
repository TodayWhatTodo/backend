package com.project.todayWhatToDo.notify.controller;

import com.project.todayWhatToDo.RestDocsTest;
import com.project.todayWhatToDo.security.JwtToken;
import com.project.todayWhatToDo.notify.dto.NotifyDto;
import com.project.todayWhatToDo.notify.service.NotifyService;
import com.project.todayWhatToDo.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("알림 API")
class NotifyControllerTest extends RestDocsTest {

    @Autowired
    NotifyController notifyController;
    @MockBean
    NotifyService notifyService;
    @MockBean
    JwtService jwtService;

    @DisplayName("알림 목록 조회 성공")
    @Test
    void test() throws Exception {
        // given
        var result = List.of(new NotifyDto(1000L, LocalDateTime.of(2000, 1, 1, 0, 0, 0), "test message"));

        given(notifyService.getNotifies(any(), any()))
                .willReturn(new PageImpl<>(result, PageRequest.of(0, 5), 10));

        given(jwtService.getToken(any()))
                .willReturn(new JwtToken(1L));

        // when // then
        mockMvc.perform(get("/api/v1/alarm/list")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                )
                .andDo(print())
                .andExpectAll(
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data.contents[0].id").value(1000),
                        jsonPath("$.data.contents[0].createdAt").value("2000-01-01T00:00:00"),
                        jsonPath("$.data.contents[0].content").value("test message"),
                        jsonPath("$.data.page").value(0),
                        jsonPath("$.data.size").value(5),
                        jsonPath("$.data.totalElements").value(10),
                        jsonPath("$.data.totalPages").value(2)
                )
                .andDo(
                        document("api-v1-alarm-list-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("page").description("페이지 인덱스"),
                                        parameterWithName("size").description("페이지 사이즈")
                                ),
                                responseFields(
                                        fieldWithPath("status").description("API 조회 상태 메세지"),
                                        fieldWithPath("data.contents").description("알림 목록"),
                                        fieldWithPath("data.contents[].id").description("알림 데이터 식별자"),
                                        fieldWithPath("data.contents[].createdAt").description("알림 생성 시간"),
                                        fieldWithPath("data.contents[].content").description("알림 내용"),
                                        fieldWithPath("data.page").description("페이지 인덱스"),
                                        fieldWithPath("data.size").description("반환된 알림 목록 개수"),
                                        fieldWithPath("data.totalElements").description("총 알림 개수"),
                                        fieldWithPath("data.totalPages").description("총 페이지 수")
                                )
                        )
                );
    }

}