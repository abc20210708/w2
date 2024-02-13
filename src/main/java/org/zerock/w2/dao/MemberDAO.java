package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.MemberVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    public MemberVo getWithPassword(String mid, String mpw) throws Exception {

        String query = "select mid, mpw, mname from tbl_member where mid=? and mpw=?";

        MemberVo memberVo = null;

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement =
                connection.prepareStatement(query);

        preparedStatement.setString(1, mid);
        preparedStatement.setString(2, mpw);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        memberVo = MemberVo.builder()
                .mid(resultSet.getString(1))
                .mpw(resultSet.getString(2))
                .mname(resultSet.getString(3))
                .build();

        return memberVo;
    }
}
