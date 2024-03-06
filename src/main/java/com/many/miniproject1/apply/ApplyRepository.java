package com.many.miniproject1.apply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplyRepository {
    private final EntityManager em;

    public List<Apply> findAll() {
        Query query = em.createNativeQuery("select * from apply_tb() order by id desc", Apply.class);

        return query.getResultList();
    }

    public Apply findById(int id) {
        Query query = em.createNativeQuery("select * from apply_tb() where id=?");
        query.setParameter(1, id);

        Apply apply = (Apply) query.getSingleResult();

        return apply;
    }

    @Transactional
    public void save(ApplyRequest.SaveDTO requestDTO) {
        Query query = em.createNativeQuery("INSERT INTO Apply_tb(resume_id, post_id, company_id, person_id, is_pass, created_at) VALUES (?,?,?,?,?,now())");
        query.setParameter(1, requestDTO.getResumeId());
        query.setParameter(2, requestDTO.getPostId());
        query.setParameter(3, requestDTO.getCompanyId());
        query.setParameter(4, requestDTO.getPersonId());
        query.setParameter(5, requestDTO.getIsPass());


        query.executeUpdate();
    }

    @Transactional
    public void update(ApplyRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update apply_tb set where id = ?");
        query.setParameter(1, id);

        query.executeUpdate();
    }

    @Transactional
    public void delete(int id) {
        Query query = em.createNativeQuery("delete from apply_tb where id = ?");
        query.setParameter(1, id);

        query.executeUpdate();
    }
}