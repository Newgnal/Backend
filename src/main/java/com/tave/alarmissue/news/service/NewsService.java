package com.tave.alarmissue.news.service;

import com.tave.alarmissue.news.converter.NewsConverter;
import com.tave.alarmissue.news.domain.News;
import com.tave.alarmissue.news.domain.enums.Thema;
import com.tave.alarmissue.news.dto.request.NewsSortType;
import com.tave.alarmissue.news.dto.response.NewsDetailResponseDto;
import com.tave.alarmissue.news.dto.response.NewsResponseDto;
import com.tave.alarmissue.news.dto.response.SliceResponseDto;
import com.tave.alarmissue.news.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service      //Spring이 관리하는 Bean으로 등록됨
@Transactional(readOnly = true)   //읽기 전용. 성능향상 목적
@RequiredArgsConstructor        //final로 선언된 필드들을 자동으로 생성자에 포함시켜 주입
public class NewsService {

//    @Autowired       //RequiredArgsConstructor썼으므로 객체 주입할 필요 없음

    private final NewsRepository newsRepository;
    private final NewsConverter newsConverter;

//    public SliceResponseDto<NewsResponseDto> getAllNewsLatest(Pageable pageable){
//        Slice<News> newsSlice=newsRepository.findAllByOrderByDateDesc(pageable);
//        List<NewsResponseDto> content=newsSlice.getContent()
//                .stream().map(newsConverter::toDto)
//                .collect(Collectors.toList());
//
//        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
//    }
//
//    public SliceResponseDto<NewsResponseDto> getAllNewsViewest(Pageable pageable) {
//        Slice<News> newsSlice=newsRepository.findAllByOrderByViewDesc(pageable);
//        List<NewsResponseDto> content=newsSlice.getContent()
//                .stream().map(newsConverter::toDto)
//                .collect(Collectors.toList());
//
//        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
//    }

//    public SliceResponseDto<NewsResponseDto> getThemaNewsLatest(Thema thema,Pageable pageable) {
//        Slice<News> newsSlice=newsRepository.findByThemaOrderByDateDesc(thema, pageable);
//        List<NewsResponseDto> content=newsSlice.getContent()
//                .stream().map(newsConverter::toDto)
//                .collect(Collectors.toList());
//
//        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
//    }
//
//    public SliceResponseDto<NewsResponseDto> getThemaNewsViewst(Thema thema,Pageable pageable) {
//        Slice<News> newsSlice=newsRepository.findByThemaOrderByViewDesc(thema, pageable);
//        List<NewsResponseDto> content=newsSlice.getContent()
//                .stream().map(newsConverter::toDto)
//                .collect(Collectors.toList());
//
//        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
//    }

    public SliceResponseDto<NewsResponseDto> getAllNews(NewsSortType sortType,Pageable pageable){
//        Slice<News> newsSlice=newsRepository.findAll(sortType,pageable);
        Slice<News> newsSlice=switch(sortType){
            case LATEST -> newsRepository.findAllByOrderByDateDesc(pageable);
            case VIEWEST -> newsRepository.findAllByOrderByViewDesc(pageable);
        };
        List<NewsResponseDto> content=newsSlice.getContent()
                .stream().map(newsConverter::toDto)
                .collect(Collectors.toList());

        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
    }

    public SliceResponseDto<NewsResponseDto> getAllThemaNews(NewsSortType sortType,Thema thema,Pageable pageable) {
        Slice<News> newsSlice=switch(sortType){
            case LATEST -> newsRepository.findByThemaOrderByDateDesc(thema,pageable);
            case VIEWEST -> newsRepository.findByThemaOrderByViewDesc(thema,pageable);
        };
        List<NewsResponseDto> content=newsSlice.getContent()
                .stream().map(newsConverter::toDto)
                .collect(Collectors.toList());

        return new SliceResponseDto<>(content, newsSlice.hasNext(),newsSlice.getNumber());
    }

    public NewsDetailResponseDto getDetailNews(Long newsId) {
        News news=newsRepository.findById(newsId).orElseThrow(()->new EntityNotFoundException("뉴스를 찾을 수 없습니다. ID: "+newsId));
        news.incrementView();
        return newsConverter.toDetailDto(news);

    }

}
