using knpl.tss as knpl_tss from '../db/data-model';
// @(requires: 'authenticated-user')
service UserService {

    @readonly
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity Users as projection on knpl_tss.Users;

    @readonly
    @(restrict: [ 
        {grant: ['READ'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity MstrUserTypes as projection on knpl_tss.MstrUserTypes;

    @(restrict: [ 
        {grant: ['READ', 'CREATE', 'UPDATE','DELETE'], to: 'READ_TSS_MOBILE'},
        {grant: ['READ', 'CREATE', 'UPDATE','DELETE'], to: 'MANAGE_ADMIN'},
    ])
    entity UserLoginData as projection on knpl_tss.UserLoginData;
}