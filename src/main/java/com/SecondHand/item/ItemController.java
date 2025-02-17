package com.SecondHand.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    // 목록
    @GetMapping("/list")
    String categoryList(Model m,
                        @RequestParam(required = false, defaultValue = "") String category,
                        @RequestParam(defaultValue = "1") Integer page){
        Page<Item> list;

        if(category.isEmpty()){ // 정해진 카테고리가 없으면 전체 목록을 반환
            list = itemRepository.findAll(PageRequest.of(page -1, 3, Sort.by(Sort.Direction.DESC, "id")));
        }else{  // 정해진 카테고리가 있다면
            list = itemRepository.findPageByCategory(category, PageRequest.of(page -1, 3, Sort.by(Sort.Direction.DESC, "id")));
        }
        m.addAttribute("items", list.getContent());
        m.addAttribute("hasPrevious", list.hasPrevious());
        m.addAttribute("hasNext", list.hasNext());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPage", list.getTotalPages());

        return "list.html";
    }

    // 검색
    @GetMapping("/list/{searchText}")
    String searchList(Model m,
                      @PathVariable String searchText,
                      @RequestParam(defaultValue = "1") Integer page){

        System.out.println(searchText);

        Page<Item> list = itemRepository.findPageByTitleContains(searchText, PageRequest.of(page -1, 3, Sort.by(Sort.Direction.DESC, "id")));

        if(list.isEmpty())
            m.addAttribute("warningMessage", "검색 결과가 없습니다.");

        m.addAttribute("searchText", searchText);
        m.addAttribute("items", list.getContent());
        m.addAttribute("hasPrevious", list.hasPrevious());
        m.addAttribute("hasNext", list.hasNext());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPage", list.getTotalPages());

        return "search.html";
    }
}
