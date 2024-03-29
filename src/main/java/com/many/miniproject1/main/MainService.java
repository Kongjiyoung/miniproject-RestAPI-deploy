package com.many.miniproject1.main;

import com.many.miniproject1._core.errors.exception.Exception401;
import com.many.miniproject1.apply.ApplyJPARepository;
import com.many.miniproject1.offer.Offer;
import com.many.miniproject1.offer.OfferJPARepository;
import com.many.miniproject1.offer.OfferRequest;
import com.many.miniproject1.post.Post;
import com.many.miniproject1.post.PostJPARepository;
import com.many.miniproject1.resume.Resume;
import com.many.miniproject1.resume.ResumeJPARepository;
import com.many.miniproject1.scrap.Scrap;
import com.many.miniproject1.scrap.ScrapJPARepository;
import com.many.miniproject1.scrap.ScrapRequest;
import com.many.miniproject1.user.User;
import com.many.miniproject1.user.UserJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MainService {
    private final ApplyJPARepository applyJPARepository;
    private final OfferJPARepository offerJPARepository;
    private final ScrapJPARepository scrapJPARepository;
    private final ResumeJPARepository resumeJPARepository;
    private final PostJPARepository postJPARepository;
    private final UserJPARepository userJPARepository;

    public List<Post> getPostList() {
        List<Post> postList = postJPARepository.findAllPost();
        return postList;
    }

    public Post getPostDetail(int postId) {
        Post post = postJPARepository.findByPostIdJoinUserAndSkill(postId);
        return post;
    }

    @Transactional
    public Offer sendPostToResume(int id, Integer postId){
        Scrap scrap = scrapJPARepository.findById(id)
                .orElseThrow(() -> new Exception401("존재하지 않는 이력서..." + id));
        Post post = postJPARepository.findById(postId)
                .orElseThrow(() -> new Exception401("존재하지 않는 공고입니다!" + postId));
        OfferRequest.ScrapOfferDTO scrapOfferDTO = new OfferRequest.ScrapOfferDTO(scrap.getResume(), post);
        Offer offer = offerJPARepository.save(scrapOfferDTO.toEntity());

        return offer;
    }

    @Transactional
    public Scrap companyScrap(int id, Integer userId) {
        Resume resume = resumeJPARepository.findById(id)
                .orElseThrow(() -> new Exception401("존재하지 않는 이력서입니다...!" + id));
        User user = userJPARepository.findById(userId)
                .orElseThrow(() -> new Exception401("띠용~?" + userId));
        ScrapRequest.MainScrapDTO mainScrapDTO = new ScrapRequest.MainScrapDTO(resume, user);
        Scrap scrap = scrapJPARepository.save(mainScrapDTO.toEntity());
        return scrap;
    }
}
