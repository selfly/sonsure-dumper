//package com.sonsure.jdbc.springjdbc.persist;
//
//import com.sonsure.jdbc.persist.PersistExecutor;
//import org.springframework.jdbc.core.JdbcOperations;
//
///**
// * JdbcTemplate实现
// * <p>
// * Created by liyd on 17/4/17.
// */
//public class JdbcTemplatePersistExecutorFactory implements PersistExecutorFactory {
//
//    private JdbcOperations jdbcOperations;
//
//    private PersistExecutor persistExecutor;
//
//    public JdbcTemplatePersistExecutorFactory() {
//    }
//
//    public JdbcTemplatePersistExecutorFactory(JdbcOperations jdbcOperations) {
//        this.jdbcOperations = jdbcOperations;
//    }
//
//    public Class<?>[] getSupportType() {
//        return new Class<?>[]{Object.class};
//    }
//
//    public PersistExecutor getExecutor() {
//        if (this.persistExecutor == null) {
//            Assert.notNull(this.jdbcOperations);
//            JdbcTemplatePersistExecutor jdbcTemplatePersistExecutor = new JdbcTemplatePersistExecutor();
//            jdbcTemplatePersistExecutor.setJdbcOperations(this.jdbcOperations);
//            this.persistExecutor = jdbcTemplatePersistExecutor;
//        }
//        return this.persistExecutor;
//    }
//
//    public void setJdbcOperations(JdbcOperations jdbcOperations) {
//        this.jdbcOperations = jdbcOperations;
//    }
//}
