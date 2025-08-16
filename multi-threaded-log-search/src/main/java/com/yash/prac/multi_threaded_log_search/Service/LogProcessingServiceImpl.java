package com.yash.prac.multi_threaded_log_search.Service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class LogProcessingServiceImpl implements LogProcessingService {
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int BATCH_SIZE = 5;

    @Override
    public List<String> searchLinesMultithreaded(File file, String keyword) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<List<String>>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> chunk = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() >= BATCH_SIZE) {
                    futures.add(searchChunkAsync(new ArrayList<>(chunk), keyword, executor));
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                futures.add(searchChunkAsync(new ArrayList<>(chunk), keyword, executor));
            }
        }

        List<String> results = new ArrayList<>();
        for (Future<List<String>> future : futures) {
            results.addAll(future.get());
        }

        executor.shutdown();
        return results;
    }

    private Future<List<String>> searchChunkAsync(List<String> chunk, String keyword, ExecutorService executor) {
        return executor.submit(() ->
                chunk.stream()
                        .filter(line -> line.contains(keyword)) // can enhance to be case-insensitive or regex
                        .collect(Collectors.toList())
        );
    }

}
