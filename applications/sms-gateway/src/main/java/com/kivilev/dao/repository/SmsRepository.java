package com.kivilev.dao.repository;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface SmsRepository extends CrudRepository<Sms, String> {

    @Transactional(timeout = 10)
    Optional<Sms> findBySmsId(@Nonnull String smsId);

    @Transactional(timeout = 10)
    @Query("""
            select s.*,
                   sr.sms                      AS "smsstatusresultinfo_sms",
                   sr."sms_state"              AS "smsstatusresultinfo_sms_state",
                   sr."error_code"             AS "smsstatusresultinfo_error_code",
                   sr."sms_result"             AS "smsstatusresultinfo_sms_result",
                   sr."error_message"          AS "smsstatusresultinfo_error_message"
             from sms s
             left join sms_state_detail sr ON sr.sms = s."sms_id"
            where sr.sms_state = :p_state
              and sr.sms_result = :p_result
            limit :p_limit   
            """)
    List<Sms> findAllBySmsStateAndResult(@Param("p_state") SmsState smsState, @Param("p_result") SmsResult smsResult, @Param("p_limit") int rowsNumber);

    @Transactional(timeout = 10)
    @Query("""
            select s.*,
                   sr.sms             AS "smsstatedetail_sms",
                   sr."sms_state"     AS "smsstatedetail_sms_state",
                   sr."error_code"    AS "smsstatedetail_error_code",
                   sr."sms_result"    AS "smsstatedetail_sms_result",
                   sr."error_message" AS "smsstatedetail_error_message"
             from sms s
             left join sms_state_detail sr ON sr.sms = s."sms_id"
            where s.is_send_status_to_queue = false 
              and (sr.sms_result = 'ERROR' or 
                   (sr.sms_state = 'SENT_TO_CLIENT' and sr.sms_result = 'SUCCESSFUL_PROCESSED')) 
            limit :p_limit                
            """)
    List<Sms> findAllSmsReadyForSendingQueue(@Param("p_limit") int packageSize);
    // TODO: без учета TTL-сообщений (и индекса по этому полю) запрос начнет со временем тормозить
}
