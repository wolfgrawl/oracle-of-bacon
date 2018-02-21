package com.serli.oracle.of.bacon.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.action.search.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ElasticSearchRepository {

    private final RestHighLevelClient client;

    public ElasticSearchRepository() {
        client = createClient();

    }

    public static RestHighLevelClient createClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                )
        );
    }

    public List<String> getActorsSuggests(String searchQuery) throws IOException {

        List<String> results = new ArrayList<>();

        SearchRequest mySearchRequest = new SearchRequest("imdb").types("actors").searchType(
                SearchType.DFS_QUERY_THEN_FETCH).source(new SearchSourceBuilder().size(5).query(new TermQueryBuilder("name", searchQuery))
                );

        SearchResponse mySearchResponse = client.search(mySearchRequest);
        for (SearchHit searchHit : response.getHits().getHits())
            result.add(searchHit.getId());

        return results;
    }
}
