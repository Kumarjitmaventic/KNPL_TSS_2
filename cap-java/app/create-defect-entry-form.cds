annotate AdminService.DefectDetails actions {
    @(
        Common.SideEffects : {
            TargetProperties : ['_it/ID'],
            TargetEntities : [_it]
        },
        cds.odata.bindingparameter.name : '_it'
    )
    createDefect(order_ID @(
        title : '{i18n>Order}',
        Common : {ValueListMapping : {
            Label : '{i18n>DefectDetails}',
            CollectionPath : 'DefectDetails',
            Parameters : [
                {
                    $Type : 'Common.ValueListParameterInOut',
                    LocalDataProperty : ID,
                    ValueListProperty : 'ID'
                },
                {
                    $Type : 'Common.ValueListParameterDisplayOnly',
                    ValueListProperty : 'createdBy'
                },
                {
                    $Type : 'Common.ValueListParameterDisplayOnly',
                    ValueListProperty : 'createdAt'
                }
            ],
        }}
    ),
    quantity @title : '{i18n>Quantity}'
    )
}

annotate MasterService.MstrDefectTypes with {
    @Common.Label : 'Defect Type'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrDefectTypes',
        CollectionPath : 'MstrDefectTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrDefectTypes'
    name
};

annotate MasterService.MstrData with {
    @Common.Label : 'Data'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrData',
        CollectionPath : 'MstrData',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrData'
    name
};

annotate MasterService.MstrCategories with {
    @Common.Label : 'Defect Category'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCategories',
        CollectionPath : 'MstrCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrCategories'
    name
};

annotate MasterService.MstrSubCategories with {
    @Common.Label : 'Defect Sub Category'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrSubCategories',
        CollectionPath : 'MstrSubCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrSubCategories'
    name
};

annotate MasterService.MstrJVs with {
    @Common.Label : 'Issued by (JV)'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrJVs',
        CollectionPath : 'MstrJVs',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrJVs'
    name
};

annotate MasterService.MstrDocumentCategories with {
    @Common.Label : 'Document Category'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrDocumentCategories',
        CollectionPath : 'MstrDocumentCategories',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrDocumentCategories'
    name
};

annotate MasterService.MstrLayers with {
    @Common.Label : 'Layer'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrLayers',
        CollectionPath : 'MstrLayers',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrLayers'
    name
};

annotate MasterService.MstrTargetSubstrates with {
    @Common.Label : 'Target Substrate'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrTargetSubstrates',
        CollectionPath : 'MstrTargetSubstrates',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrTargetSubstrates'
    name
};

annotate MasterService.MstrSubstrateMaterials with {
    @Common.Label : 'Substrate Material'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrSubstrateMaterials',
        CollectionPath : 'MstrSubstrateMaterials',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrSubstrateMaterials'
    name
};

annotate MasterService.MstrPaintTypes with {
    @Common.Label : 'Paint Type'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrPaintTypes',
        CollectionPath : 'MstrPaintTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrPaintTypes'
    name
};

annotate MasterService.MstrCrosslinkTypes with {
    @Common.Label : 'Crosslink Type'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCrosslinkTypes',
        CollectionPath : 'MstrCrosslinkTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrCrosslinkTypes'
    name
};


annotate MasterService.MstrExteriorInteriors with {
    @Common.Label : 'Exterior/Interior'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrExteriorInteriors',
        CollectionPath : 'MstrExteriorInteriors',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrExteriorInteriors'
    name
};

annotate MasterService.MstrAtomizations with {
    @Common.Label : 'Application 1 (Atomization)'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrAtomizations',
        CollectionPath : 'MstrAtomizations',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrAtomizations'
    name
};

annotate MasterService.MstrElectrostatics with {
    @Common.Label : 'Application 2 (Electrostatic)'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrElectrostatics',
        CollectionPath : 'MstrElectrostatics',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrElectrostatics'
    name
};


annotate MasterService.MstrCompositions with {
    @Common.Label : 'Application 3 (Composition)'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrCompositions',
        CollectionPath : 'MstrCompositions',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrCompositions'
    name
};

annotate MasterService.MstrDryingConditions with {
    @Common.Label : 'Drying Condition'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrDryingConditions',
        CollectionPath : 'MstrDryingConditions',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            }
        ]
    }
    @Core.Description : 'MstrDryingConditions'
    name
};