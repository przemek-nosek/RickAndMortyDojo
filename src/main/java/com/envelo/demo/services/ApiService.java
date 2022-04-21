package com.envelo.demo.services;

import com.envelo.demo.model.Result;
import com.envelo.demo.model.RickAndMorty;
import com.envelo.demo.model.dto.SeasonEpisodeCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final static String RICK_MORTY_API_URL = "https://rickandmortyapi.com/api/episode?page={id}";

    public List<SeasonEpisodeCountDto> getAllSeasons() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RickAndMorty> forEntity = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, 1);

        RickAndMorty body = forEntity.getBody();

        Integer pageCount = body.getInfo().getPages();

        List<SeasonEpisodeCountDto> seasonEpisodeCountDtos = new ArrayList<>();

        List<Result> results = forEntity.getBody().getResults();

        for (int i = 2; i <= pageCount; i++) {
            String id = String.valueOf(i);
            ResponseEntity<RickAndMorty> aaa = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, id);
            results.addAll(Objects.requireNonNull(aaa.getBody()).getResults());
        }

        Map<String, String[]> e = results.stream()
                .map(result -> result.getEpisode().split("E"))
                .collect(Collectors.toMap(strings -> strings[0], Function.identity(),
                        (existing, replacement) -> replacement));

        for (Map.Entry<String, String[]> stringEntry : e.entrySet()) {
            seasonEpisodeCountDtos.add(new SeasonEpisodeCountDto(
                    stringEntry.getKey(),
                    stringEntry.getValue()[1]
            ));
        }


        return seasonEpisodeCountDtos.stream()
                .sorted(Comparator.comparing(SeasonEpisodeCountDto::getSeason))
                .collect(Collectors.toList());
    }
}
