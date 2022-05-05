
package com.envelo.demo.external.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "air_date",
    "episode",
    "characters",
    "url",
    "created"
})
@Generated("jsonschema2pojo")
@ToString
public class Result {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("air_date")
    private String airDate;
    @JsonProperty("episode")
    private String episode;
    @JsonProperty("characters")
    private List<String> characters = null;
    @JsonProperty("url")
    private String url;
    @JsonProperty("created")
    private String created;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("air_date")
    public String getAirDate() {
        return airDate;
    }

    @JsonProperty("air_date")
    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    @JsonProperty("episode")
    public String getEpisode() {
        return episode;
    }

    @JsonProperty("episode")
    public void setEpisode(String episode) {
        this.episode = episode;
    }

    @JsonProperty("characters")
    public List<String> getCharacters() {
        return characters;
    }

    @JsonProperty("characters")
    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
