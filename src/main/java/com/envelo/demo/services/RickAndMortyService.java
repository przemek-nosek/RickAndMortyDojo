package com.envelo.demo.services;

import com.envelo.demo.model.Character;
import com.envelo.demo.model.Episode;
import com.envelo.demo.model.SeasonEpisodeCount;
import com.envelo.demo.external.response.CharacterResponse;
import com.envelo.demo.external.response.Result;
import com.envelo.demo.external.response.RickAndMorty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RickAndMortyService {

    private final RestTemplate restTemplate;

    private final static String RICK_MORTY_API_URL = "https://rickandmortyapi.com/api/episode?page={page}";


    public List<SeasonEpisodeCount> getAllSeasons() {
        List<Result> results = getAllSeasonsFromApi();

        Map<String, String[]> countedEpisodesInSeason = countEpisodesInSeason(results);

        List<SeasonEpisodeCount> seasonEpisodeCounts = mapToSeasonEpisodeCount(countedEpisodesInSeason);

        return sortSeasons(seasonEpisodeCounts);
    }

    public List<Episode> getEpisodesForSeasonBySeasonNumber(Long seasonNumber) {
        List<Result> allSeasonsFromApi = getAllSeasonsFromApi();

        List<Episode> episodes = new ArrayList<>();
        List<Character> characters;

        for (Result result : allSeasonsFromApi) {
            if (result.getEpisode().startsWith("S0" + seasonNumber)) {
                StringBuilder url = prepareUrlWithCharacterIds(result);

                characters = getCharactersForEpisode(url);

                Episode episode = new Episode(result.getName(), result.getAirDate(), characters);
                episodes.add(episode);
            }
        }
        return episodes;
    }

    private List<SeasonEpisodeCount> sortSeasons(List<SeasonEpisodeCount> seasonEpisodeCounts) {
        return seasonEpisodeCounts.stream()
                .sorted(Comparator.comparing(SeasonEpisodeCount::getSeason))
                .collect(Collectors.toList());
    }

    private List<SeasonEpisodeCount> mapToSeasonEpisodeCount(Map<String, String[]> countedEpisodesInSeason) {
        List<SeasonEpisodeCount> seasonEpisodeCounts = new ArrayList<>();

        for (Map.Entry<String, String[]> stringEntry : countedEpisodesInSeason.entrySet()) {
            seasonEpisodeCounts.add(new SeasonEpisodeCount(
                    stringEntry.getKey(),
                    Integer.parseInt(stringEntry.getValue()[1])
            ));
        }
        return seasonEpisodeCounts;
    }

    private Map<String, String[]> countEpisodesInSeason(List<Result> results) {
        return results.stream()
                .map(result -> result.getEpisode().split("E"))
                .collect(Collectors.toMap(strings -> strings[0], Function.identity(),
                        (existing, replacement) -> replacement));
    }


    private List<Character> getCharactersForEpisode(StringBuilder url) {
        List<Character> characters = new ArrayList<>();

        CharacterResponse[] characterResponses = restTemplate.getForObject(url.toString(), CharacterResponse[].class);

        for (CharacterResponse characterResponse : Objects.requireNonNull(characterResponses)) {
            Character character = new Character(characterResponse.getId(), characterResponse.getName(), characterResponse.getImage());
            characters.add(character);
        }
        return characters;
    }

    private StringBuilder prepareUrlWithCharacterIds(Result result) {
        StringBuilder url = new StringBuilder("https://rickandmortyapi.com/api/character/");

        for (String character : result.getCharacters()) {
            String id = character.replaceFirst("https://rickandmortyapi.com/api/character/", "");
            url.append(id).append(",");
        }

        url.deleteCharAt(url.length() - 1);
        return url;
    }

    private List<Result> getAllSeasonsFromApi() {
        ResponseEntity<RickAndMorty> responseEntity = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, 1);

        RickAndMorty rickAndMorty = responseEntity.getBody();

        Integer pageCount = Objects.requireNonNull(rickAndMorty).getInfo().getPages();

        List<Result> results = rickAndMorty.getResults();

        for (int i = 2; i <= pageCount; i++) {
            String page = String.valueOf(i);
            ResponseEntity<RickAndMorty> responsePage = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, page);
            results.addAll(Objects.requireNonNull(responsePage.getBody()).getResults());
        }
        return results;
    }
}
