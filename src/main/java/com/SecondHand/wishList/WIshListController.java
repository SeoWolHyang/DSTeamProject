package com.SecondHand.wishList;

import com.SecondHand.member.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WIshListController {

    private final WishListRepository wishListRepository;
    private final WIshListService wIshListService;
    private final UserRepository userRepository;

    // 찜하기
    /*
    <form action="/wishlist/add" method="post">
        <input name="itemId" th:value="${item.id}" style="display:none">
        <input name="itemTitle" th:value="${item.title}" style="display:none">
        <input name="itemImgURL" th:value="${item.imgURL}" style="display:none">
        <input name="itemPrice" th:value="${item.price}" style="display:none">
        <input name="itemUploadedDate" th:value="${item.uploadDate}" style="display:none">
        <button type="submit">찜하기</button>
    </form>
     */

    @PostMapping("/add")
    String addList(Long itemId,
                   String itemTitle,
                   String itemImgURL,
                   Integer itemPrice,
                   LocalDateTime itemUploadedDate,
                   Authentication auth){
        if(auth == null || !auth.isAuthenticated()){
            return "login.html";
        }
        CustomUser user = (CustomUser)auth.getPrincipal();
        var userdata = userRepository.findByUsername(user.getUsername());
        Optional<WishList> existingItem = wishListRepository.findByItemIdAndUser(itemId, userdata.get());

        if(existingItem.isPresent()) {
            wishListRepository.delete(existingItem.get());
            return "redirect:/detail/" + itemId;
        }else{
            wIshListService.saveWishList(itemId, itemTitle, itemImgURL, itemPrice, itemUploadedDate, auth);
            return "redirect:/detail/" + itemId;
        }
    }

    // 찜 목록 불어오기
    @GetMapping("/list")
    String showList(Model m, Authentication auth){
        CustomUser user = (CustomUser)auth.getPrincipal();
        var userdata = userRepository.findByUsername(user.getUsername());

        List<WishList> list = wishListRepository.findByUser(userdata.get());
        m.addAttribute("wishlist", list);

        return "wishlist.html";
    }

    // 찜 목록 삭제하기

    /*
        <div th:each="wl : ${wishlist}">
            <h4 th:text="${wl.itemTitle}"></h4>
            <span th:onclick="fetch('/wishlist/delete/[[${wl.id}]]', {method: 'DELETE'})  // id를 경로 변수로 사용
                        .then(r => r.text())
                        .then(() => {
                            location.reload();
                        })">
            Delete
            </span>
        </div>
    */

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteFromList(@PathVariable Long id){
        wishListRepository.deleteById(id);

        return ResponseEntity.status(200).body("삭제완료");
    }
}
