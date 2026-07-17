package hellojpa.jpql.main;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain4 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // INNER JOIN
            List<Member> members1 = em.createQuery("SELECT m FROM Member m JOIN m.team t", Member.class)
                .getResultList();

            // LEFT OUTER JOIN, ON 절 뒤에는 조인 조건이 자동으로 붙고 명시한 필터 조건 적용됨
            List<Member> members2 = em.createQuery("SELECT m FROM Member m LEFT JOIN m.team t ON t.name = 'teamA'", Member.class)
                .getResultList();

            System.out.println("members1.size = " + members1.size());
            for(Member m : members1) {
                System.out.println("member1 = " + m);
            }

            System.out.println("members2.size = " + members2.size());
            for(Member m : members2) {
                System.out.println("member2 = " + m);
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
