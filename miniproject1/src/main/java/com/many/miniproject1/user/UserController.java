package com.many.miniproject1.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final HttpSession session;

    // 회사 회원가입
    @GetMapping("/company/joinForm")
    public String companyJoinForm() {
        return "company/joinForm";
    }

    @PostMapping("/company/join")
    public String companyJoin(UserRequest.JoinDTO requestDTO) {
        System.out.println(requestDTO);
        userRepository.companySave(requestDTO);
        return "redirect:/company/loginForm";
    }

    // 회사 로그인
    @GetMapping("/company/loginForm")
    public String companyLoginForm() {
        return "company/loginForm";
    }

    @PostMapping("/company/login")
    public String companyLogin(UserRequest.LoginDTO requestDTO) {
        System.out.println(requestDTO);
//        if (requestDTO.getEmail().length() < 3) {
//            return "error/400";
//        }

        User user = userRepository.findByEmailAndPassword(requestDTO);
        if (user == null){
            return "/company/loginForm";
        } else if (!user.getRole().equals("company")) {
            return "error/404";
        } else { // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user);
        }

        return "redirect:/";
    }


    // 개인 로그인 회원가입
    @GetMapping("/person/joinForm")
    public String personJoinForm() {
        return "person/joinForm";
    }

    @PostMapping("/person/join")
    public String personJoin(UserRequest.JoinDTO requestDTO) {
        System.out.println(requestDTO);
        userRepository.personSave(requestDTO);
        return "redirect:/person/loginForm";
    }

    // 개인 로그인 회원가입
    @GetMapping("/person/loginForm")
    public String personLoginForm() {
        return "person/loginForm";
    }

    @PostMapping("/person/login")
    public String personLogin(UserRequest.LoginDTO requestDTO) {
        System.out.println(requestDTO);
//        if (requestDTO.getEmail().length() < 3) {
//            return "error/400";
//        }

        User user = userRepository.findByEmailAndPassword(requestDTO);

        if (user == null){
            return "/person/loginForm";
        } else if (!user.getRole().equals("person")) {
            return "error/404";
        } else { // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user);
        }
        return "redirect:/";
    }



    //기업 개인 로그아웃
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }


    //회사 정보 및 수정
    //회사 정보 수정
    @GetMapping("/company/info/{id}")
    public String companyInfo(@PathVariable int id, HttpServletRequest request) {
        UserResponse.DetailDTO userDTO = userRepository.findByIdWithUser(id);
        System.out.println(userRepository);
        User sessionUser = (User) session.getAttribute("sessionUser");

        return "company/companyInfo";
    }

    // 수정완료/취소 버튼 누르면 자원을 찾을 수 없음이라 나옴. 그것 수정하고 주석 지워주세요.
    @GetMapping("/company/info/{id}/updateForm")
    public String companyInfoUpdateForm(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            // sessionUser가 null인 경우, 로그인 페이지로 리다이렉트
            return "redirect:/company/loginForm";
        }
        User user = userRepository.findById(id);
        if (user.getId() != sessionUser.getId()){
            return "error/403";
        }
        request.setAttribute("user",user);
        return "company/updateInfoForm";
    }

//   여기에도 머스치에도 post를 적었는데 get이 나오는 이유가 무엇일까요.
    @PostMapping("/company/info/{id}/update")
    public String companyInfoUpdate(@PathVariable int id, UserRequest.UpdateDTO requestDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        User user = userRepository.findById(id);
        if (user.getId() != sessionUser.getId()) {
            return "error/403";
        }
        userRepository.companyUpdate(requestDTO,id);


        return "redirect:/company/info/"+id;
    }

    //개인 프로필 정보 및 수정
    @GetMapping("/person/info")
    public String personal() {
        return "person/personalInfo";
    }

    @GetMapping("/person/info/updateForm")
    public String personInfoInfoUpdateForm() {
        return "person/updatePersonalForm";
    }

    @PostMapping("/person/info/update")
    public String personInfoUpdate() {
        return "redirect:/person/Info";
    }


}