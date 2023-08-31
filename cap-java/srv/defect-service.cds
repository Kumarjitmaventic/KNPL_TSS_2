using knpl.tss as knpl_tss from '../db/data-model';

// @(requires: 'authenticated-user')
service DefectService {

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    // ])
    view GetProductsBySubCategoryAndVehicleType as select from DefectDetails {key product.ID as ID, product.name, product.code, 
            category.ID as category_ID, subCategory.ID as subCategory_ID, vehicleType.ID as vehicleType_ID, vehicleType.name as vehicleType_name,
            mainCustomer.name as cust_name }
            group by product.ID, product.name, product.code, vehicleType.ID, vehicleType.name, category.ID, subCategory.ID, mainCustomer.name;

    @cds.redirection.target : false
    view GetProductsBySubCategoryAndVehicleTypeCopy as select from DefectDetails {key product.ID as ID, product.name, product.code, 
            category.ID as category_ID, subCategory.ID as subCategory_ID, vehicleType.ID as vehicleType_ID, vehicleType.name as vehicleType_name,
            mainCustomer.name as cust_name, JV.name as JV_name, problemDescription,targetSubstrate.name as targetSubstrate,substrateMaterial.name as substrateMaterial }
            group by product.ID, product.name, product.code, vehicleType.ID, vehicleType.name, category.ID, subCategory.ID, mainCustomer.name,JV.name,problemDescription,substrateMaterial.name,targetSubstrate.name order by ID asc;

    @cds.redirection.target
    // @(restrict: [         
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    // ])
    entity Products                      as projection on knpl_tss.Products;


    @odata.draft.enabled
    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ', 'CREATE', 'UPDATE','DELETE'], to: 'MANAGE_ADMIN'},
    // ])
    entity DefectDetails                 as projection on knpl_tss.DefectDetails where isArchive = false;

    entity DefectDetailsListUnderSubCategory as projection on knpl_tss.DefectDetailsListUnderSubCategory;


    // @(restrict: [ 
    //     {grant: ['READ', 'CREATE', 'UPDATE','DELETE'], to: 'MANAGE_ADMIN'},
    // ])
    entity DefectDetailVersions          as projection on knpl_tss.DefectDetailVersions order by version desc;


    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MainCustomers                 as projection on knpl_tss.MainCustomers;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrUserTypes                 as projection on knpl_tss.MstrUserTypes;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrVehicleTypes              as projection on knpl_tss.MstrVehicleTypes;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrDefectTypes               as projection on knpl_tss.MstrDefectTypes;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrData                      as projection on knpl_tss.MstrData;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrCategories                as projection on knpl_tss.MstrCategories;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrSubCategories             as projection on knpl_tss.MstrSubCategories;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrJVs                       as projection on knpl_tss.MstrJVs;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrDocumentCategories        as projection on knpl_tss.MstrDocumentCategories;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrLayers                    as projection on knpl_tss.MstrLayers;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrTargetSubstrates          as projection on knpl_tss.MstrTargetSubstrates;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrSubstrateMaterials        as projection on knpl_tss.MstrSubstrateMaterials;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrPaintTypes                as projection on knpl_tss.MstrPaintTypes;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrCrosslinkTypes            as projection on knpl_tss.MstrCrosslinkTypes;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrExteriorInteriors         as projection on knpl_tss.MstrExteriorInteriors;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrAtomizations              as projection on knpl_tss.MstrAtomizations;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrElectrostatics            as projection on knpl_tss.MstrElectrostatics;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrCompositions              as projection on knpl_tss.MstrCompositions;
    

    // @(restrict: [ 
    //     {grant: ['READ'], to: 'READ_TSS_MOBILE'},
    //     {grant: ['READ'], to: 'MANAGE_ADMIN'},
    // ])
    entity MstrDryingConditions          as projection on knpl_tss.MstrDryingConditions;

}