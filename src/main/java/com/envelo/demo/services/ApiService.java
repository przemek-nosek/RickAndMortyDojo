package com.envelo.demo.services;

import com.envelo.demo.model.*;
import com.envelo.demo.model.Character;
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
        List<Result> results = getAllSeasonsFromApi();

        Map<String, String[]> e = results.stream()
                .map(result -> result.getEpisode().split("E"))
                .collect(Collectors.toMap(strings -> strings[0], Function.identity(),
                        (existing, replacement) -> replacement));


        List<SeasonEpisodeCountDto> seasonEpisodeCountDtos = new ArrayList<>();

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

    public List<Episode> getSeason(Long id) {
        List<Result> allSeasonsFromApi = getAllSeasonsFromApi();
        RestTemplate restTemplate = new RestTemplate();



        List<Episode> episodes = new ArrayList<>();
        List<Character> characters;
        for (Result result : allSeasonsFromApi) {
            if (result.getEpisode().startsWith("S0" + id)) {
                StringBuilder url = new StringBuilder("https://rickandmortyapi.com/api/character/");
                System.out.println(result.getEpisode());
                for (String character : result.getCharacters()) {
                    String s = character.replaceFirst("https://rickandmortyapi.com/api/character/", "");
                    url.append(s).append(",");
                }
                int length = url.length();
                url.deleteCharAt(length - 1);
                System.out.println(url);

                characters = new ArrayList<>();
                System.out.println("GOT");
                CharacterResponse[] forObject1 = restTemplate.getForObject(url.toString(), CharacterResponse[].class);
//                ResponseEntity<CharacterResponse> forEntity = restTemplate.getForEntity(url.toString(), CharacterResponse.class);
//                CharacterResponse forObject = restTemplate.getForObject(url.toString(), CharacterResponse.class);
//                System.out.println("NOT GOT");
                for (CharacterResponse characterResponse : forObject1) {
                    Character character = new Character(characterResponse.getId(), characterResponse.getName(), characterResponse.getImage());
                    characters.add(character);
                }
                Episode episode = new Episode(result.getName(), result.getAirDate(), characters);
                episodes.add(episode);
            }

        }
        return episodes;
    }

    private List<Result> getAllSeasonsFromApi() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RickAndMorty> forEntity = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, 1);

        RickAndMorty body = forEntity.getBody();

        Integer pageCount = body.getInfo().getPages();

        List<Result> results = forEntity.getBody().getResults();

        for (int i = 2; i <= pageCount; i++) {
            String id = String.valueOf(i);
            ResponseEntity<RickAndMorty> aaa = restTemplate.getForEntity(RICK_MORTY_API_URL, RickAndMorty.class, id);
            results.addAll(Objects.requireNonNull(aaa.getBody()).getResults());
        }
        return results;
    }


}
