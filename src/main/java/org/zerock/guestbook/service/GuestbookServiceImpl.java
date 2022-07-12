package org.zerock.guestbook.service;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor // final, @NotNull이 붙은 필드의 생성자를 자동 생성
public class GuestbookServiceImpl implements GuestbookService {

  private final GuestbookRepository gbRepository; // final이 붙으면 무조건 초기화를 해줘야 한다  // proxy객체와의 순환참조를 막기 위해서 final

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
    Page<Guestbook> result = gbRepository.findAll(pageable);
    Function<Guestbook, GuestbookDTO> fn = new Function<Guestbook,GuestbookDTO>() { // Function<a,b> a를 받아서 b로 내보낸다
      @Override
      public GuestbookDTO apply(Guestbook entity) {
        return entityToDTO(entity); // entity값을 DTO로 보낸다
      }
    };
    return new PageResultDTO<>(result, fn);
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
