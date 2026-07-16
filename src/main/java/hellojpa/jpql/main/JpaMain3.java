package hellojpa.jpql.main;

import hellojpa.jpql.Address;
import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
import hellojpa.jpql.dto.MemberDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain3 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + (i+1));
                member.setAge(i+1);
                em.persist(member);
            }

            em.flush();
            em.clear();

            // 체이닝으로 페이징 처리를 할 수 있다.
            List<Member> members = em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC", Member.class)
                .setFirstResult(1)
                .setMaxResults(20)
                .getResultList();

            System.out.println("members.size = " + members.size());
            for(Member m : members) {
                System.out.println("member1 = " + m);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
