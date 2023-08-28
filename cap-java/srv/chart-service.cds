using knpl.tss as knpl_tss from '../db/data-model';

@(requires: 'authenticated-user')
service ChartService {
    @(restrict: [ 
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity ChartDefects as projection on knpl_tss.DefectDetails { *, category.name as categoryName,
                                                                    product.name as productName } where isArchive = false;


    @(restrict: [ 
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity MstrCategories                as projection on knpl_tss.MstrCategories;

    @(restrict: [ 
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity Products                      as projection on knpl_tss.Products;
                           
}

annotate ChartService.ChartDefects with @(
    UI:{
        LineItem         : [
        {
            Value : defectId,
            Label : 'Defect Id'
        },
        {
            Value : categoryName,
            Label : 'Category'
        },
        {
            Value : productName,
            Label : 'Product'
        } 
    ],
    }
);

annotate ChartService.ChartDefects with @(UI.Chart : 
   {
    Title               : 'Defect Chart',
    ChartType           : #Column,
    Measures            : [defectCount],
    Dimensions          : [categoryName], // X axis values

    MeasureAttributes   : [{
        $Type   : 'UI.ChartMeasureAttributeType',
        Measure : defectCount,
        Role    : #Axis1
    }, ],
    DimensionAttributes : [{
            $Type     : 'UI.ChartDimensionAttributeType',
            Dimension : categoryName,
            Role      : #Category,
            EmphasisLabels : {
                $Type : 'UI.EmphasisLabelType',
                EmphasizedValuesLabel : 'category.name',
            },
        },
        {
            $Type     : 'UI.ChartDimensionAttributeType',
            Dimension : productName,
            Role      : #Category,
            EmphasisLabels : {
                $Type : 'UI.EmphasisLabelType',
                EmphasizedValuesLabel : 'product.name',
            },
        }
    ]
});

annotate ChartService.ChartDefects with @(
                            
    Analytics.AggregatedProperties : [
    {
        Name                 : 'defectCount',
        AggregationMethod    : 'countdistinct',
        AggregatableProperty : 'defectId', //'integerValue',
        ![@Common.Label]     : 'Defect Count'
    },

]);

annotate ChartService.ChartDefects with @(
    Aggregation.ApplySupported : {
        Transformations          : [
            'aggregate',
            'topcount',
            'bottomcount',
            'identity',
            'concat',
            'groupby',
            'filter',
            'expand',
            'top',
            'skip',
            'orderby',
            'search'
        ],
        Rollup                   : #None,
        PropertyRestrictions     : true,
        GroupableProperties : [
            defectId, categoryName, productName
        ],
        AggregatableProperties : [
            {
                $Type : 'Aggregation.AggregatablePropertyType',
                Property : defectId,//integerValue,
                RecommendedAggregationMethod : 'countdistinct',
                SupportedAggregationMethods : [
                    'min',
                    'max',
                    'average',
                    'countdistinct'
                ],
            },
        ],
    }
);

// annotate ChartService.ChartDefects with {
//     @Common.Label : 'Category'
//     @Common : {
//         Text            : category.name,
//         TextArrangement : #TextOnly //#TextOnly
//     }
//     category
// }
// annotate ChartService.ChartDefects with {
//     @Common.Label : 'Product'
//     @Common : {
//         Text            : product.name,
//         TextArrangement : #TextOnly //#TextOnly
//     }
//     product
// };

// annotate ChartService.MstrCategories with {
//     ID @Common.Text: name @Common.TextArrangement: #TextOnly;

//     @UI.DataPoint  : {
//         $Type : 'UI.DataPointType',
//         Value : name,
//         TargetValue : name,
//     }
//     ID
// };

// annotate ChartService.Products with {
//     ID @Common.Text: name @Common.TextArrangement: #TextOnly;
        
//     @UI.DataPoint  : {
//         $Type : 'UI.DataPointType',
//         Value : name,
//         TargetValue : name,
//     }
//     ID
// };