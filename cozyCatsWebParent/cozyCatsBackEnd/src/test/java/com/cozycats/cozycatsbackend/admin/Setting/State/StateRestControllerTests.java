package com.cozycats.cozycatsbackend.admin.Setting.State;


import com.cozycats.cozycatsbackend.admin.Setting.Country.CountryRepository;
import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepo;

    @Autowired StateRepository stateRepo;

    @Test
    @WithMockUser(username = "eraldi@gmail.com", password = "aldi2020", roles = "Admin")
    public void testListByCountries() throws Exception {
        Integer countryId = 3;
        String url = "/states/list_by_country/" + countryId;

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        State[] states = objectMapper.readValue(jsonResponse, State[].class);

        assertThat(states).hasSizeGreaterThan(1);
    }

//    @Test
//    @WithMockUser(username = "eraldi@gmail.com", password = "aldi2020", roles = "Admin")
//    public void testCreateState() throws Exception {
//        String url = "/states/save";
//        Integer countryId = 2;
//        Country country = countryRepo.findById(countryId).get();
//        State state = new State("Arizona", country);
//
//        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
//                        .content(objectMapper.writeValueAsString(state))
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        Integer stateId = Integer.parseInt(response);
//        Optional<State> findById = stateRepo.findById(stateId);
//
//        assertThat(findById.isPresent());
//    }
//
//    @Test
//    @WithMockUser(username = "eraldi@gmail.com", password = "aldi2020", roles = "Admin")
//    public void testUpdateState() throws Exception {
//        String url = "/states/save";
//        Integer stateId = 9;
//        String stateName = "Alaska";
//
//        State state = stateRepo.findById(stateId).get();
//        state.setName(stateName);
//
//        mockMvc.perform(post(url).contentType("application/json")
//                        .content(objectMapper.writeValueAsString(state))
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(String.valueOf(stateId)));
//
//        Optional<State> findById = stateRepo.findById(stateId);
//        assertThat(findById.isPresent());
//
//        State updatedState = findById.get();
//        assertThat(updatedState.getName()).isEqualTo(stateName);
//
//    }
//
//    @Test
//    @WithMockUser(username = "eraldi@gmail.com", password = "aldi2020", roles = "Admin")
//    public void testDeleteState() throws Exception {
//        Integer stateId = 6;
//        String uri = "/states/delete/" + stateId;
//
//        mockMvc.perform(get(uri)).andExpect(status().isOk());
//
//        Optional<State> findById = stateRepo.findById(stateId);
//
//        assertThat(findById).isNotPresent();
//    }
}
