package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {
  @Autowired
  private GuestbookService service;

  @Test
  void testRegister() {
    GuestbookDTO dto = GuestbookDTO.builder().title("Sample Title...")
        .content("Sample Content...").writer("user0").build();
    System.out.println("gno ::: " + service.register(dto));
  }

  @Test
  public void testList() {
    PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();
    PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(requestDTO);
    resultDTOPrint(resultDTO);
  }

  @Test
  public void testSearch() {
    PageRequestDTO requestDTO = PageRequestDTO.builder()
        .page(1).size(10).type("c").keyword("1").build();
    PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(requestDTO);
    resultDTOPrint(resultDTO);
  }

  public void resultDTOPrint(PageResultDTO<GuestbookDTO, Guestbook> resultDTO) {
    System.out.println("PREV: " + resultDTO.isPrev());
    System.out.println("NEXT: " + resultDTO.isNext());
    System.out.println("TOTAL: " + resultDTO.getTotalPage());
    System.out.println("---------------------------------");
    for (GuestbookDTO dto : resultDTO.getDtoList()) {
      System.out.println(dto);
    }
    System.out.println("=================================");
    resultDTO.getPageList().forEach(i -> System.out.println(i));
  }
}
