package hellojpa.jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            /**
             * TypeQuery: 반환 타입이 명확할 때 사용
             * Query: 반환 타입이 명확하지 않을 때 사용
             */
            TypedQuery<Member> query1 = em.createQuery("SELECT m FROM Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("SELECT m.username FROM Member m", String.class);
            Query query3 = em.createQuery("SELECT m.username, m.age FROM Member m");

            /**
             * getResultList: 결과가 여러 행일 때
             * getSingleResult: 결과가 한 행일 때 (결과가 없거나 여러 행이면 예외 발생)
             */
            List<Member> resultList = query1.getResultList();
            Member resultMember = query1.getSingleResult();

            for(Member m : resultList) {
                System.out.println("member = " + m);
            }

            TypedQuery<Member> query4 = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
            query4.setParameter("username", "member1");
            Member singleResult = query4.getSingleResult();
            System.out.println("singleResult = " + singleResult.getUsername());

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
