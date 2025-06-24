package com.tave.alarmissue.news.controller;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.repository.NewsRepository;
import com.tave.alarmissue.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/home")
    public List<News>index(){
        return newsService.index();
    }

}
