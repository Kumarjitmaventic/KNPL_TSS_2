package com.knpl.tss.handlers;

import java.time.Instant;

import javax.persistence.criteria.From;

import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knpl.tss.utilities.Utility;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsDeleteEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.cds.CdsUpsertEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.knpl.tss.UserLoginData;
import cds.gen.knpl.tss.UserLoginData_;
import cds.gen.knpl.tss.Users_;
import cds.gen.userservice.UserService_;
import cds.gen.userservice.Users;


@Component
@ServiceName(UserService_.CDS_NAME)
public class UserServiceHandler implements EventHandler {
    
    @Autowired
    private PersistenceService db;

    @Autowired
    private Utility utility;

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceHandler.class);

    @On(event = CdsService.EVENT_UPSERT, entity = "UserService.UserLoginData")
    public void onUpsertUserLoginData(UserLoginData userLoginData){
        System.out.println(userLoginData);
    }
    @On(event = CdsService.EVENT_CREATE, entity = "UserService.UserLoginData")
    public void onCreateUserLoginData(UserLoginData userLoginData){
        userLoginData.setTotalLogin(1);
        userLoginData.setLastLogin(Instant.now());
    }
    @Before(event = CdsService.EVENT_UPDATE, entity = "UserService.UserLoginData")
    public void beforeUpdateUserLoginData(UserLoginData userLoginData){
        
        userLoginData.setLastLogin(Instant.now());
        try{
            CqnSelect cqnSelect = Select.from(UserLoginData_.class).where(user -> user.userName().eq(userLoginData.getUserName().toString()));
            Result getUserRecord =  db.run(cqnSelect);
            int totalLogin = (int)getUserRecord.list().get(0).get("totalLogin");
            userLoginData.setTotalLogin(totalLogin+1);
        }
        catch (Exception e)
        {
            userLoginData.setTotalLogin(1);
        }
    }

}
