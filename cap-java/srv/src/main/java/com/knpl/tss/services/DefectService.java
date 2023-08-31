package com.knpl.tss.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.knpl.tss.exceptions.InvalidDefectIdException;
import com.sap.cds.EmptyResultException;
import com.sap.cds.NonUniqueResultException;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;

import com.sap.cds.services.persistence.PersistenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cds.gen.defectservice.DefectDetailVersions;
import cds.gen.defectservice.DefectDetailVersions_;
import cds.gen.defectservice.DefectDetails;
import cds.gen.defectservice.DefectDetails_;
import cds.gen.defectservice.MstrSubCategories_;
import cds.gen.defectservice.MstrVehicleTypes;
import cds.gen.defectservice.MstrVehicleTypes_;

import com.sap.cds.Result;

import java.util.stream.Collectors;
@Service
public class DefectService {

    private final static Logger LOG = LoggerFactory.getLogger(DefectService.class);

    @Autowired
    private PersistenceService db;

    public void processVersionSnapshot(DefectDetails defectDetail) {
        // LOG.info ( "Version: Defect Detail object: " + defectDetail.toJson());
        // LOG.info ( "Version: Defect Detail object: " + defectDetail.getDefectId());

        if (defectDetail.getDefectId() == null || defectDetail.getDefectId().trim().isEmpty()) {
            return;
        }

        CqnSelect cqnSelect = Select.from(DefectDetails_.class).where(d -> d.defectId().eq(defectDetail.getDefectId()));
        // CqnSelect cqnSelect = Select.from(DefectDetails_.class).where(d -> d.ID().eq(defectDetail.getId()));

        DefectDetails savedDefectDetail;
        try {
            savedDefectDetail = db.run(cqnSelect).single(DefectDetails.class);
        } catch (EmptyResultException e) {
            // LOG.info("Version: No defect with defectId : " + defectDetail.getDefectId());
            return;
        } catch (NonUniqueResultException e) {
            LOG.info("Not Unique DefectDetiail result - Multiple result. with Defect ID :" + defectDetail.getDefectId());
            return;
        }
        
        Set<String> newDefectDetailKeySet = defectDetail.keySet();
        for (String key : newDefectDetailKeySet) {
            if (!defectDetail.get(key).equals(savedDefectDetail.get(key))) {
                // defectDetail entry modified
                createVersionSnapshot(savedDefectDetail);
                break;
            }
        }
    }

    private boolean createVersionSnapshot(DefectDetails defectDetail) {
        CqnSelect cqnSelect = Select.from(DefectDetailVersions_.class)
                .where(d -> d.defectId().eq(defectDetail.getDefectId()).and(d.isArchive().eq(false)));
        List<DefectDetailVersions> savedVersions = db.run(cqnSelect).listOf(DefectDetailVersions.class);

        int nextVersionNumber = 1;
        for (DefectDetailVersions defectVersion : savedVersions) {

            if (defectVersion.getVersion() >= nextVersionNumber) {
                nextVersionNumber = defectVersion.getVersion() + 1;
            }
        }
        Map<String, Object> defectDetailVersionMap = defectDetail;
        defectDetailVersionMap.put("defectDetail_ID", defectDetail.get("ID"));
        defectDetailVersionMap.remove("ID");
        defectDetailVersionMap.remove("versions");
        defectDetailVersionMap.remove("IsActiveEntity");
        defectDetailVersionMap.remove("HasActiveEntity");
        defectDetailVersionMap.remove("HasDraftEntity");
        defectDetailVersionMap.put("version", nextVersionNumber);
        defectDetailVersionMap.put("createdAt", defectDetail.get("modifiedAt"));
        defectDetailVersionMap.put("createdBy", defectDetail.get("modifiedBy"));
        defectDetailVersionMap.remove("modifiedAt");
        defectDetailVersionMap.remove("modifiedBy");

        CqnInsert insert = Insert.into(DefectDetailVersions_.class).entry(defectDetailVersionMap);
        db.run(insert);
        LOG.info("Version Snapshot: " + nextVersionNumber + " created for Defect Id: "
                + defectDetailVersionMap.get("defectId"));
        return true;
    }
    
    public List<String> getAllNotArchivedDefectId() {
        CqnSelect cqnSelect = Select.from(DefectDetails_.class)
            .where(a-> a.isArchive().eq(false));
            List<DefectDetails> defectDetails = db.run(cqnSelect).listOf(DefectDetails.class);
            return defectDetails.stream().map(b -> b.getDefectId())
            .collect(Collectors.toList());
    }

    public void generateDefectId(DefectDetails defectDetails) {
        generateNextDefectId(defectDetails);
    }

    public void generateDefectIdbyTime(DefectDetails defectDetails) {
        long timeNowMilli = new Date().getTime();
        String defectId = String.valueOf(timeNowMilli);
        defectDetails.setDefectId(defectId);
        LOG.info("Generated defectId: " + defectId);
    }
    
    // Defect ID example 4W-00000001, length 11 characters
    public void generateNextDefectId(DefectDetails defectDetails) {

        String vehicleTypeSuffix = getVehicleTypeSuffix(defectDetails);

        String lastDefectId = getDefectIdOfLastCreatedDefectByVehicleType(defectDetails, vehicleTypeSuffix);

        long lastNumericDefectId = getLastDefectIdCount(lastDefectId);
        long nextNumericDefectId = lastNumericDefectId + 1;
        
        String nextDefectId = vehicleTypeSuffix + String.format("%08d", nextNumericDefectId);
        defectDetails.setDefectId(nextDefectId);
        LOG.info("Generated defectId: " + nextDefectId);
    }

    private long getLastDefectIdCount(String lastDefectId) {
        String lastNumericID = lastDefectId.trim().substring(3);
        long lastNumericDefectId;
        try {
            lastNumericDefectId = Long.parseLong(lastNumericID);
        } catch (NumberFormatException e) {
            LOG.info("Existing DefectId("+lastDefectId+") has invalid format");
            throw new InvalidDefectIdException("Existing DefectId("+lastDefectId+") has invalid format.");
        }
        return lastNumericDefectId;
    }

    private String getDefectIdOfLastCreatedDefectByVehicleType(DefectDetails defectDetails, String vehicleTypeSuffix) {
        CqnSelect cqnSelect = Select.from(DefectDetails_.class).where(a -> a.vehicleType_ID().eq(defectDetails.getVehicleTypeId()))
        .orderBy(a -> a.createdAt().desc()).limit(1, 0);
        String lastDefectId;
        try {
            DefectDetails lastDefectDetailByVehicleType = db.run(cqnSelect).single(DefectDetails.class);
            lastDefectId = lastDefectDetailByVehicleType.getDefectId();
        } catch (EmptyResultException e) {
            // DefectId of 2/4W-00000001 when no defect exist for that vehicle type
            lastDefectId = vehicleTypeSuffix + String.format("%08d", 1);
        }
        return lastDefectId;
    }

    private String getVehicleTypeSuffix(DefectDetails defectDetails) {
        CqnSelect vehicleSelect = Select.from(MstrVehicleTypes_.class).where(a -> a.ID().eq(defectDetails.getVehicleTypeId()));
        MstrVehicleTypes vehicleType = db.run(vehicleSelect).single(MstrVehicleTypes.class);
        String vehicleSuffix;
        if (vehicleType.getName().startsWith("2")) {
            vehicleSuffix = "2W-";
        } else {
            vehicleSuffix = "4W-";
        }
        return vehicleSuffix;
    }

    // Defect ID example 4W-00000001, length 11 characters
    public boolean validateDefectId(String defectId) {
        if (! (defectId.startsWith("2W-") || defectId.startsWith("4W-"))) {
            return false;
        }
        try {
            long id = Long.parseLong(defectId.substring(3));
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    public void softDeleteVersionSnapshots(String defectDetailUUID) {
        CqnSelect cqnSelect = Select.from(DefectDetailVersions_.class).where(a -> a.defectDetail_ID().eq(defectDetailUUID));
        Map<String,Object> defectDetailMap = new HashMap<>();
        defectDetailMap.put("isArchive", Boolean.TRUE);
        CqnUpdate cqnUpdate = Update.entity(DefectDetailVersions_.class).data(defectDetailMap).where(a -> a.defectDetail_ID().eq(defectDetailUUID));
        db.run(cqnUpdate);
    }

    public Result fetchSubCategory(String categoryName){
        CqnSelect cqnSelect = Select.from(MstrSubCategories_.class).where(subCat -> subCat.category().name().eq(categoryName));
        return db.run(cqnSelect);
    }
}
