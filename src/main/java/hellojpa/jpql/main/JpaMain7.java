package hellojpa.jpql.main;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberType;
import hellojpa.jpql.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OrderColumn;
import javax.persistence.Persistence;

public class JpaMain7 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            /**
             * JPQL 기본 함수
             * - CONCAT
             * - SUBSTRING
             * - TRIM
             * - LOWER, UPPER
             * - LENGTH
             * - LOCATE
             * - ABS, SQRT. MOD
             * - SIZE, INDEX(JPA 용도) 
             */
            String query = "SELECT FUNCTION('group_concat', m.username) FROM Member m";

            List<String> result = em.createQuery(query, String.class)
                .getResultList();

            for(String s : result) {
                System.out.println("s = " + s);
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
