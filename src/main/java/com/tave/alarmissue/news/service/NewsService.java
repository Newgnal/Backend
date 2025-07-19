package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.dto.request.enums.NewsSortType;
import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.news.dto.response.SliceResponseDto;
import com.tave.alarmissue.news.exceptions.NewsException;
import com.tave.alarmissue.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.tave.alarmissue.news.exceptions.NewsErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

@Service      //Spring이 관리하는 Bean으로 등록됨
@Transactional(readOnly = true)   //읽기 전용. 성능향상 목적
@RequiredArgsConstructor        //final로 선언된 필드들을 자동으로 생성자에 포함시켜 주입
public class NewsService {


    private final NewsRepository newsRepository;
    private final NewsConverter newsConverter;

    public SliceResponseDto<NewsResponseDto> getAllNews(NewsSortType sortType,Pageable pageable){

        Slice<News> newsSlice=switch(sortType){
            case LATEST -> newsRepository.findAllByOrderByDateDesc(pageable);
            case POPULAR -> newsRepository.findAllByOrderByViewDescDateDesc(pageable);
        };
        List<NewsResponseDto> content=newsSlice.getContent()
                .stream().map(newsConverter::toDto)
                .collect(Collectors.toList());

        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
    }

    @Transactional
    public SliceResponseDto<NewsResponseDto> getAllThemaNews(NewsSortType sortType,Thema thema,Pageable pageable) {
        Slice<News> newsSlice=switch(sortType){
            case LATEST -> newsRepository.findByThemaOrderByDateDesc(thema,pageable);
            case POPULAR -> newsRepository.findByThemaOrderByViewDescDateDesc(thema,pageable);
        };
        List<NewsResponseDto> content=newsSlice.getContent()
                .stream().map(newsConverter::toDto)
                .collect(Collectors.toList());

        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
    }

    @Transactional
    public NewsDetailResponseDto getDetailNews(Long newsId) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsException(NEWS_ID_NOT_FOUND, "해당 뉴스를 찾을 수 없습니다."));

        news.incrementView();
        return newsConverter.toDetailDto(news);

    }

}
