package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.time.Instant;

@Mapper
public interface PageResponseMapper{
  default <T> PageResponse<T> fromPage(Page<T> page, Instant nextCursor){
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
