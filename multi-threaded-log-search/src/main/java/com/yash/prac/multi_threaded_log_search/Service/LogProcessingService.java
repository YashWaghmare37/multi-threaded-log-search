package com.yash.prac.multi_threaded_log_search.Service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;


public interface LogProcessingService {

    List<String> searchLinesMultithreaded(File file, String keyword) throws Exception;

    }
