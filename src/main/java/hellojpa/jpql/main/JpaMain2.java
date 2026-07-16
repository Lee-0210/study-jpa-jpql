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
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMain2 {

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

            em.flush();
            em.clear();

            // [Entity Projection]
            List<Member> result = em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();

            Member findMember = result.getFirst();
            findMember.setAge(20); // 여기까지도 영속성 컨텍스트에서 관리된다.

            // 아래 JPQL 처럼 JOIN 을 명시적으로 해야한다. (SELECT m.team FROM Member m 은 사용 X)
            List<Team> teams = em.createQuery("SELECT t FROM Member m JOIN m.team t", Team.class)
                .getResultList();

            // [Embedded Type Projection]
            List<Address> addresses = em.createQuery("SELECT o.address FROM Order o", Address.class)
                .getResultList();

            // [Scalar Projection]
            // NEW 를 사용해서 MemberDto 객체를 생성하면서 받을 수 있음
            List<MemberDto> scalarQueryResult = em.createQuery("SELECT NEW hellojpa.jpql.dto.MemberDto(m.username, m.age) FROM Member m", MemberDto.class)
                .getResultList();

            MemberDto memberDto = scalarQueryResult.getFirst();
            System.out.println("memberDto = " + memberDto.getUsername());
            System.out.println("memberDto = " + memberDto.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
