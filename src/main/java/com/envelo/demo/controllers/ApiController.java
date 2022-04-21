package com.envelo.demo.controllers;

import com.envelo.demo.model.dto.SeasonEpisodeCountDto;
import com.envelo.demo.services.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/api")
    public List<SeasonEpisodeCountDto> aaa() {
        return apiService.getAllSeasons();
    }
}
