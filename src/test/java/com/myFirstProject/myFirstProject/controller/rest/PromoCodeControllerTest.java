//package com.myFirstProject.myFirstProject.controller.rest;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.myFirstProject.myFirstProject.dto.PromoCodeReq;
//import com.myFirstProject.myFirstProject.enums.PromoType;
//import com.myFirstProject.myFirstProject.service.PromoCodeService;
//import kafka.utils.Json;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.Assert.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//public class PromoCodeControllerTest {
//
//    private MockMvc mvc;
//
//    @InjectMocks
//    private PromoCodeController promoCodeController;
//    @Mock
//    private PromoCodeService promoCodeService;
//
//    //    Виконується перед кожним тестом
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        MappingJackson2HttpMessageConverter converter = getConverter();
//        mvc = MockMvcBuilders.standaloneSetup(promoCodeController)
//                .setMessageConverters(converter)
//                .build();
//    }
//
//    public static MappingJackson2HttpMessageConverter getConverter() {
//        ObjectMapper mapper = getObjectMapper();
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(mapper);
//        return converter;
//    }
//
//    public static ObjectMapper getObjectMapper() {
//        return new ObjectMapper()
//                .registerModule(new Jdk8Module())
//                .registerModule(new JavaTimeModule())
//                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
//                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
//    }
//
//    @Test
//    public void savePromoCode() throws Exception {
////        Given
//        Mockito.when(promoCodeService.save(buildPromoCodeReq())).thenReturn("PROMO_10");
////        When
//        mvc.perform(post("/promocode/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("\"id\": \"PROMO_3\",\n" +
//                        "\t\"promoType\": \"PERCENT\",\n" +
//                        "\t\"value\": \"3\",\n" +
//                        "\t\"expiredDate\": \"2029-10-30T15:00:00.000\"")
//        )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "http://localhost:8081/promocode/PROMO_10"));
//    }
//
//    private PromoCodeReq buildPromoCodeReq() {
//        PromoCodeReq promoCodeReq = new PromoCodeReq();
//        promoCodeReq.setId("PROMO_10");
//        promoCodeReq.setValue(BigDecimal.TEN);
//        promoCodeReq.setPromoType(PromoType.PERCENT);
//        promoCodeReq.setExpired(LocalDateTime.now().plusDays(10L));
//        return promoCodeReq;
//    }
//}