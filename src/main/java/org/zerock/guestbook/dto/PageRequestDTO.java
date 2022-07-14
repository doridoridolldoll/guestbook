package org.zerock.guestbook.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO { // which page, 어떤 페이지를 요청
  private int page;
  private int size;
  private String type;
  private String keyword;

  public PageRequestDTO() {
    page = 1;
    size = 10;
  }
  public Pageable getPageable(Sort sort) {
    return PageRequest.of(page -1, size, sort); // JPA에서는 시작이 0이라서 -1
  }
}
