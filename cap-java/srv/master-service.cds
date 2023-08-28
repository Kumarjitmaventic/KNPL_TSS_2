using knpl.tss as knpl_tss from '../db/data-model';

@(requires: 'authenticated-user')
service MasterService {
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])    
    entity MstrUserTypes                 as projection on knpl_tss.MstrUserTypes;


    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrVehicleTypes              as projection on knpl_tss.MstrVehicleTypes;


    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrDefectTypes               as projection on knpl_tss.MstrDefectTypes;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrData                      as projection on knpl_tss.MstrData;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrCategories                as projection on knpl_tss.MstrCategories;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrSubCategories             as projection on knpl_tss.MstrSubCategories;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrJVs                       as projection on knpl_tss.MstrJVs;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrDocumentCategories        as projection on knpl_tss.MstrDocumentCategories;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrLayers                    as projection on knpl_tss.MstrLayers;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrTargetSubstrates          as projection on knpl_tss.MstrTargetSubstrates;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrSubstrateMaterials        as projection on knpl_tss.MstrSubstrateMaterials;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrPaintTypes                as projection on knpl_tss.MstrPaintTypes;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrCrosslinkTypes            as projection on knpl_tss.MstrCrosslinkTypes;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrExteriorInteriors         as projection on knpl_tss.MstrExteriorInteriors;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrAtomizations              as projection on knpl_tss.MstrAtomizations;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrElectrostatics            as projection on knpl_tss.MstrElectrostatics;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrCompositions              as projection on knpl_tss.MstrCompositions;
    
    
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ]) 
    entity MstrDryingConditions          as projection on knpl_tss.MstrDryingConditions;
}