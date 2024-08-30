package com.example.ArtAuction_24.domain.member.controller;


import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.bid.service.BidService;
import com.example.ArtAuction_24.domain.deliver.entity.Delivery;
import com.example.ArtAuction_24.domain.deliver.service.DeliveryService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.form.MemberAddressForm;
import com.example.ArtAuction_24.domain.member.form.MemberForm;
import com.example.ArtAuction_24.domain.member.form.MemberModifyForm;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.order.entity.Order;
import com.example.ArtAuction_24.domain.order.service.OrderService;
import com.example.ArtAuction_24.domain.product.entity.LikeProduct;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final ProductService productService;
    private final OrderService orderService;
    private final BidService bidService;
    private final DeliveryService deliveryService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage(Model model) {
        // 'message' 속성에 접근
        if (model.containsAttribute("message")) {
            String message = (String) model.getAttribute("message");
            System.out.println("전달된 메시지: " + message);

            model.addAttribute("findAccountMessage", "계정 찾기 메시지 : " + message);
        }

        // 'message' 속성에 접근
        if (model.containsAttribute("error")) {
            String error = (String) model.getAttribute("error");
            System.out.println("전달된 메시지: " + error);

            model.addAttribute("findAccountMessage", "계정 찾기 메시지 : " + error);
        }

        return "member/login";
    }

    @PostMapping("/member/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        boolean isAuthenticated  = memberService.authenticate(username, password);

        if (!isAuthenticated ) {
            model.addAttribute("loginMatchError", "아이디 또는 비밀번호가 존재하지 않습니다.");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = memberService.checkUsernameExists(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberService.checkNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(
            @ModelAttribute @Valid MemberForm memberForm,
            BindingResult bindingResult) {

        // 이메일 및 주소 필드 결합
        String email = memberForm.getLogemailT() + "@" + memberForm.getLogemail();

        System.out.println("이메일 도메인 전송 확인 : " + memberForm.getLogemail() );

        memberForm.setEmail(email);


        String address = memberForm.getAddress1() + ", " + memberForm.getAddress2() + memberForm.getAddress3();
        memberForm.setAddress(address);


        System.out.println("============== test 2 ==============");
        System.out.println(email);
        System.out.println(address);

        System.out.println(memberForm.getUsername());
        System.out.println(memberForm.getNickname());
        System.out.println(memberForm.getPassword());
        System.out.println(memberForm.getPhoneNumber());
        System.out.println(memberForm.getProviderTypeCode());
        System.out.println(memberForm.getAddress());
        System.out.println(memberForm.getEmail());
        System.out.println("============== test 2 ==============");


        // BindingResult 에러 출력
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });
            return "member/login";
        }



        // 회원가입 서비스 호출
        try {

            Member member = this.memberService.join(
                    memberForm.getProviderTypeCode(),
                    memberForm.getUsername(),
                    memberForm.getPassword(),
                    memberForm.getEmail(),
                    memberForm.getNickname(),
                    memberForm.getPhoneNumber(),
                    memberForm.getAddress()
            );

            if (member == null) {
                System.out.println("DB에 회원 정보가 저장되지 않았습니다.");
                return "member/login";
            }

            // 회원가입 성공 시 이메일 발송
            String bodyText = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Art Auction에 가입해주신 것을 진심으로 환영합니다! 저희는 미술 작품을 온라인 경매를 통해 작품을 공유하고 소장할 수 있는 공간을 만들고자 합니다.<br><br>" +
                            "Art Auction에서는 작가가 손수 작업한 예술품이 많은 이들과 함께 소중한 경험, 건강한 힐링이 될 것입니다.<br><br>" +
                            "저희는 Art Auction을 이용해주시는 고객님들께 아래 정보를 제공합니다.<br><br>" +
                            "* 다양한 미술 작품과 작가들의 최신 경매 소식을 먼저 접하실 수 있습니다.<br><br>" +
                            "* 원하는 경매에 대한 알림과 진행중이거나 예정된 경매를 확인하실 수 있습니다.<br><br>" +
                            "* 고객님의 만족을 위한 안전한 경매, 확실한 작품 이전을 하기 위해 노력합니다.<br><br>" +
                            "* 또한, 투명한 경매 과정과 전문적인 지원을 통해 안심하고 거래하실 수 있도록 도와드립니다.<br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 [내 프로필] -> [1:1 문의] 를 통해 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "감사합니다, Art Auction 드림.",
                    memberForm.getUsername()
            );

            emailService.send(memberForm.getEmail(), "Art Auction 가입을 환영합니다!", bodyText);

        } catch (Exception e) {
            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            return "member/login";
        }

        return "redirect:/member/login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("member", member);
        String username = member.getUsername();

        MemberModifyForm memberModifyForm = new MemberModifyForm();
        model.addAttribute("memberModifyForm", memberModifyForm);
        memberModifyForm.setUsername(username); // 로그인된 사용자 아이디 못바꾸게할려고 폼에집어넣음

        Set<Long> likedProductIds = productService.getLikedProductIdsByMember(member.getId());
        model.addAttribute("likedProductIds", likedProductIds);


        List<LikeProduct> likeProductList = productService.getLikeProduct(member);
        Collections.reverse(likeProductList);
        model.addAttribute("likeProductList", likeProductList);

        List<Question> questionList = questionService.findByMember(member);
        Collections.reverse(questionList);
        model.addAttribute("questionList", questionList);

        // 주문 목록 추가
        List<Order> orderList = orderService.getOrdersByMemberId(member.getId());
        Collections.reverse(orderList);
        model.addAttribute("orderList", orderList);

        // 입찰 목록 추가 멤버값이랑 일치한 bid 를 리스트로가져옴
        List<Bid> bidListByMember = bidService.getBidsByMemberId(member.getId());
        Collections.reverse(bidListByMember);
        model.addAttribute("bidListByMember", bidListByMember);

        // 각 상품의 최고 입찰가를 가져오기
        Map<Product, Bid> highestBidsByProduct = bidService.findHighestBidsByProduct();
        model.addAttribute("highestBidsByProduct", highestBidsByProduct);

        // 주문별 배송 상태 추가
        Map<Long, Delivery> deliveryMap = new HashMap<>();
        for (Order order : orderList) {
            Delivery delivery = deliveryService.getDeliveryByOrder(order);
            if (delivery != null) {
                deliveryMap.put(order.getId(), delivery);
            }
        }
        model.addAttribute("deliveryMap", deliveryMap);


        return "member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String updateMember(@Valid MemberModifyForm memberModifyForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {


        memberService.updateMember(memberModifyForm, principal.getName());
        return "redirect:/member/logout"; // 업데이트하고나서 로그아웃

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addressUpdate")
    public String updateMemberAddress(@Valid MemberAddressForm memberAddressForm, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {

        String address = memberAddressForm.getAddress1() + ", " + memberAddressForm.getAddress2() + memberAddressForm.getAddress3();
        memberAddressForm.setAddress(address);


        memberService.updateMemberAddress(memberAddressForm, principal.getName());
        return "redirect:/member/logout"; // 업데이트하고나서 로그아웃

    }

    @PostMapping("/delete")
    public String deleteMember(Principal principal) {
        Member member = memberService.getMember(principal.getName());
        memberService.delete(member);
        return "redirect:/member/logout"; // 메인 페이지로 리다이렉트 }
    }



    @GetMapping("/applicantArtist")
    public String applicantArtist() {

        return "member/applicantArtist";
    }

    @GetMapping("/orderDetail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);

        // 제품 정보 가져오기
        Product product = order.getProduct();
        model.addAttribute("product", product);

        // 가격을 통화 형식으로 변환
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("ko", "KR"));
        String formattedPrice = currencyFormat.format(order.getProductPrice());

        // 모델에 가격 정보 추가
        model.addAttribute("formattedPrice", formattedPrice);

        // 주문 상태의 한글 설명을 모델에 추가
        model.addAttribute("orderStatusDescription", order.getStatus().getDescription());


        return "member/orderDetail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/deliverDetail/{id}")
    public String viewDeliveryStatus(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        Delivery delivery = deliveryService.getDeliveryByOrder(order);

        LocalDateTime adjustedDate = delivery.getStartDate().plusDays(1).plusHours(5);
        LocalDateTime adjustedDate2 = delivery.getStartDate().plusDays(2).plusHours(5);
        LocalDateTime adjustedDate3 = delivery.getStartDate().plusDays(3).plusHours(5);
        LocalDateTime adjustedDate4 = delivery.getStartDate().plusDays(5).plusHours(5);

        model.addAttribute("order", order);
        model.addAttribute("delivery", delivery);
        model.addAttribute("adjustedDate", adjustedDate);
        model.addAttribute("adjustedDate2", adjustedDate2);
        model.addAttribute("adjustedDate3", adjustedDate3);
        model.addAttribute("adjustedDate4", adjustedDate4);

        return "member/deliverDetail";
    }
}

