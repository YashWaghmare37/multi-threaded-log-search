package com.yash.prac.multi_threaded_log_search.Controller;
import com.yash.prac.multi_threaded_log_search.Service.LogProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class LogSearchController {
    @Autowired
    LogProcessingService logProcessingService;

    private static final String LOG_FILE_PATH = "logs/acktLog";

    @GetMapping
    public List<String> searchLogs(@RequestParam String keyword) throws Exception {
        URL resource = getClass().getClassLoader().getResource(LOG_FILE_PATH);
        if (resource == null) {
            throw new FileNotFoundException("Resource not found: "+LOG_FILE_PATH);
        }
        File file = new File(resource.toURI());

        return logProcessingService.searchLinesMultithreaded(file, keyword);
    }


}
