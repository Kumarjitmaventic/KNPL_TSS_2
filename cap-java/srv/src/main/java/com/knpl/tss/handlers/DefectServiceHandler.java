package com.knpl.tss.handlers;

import java.util.HashMap;
import java.util.Map;

import com.knpl.tss.exceptions.UUIDNotFoundException;
import com.knpl.tss.services.DefectService;
import com.knpl.tss.utilities.Utility;
import com.sap.cds.Result;

import com.sap.cds.ql.Update;

import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsDeleteEventContext;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.defectservice.DefectDetails;
import cds.gen.defectservice.DefectDetails_;
import cds.gen.defectservice.DefectService_;


@Component
@ServiceName(DefectService_.CDS_NAME)
public class DefectServiceHandler implements EventHandler {

    @Autowired
    private PersistenceService db;
    
    @Autowired
    private Utility utility;

    @Autowired
    private DefectService defectService;

    // @Autowired
    // private CqnAnalyzer analyzer;

    private final static Logger LOG = LoggerFactory.getLogger(DefectServiceHandler.class);  

    @On(event = {CdsService.EVENT_DELETE}, entity = "DefectService.DefectDetails")
	public void delete(CdsDeleteEventContext cdsDeleteEventContext) {
        LOG.info("Soft Deleting/Archiving the record");
        
        String existingEntityUUID;
        try {
            // This approach will work in case of this /DefectDetails(ID=19048ad4-74cb-44ec-b648-2bf35775ef9b,IsActiveEntity=true) if this is changing need to change utility method
            existingEntityUUID = utility.getUUIDFromCQNRef(cdsDeleteEventContext.getCqn().ref().targetSegment().filter().get().toJson());
        } catch (UUIDNotFoundException e) {
            throw new ServiceException(ErrorStatuses.NOT_FOUND, e.getMessage());
        }
        LOG.info("Soft Delete/Archive the record {}", existingEntityUUID);
        Map<String,Object> defectDetailMap = new HashMap<>();
        defectDetailMap.put("ID", existingEntityUUID);
        defectDetailMap.put("isArchive", Boolean.TRUE);
        CqnUpdate cqnUpdate = Update.entity(DefectDetails_.class).data(defectDetailMap).where(a-> a.ID().eq(existingEntityUUID));
        Result result = db.run(cqnUpdate);

        defectService.softDeleteVersionSnapshots(existingEntityUUID);

        cdsDeleteEventContext.setCompleted();
        cdsDeleteEventContext.setResult(result);
        LOG.info("Defect entry archived successfully for the ID {}",existingEntityUUID);
    }

    @On(event = CdsService.EVENT_CREATE, entity = "DefectService.DefectDetails")
	public void onDefectDetailsCreate(DefectDetails defectDetails) {
        // defectService.processVersionSnapshot(defectDetails);
        defectService.generateDefectId(defectDetails);

    }

    @On(event = CdsService.EVENT_UPDATE, entity = "DefectService.DefectDetails")
	public void onDefectDetailsUpdate(DefectDetails defectDetails) {
        defectService.processVersionSnapshot(defectDetails);
    }

}