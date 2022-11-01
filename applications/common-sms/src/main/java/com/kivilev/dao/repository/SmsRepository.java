package com.kivilev.dao.repository;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface SmsRepository extends CrudRepository<Sms, Long> {

    @Transactional(timeout = 10)
    Optional<Sms> findBySmsId(@Nonnull Long smsId);

    Optional<Sms> findByClientIdAndSourceIdAndSourceIdempotencyKey(Long clientId, String sourceId, String sourceIdempotencyKey);

    // TODO: сделать limit, можно через прямой запрос, но не хочется
    //, int limit);
    List<Sms> findByClientId(Long clientId);

    @Transactional(timeout = 10)
    @Query("""
            select s.*,
                   sr.sms                      AS "smsstatedetail_sms",
                   sr."sms_state"              AS "smsstatedetail_sms_state",
                   sr."error_code"             AS "smsstatedetail_error_code",
                   sr."sms_result"             AS "smsstatedetail_sms_result",
                   sr."error_message"          AS "smsstatedetail_error_message"
             from sms s
             left join sms_state_detail sr ON sr.sms = s."sms_id"
            where sr.sms_state = :p_state
              and sr.sms_result = :p_result
            limit :p_limit   
            """)
    List<Sms> findAllBySmsStateAndResult(@Param("p_state") SmsState smsState, @Param("p_result") SmsResult smsResult, @Param("p_limit") int rowsNumber);
}
