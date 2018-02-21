package com.serli.oracle.of.bacon.loader.elasticsearch;

import com.serli.oracle.of.bacon.repository.ElasticSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletionLoader {
    private static AtomicInteger count = new AtomicInteger(0);
    private final static Stack<BulkRequest> myBulkrequest = new Stack<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient client = ElasticSearchRepository.createClient();

        if (args.length != 1) {
            System.err.println("Expecting 1 arguments, actual : " + args.length);
            System.err.println("Usage : completion-loader <actors file path>");
            System.exit(-1);
        }

        String inputFilePath = args[0];

        myBulkrequest.push(new BulkRequest());

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            bufferedReader
                    .lines()
                    .forEach(line -> {
                        Map<String, Object> jsonMap = new HashMap<>();
                        jsonMap.put("fullname", line);
                        myBulkrequest.peek().add(
                                new IndexRequest("imdb", "actor").source(jsonMap)
                        );
                    });
        }

        System.out.println("Inserted total of " + count.get() + " of " + initialNumberOfLine.get() + " actors");

        client.close();
    }
}
