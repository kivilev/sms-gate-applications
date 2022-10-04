package com.kivilev.service.processor;

import com.kivilev.config.SmsStateProcessingConfig;
import com.kivilev.dao.SmsDao;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.queue.ProducerQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableConfigurationProperties(SmsStateProcessingConfig.class)
public class SendStatusChangeMessageQueueProcessor implements SmsStateProcessor {

    private final SmsState processingSmsState = SmsState.SENT_TO_CLIENT;
    private final SmsDao smsDao;
    private final SmsStateProcessingConfig smsStateProcessingConfig;
    private final ProducerQueueService producerQueueService;
    private final Logger logger = LoggerFactory.getLogger(SendStatusChangeMessageQueueProcessor.class);

    public SendStatusChangeMessageQueueProcessor(SmsDao smsDao, SmsStateProcessingConfig smsStateProcessingConfig, ProducerQueueService producerQueueService) {
        this.smsDao = smsDao;
        this.smsStateProcessingConfig = smsStateProcessingConfig;
        this.producerQueueService = producerQueueService;
    }

    @Transactional
    @Override
    public void process() {
        try {
            var smsList = smsDao.getSmsMessagesReadyForSendingQueue(smsStateProcessingConfig.getPackageSize());
            producerQueueService.sendSmsStatusMessages(smsList);
            smsList.forEach(sms -> {
                sms.setSendStatusToQueue(true);
                smsDao.saveSms(sms);

                logger.debug(String.format("sending sms status to queue. smsId: %s, status: %s, result: %s, error_code: %s, error_msg: %s",
                        sms.getSmsId(),
                        sms.getSmsStatusInfo().getSmsStatus(),
                        sms.getSmsStatusInfo().getSmsResult(),
                        sms.getSmsStatusInfo().getErrorCode(),
                        sms.getSmsStatusInfo().getErrorMessage()));
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public SmsState getSmsState() {
        return processingSmsState;
    }

    private boolean isSmsReadyForSendingToQueue(Sms sms) {
        var smsResult = sms.getSmsStatusInfo().getSmsResult();

        boolean isDidNotSendToQueueBefore = !sms.isSendStatusToQueue();
        boolean isErrorProcessingResult = smsResult == SmsResult.ERROR;
        boolean isSuccessfulSendToClient = sms.getSmsStatusInfo().getSmsStatus() == SmsState.SENT_TO_CLIENT &&
                smsResult == SmsResult.SUCCESSFUL_PROCESSED;

        return isDidNotSendToQueueBefore && (isErrorProcessingResult || isSuccessfulSendToClient);
    }
}
