package com.knpl.tss.services;

import java.util.Arrays;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.knpl.tss.constants.CsvFileHeader;
import com.knpl.tss.exceptions.InvalidCsvHeaderException;
import com.knpl.tss.exceptions.InvalidDefectIdException;
import com.knpl.tss.exceptions.MasterDataNotMatchedException;
import com.knpl.tss.exceptions.VehicleTypeMainCustomerMismatchException;
import com.knpl.tss.exceptions.BlankColumnException;
import com.knpl.tss.exceptions.CategorySubCategoryMismatchException;
import com.knpl.tss.exceptions.IncidentDateRecordDateConstraintException;
import com.sap.cds.ql.Update;

import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CdsCreateEventContext;

import com.sap.cds.services.persistence.PersistenceService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cds.gen.defectservice.DefectDetails;
import cds.gen.defectservice.DefectDetails_;

import com.sap.cds.ql.Upsert;
import java.util.List;

import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.Insert;

@Service
public class DefectFileService {

    private final static Logger LOG = LoggerFactory.getLogger(DefectFileService.class);

    @Autowired
    private PersistenceService db;

    @Autowired
    private MasterDataInmemoryService mstrDataInmemoryService;

    @Autowired    
    private DefectService defectService;

    private List<String> defectIds;

    public void valiadateHeader(String[] headers) throws InvalidCsvHeaderException {
        LOG.info("Validating header...");
        StringBuilder strBuilder = new StringBuilder("Row 1: Column(s) are missing ");
        Set<String> sourceArray = new HashSet<String>(Arrays.asList(CsvFileHeader.HEADER));
        Set<String> targetArray = new HashSet<String>(Arrays.asList(headers));

        sourceArray.removeAll(targetArray);
        if (!sourceArray.isEmpty()) {
            strBuilder.append(sourceArray);
            throw new InvalidCsvHeaderException(strBuilder.toString());
        }
    }

    public void blankCheck(int rowNumber, String[] columnValues) throws BlankColumnException {
        LOG.info("Validating row...");
        StringBuilder strBuilder = new StringBuilder("Row " + rowNumber + " Column(s) contain blank or empty values ");
        int index = 0;
        Set<String> columnNames = new HashSet<>();
        for (String value : columnValues) {
            if (StringUtils.isBlank(value)) {
                columnNames.add(CsvFileHeader.HEADER[index]);
            }
            index++;
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new BlankColumnException(strBuilder.toString());
        }
        if (columnValues.length < CsvFileHeader.HEADER.length) {
            strBuilder.append("[");
            for (int remainingIndex = index; remainingIndex < CsvFileHeader.HEADER.length; remainingIndex++) {
                strBuilder.append(CsvFileHeader.HEADER[remainingIndex]).append(", ");
            }
            strBuilder.append("]");
            throw new BlankColumnException(strBuilder.toString());
        }
    }

    public void validateMasterDataValue(int rowNumber, DefectDetails defectDetails)
            throws MasterDataNotMatchedException, CategorySubCategoryMismatchException, VehicleTypeMainCustomerMismatchException{
        LOG.info("Validating master data...");
        StringBuilder strBuilder = new StringBuilder("Row " + rowNumber + " Column(s) contain value that is not matching with master data ");
       
        Set<String> columnNames = new HashSet<>();
        //Product Master
        Map<String, String> productMap = mstrDataInmemoryService.getInMemoryProducts();
        if (!mstrDataInmemoryService.getInMemoryProducts().containsKey(defectDetails.getProductId())) {
            columnNames.add(CsvFileHeader.HEADER[2]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setProductId(productMap.get(defectDetails.getProductId()));
        }
        
        //Mstr vehicle Type
        Map<String, String> mstrVehicleTypetMap = mstrDataInmemoryService.getInMemoryMstrVehicleType();
        if (!mstrVehicleTypetMap.containsKey(defectDetails.getVehicleTypeId())) {
            columnNames.add(CsvFileHeader.HEADER[1]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setVehicleTypeId(mstrVehicleTypetMap.get(defectDetails.getVehicleTypeId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        // Main Customer
        Map<String, String> mainCustomerMap = mstrDataInmemoryService.getInMemoryMainCustomers();
        if (!mainCustomerMap.containsKey(defectDetails.getMainCustomerId())) {
            columnNames.add(CsvFileHeader.HEADER[12]);
        } else {
            // Replace value from UUID of Master data, also check mapping with VehicleType
            //defectDetails.setMainCustomerId(mainCustomerMap.get(defectDetails.getMainCustomerId()));
            String mainCustomerName = defectDetails.getMainCustomerId();
            String mainCustomerID = mstrDataInmemoryService.getMainCustomerID(mainCustomerName, defectDetails.getVehicleTypeId());
            defectDetails.setMainCustomerId(mainCustomerID);
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr Defect Type
        // Map<String, String> mstrDefectTypetMap = mstrDataInmemoryService.getInMemoryMstrDefectTypes();
        // if (!mstrDefectTypetMap.containsKey(defectDetails.getDefectTypeId())) {
        //     columnNames.add(CsvFileHeader.HEADER[3]);
        // } else {
        //     // Replace value from UUID of Master data
        //     defectDetails.setDefectTypeId(mstrDefectTypetMap.get(defectDetails.getDefectTypeId()));
        // }

        // if (!columnNames.isEmpty()) {
        //     strBuilder.append(columnNames);
        //     throw new MasterDataNotMatchedException(strBuilder.toString());
        // }

        //Mstr categories
        Map<String, String> mstrCategoryMap = mstrDataInmemoryService.getInMemoryMstrCategories();;
        if (!mstrCategoryMap.containsKey(defectDetails.getCategoryId())) {
            columnNames.add(CsvFileHeader.HEADER[9]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setCategoryId(mstrCategoryMap.get(defectDetails.getCategoryId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }
        //Mstr sub categories
        Map<String, String> mstrSubCategoryMap = mstrDataInmemoryService.getInMemoryMstrSubCategories();
        if (!mstrSubCategoryMap.containsKey(defectDetails.getSubCategoryId())) {
            columnNames.add(CsvFileHeader.HEADER[10]);
        } else {
            // Replace value from UUID of Master data, also check mapping with MstrCategory.
            //defectDetails.setSubCategoryId(mstrSubCategoryMap.get(defectDetails.getSubCategoryId()));
            String subCategoryName = defectDetails.getSubCategoryId();
            String subCategoryID = mstrDataInmemoryService.getSubCategoryID(subCategoryName, defectDetails.getCategoryId());
            defectDetails.setSubCategoryId(subCategoryID);
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr JVs
        Map<String, String> mstrJVMap = mstrDataInmemoryService.getInMemoryMstrJVs();
        if (!mstrJVMap.containsKey(defectDetails.getJvId())) {
            columnNames.add(CsvFileHeader.HEADER[11]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setJvId(mstrJVMap.get(defectDetails.getJvId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr Layers
        Map<String, String> mstrLayersMap = mstrDataInmemoryService.getInMemoryMstrLayers();
        if (!mstrLayersMap.containsKey(defectDetails.getLayerId())) {
            columnNames.add(CsvFileHeader.HEADER[13]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setLayerId(mstrLayersMap.get(defectDetails.getLayerId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrTargetSubstrates
        Map<String, String> mstrTargetSubstratesMap = mstrDataInmemoryService.getInMemoryMstrTargetSubstrates();
        if (!mstrTargetSubstratesMap.containsKey(defectDetails.getTargetSubstrateId())) {
            columnNames.add(CsvFileHeader.HEADER[14]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setTargetSubstrateId(mstrTargetSubstratesMap.get(defectDetails.getTargetSubstrateId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrSubstrateMaterials
        Map<String, String> mstrSubstrateMaterialsMap = mstrDataInmemoryService.getInMemoryMstrSubstrateMaterials();
        if (!mstrSubstrateMaterialsMap.containsKey(defectDetails.getSubstrateMaterialId())) {
            columnNames.add(CsvFileHeader.HEADER[15]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setSubstrateMaterialId(mstrSubstrateMaterialsMap.get(defectDetails.getSubstrateMaterialId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrPaintTypes
        Map<String, String> mstrPaintTypesMap = mstrDataInmemoryService.getInMemoryMstrPaintTypes();
        if (!mstrPaintTypesMap.containsKey(defectDetails.getPaintTypeId())) {
            columnNames.add(CsvFileHeader.HEADER[16]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setPaintTypeId(mstrPaintTypesMap.get(defectDetails.getPaintTypeId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrCrosslinkTypes
        Map<String, String> mstrCrosslinkTypesMap = mstrDataInmemoryService.getInMemoryMstrCrosslinkTypes();
        if (!mstrCrosslinkTypesMap.containsKey(defectDetails.getCrossLinkTypeId())) {
            columnNames.add(CsvFileHeader.HEADER[17]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setCrossLinkTypeId(mstrCrosslinkTypesMap.get(defectDetails.getCrossLinkTypeId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrExteriorInteriors
        Map<String, String> mstrExteriorInteriorsMap = mstrDataInmemoryService.getInMemoryMstrExteriorInteriors();
        
        if (!mstrExteriorInteriorsMap.containsKey(defectDetails.getExteriorInteriorId())) {
            columnNames.add(CsvFileHeader.HEADER[18]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setExteriorInteriorId(mstrExteriorInteriorsMap.get(defectDetails.getExteriorInteriorId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrAtomizations
        Map<String, String> mstrAtomizationsMap = mstrDataInmemoryService.getInMemoryMstrAtomizations();
        if (!mstrAtomizationsMap.containsKey(defectDetails.getAtomizationId())) {
            columnNames.add(CsvFileHeader.HEADER[19]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setAtomizationId(mstrAtomizationsMap.get(defectDetails.getAtomizationId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrElectrostatics
        Map<String, String> mstrElectrostaticsMap = mstrDataInmemoryService.getInMemoryMstrElectrostatics();
        if (!mstrElectrostaticsMap.containsKey(defectDetails.getElectrostaticId())) {
            columnNames.add(CsvFileHeader.HEADER[20]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setElectrostaticId(mstrElectrostaticsMap.get(defectDetails.getElectrostaticId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrCompositions
        Map<String, String> mstrCompositionsMap = mstrDataInmemoryService.getInMemoryMstrCompositions();
        if (!mstrCompositionsMap.containsKey(defectDetails.getCompositionId())) {
            columnNames.add(CsvFileHeader.HEADER[21]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setCompositionId(mstrCompositionsMap.get(defectDetails.getCompositionId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }

        //Mstr MstrDryingConditions
        Map<String, String> mstrDryingConditionsMap = mstrDataInmemoryService.getInMemoryMstrDryingConditions();
        if (!mstrDryingConditionsMap.containsKey(defectDetails.getDryingConditionId())) {
            columnNames.add(CsvFileHeader.HEADER[22]);
        } else {
            // Replace value from UUID of Master data
            defectDetails.setDryingConditionId(mstrDryingConditionsMap.get(defectDetails.getDryingConditionId()));
        }

        if (!columnNames.isEmpty()) {
            strBuilder.append(columnNames);
            throw new MasterDataNotMatchedException(strBuilder.toString());
        }
    }

    public void validateDefectId(int rowNumber, DefectDetails defectDetails) {
        if (! defectService.validateDefectId(defectDetails.getDefectId())){
            throw new InvalidDefectIdException("Row "+ rowNumber+" Invalid DefectId found. Should be of format 2W-00000001 or 4W-00000001");
        }
    }

    public void validateIncidentDateRecordDate(int rowNumber, DefectDetails defectDetails) {
        if (defectDetails.getIncidentDate().isAfter(defectDetails.getRecordDate())){
            throw new IncidentDateRecordDateConstraintException("Row "+ rowNumber+" Incident Date Should be before Record Date");
        }
    }

    public void createOrUpdateDefectEntry(CdsUpdateEventContext uploadDefectFileContext, DefectDetails defectDetails) {
        // Separate transaction per line
        if(this.getDefectIds().contains(defectDetails.getDefectId())) {
            defectService.processVersionSnapshot(defectDetails);
            uploadDefectFileContext.getCdsRuntime().changeSetContext().run(ctx -> {
                CqnUpdate update = Update.entity(DefectDetails_.class).data(defectDetails).where(p -> p.get("defectId").eq(defectDetails.getDefectId()));
                db.run(update);
            });
        } else {
            uploadDefectFileContext.getCdsRuntime().changeSetContext().run(ctx -> {
                CqnInsert insert = Insert.into(DefectDetails_.class).entry(defectDetails);
                db.run(insert);
            });
            defectIds.add(defectDetails.getDefectId());
        }
    }

    public List<String> getDefectIds() {
        return defectIds;
    }

    public void intialLoadDefectIds() {
        this.defectIds = defectService.getAllNotArchivedDefectId();
    }
}