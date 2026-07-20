package hellojpa.jpql.main;

import hellojpa.jpql.Member;
import hellojpa.jpql.Team;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain8 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // Fetch Join 으로 한번에 콜
            // JPQL 의 DISTINCT 는 쿼리 결과 뿐만 아니라 애플리케이션의 객체도 중복 제거해줌
            // String query = "SELECT m FROM Member m JOIN FETCH m.team";
            String query = "SELECT DISTINCT t FROM Team t JOIN FETCH t.members m";
            /*List<Member> result = em.createQuery(query, Member.class)
                .getResultList();*/
            List<Team> result = em.createQuery(query, Team.class)
                .getResultList();

            /*for(Member member : result) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
            }*/

            for(Team team : result) {
                System.out.println("team = " + team.getName() + "|members = " + team.getMembers().size());
                for(Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
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
