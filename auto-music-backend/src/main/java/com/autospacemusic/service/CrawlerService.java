package com.autospacemusic.service;

import java.util.List;

public interface CrawlerService {
    List<String> crawlMusicFromSource(String sourceUrl);
    void scheduleCrawlingJob(String cronExpression);
    void dailyCrawlingJob();
}