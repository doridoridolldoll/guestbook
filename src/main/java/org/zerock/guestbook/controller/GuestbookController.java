package org.zerock.guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;
import org.zerock.guestbook.dto.GuestbookDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("guestbook")
@Log4j2
@AllArgsConstructor // GuestbookService를 초기화시켜줌 생성자 역할
public class GuestbookController {
  private final GuestbookService gbService;

  @GetMapping({ "/" })
  public String index() {
    log.info("index...........");
    return "redirect:/guestbook/list";
  }

  @GetMapping({ "/list" })
  public void list(PageRequestDTO requestDTO, Model model) { // model은 결과 데이터를 화면에 전달하기 위해서 사용
    log.info("list..........." + requestDTO);
    model.addAttribute("result", gbService.getList(requestDTO));
  }

  @GetMapping("/register")
  public void register() {
    log.info("register get...");
  }

  @PostMapping("/register")
  public String registerPost(GuestbookDTO dto, RedirectAttributes ra) {
    log.info("dto..." + dto);
    Long gno = gbService.register(dto);
    ra.addFlashAttribute("msg", gno + " 등록");
    return "redirect:/guestbook/list"; // redirect로 주소로 보냄 없으면 resource로
  }

  @GetMapping({ "/read", "/modify" })
  public void read(long gno, Model model,
      @ModelAttribute("requestDTO") PageRequestDTO requestDTO) { // 커맨드객체로 받은 것은 다음 페이지에도 넘어감
    // PageRequestDTO = 커맨드객체, @ModelAttribute("") A a = 커맨드객체(A a)를 "b"로 쓰게 해줌
    log.info("read ....... gno: " + gno);
    GuestbookDTO dto = gbService.read(gno);
    model.addAttribute("dto", dto);
  }

  @PostMapping("/modify")
  public String modifyPost(GuestbookDTO dto,
      PageRequestDTO requestDTO, RedirectAttributes ra) {
    log.info("modifyPost..." + dto);
    gbService.modify(dto);
    ra.addFlashAttribute("msg", dto.getGno()+" 수정");
    ra.addAttribute("gno", dto.getGno());
    ra.addAttribute("page", requestDTO.getPage());
    ra.addAttribute("type", requestDTO.getType());
    ra.addAttribute("keyword", requestDTO.getKeyword());
    return "redirect:/guestbook/read";
  }

  @PostMapping("/remove")
  public String remove(long gno, RedirectAttributes ra, PageRequestDTO requestDTO) {
    log.info("remove... " + gno);
    gbService.remove(gno);
    ra.addFlashAttribute("msg", gno+ " 삭제");
    ra.addAttribute("page", requestDTO.getPage());
    ra.addAttribute("type", requestDTO.getType());
    ra.addAttribute("keyword", requestDTO.getKeyword());
    return "redirect:/guestbook/list";
  }
}
