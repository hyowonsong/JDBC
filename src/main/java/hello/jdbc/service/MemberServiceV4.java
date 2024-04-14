package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import hello.jdbc.repository.MemberRepositoryV3;
import hello.jdbc.repository.MemberRepositoryV4_1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV4 {

    // PlatformTransactionManager 에서 TransactionTemplate 를 사용해서 중복 제거해보자
    // private final PlatformTransactionManager transactionManager;

    private final MemberRepository memberRepository;
    /*
    public MemberServiceV3_3(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }
     */

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

        // @Transactional 쓰면서 다 삭제
        //TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        /*
        txTemplate.executeWithoutResult((status)-> {
            try {
                // con.setAutoCommit(false);
                //비즈니스 로직
                bizLogic(fromId, toId, money);
                // transactionManager.commit(status); //성공시 커밋
            } catch (SQLException e) {
                // transactionManager.rollback(status); //실패시 롤백
                throw new IllegalStateException(e);
            }
        });
         */

    private void bizLogic(String fromId, String toId, int money) {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember){
        if (toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    /*
    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); //커넥션 풀 고려
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
     */

}