package com.knpl.tss.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knpl.tss.MessageKeys;
import com.knpl.tss.constants.CsvFileHeader;
import com.knpl.tss.exceptions.InvalidCsvHeaderException;
import com.knpl.tss.exceptions.InvalidDateFormatException;
import com.knpl.tss.exceptions.InvalidDefectIdException;
import com.knpl.tss.exceptions.MasterDataNotMatchedException;
import com.knpl.tss.exceptions.VehicleTypeMainCustomerMismatchException;
import com.knpl.tss.exceptions.BlankColumnException;
import com.knpl.tss.exceptions.CategorySubCategoryMismatchException;
import com.knpl.tss.services.DefectFileService;
import com.knpl.tss.services.MasterDataInmemoryService;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;


import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.cds.CdsUpdateEventContext;

import com.sap.cds.services.handler.EventHandler;

import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.defectfileservice.DefectFileService_;
import cds.gen.defectfileservice.UploadDefectFiles;

import cds.gen.defectservice.DefectDetails;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServiceName(DefectFileService_.CDS_NAME)
public class DefectFileServiceHandler implements EventHandler {
    
    private final static Logger LOG = LoggerFactory.getLogger(DefectFileServiceHandler.class);  
    
    @Autowired
    private DefectFileService defectFileService;

    @Autowired
    private MasterDataInmemoryService mstrDataInmemoryService;
    
    @On(event = {CdsService.EVENT_UPDATE}, entity = "DefectFileService.UploadDefectFiles")
	public void uploadDefectCSVFile(CdsUpdateEventContext uploadDefectFileContext, UploadDefectFiles uploadDefectFiles) {
        LOG.info("Uploading Defect Entries...");
        
        mstrDataInmemoryService.loadProductInMemory();
        mstrDataInmemoryService.loadMainCustomerInMemory();
        mstrDataInmemoryService.loadMstrVehicleTypeInMemory();
        mstrDataInmemoryService.loadMstrDefectTypesInMemory();
        mstrDataInmemoryService.loadMstrCategoriesInMemory();
        mstrDataInmemoryService.loadMstrSubCategoriesInMemory();
        mstrDataInmemoryService.loadMstrJVsInMemory();
        mstrDataInmemoryService.loadMstrLayersInMemory();
        mstrDataInmemoryService.loadMstrExteriorInteriorsInMemory();
        mstrDataInmemoryService.loadMstrTargetSubstratesInMemory();
        mstrDataInmemoryService.loadMstrSubstrateMaterialsInMemory();
        mstrDataInmemoryService.loadMstrPaintTypesInMemory();
        mstrDataInmemoryService.loadMstrCrosslinkTypesInMemory();
        mstrDataInmemoryService.loadMstrAtomizationsInMemory();
        mstrDataInmemoryService.loadMstrElectrostaticsInMemory();
        mstrDataInmemoryService.loadMstrCompositionsInMemory();
        mstrDataInmemoryService.loadMstrDryingConditionsInMemory();

        defectFileService.intialLoadDefectIds();

        InputStream is = uploadDefectFiles.getContent();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                //Read header of the CSV file
                String headerLine = br.readLine();
                if(headerLine == null) {
                    throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.DEFECT_IMPORT_BLANK_FILE);
                }
                String []headers = headerLine.split(";");
                defectFileService.valiadateHeader(headers);

                AtomicInteger rowNumber = new AtomicInteger(1);
                AtomicBoolean isExceptionOccurred = new AtomicBoolean();
                AtomicBoolean hasDataRows = new AtomicBoolean();
                StringBuilder strBuilder = new StringBuilder();
                br.lines().forEach((line) -> {
                    hasDataRows.set(true);
					String[] columnsValues = line.split(";");
                    try {
                        rowNumber.getAndIncrement();

                        //Blank check
                        defectFileService.blankCheck(rowNumber.get(), columnsValues);

                        DefectDetails defectDetails = this.populateCSVLinetoPojo(rowNumber.get(), columnsValues);

                        //Match master data
                        defectFileService.validateMasterDataValue(rowNumber.get(), defectDetails);
                        // Validate DefectID format
                        defectFileService.validateDefectId(rowNumber.get(), defectDetails);
                        defectFileService.validateIncidentDateRecordDate(rowNumber.get(), defectDetails);
                        
                        defectFileService.createOrUpdateDefectEntry(uploadDefectFileContext, defectDetails);

                    } catch (BlankColumnException | MasterDataNotMatchedException | InvalidDateFormatException | InvalidDefectIdException e) {
                        //LOG.error(e.getMessage());
                        strBuilder.append(e.getMessage());
                        strBuilder.append("\n ");
                        isExceptionOccurred.set(Boolean.TRUE);
                    } catch (VehicleTypeMainCustomerMismatchException | CategorySubCategoryMismatchException e) {
                        strBuilder.append("Row ").append(rowNumber.get()).append(" ").append(e.getMessage());
                        strBuilder.append("\n ");
                        isExceptionOccurred.set(Boolean.TRUE);
                    }
                });
                if (isExceptionOccurred.get()){
                    throw new ServiceException(ErrorStatuses.SERVER_ERROR, strBuilder.toString());
                }
                if (hasDataRows.get() == false) {
                    throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.DEFECT_IMPORT_NO_ROWS);
                }
                LOG.info("Defect CSV file uploaded successfully");
            } catch (IOException e) {
				throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.DEFECT_IMPORT_INVALID_CSV, e);
			} catch (IndexOutOfBoundsException e) {
				throw new ServiceException(ErrorStatuses.SERVER_ERROR, e.getMessage());
			} catch (InvalidCsvHeaderException e) {
                throw new ServiceException(ErrorStatuses.SERVER_ERROR, e.getMessage());
            }
        }

        
        uploadDefectFileContext.setCompleted();
        uploadDefectFileContext.setResult(Arrays.asList(uploadDefectFiles));
    }

    private DefectDetails populateCSVLinetoPojo(int rowNumber, String[] p) throws InvalidDateFormatException{
        DefectDetails defectDetails =  DefectDetails.create();
        defectDetails.setDefectId(p[0]);
        defectDetails.setVehicleTypeId(p[1]);
        defectDetails.setProductId(p[2]);
        //defectDetails.setDefectTypeId(p[3]);
        defectDetails.setTitle(p[3]);
        defectDetails.setProblemDescription(p[4]);
        defectDetails.setCauseOfProblem(p[5]);
        defectDetails.setCountermeasureTaken(p[6]);
        try {
            defectDetails.setIncidentDate(LocalDate.parse(p[7]));
        } catch(DateTimeParseException e) {
            throw new InvalidDateFormatException("Row "+rowNumber+" Column 8 has Invalid Date("+CsvFileHeader.HEADER[8]+"). Expected YYYY-MM-DD format at " + e.getParsedString());
        }
        try {
            defectDetails.setRecordDate(LocalDate.parse(p[8]));
        } catch(DateTimeParseException e) {
            throw new InvalidDateFormatException("Row "+rowNumber+" Column 9 has Invalid Date("+CsvFileHeader.HEADER[9]+"). Expected YYYY-MM-DD format at " + e.getParsedString());

        }
        defectDetails.setCategoryId(p[9]);
        defectDetails.setSubCategoryId(p[10]);
        defectDetails.setJvId(p[11]);
        defectDetails.setMainCustomerId(p[12]);
        defectDetails.setLayerId(p[13]);
        defectDetails.setTargetSubstrateId(p[14]);
        defectDetails.setSubstrateMaterialId(p[15]);
        defectDetails.setPaintTypeId(p[16]);
        defectDetails.setCrossLinkTypeId(p[17]);
        defectDetails.setExteriorInteriorId(p[18]);
        defectDetails.setAtomizationId(p[19]); // Application 1
        defectDetails.setElectrostaticId(p[20]); // Application 2
        defectDetails.setCompositionId(p[21]); // Application 3
        defectDetails.setDryingConditionId(p[22]);
        return defectDetails;
    }
}
