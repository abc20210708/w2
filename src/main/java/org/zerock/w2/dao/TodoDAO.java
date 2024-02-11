package org.zerock.w2.dao;

import lombok.Cleanup;
import org.zerock.w2.domain.TodoVo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    public String getTime() {
        String now = null;

        try(Connection connection = ConnectionUtil.INSTANCE.getConnection();
            PreparedStatement preparedStatement = connection.
                    prepareStatement("select now()");
            ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            resultSet.next();

            now = resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    public String getTime2() throws Exception {

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection
                                                .prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String now = resultSet.getString(1);

        return now;
    }


    public void insert(TodoVo vo) throws Exception {
        String sql = "insert into tbl_todo (title, dueDate, finished) values (?, ?, ?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.
                                            prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();
    }

    // tbl_todo 내의 모든 데이터를 가져오는 기능
    // 테이블 각 행(row)은 하나의 TodoVO 객체가 될 것이고,
    // 모든 TodoVO를 담을 수 있도록 List<TOdoVO> 타입을 리턴 타입으로 지정
    public List<TodoVo> selectAll() throws Exception {

        String sql = "select * from tbl_todo";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection
                                                    .prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        List<TodoVo> list = new ArrayList<>();

        while(resultSet.next()) {
            TodoVo vo = TodoVo.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }

        return list;

    }

    //TodoDAO의 조회 기능 구현
    public TodoVo selectOne(Long tno) throws Exception {

        String sql = "select * from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection
                                                .prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        TodoVo vo = TodoVo.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("dueDate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    // 삭제 기능 구현하기
    public void deleteOne(Long tno) throws Exception {

        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection
                                                        .prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        preparedStatement.executeUpdate();
    }

    // 수정 기능 구현하기
    public void updateOne(TodoVo todoVo) throws Exception {
        String sql = "update tbl_todo set title = ?, dueDate = ?, finished = ? where tno  = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection
                .prepareStatement(sql);

        preparedStatement.setString(1, todoVo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(todoVo.getDueDate()));
        preparedStatement.setBoolean(3, todoVo.isFinished());
        preparedStatement.setLong(4, todoVo.getTno());

        preparedStatement.executeUpdate();
    }

}
