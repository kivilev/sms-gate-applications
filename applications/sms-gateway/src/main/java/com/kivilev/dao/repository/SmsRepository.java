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

    // Вопрос: я так понял, что @Transactional при чтении эквивалентно select * from ... for update wait N; ? или я не прав?
    // Если эквивалентен, то как например указать, чтобы пропускал заблокированные? аля skip locked?
    @Transactional(timeout = 10)
    Optional<Sms> findBySmsId(@Nonnull String smsId);

    // Вопрос: как можно написать запрос не используя текст? У меня никак не получалось написать текстом в названии метода
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

    // Вопрос: как можно написать запрос не используя текст? У меня никак не получалось написать текстом в названии метода
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
