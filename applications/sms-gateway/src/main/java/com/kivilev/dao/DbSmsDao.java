package com.kivilev.dao;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatusResultInfo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Predicate;

@Repository
public class DbSmsDao implements SmsDao {
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DbSmsDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public boolean isExists(String smsId) {
        return false;
    }

    @Override
    public void saveSms(Sms Sms) {

    }

    @Override
    public void saveSmsStatusResultInfo(String smsId, SmsStatusResultInfo smsStatusResultInfo) {

    }

    @Override
    public List<Sms> getSmsList(Predicate<Sms> filterPredicate, int packageSize) {
        // TODO: с блокировкой
        return null;
    }

    private static final String UPSERT_SMS = "";

}
