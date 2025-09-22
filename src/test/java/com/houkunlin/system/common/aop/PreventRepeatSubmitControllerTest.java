package com.houkunlin.system.common.aop;

import com.houkunlin.system.common.aop.annotation.PreventRepeatSubmit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PreventRepeatSubmitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void m1() throws Exception {
        String path = "/PreventRepeatSubmit/m1";
        mockMvc.perform(get(path))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        mockMvc.perform(get(path))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m11() throws Exception {
        String path = "/PreventRepeatSubmit/m11";
        mockMvc.perform(get(path))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        mockMvc.perform(get(path))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m2() throws Exception {
        String path = "/PreventRepeatSubmit/m2";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2 + "  "))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m22() throws Exception {
        String path = "/PreventRepeatSubmit/m22";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m3() throws Exception {
        String path = "/PreventRepeatSubmit/m3";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=2").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=3").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=3").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path + "?t=2").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m33() throws Exception {
        String path = "/PreventRepeatSubmit/m33";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=2").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=3").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=3").contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path + "?t=2").contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m4() throws Exception {
        String path = "/PreventRepeatSubmit/m4";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json1 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }

    @Test
    void m44() throws Exception {
        String path = "/PreventRepeatSubmit/m44";
        String json1 = "{}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json1));
        mockMvc.perform(post(path + "?t=1").contentType(MediaType.APPLICATION_JSON).content(json1 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));

        String json2 = "{\"name\":\"test\", \"age\":12}";
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(content().json(json2));
        mockMvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(json2 + "  "))
                .andDo(log())
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(PreventRepeatSubmit.DEFAULT_MESSAGE));
    }
}
