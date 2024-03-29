package org.zerock.guestbook.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor // final, @NotNull이 붙은 필드의 생성자를 자동 생성
public class GuestbookServiceImpl implements GuestbookService {

  private final GuestbookRepository gbRepository; // final이 붙으면 무조건 초기화를 해줘야 한다  // proxy객체와의 순환참조를 막기 위해서 final

  @Override
  public GuestbookDTO read(Long gno) {
    Optional<Guestbook> result = gbRepository.findById(gno);
    return result.isPresent()? entityToDTO(result.get()) : null;
    // if(result.isPresent()) {
    //   GuestbookDTO dto = entityToDTO(result.get());
    //   return dto;
    // }
    // return null;
  }
  @Override
  public Long register(GuestbookDTO dto) {
    log.info("register dto"+dto);
    Guestbook entity = dtoToEntity(dto); // null
    gbRepository.save(entity); // DB에 entity를 저장한 순간 번호가 생김
    return entity.getGno();
  }

  @Override
  public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
    Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
    BooleanBuilder booleanBuilder = getSearch(requestDTO);
    Page<Guestbook> result = gbRepository.findAll(booleanBuilder, pageable);
    Function<Guestbook, GuestbookDTO> fn = entity -> entityToDTO(entity);
    return new PageResultDTO<>(result, fn);
  }
  @Override
  public void remove(Long gno) {
    log.info("remove......" + gno);
    gbRepository.deleteById(gno);
  }
  @Override
  public void modify(GuestbookDTO dto) {
    // dto를 먼저 들고오는 이유: Entity가 있어야 부분 변경이 가능.
    Optional<Guestbook> result = gbRepository.findById(dto.getGno());
    if(result.isPresent()) {
      Guestbook entity = result.get();
      entity.changeTitle(dto.getTitle());// 부분만 바꿈
      entity.changeContent(dto.getContent()); // 부분만 바꿈
      gbRepository.save(entity);
    }
  }
  private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
    String type = requestDTO.getType();
    String keyword = requestDTO.getKeyword();

    QGuestbook qGuestbook = QGuestbook.guestbook; // 관련된 테이블에 대한 쿼리 객체
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression expression = qGuestbook.gno.gt(0L);
    booleanBuilder.and(expression);
    if(type == null || type.trim().length() == 0) { // 검색 조건 無
      return booleanBuilder;
    }

    BooleanBuilder conditBuilder = new BooleanBuilder();
    if(type.contains("t")) conditBuilder.or(qGuestbook.title.contains(keyword));
    if(type.contains("c")) conditBuilder.or(qGuestbook.content.contains(keyword));
    if(type.contains("w")) conditBuilder.or(qGuestbook.writer.contains(keyword));
    booleanBuilder.and(conditBuilder);

    return booleanBuilder;
  }
  // @RequiredArgsConstructor이 생성한 생성자
  // 복수개면 자동으로 @Autowired
  // public GuestbookServiceImpl(GuestbookRepository gbRepository)
  // {
  //   this.gbRepository = gbRepository;
  // }
}
// 클라이언트로부터 요청받은 DTO를 테이블로 만들기 위해서
// serviceImpl(구현체)에서 service interface를 implements받고 repository에 실어서 DB에 저장
