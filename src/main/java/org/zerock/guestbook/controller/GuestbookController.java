package org.zerock.guestbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping({"/"})
  public String index(){
    log.info("index...........");
    return "redirect:/guestbook/list";
  }
  @GetMapping({"/list"})
  public void list(PageRequestDTO requestDTO, Model model){ // model은 결과 데이터를 화면에 전달하기 위해서 사용
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

    // 새로 추가한 엔티티의 번호
    Long gno = gbService.register(dto);
    ra.addFlashAttribute("msg",gno);
    
    return "redirect:/guestbook/list";
  }
}
