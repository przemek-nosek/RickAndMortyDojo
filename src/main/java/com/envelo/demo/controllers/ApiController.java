package com.envelo.demo.controllers;

import com.envelo.demo.model.Episode;
import com.envelo.demo.model.dto.SeasonEpisodeCountDto;
import com.envelo.demo.services.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
@CrossOrigin
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/all")
    public List<SeasonEpisodeCountDto> getAllSeasons() {
        return apiService.getAllSeasons();
    }

    @GetMapping("/{id}")
    public List<Episode> getSeason(@PathVariable Long id) {
        return apiService.getSeason(id);
    }
}
