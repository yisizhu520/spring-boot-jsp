package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findAll() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<List<String>>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            final int j = i;
            futures.add(executorService.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + ": " + j);
                    return getUsers();
                }
            }));
        }
        List<String> result = new ArrayList<>();
        for(Future<List<String>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return result;
    }

    private List<String> getUsers() {
        String sql = "select * from test_index";
        List<String> result =  jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return resultSet.getString("a");
            }
        });
        try {
            jdbcTemplate.getDataSource().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
