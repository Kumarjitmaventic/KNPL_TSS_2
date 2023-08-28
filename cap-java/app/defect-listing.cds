annotate DefectService.DefectDetails with @(UI : {
    HeaderInfo       : {
        TypeName       : 'Defect',
        TypeNamePlural : 'Defect List',
        Title          : {Value : title},
        Description    : {Value : defectId},
    },
    SelectionFields  : [
        vehicleType_ID, mainCustomer.ID, product_ID, category_ID, subCategory.ID
        //defectType_ID, 
    ],
    LineItem      : [
        {
            Value : defectId, 
            Label: '{i18n>defectId}'
        },
        {
            Value : title, 
            Label: '{i18n>title}'
        },
        // {
        //     Value : defectType.name, 
        //     Label: '{i18n>defectType.name}'
        // },
        {
            Value : product.name, 
            Label: '{i18n>productName}'
        },
        {
            Value : vehicleType.name,
            Label: '{i18n>vehicleType}'
        },
        {
            Value : mainCustomer.name,
            Label: '{i18n>mainCustomer}'
        },
    ],
    Facets           : [{
        $Type  : 'UI.CollectionFacet',
        Label  : 'Defect Details',
        Facets : [
            {
                $Type  : 'UI.ReferenceFacet',
                Target : '@UI.FieldGroup#ProductDetails',
                Label  : 'Product Details'
            },
            {
                $Type  : 'UI.ReferenceFacet',
                Target : '@UI.FieldGroup#Main',
                Label  : 'Basic Details'
            },
            {
                $Type  : 'UI.ReferenceFacet',
                Target : '@UI.FieldGroup#Main2',
                Label  : 'Defect Details'
            },
            {
                $Type  : 'UI.ReferenceFacet',
                Target : '@UI.FieldGroup#Main3',
                Label  : 'Paint Details'
            },
            {
                $Type  : 'UI.ReferenceFacet',
                Target : 'versions/@UI.LineItem',
                Label  : 'Versions'
            }
        ]
    }],
    FieldGroup #ProductDetails : {Data : [
        {
            Value : product_ID, 
            Label: '{i18n>productName}'
        },
        {
            Value : vehicleType_ID, 
            Label: '{i18n>vehicleType}'
        },
        {
            Value : mainCustomer_ID,
            Label: '{i18n>product.mainCustomer.name}'
        },
    ]},
    FieldGroup #Main : {Data : [
        // {
        //     Value : defectType_ID, 
        //     Label: '{i18n>defectType.name}'
        // },
        {
            Value : createdBy, 
            Label: '{i18n>registerer}'
        },
        {
            Value : problemDescription, 
            Label: '{i18n>problemDescription}'
        },
        {
            Value : causeOfProblem, 
            Label: '{i18n>causeOfProblem}'
        },
        {
            Value : countermeasureTaken, 
            Label: '{i18n>defectDetails.countermeasureTaken}'
        },
        {
            Value : incidentDate, 
            Label: '{i18n>incidentDate}'
        },
        {
            Value : recordDate, 
            Label: '{i18n>recordDate}'
        },
    ]},
    FieldGroup #Main2 : {Data : [
        {
            Value : category_ID,
            Label: '{i18n>defectClassification.category}'
        },
        {
            Value : subCategory_ID, 
            Label: '{i18n>defectClassification.subCategory}'
        },
        {
            Value : JV_ID,
            Label: '{i18n>defectClassification.JV.name}'
        },
    ]},
    FieldGroup #Main3 : {Data : [
        {
            Value : layer_ID,
            Label: '{i18n>paintDetail.layer.name}'
        },
        {
            Value : targetSubstrate_ID,
            Label: '{i18n>paintDetail.targetSubstrate.name}'
        },
        {
            Value : substrateMaterial_ID,
            Label: '{i18n>paintDetail.substrateMaterial.name}'
        },
        {
            Value : paintType_ID,
            Label: '{i18n>paintDetail.paintType.name}'
        },
        {
            Value : crossLinkType_ID,
            Label: '{i18n>paintDetail.crossLinkType.name}'
        },
        {
            Value : exteriorInterior_ID,
            Label: '{i18n>paintDetail.exteriorInterior.name}'
        },
        {
            Value : atomization_ID,
            Label: '{i18n>paintDetail.atomization.name}'
        },
        {
            Value : electrostatic_ID,
            Label: '{i18n>paintDetail.electrostatic.name}'
        },
        {
            Value : composition_ID,
            Label: '{i18n>paintDetail.composition.name}'
        },
        {
            Value : dryingCondition_ID,
            Label: '{i18n>paintDetail.dryingCondition.name}'
        },
    ]}
});


annotate DefectService.DefectDetails with {
    @Common.Label : 'Product Name'
    @Common : {
        Text            : product.name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'Products',
        CollectionPath : 'Products',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : product_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : product.name,
            }
        ]
    }
    @Core.Description : 'Products'
    @mandatory
    @Core.Immutable
    product
};

annotate DefectService.Products with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Main Customer'
    @Common : {
        Text            : mainCustomer.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MainCustomers',
        CollectionPath : 'MainCustomers',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : mainCustomer_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : mainCustomer.name,
            },
            {
                $Type               : 'Common.ValueListParameterIn', //Input parameter used for filtering
                LocalDataProperty   : vehicleType_ID,
                ValueListProperty   : 'vehicleType_ID',
            },
        ]
    }
    @Core.Description : 'MainCustomers'
    @mandatory
    @Core.Immutable
    mainCustomer
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Vehicle Type'
    @Common : {
        Text            : vehicleType.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrVehicleTypes',
        CollectionPath : 'MstrVehicleTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : vehicleType_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : vehicleType.name,
            }
        ]
    }
    @Core.Description : 'MstrVehicleTypes'
    @mandatory
    @Core.Immutable
    vehicleType
};

annotate DefectService.MstrVehicleTypes with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Defect Type'
    @Common : {
        Text            : defectType.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrDefectTypes',
        CollectionPath : 'MstrDefectTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : defectType_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : defectType.name,
            }
        ]
    }
    @Core.Description : 'MstrDefectTypes'
    @mandatory
    @Core.Immutable
    defectType
};

annotate DefectService.MstrDefectTypes with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Category'
    @Common : {
        Text            : category.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCategories',
        CollectionPath : 'MstrCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : category_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : category.name,
            }
        ]
    }
    @mandatory
    category
};

annotate DefectService.MstrCategories with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Sub Category'
    @Common : {
        Text            : subCategory.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrSubCategories',
        CollectionPath : 'MstrSubCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : subCategory_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : subCategory.name,
            },
            {
                $Type               : 'Common.ValueListParameterIn', //Input parameter used for filtering
                LocalDataProperty   : category_ID,
                ValueListProperty   : 'category_ID',
            },
        ]
    }
    @Core.Description : 'MstrSubCategories'
    @mandatory
    subCategory
};



annotate DefectService.DefectDetails with {
    @Common.Label : 'Issued by (JV)'
    @Common : {
        Text            : JV.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrJVs',
        CollectionPath : 'MstrJVs',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : JV_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : JV.name,
            }
        ]
    }
    @Core.Description : 'MstrJVs'
    @mandatory
    JV
};

annotate DefectService.MstrJVs with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Layer'
    @Common : {
        Text            : layer.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrLayers',
        CollectionPath : 'MstrLayers',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : layer_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : layer.name,
            }
        ]
    }
    @Core.Description : 'MstrLayers'
    @mandatory
    layer
};

annotate DefectService.MstrLayers with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Target Substrate'
    @Common : {
        Text            : targetSubstrate.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrTargetSubstrates',
        CollectionPath : 'MstrTargetSubstrates',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : targetSubstrate_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : targetSubstrate.name,
            }
        ]
    }
    @Core.Description : 'MstrTargetSubstrates'
    @mandatory
    targetSubstrate
};

annotate DefectService.MstrTargetSubstrates with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Substrate Material'
    @Common : {
        Text            : substrateMaterial.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrSubstrateMaterials',
        CollectionPath : 'MstrSubstrateMaterials',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : substrateMaterial_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : substrateMaterial.name,
            }
        ]
    }
    @Core.Description : 'MstrSubstrateMaterials'
    @mandatory
    substrateMaterial
};

annotate DefectService.MstrSubstrateMaterials with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Paint Type'
    @Common : {
        Text            : paintType.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrPaintTypes',
        CollectionPath : 'MstrPaintTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : paintType_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : paintType.name,
            }
        ]
    }
    @Core.Description : 'MstrPaintTypes'
    @mandatory
    paintType
};

annotate DefectService.MstrPaintTypes with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};


annotate DefectService.DefectDetails with {
    @Common.Label : 'Crosslink Type'
    @Common : {
        Text            : crossLinkType.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCrosslinkTypes',
        CollectionPath : 'MstrCrosslinkTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : crossLinkType_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : crossLinkType.name,
            }
        ]
    }
    @Core.Description : 'MstrCrosslinkTypes'
    @mandatory
    crossLinkType
};

annotate DefectService.MstrCrosslinkTypes with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Exterior/Interiors'
    @Common : {
        Text            : exteriorInterior.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrExteriorInteriors',
        CollectionPath : 'MstrExteriorInteriors',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : exteriorInterior_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : exteriorInterior.name,
            }
        ]
    }
    @Core.Description : 'MstrExteriorInteriors'
    @mandatory
    exteriorInterior
};

annotate DefectService.MstrExteriorInteriors with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Atomization'
    @Common : {
        Text            : atomization.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrAtomizations',
        CollectionPath : 'MstrAtomizations',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : atomization_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : atomization.name,
            }
        ]
    }
    @Core.Description : 'MstrAtomizations'
    @mandatory
    atomization
};

annotate DefectService.MstrAtomizations with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Electrostatic'
    @Common : {
        Text            : electrostatic.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrElectrostatics',
        CollectionPath : 'MstrElectrostatics',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : electrostatic_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : electrostatic.name,
            }
        ]
    }
    @Core.Description : 'MstrElectrostatics'
    @mandatory
    electrostatic
};

annotate DefectService.MstrElectrostatics with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Composition'
    @Common : {
        Text            : composition.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCompositions',
        CollectionPath : 'MstrCompositions',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : composition_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : composition.name,
            }
        ]
    }
    @Core.Description : 'MstrCompositions'
    @mandatory
    composition
};

annotate DefectService.MstrCompositions with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.DefectDetails with {
    @Common.Label : 'Composition'
    @Common : {
        Text            : dryingCondition.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrDryingConditions',
        CollectionPath : 'MstrDryingConditions',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : dryingCondition_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : dryingCondition.name,
            }
        ]
    }
    @Core.Description : 'MstrDryingConditions'
    @mandatory
    dryingCondition
};

annotate DefectService.MstrDryingConditions with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};

annotate DefectService.MstrSubCategories with {
    @Common.Label : 'Sub Category'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrSubCategories',
        CollectionPath : 'MstrSubCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : ID,
                ValueListProperty : 'ID',
                
            },
            {
                $Type  : 'Common.ValueListParameterDisplayOnly',
                ValueListProperty   : 'category/name',
            },
        ]
    }
    @Core.Description : 'MstrSubCategories'
    ID
};

annotate DefectService.MstrSubCategories with {
    // ID @Common.Text: name @Common.TextArrangement: #TextOnly;
    category_ID @Common.Text: category.ID @Common.TextArrangement: #TextOnly;
};

annotate DefectService.MainCustomers with {
    @Common.Label : 'Main Customer'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MainCustomers',
        CollectionPath : 'MainCustomers',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : ID,
                ValueListProperty : 'ID'
            },
            {
                $Type  : 'Common.ValueListParameterDisplayOnly',
                ValueListProperty   : 'vehicleType/name',
            },
        ]
    }
    @Core.Description : 'MainCustomers'
    ID
};
