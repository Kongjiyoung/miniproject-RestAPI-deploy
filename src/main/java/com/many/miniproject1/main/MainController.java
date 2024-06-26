package com.many.miniproject1.main;

import com.many.miniproject1._core.utils.ApiUtil;
import com.many.miniproject1.user.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final HttpSession session;
    private final MainService mainService;

    ////////////////////// 기업.개인 공통
    // 메인 화면에 게시된 이력서 목록
    @GetMapping("/main/resumes") // @GetMapping("/resumes")
    public ResponseEntity<?> resumes() {
        List<MainResponse.MainResumesDTO> respDTO = mainService.mainResumes();

        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    // 메인 화면에 게시된 이력서 디테일
    // TODO: 이거 맞음? 밑에 respDTO로 반환하면서 그 안에 두 가지를 담았는데 하나는 로그인을 해야 보여지고 하나는 그냥 보여진다.
    @GetMapping("/main/resumes/{id}") //  @GetMapping("/resumes/{id}")
    public ResponseEntity<?> mainResumeDetail(@PathVariable Integer id) {
        // 현재 로그인한 사용자가 회사인 경우에만 해당 회사가 작성한 채용 공고 목록 가져오기
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        List<MainResponse.PostTitleListDTO> postTitleListDTOs = new ArrayList<>();
        boolean isCompany = false;
        if (sessionUser != null) {
            String role = sessionUser.getRole();
            if (role.equals("company")) {
                isCompany = true;
            }
            Integer companyId = sessionUser.getId();
            postTitleListDTOs = mainService.getPostTitleListDTOs(sessionUser.getId(), companyId); // 세션유저의 아이디와 컴퍼니 아이디가 일치해야 정보가 넘어감
        }
        // TODO: 테스트 끝나고 바로 아래 한 줄의 코드 삭제. 세션유저의 아이디와 컴퍼니 아이디가 일치해야 정보가 넘어가서 테스트할 때 주석 해제하고 보라고 빼놓음. 테스트할때 14, 14 넣으면 됨.
        // postTitleListDTOs = mainService.getPostTitleListDTOs(14, 14);

        MainResponse.MainResumeDetailDTO mainResumeDetailDTO = mainService.getResumeDetail(id);
        //resume만 아니라 postList도 같이 넘겨야함
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("postTitleListDTOs", postTitleListDTOs);
        responseBody.put("mainResumeDetailDTO", mainResumeDetailDTO);

        return ResponseEntity.ok(new ApiUtil<>(responseBody));
    }

    // 메인 화면에 게시된 채용 공고 목록
    @GetMapping({"/main/posts", "/"}) // @GetMapping({"/posts", "/"})
    public ResponseEntity<?> posts() {
        List<MainResponse.MainPostsDTO> respDTO = mainService.getPostList();

        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    // 메인 화면에 게시된 채용 공고 디테일
    @GetMapping("/api/main/posts/{id}") //  @GetMapping("/api/posts/{id}")
    public ResponseEntity<?> postDetail(@PathVariable Integer id) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        // 목적: 로그인 하지 않아도 채용 공고가 보임, 개인일때 resume제목을 주고 선택하여 지원함.
        boolean isPerson = false;
        if (sessionUser != null) {
            if (sessionUser.getRole().equals("person")) {
                isPerson = true;
                MainResponse.PostDetailDTO respDTO = mainService.getPostIsCompanyDetail(id, sessionUser.getId(), isPerson);

                return ResponseEntity.ok(new ApiUtil<>(respDTO));
            }
        }
        MainResponse.PostDetailDTO respDTO = mainService.getPostDetail(id, isPerson);

        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    //////////////////// 기업
    // 회사가 선택한 공고와 매칭되는 이력서 목록
    @GetMapping("/api/main/company/matching") // @GetMapping("/api/main/company/matching")
    public ResponseEntity<?> matchingPosts(@RequestParam(value = "postChoice", defaultValue = "") Integer postChoice) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        List<MainResponse.PostMatchingChoiceDTO> respDTO = mainService.findByUserIdPost(sessionUser.getId());

        if (postChoice != null) {
            MainResponse.MainPostMatchDTO respResultDTO = mainService.matchingResume(postChoice);

            return ResponseEntity.ok(new ApiUtil<>(respResultDTO));
        }

        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    //////////////////// 개인
    // 개인이 선택한 이력서와 메칭되는 공고 목록
    @GetMapping("/api/main/person/matching")
    public ResponseEntity<?> matchingResumes(@RequestParam(value = "resumeChoice", defaultValue = "") Integer resumeChoice) {
        //공고 가져오기
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        List<MainResponse.ResumeMatchingChoiceDTO> resumeList = mainService.findByUserIdResume(sessionUser.getId());

        if (resumeChoice != null) {
            MainResponse.MainResumeMatchDTO postResultList = mainService.matchingPost(resumeChoice);

            //resumeList와 함께 DTO에 담기
            return ResponseEntity.ok(new ApiUtil<>(postResultList));
        }

        return ResponseEntity.ok(new ApiUtil<>(resumeList));
    }
}



