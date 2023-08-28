namespace knpl.tss;

using {
    cuid,
    managed
} from '@sap/cds/common';

entity Users : cuid, managed {
    key ID       : UUID                             @UI.Hidden  @UI.HiddenFilter;
        userType : Association to one MstrUserTypes @title : 'User Type'; //@UI.Hidden  @UI.HiddenFilter;
        name     : String(30)                       @title : 'Name';
        email    : String(50)                       @title : 'Email';
}

entity Products : cuid, managed {
        code          : String(120)@title : 'Product Code';
        name          : String(120)@title : 'Product Name';
        defectDetails : Association to many DefectDetails
                            on defectDetails.product = $self; // @UI.Hidden @UI.HiddenFilter;
}

entity DefectDetails : managed {
    key ID                  : UUID                                      @Core.Computed  @UI.Hidden  @UI.HiddenFilter;
        product             : Association to one Products               @assert.notNull;
        vehicleType         : Association to one MstrVehicleTypes       @assert.notNull;
        mainCustomer        : Association to one MainCustomers          @assert.notNull;
        // defectType          : Association to one MstrDefectTypes        @assert.notNull;
        title               : String(300)                               @Core.Immutable  @mandatory  @assert.notNull  @title : 'Title';
        problemDescription  : String(1000)                               @mandatory  @assert.notNull  @title :                  'Problem Description'  @UI.MultiLineText;
        causeOfProblem      : String(1000)                               @mandatory  @assert.notNull  @title :                  'Cause of Problem'  @UI.MultiLineText;
        countermeasureTaken : String(1000)                               @mandatory  @title :                                   'Countermeasures Taken'  @UI.MultiLineText;
        incidentDate : Date                                             @title :                                               'Incident Date'  @mandatory  @assert.notNull;
        recordDate   : Date                                             @title :'Record Date'  @mandatory  @assert.notNull;
        category            : Association to one MstrCategories         @title :                                               'Category'  @assert.notNull;
        subCategory         : Association to one MstrSubCategories      @title :                                               'Sub Category'  @assert.notNull;
        JV                  : Association to one MstrJVs                @assert.notNull;
        layer               : Association to one MstrLayers             @assert.notNull;
        targetSubstrate     : Association to one MstrTargetSubstrates   @assert.notNull;
        substrateMaterial   : Association to one MstrSubstrateMaterials @assert.notNull;
        paintType           : Association to one MstrPaintTypes         @assert.notNull;
        crossLinkType       : Association to one MstrCrosslinkTypes     @assert.notNull;
        exteriorInterior    : Association to one MstrExteriorInteriors  @assert.notNull;
        atomization         : Association to one MstrAtomizations       @assert.notNull;
        electrostatic       : Association to one MstrElectrostatics     @assert.notNull;
        composition         : Association to one MstrCompositions       @assert.notNull;
        dryingCondition     : Association to one MstrDryingConditions   @assert.notNull;

        versions            : Association to many DefectDetailVersions
                                  on versions.defectDetail = $self;
        isArchive           : Boolean default false;
        defectId            : String(15)                                @title :                                               'Defect ID'  @assert.unique @assert.notNull ;// @Core.Immutable  @mandatory;
}


entity DefectDetailVersions : cuid, managed {
    @readonly key ID    : UUID                                 @Core.Computed  @UI.Hidden  @UI.HiddenFilter;
    defectDetail        : Association to one DefectDetails;
    version             : Integer;
    product             : Association to one Products;
    vehicleType         : Association to one MstrVehicleTypes;
    mainCustomer        : Association to one MainCustomers;
    // defectType          : Association to one MstrDefectTypes;
    title               : String(300)                          @assert.notNull  @title : 'Title';
    problemDescription  : String(1000)                          @assert.notNull  @title : 'Problem Description';
    causeOfProblem      : String(1000)                          @assert.notNull  @title : 'Cause of Problem';
    countermeasureTaken : String(1000);
    incidentDate : Date;
    recordDate   : Date;
    category            : Association to one MstrCategories    @title :                  'Category';
    subCategory         : Association to one MstrSubCategories @title :                  'Sub Category';
    JV                  : Association to one MstrJVs;
    layer               : Association to one MstrLayers;
    targetSubstrate     : Association to one MstrTargetSubstrates;
    substrateMaterial   : Association to one MstrSubstrateMaterials;
    paintType           : Association to one MstrPaintTypes;
    crossLinkType       : Association to one MstrCrosslinkTypes;
    exteriorInterior    : Association to one MstrExteriorInteriors;
    atomization         : Association to one MstrAtomizations;
    electrostatic       : Association to one MstrElectrostatics;
    composition         : Association to one MstrCompositions;
    dryingCondition     : Association to one MstrDryingConditions;
    isArchive           : Boolean default false;
    defectId            : String(15);
}

entity MstrUserTypes : cuid, managed {
    name : String(60)@title : 'User Type';
}

entity MstrVehicleTypes : cuid, managed {
    name : String(120)@title : 'Vehicle Type';
}

entity MstrDefectTypes : cuid, managed {
    name : String(120)@title : 'Defect Type';
}

entity MstrData : cuid, managed {
    name : String(120)@title : 'Data';
}


entity MstrCategories : cuid, managed {
    name : String(120)@title : 'Category';
    subCategories : Association to many MstrSubCategories on subCategories.category=$self;   
}

entity MstrSubCategories : cuid, managed {
    name : String(120)@title : 'Sub Category';
    category : Association to one MstrCategories;
}


entity MainCustomers : cuid, managed {
    name : String(120)@title : 'Main Customer';
    vehicleType : Association to one MstrVehicleTypes;
}

entity MstrJVs : cuid, managed {
    name : String(120)@title : 'Issued By (JV)';
}

entity MstrDocumentCategories : cuid, managed {
    name : String(120)@title : 'Document Category';
}

entity MstrLayers : cuid, managed {
    name : String(120)@title : 'Layer';
}

entity MstrTargetSubstrates : cuid, managed {
    name : String(120)@title : 'Target Substrate';
}

entity MstrSubstrateMaterials : cuid, managed {
    name : String(120)@title : 'Substrate Material';
}

entity MstrPaintTypes : cuid, managed {
    name : String(120)@title : 'Paint Type';
}

entity MstrCrosslinkTypes : cuid, managed {
    name : String(120)@title : 'Crosslink Type';
}

entity MstrExteriorInteriors : cuid, managed {
    name : String(120)@title : 'Exterior/Interior';
}

entity MstrAtomizations : cuid, managed {
    name : String(120)@title : 'Atomization';
}

entity MstrElectrostatics : cuid, managed {
    name : String(120)@title : 'Electrostatic';
}

entity MstrCompositions : cuid, managed {
    name : String(120);
}

entity MstrDryingConditions : cuid, managed {
    name : String(120);
}

entity DownloadDefectCSVSampleFiles : cuid, managed {
    @Core.MediaType                   :  mediatype
    @Core.ContentDisposition.Filename :  filename
    content   : LargeBinary     @title : '{i18n>content}';
    @Core.IsMediaType                 :  true
    @mandatory
    mediatype : String not null;
    @mandatory
    filename  : String not null @title : '{i18n>filename}';
}

@cds.persistence.skip
entity UploadDefectFiles : cuid {
    key ID      : UUID;
        content : LargeBinary @Core.MediaType : 'text/csv';
}

