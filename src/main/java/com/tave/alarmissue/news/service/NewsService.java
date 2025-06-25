package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.repository.NewsMapping;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    @Autowired
    private final NewsRepository newsRepository;

    public List<NewsMapping> getThemaNews(Thema thema) {
        return newsRepository.findByThema(thema);
    }

    public List<NewsMapping> getAllNews() {
        return newsRepository.findAllByOrderByDateDesc();
    }


//    public Page<News> getAllNewsLatest(Pageable pageable) {
//        return newsRepository.findAllByOrderByDateDesc(pageable);
//    }

//    public Page<News> getNewsList(String sort, Pageable pageable) {
//        return switch (sort) {
//            case "views" -> newsRepository.findAllByOrderByViewDesc(pageable);
//            default -> newsRepository.findAllByOrderByDateDesc(pageable);
//        };
//    }

//    public News getNewsById(Long id) {
//        return newsRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("뉴스가 존재하지 않습니다. id=" + id));
//    }

}
