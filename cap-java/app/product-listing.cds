
// annotate DefectService.Products with @(
//     odata.draft.enabled: false,
//     Capabilities.DeleteRestrictions : {
//         Deletable : false,  // Hides the delete button
//     },
    
// );

// annotate DefectService.Products with @(UI : {
//     HeaderInfo       : {
//         TypeName       : 'Product',
//         TypeNamePlural : 'Products',
//         Title          : {Value : name},
//         Description    : {Value : code},

//     },
//     SelectionFields  : [
//         name , code
//     ],
    
//     LineItem         : [
//         {
//             Value : name, 
//             Label: '{i18n>productName}'
//         },
//         {
//             Value : code, 
//             Label: '{i18n>productCode}'
//         },
//     ],
//     // Facets : [{
//     //     $Type  : 'UI.ReferenceFacet',
//     //     Target : 'defectDetails/@UI.LineItem',
//     //     // Label  : 'Main Facet'
//     // }],


// });

annotate DefectService.MstrVehicleTypes with {
    @Common.Label : 'Vehicle Type'
    @Common : {
        Text            : name,
        TextArrangement : #TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrVehicleTypes',
        CollectionPath : 'MstrVehicleTypes',
        
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                LocalDataProperty : name,
                ValueListProperty : 'name'
            },
            // {
            //     $Type : 'Common.ValueListParameterDisplayOnly',
            //     ValueListProperty : 'name'
            // }
        ]
    }
    @Core.Description : 'VehicleTypes'
    name
};


