package com.kivilev.controller.api.v1;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

class ControllerUtils {

    private ControllerUtils() {
    }

    public static String executePutRequest(
            MockMvc mockMvc,
            String path,
            String body,
            ResultMatcher statusResultMatcher,
            ResultMatcher... resultMatchers
    ) {
        try {
            var resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.put(path)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
            ).andExpect(statusResultMatcher);

            for (var matcher : resultMatchers) {
                resultActions.andExpect(matcher);
            }

            return getResponseContent(resultActions);
        } catch (Exception e) {
            throw new RuntimeException("Error during execution put request");
        }
    }

    public static String executeGetRequest(
            MockMvc mockMvc,
            String path,
            ResultMatcher statusResultMatcher,
            ResultMatcher... resultMatchers
    ) {
        try {
            var resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.get(path)
            ).andExpect(statusResultMatcher);

            for (var matcher : resultMatchers) {
                resultActions.andExpect(matcher);
            }

            return getResponseContent(resultActions);
        } catch (Exception e) {
            throw new RuntimeException("Error during execution get request");
        }
    }

    public static String executePostRequest(
            MockMvc mockMvc,
            String path,
            String body,
            ResultMatcher statusResultMatcher,
            ResultMatcher... resultMatchers
    ) {
        try {
            var resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(path)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(body)
            ).andExpect(statusResultMatcher);

            for (var matcher : resultMatchers) {
                resultActions.andExpect(matcher);
            }

            return getResponseContent(resultActions);
        } catch (Exception e) {
            throw new RuntimeException("Error during execution post request");
        }
    }

    private static String getResponseContent(ResultActions resultActions) throws UnsupportedEncodingException {
        var response = resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        return response.isEmpty() ? resultActions.andReturn().getResponse().getErrorMessage() : response;
    }
}