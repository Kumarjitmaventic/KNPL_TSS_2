using knpl.tss as knpl_tss from '../db/data-model';

@(requires: 'authenticated-user')
service DefectFileService {
    @(restrict: [ 
        {grant: ['READ', 'CREATE', 'UPDATE','DELETE'], to: 'MANAGE_ADMIN'},
    ])
    entity UploadDefectFiles as projection on knpl_tss.UploadDefectFiles;
    @(restrict: [ 
        {grant: ['READ'], to: 'MANAGE_ADMIN'},
    ])
    entity DownloadDefectCSVSampleFiles as projection on knpl_tss.DownloadDefectCSVSampleFiles;
}