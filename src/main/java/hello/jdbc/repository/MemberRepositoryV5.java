package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * SQLExceptionTranslator 추가
 */

// 회원 등록
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate template;

    // 새로 생김
    //private final DataSource dataSource;
    // SQLExceptionTranslator 새로 추가
    // private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV5(DataSource dataSource){
        template = new JdbcTemplate(dataSource);
        //this.dataSource = dataSource;
        //this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }
    // 새로 생김

    @Override
    // 예외 던지는 것 다 필요 없음
    public Member save(Member member){
        String sql = "insert into member(member_id, money) values(?,?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

        /*
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e){
            // 반드시 (e) 다시 던저 주어야 한다.
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(con,pstmt,null);
        }
    }
    */

    // 엄청 짧아짐.

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }
    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);
    }
    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
