package com.knpl.tss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knpl.tss.exceptions.CategorySubCategoryMismatchException;
import com.knpl.tss.exceptions.VehicleTypeMainCustomerMismatchException;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.persistence.PersistenceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cds.gen.defectservice.Products;
import cds.gen.defectservice.Products_;

import cds.gen.defectservice.MainCustomers;
import cds.gen.defectservice.MainCustomers_;

import cds.gen.defectservice.MstrVehicleTypes;
import cds.gen.defectservice.MstrVehicleTypes_;

import cds.gen.defectservice.MstrDefectTypes;
import cds.gen.defectservice.MstrDefectTypes_;

import cds.gen.defectservice.MstrCategories;
import cds.gen.defectservice.MstrCategories_;

import cds.gen.defectservice.MstrSubCategories;
import cds.gen.defectservice.MstrSubCategories_;

import cds.gen.defectservice.MstrJVs;
import cds.gen.defectservice.MstrJVs_;

import cds.gen.defectservice.MstrLayers;
import cds.gen.defectservice.MstrLayers_;

import cds.gen.defectservice.MstrTargetSubstrates;
import cds.gen.defectservice.MstrTargetSubstrates_;

import cds.gen.defectservice.MstrSubstrateMaterials;
import cds.gen.defectservice.MstrSubstrateMaterials_;

import cds.gen.defectservice.MstrPaintTypes;
import cds.gen.defectservice.MstrPaintTypes_;

import cds.gen.defectservice.MstrCrosslinkTypes;
import cds.gen.defectservice.MstrCrosslinkTypes_;

import cds.gen.defectservice.MstrExteriorInteriors;
import cds.gen.defectservice.MstrExteriorInteriors_;

import cds.gen.defectservice.MstrAtomizations;
import cds.gen.defectservice.MstrAtomizations_;

import cds.gen.defectservice.MstrElectrostatics;
import cds.gen.defectservice.MstrElectrostatics_;

import cds.gen.defectservice.MstrCompositions;
import cds.gen.defectservice.MstrCompositions_;

import cds.gen.defectservice.MstrDryingConditions;
import cds.gen.defectservice.MstrDryingConditions_;

@Service
public class MasterDataInmemoryService {
    
    private Map<String, String> productMap = new HashMap<>();
    
    private Map<String, String> mainCustomerMap = new HashMap<>();

    private Map<String, String> mstrVehicleTypeMap = new HashMap<>();

    private Map<String, String> mstrDefectTypesMap = new HashMap<>();

    private Map<String, String> mstrCategoryMap = new HashMap<>();

    private Map<String, String> mstrSubCategoriesMap = new HashMap<>();

    private Map<String, String> mstrJVsMap = new HashMap<>();

    private Map<String, String> mstrLayersMap = new HashMap<>();

    private Map<String, String> mstrTargetSubstratesMap = new HashMap<>();

    private Map<String, String> mstrSubstrateMaterialsMap = new HashMap<>();

    private Map<String, String> mstrPaintTypesMap = new HashMap<>();

    private Map<String, String> mstrCrosslinkTypesMap = new HashMap<>();

    private Map<String, String> mstrExteriorInteriorsMap = new HashMap<>();

    private Map<String, String> mstrAtomizationsMap = new HashMap<>();

    private Map<String, String> mstrElectrostaticsMap = new HashMap<>();

    private Map<String, String> mstrCompositionsMap = new HashMap<>();

    private Map<String, String> mstrDryingConditionsMap = new HashMap<>();

    List<MainCustomers> mainCustomers = new ArrayList<>();

    List<MstrSubCategories> mstrSubCategories = new ArrayList<>();
    
    @Autowired
    private PersistenceService db;

    public void loadProductInMemory() {
        CqnSelect cqnSelect = Select.from(Products_.class);
        List<Products>products = db.run(cqnSelect).listOf(Products.class);
       for(Products product: products) {
        productMap.put(product.getName(), product.getId());
       }
    }

    public Map<String, String> getInMemoryProducts() {
        return productMap;
    }

    public void loadMainCustomerInMemory() {
        CqnSelect cqnSelect = Select.from(MainCustomers_.class);
        List<MainCustomers> mainCustomers = db.run(cqnSelect).listOf(MainCustomers.class);
        this.mainCustomers = mainCustomers;
        for(MainCustomers mainCustomer: mainCustomers) {
            mainCustomerMap.put(mainCustomer.getName(), mainCustomer.getId());
        }
    }

    public Map<String, String> getInMemoryMainCustomers() {
        return mainCustomerMap;
    }

    public String getMainCustomerID(String mainCustomerName, String vehicleTypeID) {
        String mainCustomerUUID = "";
        for (MainCustomers c : this.mainCustomers) {
            if (c.getName().equals(mainCustomerName) && c.getVehicleTypeId().equals(vehicleTypeID)) {
                mainCustomerUUID =  c.getId();
                break;
            }
        }
        if (mainCustomerUUID.isEmpty()){
            throw new VehicleTypeMainCustomerMismatchException("Vehicle Type and Main Customer does not match");
        }
        return mainCustomerUUID;
    }

    public void loadMstrVehicleTypeInMemory() {
        CqnSelect cqnSelect = Select.from(MstrVehicleTypes_.class);
        List<MstrVehicleTypes> mstrVehicleTypes = db.run(cqnSelect).listOf(MstrVehicleTypes.class);
        for(MstrVehicleTypes mstrVehicleType: mstrVehicleTypes) {
            mstrVehicleTypeMap.put(mstrVehicleType.getName(), mstrVehicleType.getId());
        }
    }

    public Map<String, String> getInMemoryMstrVehicleType() {
        return mstrVehicleTypeMap;
    }

    public void loadMstrDefectTypesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrDefectTypes_.class);
        List<MstrDefectTypes> mstrDefectTypes = db.run(cqnSelect).listOf(MstrDefectTypes.class);
        for(MstrDefectTypes mstrDefectType: mstrDefectTypes) {
            mstrDefectTypesMap.put(mstrDefectType.getName(), mstrDefectType.getId());
        }
    }

    public Map<String, String> getInMemoryMstrDefectTypes() {
        return mstrDefectTypesMap;
    }

    public void loadMstrCategoriesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrCategories_.class);
        List<MstrCategories> mstrCategories = db.run(cqnSelect).listOf(MstrCategories.class);
        for(MstrCategories mstrCategory: mstrCategories) {
            mstrCategoryMap.put(mstrCategory.getName(), mstrCategory.getId());
        }
    }

    public Map<String, String> getInMemoryMstrCategories() {
        return mstrCategoryMap;
    }

    public void loadMstrSubCategoriesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrSubCategories_.class);
        List<MstrSubCategories> mstrSubCategories = db.run(cqnSelect).listOf(MstrSubCategories.class);
        this.mstrSubCategories = mstrSubCategories;
        for(MstrSubCategories mstrSubCategory: mstrSubCategories) {
            mstrSubCategoriesMap.put(mstrSubCategory.getName(), mstrSubCategory.getId());
        }
    }

    public Map<String, String> getInMemoryMstrSubCategories() {
        return mstrSubCategoriesMap;
    }

    public String getSubCategoryID(String subCategoryName, String categoryID) {
        String subCategoryUUID = "";
        for (MstrSubCategories msc : this.mstrSubCategories) {
            if (msc.getName().equals(subCategoryName) && msc.getCategoryId().equals(categoryID)) {
                subCategoryUUID =  msc.getId();
                break;
            }
        }
        if (subCategoryUUID.isEmpty()) {
            throw new CategorySubCategoryMismatchException("Category and Sub Category does not match.");
        }
        return subCategoryUUID;
    }

    public void loadMstrJVsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrJVs_.class);
        List<MstrJVs> mstrJVs = db.run(cqnSelect).listOf(MstrJVs.class);
        for(MstrJVs mstrJV: mstrJVs) {
            mstrJVsMap.put(mstrJV.getName(), mstrJV.getId());
        }
    }

    public Map<String, String> getInMemoryMstrJVs() {
        return mstrJVsMap;
    }

    public void loadMstrLayersInMemory() {
        CqnSelect cqnSelect = Select.from(MstrLayers_.class);
        List<MstrLayers> mstrLayers = db.run(cqnSelect).listOf(MstrLayers.class);
        for(MstrLayers mstrLayer: mstrLayers) {
            mstrLayersMap.put(mstrLayer.getName(), mstrLayer.getId());
        }
    }

    public Map<String, String> getInMemoryMstrLayers() {
        return mstrLayersMap;
    }

    public void loadMstrTargetSubstratesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrTargetSubstrates_.class);
        List<MstrTargetSubstrates> mstrTargetSubstrates = db.run(cqnSelect).listOf(MstrTargetSubstrates.class);
        for(MstrTargetSubstrates mstrTargetSubstrate: mstrTargetSubstrates) {
            mstrTargetSubstratesMap.put(mstrTargetSubstrate.getName(), mstrTargetSubstrate.getId());
        }
    }

    public Map<String, String> getInMemoryMstrTargetSubstrates() {
        return mstrTargetSubstratesMap;
    }

    public void loadMstrSubstrateMaterialsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrSubstrateMaterials_.class);
        List<MstrSubstrateMaterials> mstrSubstrateMaterials = db.run(cqnSelect).listOf(MstrSubstrateMaterials.class);
        for(MstrSubstrateMaterials mstrSubstrateMaterial: mstrSubstrateMaterials) {
            mstrSubstrateMaterialsMap.put(mstrSubstrateMaterial.getName(), mstrSubstrateMaterial.getId());
        }
    }

    public Map<String, String> getInMemoryMstrSubstrateMaterials() {
        return mstrSubstrateMaterialsMap;
    }

    public void loadMstrPaintTypesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrPaintTypes_.class);
        List<MstrPaintTypes> mstrPaintTypes = db.run(cqnSelect).listOf(MstrPaintTypes.class);
        for(MstrPaintTypes mstrPaintType: mstrPaintTypes) {
            mstrPaintTypesMap.put(mstrPaintType.getName(), mstrPaintType.getId());
        }
    }

    public Map<String, String> getInMemoryMstrPaintTypes() {
        return mstrPaintTypesMap;
    }

    public void loadMstrCrosslinkTypesInMemory() {
        CqnSelect cqnSelect = Select.from(MstrCrosslinkTypes_.class);
        List<MstrCrosslinkTypes> mstrCrosslinkTypes = db.run(cqnSelect).listOf(MstrCrosslinkTypes.class);
        for(MstrCrosslinkTypes mstrCrosslinkType: mstrCrosslinkTypes) {
            mstrCrosslinkTypesMap.put(mstrCrosslinkType.getName(), mstrCrosslinkType.getId());
        }
    }

    public Map<String, String> getInMemoryMstrCrosslinkTypes() {
        return mstrCrosslinkTypesMap;
    }

    public void loadMstrExteriorInteriorsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrExteriorInteriors_.class);
        List<MstrExteriorInteriors> mstrExteriorInteriors = db.run(cqnSelect).listOf(MstrExteriorInteriors.class);
        for(MstrExteriorInteriors mstrExteriorInterior: mstrExteriorInteriors) {
            mstrExteriorInteriorsMap.put(mstrExteriorInterior.getName(), mstrExteriorInterior.getId());
        }
    }

    public Map<String, String> getInMemoryMstrExteriorInteriors() {
        return mstrExteriorInteriorsMap;
    }

    public void loadMstrAtomizationsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrAtomizations_.class);
        List<MstrAtomizations> mstrAtomizations = db.run(cqnSelect).listOf(MstrAtomizations.class);
        for(MstrAtomizations mstrAtomization: mstrAtomizations) {
            mstrAtomizationsMap.put(mstrAtomization.getName(), mstrAtomization.getId());
        }
    }

    public Map<String, String> getInMemoryMstrAtomizations() {
        return mstrAtomizationsMap;
    }

    public void loadMstrElectrostaticsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrElectrostatics_.class);
        List<MstrElectrostatics> mstrElectrostatics = db.run(cqnSelect).listOf(MstrElectrostatics.class);
        for(MstrElectrostatics mstrElectrostatic: mstrElectrostatics) {
            mstrElectrostaticsMap.put(mstrElectrostatic.getName(), mstrElectrostatic.getId());
        }
    }

    public Map<String, String> getInMemoryMstrElectrostatics() {
        return mstrElectrostaticsMap;
    }

    public void loadMstrCompositionsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrCompositions_.class);
        List<MstrCompositions> mstrCompositions = db.run(cqnSelect).listOf(MstrCompositions.class);
        for(MstrCompositions mstrComposition: mstrCompositions) {
            mstrCompositionsMap.put(mstrComposition.getName(), mstrComposition.getId());
        }
    }

    public Map<String, String> getInMemoryMstrCompositions() {
        return mstrCompositionsMap;
    }

    public void loadMstrDryingConditionsInMemory() {
        CqnSelect cqnSelect = Select.from(MstrDryingConditions_.class);
        List<MstrDryingConditions> mstrDryingConditions = db.run(cqnSelect).listOf(MstrDryingConditions.class);
        for(MstrDryingConditions mstrDryingCondition: mstrDryingConditions) {
            mstrDryingConditionsMap.put(mstrDryingCondition.getName(), mstrDryingCondition.getId());
        }
    }

    public Map<String, String> getInMemoryMstrDryingConditions() {
        return mstrDryingConditionsMap;
    }

    
}
