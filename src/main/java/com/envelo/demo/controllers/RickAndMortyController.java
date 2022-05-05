package com.envelo.demo.controllers;

import com.envelo.demo.model.Episode;
import com.envelo.demo.model.SeasonEpisodeCount;
import com.envelo.demo.services.RickAndMortyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
@CrossOrigin
public class RickAndMortyController {

    private final RickAndMortyService rickAndMortyService;

    @GetMapping("/all")
    public List<SeasonEpisodeCount> getAllSeasons() {
        return rickAndMortyService.getAllSeasons();
    }

    @GetMapping("/{id}")
    public List<Episode> getSeason(@PathVariable Long id) {
        return rickAndMortyService.getEpisodesForSeasonBySeasonNumber(id);
    }
}
