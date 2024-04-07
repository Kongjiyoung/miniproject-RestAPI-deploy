package com.many.miniproject1.apply;


import com.many.miniproject1._core.utils.ApiUtil;
import com.many.miniproject1.main.MainService;
import com.many.miniproject1.user.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplyController {
    private final HttpSession session;
    private final ApplyService applyService;
    private final MainService mainService;

    // 기업이 받은 이력서 관리
    @GetMapping("/api/company/my-page/receive-resumes") //@GetMapping("/api/company/resumes")
    public ResponseEntity<?> companyResumes() {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        List<ApplyResponse.AppliedResumeSkillDTO> appliedResumeSkillDTOList = applyService.getAppliedResumeSkillDTOs(sessionUser.getId());
        return ResponseEntity.ok(new ApiUtil<>(appliedResumeSkillDTOList));
    }  // 체크 완


    // 기업에서 받은 이력서 디테일
    @GetMapping("/api/company/my-page/receive-resumes/{id}") //@GetMapping("/api/company/resumes/{id}")

    public ResponseEntity<?> companyResumeDetail(@PathVariable int id) {
        ApplyResponse.AppliedResumeSkillDetailDTO appliedResumeDetail = applyService.getAppliedResume(id);
        applyService.companyResumeDetail(id);

        return ResponseEntity.ok(new ApiUtil<>(appliedResumeDetail));
    }  // 체크 완

    @PutMapping("/api/company/my-page/receive/{id}/is-pass")// @PutMapping("/api/company/resumes/{id}/is-pass")
    public ResponseEntity<?> companyPass(@PathVariable Integer id, @RequestBody ApplyRequest.UpdateIsPassDTO reqDTO) {
        ApplyResponse.UpdateIsPassDTO respDTO = applyService.isPassResume(id, reqDTO);

        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    } // 체크 완;;

    // 개인이 지원한 이력서 목록
    @GetMapping("/api/person/my-page/apply-posts")
    public ResponseEntity<?> personApplies() {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        List<ApplyResponse.ApplyPostSkillDTO> applyPostSkillDTOList = applyService.getApplyPostSkillDTOs(sessionUser.getId());

        return ResponseEntity.ok(new ApiUtil<>(applyPostSkillDTOList));
    }  // 체크 완

    // 개인이 지원한 이력서 디테일
    @GetMapping("/api/person/my-page/apply-posts/{id}") // 내가 지원한 공고 디테일

    public ResponseEntity<?> personApply(@PathVariable int id) {
        ApplyResponse.ApplyPostSkillDetailDTO applyPostDetail = applyService.getPostDetail(id);

        return ResponseEntity.ok(new ApiUtil<>(applyPostDetail));
    }  // 체크 완

    @DeleteMapping("/api/person/my-page/apply-posts/{id}")
    public ResponseEntity<?> applyDelete(@PathVariable Integer id) {
        applyService.deleteApply(id);

        return ResponseEntity.ok(new ApiUtil<>(null));
    }  // 체크 완


    //메인 이력서 지원하기
    @PostMapping("/api/person/main/posts/{id}/apply")
    public ResponseEntity<?> personMainApply(@PathVariable Integer id, @RequestBody ApplyRequest.ResumeChoiceDTO resumeChoice) {
        ApplyResponse.ApplyDTO respDTO = applyService.saveApplyByMain(id, resumeChoice.getResumeChoice());
        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    //스크랩페이지에서 지원하기
    @PostMapping("/api/person/my-page/scraps/{id}/apply")
    public ResponseEntity<?> companyResumeOffer(@PathVariable Integer id, @RequestBody ApplyRequest.ResumeChoiceDTO resumeChoice) {
        ApplyResponse.ApplyDTO respDTO = applyService.saveApplyByScrap(id, resumeChoice.getResumeChoice());
        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }
}