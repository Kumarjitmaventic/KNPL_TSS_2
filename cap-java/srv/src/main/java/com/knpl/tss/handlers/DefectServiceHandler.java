package com.knpl.tss.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.knpl.tss.exceptions.UUIDNotFoundException;
import com.knpl.tss.services.DefectService;
import com.knpl.tss.utilities.Utility;
import com.sap.cds.CdsData;
import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.Struct;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;

import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsDeleteEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.services.request.ParameterInfo;

import org.hibernate.annotations.SourceType;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.defectservice.DefectDetails;
import cds.gen.defectservice.DefectDetails_;
import cds.gen.defectservice.DefectService_;
import cds.gen.defectservice.GetProductsBySubCategoryAndVehicleType;
import cds.gen.defectservice.GetProductsBySubCategoryAndVehicleType_;
import cds.gen.defectservice.MstrSubCategories;
import cds.gen.defectservice.MstrSubCategories_;
import com.sap.cds.ql.cqn.CqnSelect;



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

    // @On(event = CdsService.EVENT_READ, entity = "DefectService.GetProductsBySubCategoryAndVehicleType")
    // public void onDefectDetailsCompanyRead(CdsReadEventContext context){
    //     // System.out.println("its DefectDetailsCompany DefectDetailsCompany DefectDetailsCompany");
    //     ParameterInfo parameterInfo = context.getParameterInfo();
    //     try{
    //         String query= parameterInfo.getQueryParams().get("$filter");
        
    //         int indexVehicle_type_start = query.indexOf("'",query.indexOf("vehicleType_name eq '")) + 1;
    //         int indexVehicle_type_end = query.indexOf("'",indexVehicle_type_start);

    //         int indexCategory_start = query.indexOf(" ",query.indexOf("category_ID eq ")) + 4;
    //         int indexCategory_end = query.indexOf(" ",indexCategory_start);

    //         int indexSubCategory_start = query.indexOf(" ",query.indexOf("subCategory_ID eq ")) + 4;
    //         int indexSubCategory_end = query.indexOf(" ",indexSubCategory_start);

    //         int indexKeyWord_start = query.indexOf("'",query.indexOf("(cust_name),'"))+1;
    //         int indexKeyWord_end = query.indexOf("'",indexKeyWord_start);

    //         String vehicle_type = query.substring(indexVehicle_type_start,indexVehicle_type_end);
    //         String category = query.substring(indexCategory_start,indexCategory_end);
    //         String subCategory = query.substring(indexSubCategory_start,indexSubCategory_end);
    //         String keyWord = query.substring(indexKeyWord_start,indexKeyWord_end);
    //     }
    //     catch (Exception e){
    //         // LOG.warn("No filter");
    //     }
    // }

    @After(event = CdsService.EVENT_READ, entity = "DefectService.GetProductsBySubCategoryAndVehicleTypeCopy")
    public void afterDefectDetailsCompanyRead(CdsReadEventContext context){
        Result defct = context.getResult();
        try{
            List<Row> copyResult = defct.list();
            String previousID = "";
            String name = "";
            String code ="";
            String vechicleType_ID = "";
            String vechicleType_name = "";
            String category_ID = "";
            String subCategory_ID = "";
            String customerName = "";
            for(int i = 0;i<copyResult.size();i++){


                
                if( previousID.equals(copyResult.get(i).get("ID").toString())                       &&
                    name.equals(copyResult.get(i).get("name").toString())                           &&
                    code.equals(copyResult.get(i).get("code").toString())                           &&
                    vechicleType_ID.equals(copyResult.get(i).get("vehicleType_ID").toString())      &&
                    vechicleType_name.equals(copyResult.get(i).get("vehicleType_name").toString())  &&
                    category_ID.equals(copyResult.get(i).get("category_ID").toString())             &&
                    subCategory_ID.equals(copyResult.get(i).get("subCategory_ID").toString())       &&
                    customerName.equals(copyResult.get(i).get("cust_name").toString())
                ){
                    copyResult.remove(i);
                    --i;
                }
                else{
                    previousID = copyResult.get(i).get("ID").toString();
                    name = copyResult.get(i).get("name").toString();
                    code = copyResult.get(i).get("code").toString();
                    vechicleType_ID = copyResult.get(i).get("vehicleType_ID").toString();
                    vechicleType_name = copyResult.get(i).get("vehicleType_name").toString();
                    category_ID = copyResult.get(i).get("category_ID").toString();
                    subCategory_ID = copyResult.get(i).get("subCategory_ID").toString();
                    customerName = copyResult.get(i).get("cust_name").toString();
                }
            }
            previousID ="";
            context.setResult(copyResult);
        }
        catch(Exception e){
            LOG.error(e.getMessage());
        }
        // List<Row> copyRes = defct.list();

        // for(int i=0;i<defct.list().size();i++){
        //     System.out.println(copyRes.get(i));
        // }

        // LOG.info("List");
        // System.out.println(defct.list());
        // // LOG.info("List");
        // // System.out.println(defct.list().get(0));

        // for(int i=0;i<defct.list().size();i++){
        //     LOG.info("List" + Integer.toString(i));
        //     System.out.println(defct.list().get(i));
        // }
        

        // defct.forEach((row) -> {
            
        //     String Id = row.get("ID").toString();
        //     try{
                
        //         if(defectService.GetIdName().equals(Id)){
        //             System.out.print("TRUEEEEEEEEEEEEEEEEEEE");
                    
        //             defct.list().remove(defectService.getCount());
        //         }
        //         else{
        //             defectService.SetIdName(Id);
        //         } 
        //     }
        //     catch (Exception e){
        //         LOG.info("Error");
        //     }   
        //     defectService.SetCount();
        // });
        // defectService.SetCountDefault();
        // LOG.info("Updated List : ");
        // System.out.print(defct);
        // context.setResult(defct);
        // defectService.SetIdName("");
    }

    @After(event = CdsService.EVENT_READ, entity = "DefectService.DefectDetailsListUnderSubCategory")
    public void afterDefectDetailsListUnderSubCategory(CdsReadEventContext context){
        Result defctList = context.getResult();
        try{
            List<Row> copyResult = defctList.list();
            String previousID = "";
            for(int i = 0;i<copyResult.size();i++){
                if(previousID.equals(copyResult.get(i).get("ID").toString())){
                    copyResult.remove(i);
                    --i;    // after removing next value row index will same as current so to check that staying current position
                }
                else{
                    previousID = copyResult.get(i).get("ID").toString();
                }
            }
            previousID ="";
            context.setResult(copyResult);
        }
        catch(Exception e){
            LOG.error(e.getMessage());
        }
    }
}