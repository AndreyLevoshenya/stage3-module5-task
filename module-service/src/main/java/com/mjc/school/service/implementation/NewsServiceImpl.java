package com.mjc.school.service.implementation;

import com.mjc.school.repository.AuthorRepository;
import com.mjc.school.repository.NewsRepository;
import com.mjc.school.repository.TagRepository;
import com.mjc.school.repository.filter.Page;
import com.mjc.school.repository.filter.Pagination;
import com.mjc.school.repository.filter.SearchCriteria;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.SearchParameters;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.NewsService;
import com.mjc.school.service.annotations.Valid;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.NewsDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mjc.school.service.exceptions.ExceptionErrorCodes.*;
import static com.mjc.school.service.utils.Utils.getPagination;
import static com.mjc.school.service.utils.Utils.getSearchCriteria;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final NewsDtoMapper newsDtoMapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, AuthorRepository authorRepository, TagRepository tagRepository, NewsDtoMapper newsDtoMapper) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.newsDtoMapper = newsDtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PageDtoResponse<NewsDtoResponse> readAll(@Valid SearchingRequest searchingRequest) {
        Pagination pagination = getPagination(searchingRequest);
        SearchCriteria searchCriteria = getSearchCriteria(searchingRequest);

        Page<News> page = newsRepository.readAll(pagination, searchCriteria);
        return new PageDtoResponse<>(newsDtoMapper.modelListToDtoList(page.getEntities()), page.getPageNumber(), page.getPagesCount());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDtoResponse readById(@Valid Long id) {
        if (newsRepository.existById(id)) {
            News news = newsRepository.readById(id).get();
            return newsDtoMapper.modelToDto(news);
        } else {
            throw new NotFoundException(NEWS_DOES_NOT_EXIST.getErrorCode(), String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Override
    @Transactional
    public NewsDtoResponse create(@Valid NewsDtoRequest createRequest) {
        if (!authorRepository.existById(createRequest.getAuthorId())) {
            throw new NotFoundException(NEWS_DOES_NOT_EXIST.getErrorCode(), String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), createRequest.getAuthorId()));
        }
        News model = newsDtoMapper.dtoToModel(createRequest, newsRepository, authorRepository, tagRepository);

        return newsDtoMapper.modelToDto(newsRepository.create(model));
    }

    @Override
    @Transactional
    public NewsDtoResponse update(@Valid NewsDtoRequest updateRequest) {
        if (!newsRepository.existById(updateRequest.getId())) {
            throw new NotFoundException(NEWS_DOES_NOT_EXIST.getErrorCode(), String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), updateRequest.getId()));
        }
        if (!authorRepository.existById(updateRequest.getAuthorId())) {
            throw new NotFoundException(AUTHOR_DOES_NOT_EXIST.getErrorCode(), String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), updateRequest.getAuthorId()));
        }

        for (Long id : updateRequest.getTagIds()) {
            if (!tagRepository.existById(id)) {
                throw new NotFoundException(TAG_DOES_NOT_EXIST.getErrorCode(), String.format(TAG_DOES_NOT_EXIST.getErrorMessage(), id));
            }
        }

        News news = newsDtoMapper.dtoToModel(updateRequest, newsRepository, authorRepository, tagRepository);
        return newsDtoMapper.modelToDto(newsRepository.update(news));
    }

    @Override
    @Transactional
    public NewsDtoResponse patch(NewsDtoRequest patchRequest) {
        Long id;
        String title;
        String content;
        Long authorId;
        List<Long> tagIds = new ArrayList<>();
        if (patchRequest.getId() != null && newsRepository.existById(patchRequest.getId())) {
            id = patchRequest.getId();
        } else {
            throw new NotFoundException(NEWS_DOES_NOT_EXIST.getErrorCode(), String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), patchRequest.getId()));
        }
        News prevNews = newsRepository.readById(id).get();
        title = patchRequest.getTitle() != null ? patchRequest.getTitle() : prevNews.getTitle();
        content = patchRequest.getContent() != null ? patchRequest.getContent() : prevNews.getContent();
        if (patchRequest.getAuthorId() != null) {
            if (authorRepository.existById(patchRequest.getAuthorId())) {
                authorId = patchRequest.getAuthorId();
            } else {
                throw new NotFoundException(AUTHOR_DOES_NOT_EXIST.getErrorCode(), String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), patchRequest.getId()));
            }
        } else {
            authorId = prevNews.getAuthor().getId();
        }

        if (patchRequest.getTagIds() != null) {
            for (Long tagId : patchRequest.getTagIds()) {
                if (!tagRepository.existById(tagId)) {
                    throw new NotFoundException(TAG_DOES_NOT_EXIST.getErrorCode(), String.format(TAG_DOES_NOT_EXIST.getErrorMessage(), tagId));
                }
                tagIds.addAll(patchRequest.getTagIds());
            }
        } else {
            tagIds.addAll(prevNews.getTags().stream().map(Tag::getId).toList());
        }

        NewsDtoRequest updateRequest = new NewsDtoRequest(id, title, content, authorId, tagIds);

        return update(updateRequest);
    }

    @Override
    @Transactional
    public boolean deleteById(@Valid Long id) {
        if (newsRepository.existById(id)) {
            return newsRepository.deleteById(id);
        } else {
            throw new NotFoundException(NEWS_DOES_NOT_EXIST.getErrorCode(), String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDtoResponse> readByParams(ParametersDtoRequest parametersDtoRequest) {
        SearchParameters params = new SearchParameters(
                !parametersDtoRequest.newsTitle().isEmpty() ? parametersDtoRequest.newsTitle() : null,
                !parametersDtoRequest.newsContent().isEmpty() ? parametersDtoRequest.newsContent() : null,
                !parametersDtoRequest.authorName().isEmpty() ? parametersDtoRequest.authorName() : null,
                (parametersDtoRequest.tagIds() != null && !parametersDtoRequest.tagIds().isEmpty()) ? parametersDtoRequest.tagIds() : null,
                (parametersDtoRequest.tagNames() != null && !parametersDtoRequest.tagNames().isEmpty()) ? parametersDtoRequest.tagNames() : null);
        return newsDtoMapper.modelListToDtoList(newsRepository.readByParams(params));
    }
}
