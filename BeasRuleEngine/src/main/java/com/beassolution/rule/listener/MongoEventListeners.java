package com.beassolution.rule.listener;

import com.beassolution.rule.crypto.Cryptography;
import com.beassolution.rule.model.base.BaseModel;
import com.beassolution.rule.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * MongoDB event listener for automatic field management and encryption.
 * 
 * <p>This class provides event listeners for MongoDB operations that automatically
 * handle audit fields, ID generation, and field encryption. It ensures that
 * all entities have proper audit information and encrypted fields when needed.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Automatic ID generation</li>
 *   <li>Audit field population</li>
 *   <li>Field encryption</li>
 *   <li>User tracking</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MongoEventListeners extends AbstractMongoEventListener<BaseModel> {
    
    /**
     * Cryptography component for field encryption.
     */
    private final Cryptography cryptography;
    
    /**
     * User service for getting current user information.
     */
    private final UserService userService;

    /**
     * Handles before save events.
     * 
     * <p>This method is called before an entity is saved to MongoDB. It ensures
     * that audit fields are properly set with current user and timestamp information.
     * 
     * @param event The before save event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<BaseModel> event) {
        Date now = Calendar.getInstance().getTime();
        if (Objects.isNull(event.getSource().getCreatedDate())) event.getSource().setCreatedDate(now);
        if (Objects.isNull(event.getSource().getCreatedBy())) event.getSource().setCreatedBy(userService.getUsername());
        event.getSource().setLastModifiedBy(userService.getUsername());
        event.getSource().setLastModifiedDate(now);
        super.onBeforeSave(event);
    }

    /**
     * Handles before convert events.
     * 
     * <p>This method is called before an entity is converted for MongoDB storage.
     * It ensures that the entity has a unique ID, proper audit fields, and
     * encrypted fields if needed.
     * 
     * @param event The before convert event
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseModel> event) {
        if (Objects.isNull(event.getSource().getId())) event.getSource().setId(UUID.randomUUID());
        Date now = Calendar.getInstance().getTime();
        if (Objects.isNull(event.getSource().getCreatedDate())) event.getSource().setCreatedDate(now);
        if (Objects.isNull(event.getSource().getCreatedBy())) event.getSource().setCreatedBy(userService.getUsername());
        event.getSource().setLastModifiedBy(userService.getUsername());
        event.getSource().setLastModifiedDate(now);
        try {
            cryptography.encrypt(event.getSource());
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            log.error("Encryption error", e);
        }
        super.onBeforeConvert(event);
    }

    /**
     * Handles after convert events.
     * 
     * @param event The after convert event
     */
    @Override
    public void onAfterConvert(AfterConvertEvent<BaseModel> event) {
        super.onAfterConvert(event);
    }

    /**
     * Handles before delete events.
     * 
     * @param event The before delete event
     */
    @Override
    public void onBeforeDelete(BeforeDeleteEvent<BaseModel> event) {
        super.onBeforeDelete(event);
    }

    /**
     * Handles after delete events.
     * 
     * @param event The after delete event
     */
    @Override
    public void onAfterDelete(AfterDeleteEvent<BaseModel> event) {
        super.onAfterDelete(event);
    }

    /**
     * Handles after load events.
     * 
     * @param event The after load event
     */
    @Override
    public void onAfterLoad(AfterLoadEvent<BaseModel> event) {
        super.onAfterLoad(event);
    }

    /**
     * Handles after save events.
     * 
     * @param event The after save event
     */
    @Override
    public void onAfterSave(AfterSaveEvent<BaseModel> event) {
        super.onAfterSave(event);
    }

    /**
     * Handles general MongoDB mapping events.
     * 
     * @param event The MongoDB mapping event
     */
    @Override
    public void onApplicationEvent(MongoMappingEvent<?> event) {
        super.onApplicationEvent(event);
    }

}
