
annotate UserService.Users with @(UI : {
    HeaderInfo       : {
        TypeName       : 'User',
        TypeNamePlural : 'User List',
        //Title          : {Value : ID},
        //Description    : {Value : name}
    },
    SelectionFields  : [
        userType_ID //userType.name
    ],
    LineItem         : [
        {
            Value : userType.name,
            Label : '{i18n>userType}'
        },
        {
            Value : name, 
            Label: '{i18n>userName}'
        },
        {
            Value : email, 
            Label: '{i18n>email}'
        },
    ],
});

annotate UserService.MstrUserTypes with {
    ID @Common.Text: name @Common.TextArrangement: #TextOnly;
};
annotate UserService.Users with {
    @Common.Label : 'User Type'
    @Common : {
        Text            : userType.name,
        TextArrangement : #TextOnly //#TextOnly
    }
    @Common.ValueListWithFixedValues : true
    @Common.ValueList : {
        $Type : 'Common.ValueListType',
        Label : 'MstrUserTypes',
        CollectionPath : 'MstrUserTypes',
        Parameters : [
            {
                $Type : 'Common.ValueListParameterInOut',
                ValueListProperty : 'ID',
                LocalDataProperty : userType_ID,
                
            },
            {
                $Type : 'Common.ValueListParameterOut',
                ValueListProperty : 'name',
                LocalDataProperty : userType.name,
            }
        ]
    }
    @Core.Description : 'MstrUserTypes'
    @mandatory
    @Core.Immutable
    userType
};