'use strict';

var app = angular.module('angularApp')
.controller('editSampleCtrl', function (sampleService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();


    $scope.sampleResultData =sampleService.sampleData;



    $scope.sampleData = {};
    $scope.sampleId = sampleService.sampleId;
    


    // $scope.sampleResultData = {"data":[{"sampleId":27131907,"sampleName":"NCL-MGelderman-IJN2008-01","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","composition":["Fullerene"],"functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1238731200000,"editable":true},{"sampleId":24063238,"sampleName":"NCL-49","pointOfContact":"my org<br>searea<br>dfafa","composition":["Liposome"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":1407591871000,"editable":true},{"sampleId":24063237,"sampleName":"NCL-48","pointOfContact":"Mark Kester PSU","composition":["Liposome"],"functions":[],"characterizations":["Cytotoxicity","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1181275200000,"editable":true},{"sampleId":24063236,"sampleName":"NCL-45","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 26%; MINChar: 33%","createdDate":1179374400000,"editable":true},{"sampleId":24063235,"sampleName":"NCL-42","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","OxidativeStress","Purity","Size","Surface"],"dataAvailability":"caNanoLab: 40%; MINChar: 44%","createdDate":1179374400000,"editable":true},{"sampleId":24063234,"sampleName":"NCL-19","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000,"editable":true},{"sampleId":24063233,"sampleName":"NCL-17","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity"],"dataAvailability":"caNanoLab: 20%; MINChar: 11%","createdDate":1179374400000,"editable":true},{"sampleId":24063232,"sampleName":"NCL-16","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000,"editable":true},{"sampleId":20917519,"sampleName":"NCL-59-2","pointOfContact":"Mansoor Amiji","composition":null,"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 3%; MINChar: 0%","createdDate":1152504000000,"editable":true},{"sampleId":20917518,"sampleName":"NCL-58-2","pointOfContact":"Mansoor Amiji","composition":null,"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 3%; MINChar: 0%","createdDate":1152504000000,"editable":true},{"sampleId":20917517,"sampleName":"NCL-57-2","pointOfContact":"Mansoor Amiji","composition":null,"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 3%; MINChar: 0%","createdDate":1152504000000,"editable":true},{"sampleId":20917516,"sampleName":"NCL-56-2","pointOfContact":"Mansoor Amiji","composition":null,"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 3%; MINChar: 0%","createdDate":1152504000000,"editable":true},{"sampleId":20917515,"sampleName":"NCL-55-1","pointOfContact":"Mansoor Amiji","composition":null,"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 3%; MINChar: 0%","createdDate":1152504000000,"editable":true},{"sampleId":20917514,"sampleName":"NCL-51-3","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000,"editable":true},{"sampleId":20917513,"sampleName":"NCL-50-1","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000,"editable":true},{"sampleId":20917512,"sampleName":"NCL-49-4","pointOfContact":"BB_SH_DFCI_WCMC_BWH_MIT","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1407360221000,"editable":true},{"sampleId":20917511,"sampleName":"NCL-48-4","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000,"editable":true},{"sampleId":20917510,"sampleName":"NCL-26-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000,"editable":true},{"sampleId":20917509,"sampleName":"NCL-25-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000,"editable":true},{"sampleId":20917508,"sampleName":"NCL-24-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction"],"dataAvailability":"caNanoLab: 30%; MINChar: 11%","createdDate":1152504000000,"editable":true},{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":"C-Sixty (CNI)","composition":["OtherNanomaterialEntity","OtherNanomaterialEntity","Emulsion","CarbonNanotube","Biopolymer","Polymer","QuantumDot","MetalParticle","Liposome","Fullerene","Dendrimer"],"functions":["ImagingFunction","TargetingFunction","endosomolysis"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 50%; MINChar: 33%","createdDate":1152504000000,"editable":true},{"sampleId":20917506,"sampleName":"NCL-22-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 40%; MINChar: 33%","createdDate":1152504000000,"editable":true},{"sampleId":20917505,"sampleName":"NCL-21-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity"],"dataAvailability":"caNanoLab: 20%; MINChar: 22%","createdDate":1152504000000,"editable":true},{"sampleId":20917504,"sampleName":"NCL-20-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1152504000000,"editable":true}]};



    $scope.pocData = sampleService.pocData;
    $scope.scratchPad = sampleService.scratchPad;
    $scope.master = {};
    $scope.newKeyword = "";
    $scope.message = "";
    // Access variables
    $scope.csmRoleNames = {"R":"read","CURD":"read update delete"};
    $scope.sampleData.theAccess = {};
    $scope.accessForm = {};
    $scope.dataType = 'Sample';
    $scope.parentFormName = 'sampleForm';
    $scope.accessForm.theAcccess = {};
    $scope.accessForm.theAcccess.userBean = {};
    $scope.isCurator = groupService.isCurator();
    $scope.groupAccesses = [];
    $scope.userAccesses = [];
    $scope.addAccess = false;
    $scope.showAddAccessButton = true;
    $scope.showCollaborationGroup = true;
    $scope.showAccessuser = false;
    $scope.showAccessSelection = false;
    $scope.accessForm.theAccess = {};
    $scope.accessForm.theAccess.groupName = '';
    $scope.accessForm.theAccess.userBean = {};
    $scope.accessForm.theAccess.userBean.loginName = '';
    $scope.access = {};
    $scope.access.groupName = '';
    $scope.access.loginName = '';
    $scope.sampleData.isPublic = false;
    $scope.accessForm.theAccess.accessBy = 'group';        
    $scope.accessExists = false;    

    var editSampleData = {"editSampleData":{"dirty": false}};
    $scope.scratchPad = editSampleData;

    $scope.editSampleForm = false;
    $scope.loader = true;


    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 0;

    //goBack
    //Change location if user hits the Back button
    $scope.goBack = function() {
        $location.path("/sampleResults").replace();
        $location.search('sampleId', null);
    };

    //Get URL params
    if ($routeParams.sampleId) {
      //  $scope.sampleId = $routeParams.sampleId.data;
      $scope.sampleId.data = $routeParams.sampleId;

    } else {
        $scope.message = "Need to add a sampleId to the url";
    }

    $scope.returnUserReadableBoolean = function(val) {
        if (val== true) {
            return "Yes";
        }
        return "No";
    }

    //Populate the sampleData if browsing in Firefox to local file
    if(location.hostname == "") {
        $scope.loader = true;
        $scope.loaderText = "Loading";
        /* if no hostname then populate with raw data */
    	console.info("No REST Service, loading hard coded sampleData")
        var hardCodedSampleData = {"sampleName":"NCL-23-1","newSampleName":null,"sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":52985856,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":true},{"id":53051393,"contactPerson":"","organization":{"id":11239424,"name":"CWRU"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52985856,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53051392,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"shaun","lastName":"Einolf","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52985857,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"Einolf","lastName":"Shaun","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52920323,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52920321,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"BOB","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52953088,"contactPerson":"","organization":{"id":36306944,"name":"BB_SH_KCI_DFCI_WCMC_BWH_MIT"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53018624,"contactPerson":"","organization":{"id":15728641,"name":"DWU_SNU_SU_US"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52953089,"contactPerson":"","organization":{"id":36306944,"name":"BB_SH_KCI_DFCI_WCMC_BWH_MIT"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":43646976,"contactPerson":"","organization":{"id":35782656,"name":"BB_SH_DFCI_WCMC_BWH_MIT"},"role":"Chris Contact Role","address":{"line1":"","line2":"","city":"sdadsadsa","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53673984,"contactPerson":"","organization":{"id":53706752,"name":"test org"},"role":"investigator","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[{"groupName":"","roleName":"","roleDisplayName":"read update delete","loginName":"Omelchen","accessBy":"","dirty":false}],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","dirty":false}]},"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"userAccesses":[{"userBean":{"userId":null,"displayName":"Omelchen","loginName":"Omelchen","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"dataAvailability":{"sampleName":"NCL-23-1","dataAvailability":"caNanoLab: 50%; MINChar: 33%","caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"availableEntityNames":["molecular weight","sample function","physical state","sample composition","purity","blood contact","association","solubility","publications","general sample information","functionalizing entities","encapsulation","size","nanomaterial entities","cytotoxicity"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","Chris 2","Chris Org","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","Monday Org","Monday4","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp","test org"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]} ;
        // data availability is empty in this one //
        // var hardCodedSampleData = {"sampleName":"NCL-23-1","newSampleName":null,"sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":52985856,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":true},{"id":53051393,"contactPerson":"","organization":{"id":11239424,"name":"CWRU"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52985856,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53051392,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"shaun","lastName":"Einolf","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52985857,"contactPerson":"","organization":{"id":24030464,"name":"C-Sixty (CNI)"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"Einolf","lastName":"Shaun","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52920323,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52920321,"contactPerson":"","organization":{"id":9568257,"name":"BROWN_STANFORD"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"BOB","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52953088,"contactPerson":"","organization":{"id":36306944,"name":"BB_SH_KCI_DFCI_WCMC_BWH_MIT"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53018624,"contactPerson":"","organization":{"id":15728641,"name":"DWU_SNU_SU_US"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"23232232","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":52953089,"contactPerson":"","organization":{"id":36306944,"name":"BB_SH_KCI_DFCI_WCMC_BWH_MIT"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"weq33","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":43646976,"contactPerson":"","organization":{"id":35782656,"name":"BB_SH_DFCI_WCMC_BWH_MIT"},"role":"Chris Contact Role","address":{"line1":"","line2":"","city":"sdadsadsa","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false},{"id":53673984,"contactPerson":"","organization":{"id":53706752,"name":"test org"},"role":"investigator","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[{"groupName":"","roleName":"","roleDisplayName":"read update delete","loginName":"Omelchen","accessBy":"","dirty":false}],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","dirty":false}]},"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"userAccesses":[{"userBean":{"userId":null,"displayName":"Omelchen","loginName":"Omelchen","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"dataAvailability":null,"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","Chris 2","Chris Org","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","Monday Org","Monday4","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp","test org"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]} ;

        $scope.sampleData = hardCodedSampleData;
        $scope.loader = false;
        $scope.editSampleForm = true;

    } else {
        $scope.loaderText = "Loading";

        $http({method: 'GET', url: '/caNanoLab/rest/sample/edit?sampleId='+$scope.sampleId.data}).
            success(function(data, status, headers, config, statusText) {
                $scope.sampleData = data;
                $scope.loader = false;
                $scope.editSampleForm = true;
                
                $scope.groupAccesses = $scope.sampleData.groupAccesses;
                $scope.userAccesses = $scope.sampleData.userAccesses;
                
                if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
                	$scope.accessExists = true;
                }  
                
                if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
                	$scope.accessExists = true;
                }
            }).
            error(function(data, status, headers, config, statusText) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                if(status != 200){
                    $scope.message = "Response code " + status.toString() + ":  " + statusText;
                } else {
                    $scope.message = data;
                }
                $scope.loader = false;
                $scope.submissionView = false;
        });
    }

// * Page Change Events *
//Add keyword
    $scope.addKeyword=function(){
        if($scope.newKeyword.length > 1) {
            $scope.sampleData.keywords.push($scope.newKeyword.toUpperCase());
            $scope.newKeyword = "";
            $scope.scratchPad.editSampleData.dirty = true;
        }
    };
    $scope.removeKeyword = function(item) {
        var index = $scope.sampleData.keywords.indexOf(item)
        $scope.sampleData.keywords.splice(index, 1);
        $scope.scratchPad.editSampleData.dirty = true;
    }

    $scope.changedSampleName = function() {
        $scope.scratchPad.editSampleData.dirty = true;
    };




    $scope.copy = function() {
        //Submit a copy of sampleName to cloneSample url //
        $location.path("/cloneSample").search({'sampleName':$scope.sampleData.sampleName}).replace();
    };

    $scope.delete = function() {
        // Delete sample, send back to results //        

        var modalHtml = '<div class="modal-header">Are you sure you wish to delete this sample?</div>';
         modalHtml+= '<div class="modal-body"><button ng-disabled="loader" style="margin-right:50px !important;" ng-click="closeModal(1)" class="btn btn-primary btn-sm">Yes</button>';
         modalHtml+= '<button ng-disabled="loader" ng-click="closeModal(0)" class="btn btn-primary  btn-sm">No</button></div>';
         modalHtml+='<div id="loader" style="top:0px;left:30px;" ng-show="loader"><div id="loaderText">Deleting sample</div><div id="loaderGraphic"></div></div>';        
        var modalInstance = $modal.open({
            template:modalHtml,
            size:"sm",
            controller:"modalCtrl",
            resolve: {
                id: function() {
                    return $scope.sampleId.data;
                }
            }
        });
        modalInstance.result.then(function(selection) {
            if (selection) {
                $scope.loader = true;
                $scope.loaderText = "Deleting Sample";

                $http({method: 'GET', url: '/caNanoLab/rest/sample/deleteSample',params: {"sampleId":$scope.sampleData.sampleId}}).
                success(function(data, status, headers, config) {
                    var b = $scope.sampleResultData['data'];
                    for (var g = 0;g<b.length;g++) {
                      if (b[g].sampleId==$scope.sampleId.data) {
                        $scope.sampleResultsData = b.splice(g,1);
                      };
                    };
                    $location.path("/sampleResults").replace();
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.message = data;
                }); 
            };
        });       
    };    

    $scope.reset = function() {
         $scope.sampleData = angular.copy($scope.master);
    };
    $scope.update = function() {
        console.info("Rest call here.  /caNanoLab/rest/sample/updateSample");
        $http({method: 'POST', url: '/caNanoLab/rest/sample/updateSample',data: $scope.sampleData}).
        success(function(data, status, headers, config) {
            $scope.sampleData.pointOfContacts = data.pointOfContacts;
            $scope.master = angular.copy($scope.sampleData);
            $scope.message = "Sample has been saved";
            $scope.scratchPad.editSampleData.dirty = false;

        }).
        error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            // $rootScope.sampleData = data;
            $scope.loader = false;
            $scope.message = data;
        });


    };

// Modal for Access To Sample (1)
    $scope.openPointOfContactModal = function(sampleId, poc) {
        sampleService.sampleData = angular.copy($scope.sampleData);
        sampleService.pocData = angular.copy(poc);

        $scope.pocData = poc.data;
        var modalInstance = $modal.open({
          templateUrl: 'views/sample/edit/modal/pointOfContactModal.html',
          controller: 'PointOfContactModalCtrl',
          windowClass: 'pointOfContact-modal-window',
          resolve: {
            sampleId: function () {
              return sampleId;
            },
            sampleData: function () {
              return $scope.sampleData;
            },            
            originalPoc: function () {
              return poc;
            },
            master: function() {
                return $scope.master;
            },
            message: function() {
                return $scope.message;
            }    
          }
        });

        modalInstance.result.then(function (sampleData) {
            $scope.sampleData = sampleData;
        });

        var savePoc = function(poc) {
                $scope.message = "";
                $scope.loader = true;
                //$scope.newPoc = sampleService.pocData.data;
                //console.dir($scope.newPoc);
                //Set dirty flag
                //poc.dirty = true;
                sampleService.pocData.dirty = true;
                //Update sampleData with newPoc
                if(parseInt(sampleService.pocData.id) > 0){
                    //Update
                    //$scope.sampleData.pointOfContacts.push(sampleService.pocData);
                    //Find the id and replace it.
                    //
                    //sampleService.pocData = poc;
                    //Update Screen
                    poc = sampleService.pocData;
                    var index = sampleService.sampleData.pointOfContacts.map(function(x) {return x.id; }).indexOf(sampleService.pocData.id);
                    $scope.sampleData.pointOfContacts[index] = poc;
                } else {
                    //Append to PointOfContact
                    console.info("Appending to PointOfContact");
                    $scope.sampleData.pointOfContacts.push(sampleService.pocData);
                };
                if(location.hostname == "") {
                } else {
                    console.info("Rest call here.  /caNanoLab/rest/sample/savePOC");
                    $http({method: 'POST', url: '/caNanoLab/rest/sample/savePOC',data: $scope.sampleData}).
                    success(function(data, status, headers, config) {
                        //alert(data);
                        //TODO: This next line needs to be $scope.sampleData = data;
                        // The server is messing up keywords by changing array of strings to string with returns.
                        // So I left this alone until after the demo.
                        //
                        $scope.sampleData.pointOfContacts = data.pointOfContacts;
                        $scope.master = angular.copy($scope.sampleData);
                        $scope.message = "Point of Contact has been saved";

                    }).
                    error(function(data, status, headers, config) {
                        // called asynchronously if an error occurs
                        // or server returns response with an error status.
                        // $rootScope.sampleData = data;
                        $scope.loader = false;
                        $scope.message = data;
                    });
                }
                $scope.loader = false;        alert($scope.sampleData.organizationNamesForUser)

        };
        // modalInstance.result.then(function (poc) {
        //     // Save POC
        //     //alert("Save hit");
        //     savePoc(poc);
        //     console.info('User hits save.');
        // }, function () {
        //     //Do not save - replace any changes to POC
        //     //alert("Cancel hit");
        //     console.info('Modal dismissed at: ' + new Date());
        // });

    };

// Modal for Access To Sample (2)
// Modal for Access To Sample (1)
    $scope.openAccessToSampleModal = function(sampleId, ats) {
        console.log('openAccessToSampleModal');
        console.dir(sampleId);
        console.log('ats');
        console.dir(ats);

        sampleService.sampleData = angular.copy($scope.sampleData);
        $scope.pocData = poc.data;
        var modalInstance = $modal.open({
          templateUrl: 'views/sample/edit/modal/pointOfContactModal.html',
          controller: 'PointOfContactModalCtrl',
          windowClass: 'pointOfContact-modal-window',
          resolve: {
            sampleId: function () {
              return sampleId;
            },
            poc: function() {
              return poc;
            }
          }
        });
        modalInstance.result.then(function (poc) {
            $scope.newPoc = poc;
            /*Save POC*/
            alert("Save hit");
            console.info('User hit save.');
        }, function () {
            /*Do not save - replace any changes to POC*/
            alert("Cancel hit");
            console.info('Modal dismissed at: ' + new Date());
        });
     };

    // generates data availability //
    $scope.generateDataAvailability = function(sampleId) {
        $scope.generateLoader = true;

          $http({method: 'GET', url: '/caNanoLab/rest/sample/regenerateDataAvailability',params: {"sampleId":sampleId}}).
            success(function(data, status, headers, config) {
                // $scope.accessUsers = data;
                $scope.sampleData = data;
                $scope.generateLoader = false;
            }).
            error(function(data, status, headers, config) {
                alert(data);
                $scope.message = data;
                $scope.generateLoader = false;                
            });        

    };

// Modal for Data Availability (3)
    $scope.openDataAvailability = function(sampleId) {

          $http({method: 'GET', url: '/caNanoLab/rest/sample/viewDataAvailability',params: {"sampleId":sampleId}}).
          success(function(data, status, headers, config) {
            var modalInstance = $modal.open({
              templateUrl: 'views/sample/view/sampleDataAvailability.html',
              controller: 'SampleDataAvailabilityCtrl',
              size: 'sm',
              resolve: {
                sampleId: function () {
                  return sampleId;
                },
                availabilityData: function() {
                  return data;
                },
                sampleData: function() {
                  return $scope.sampleData;
                },                
                edit: function() {
                  return 1;
                }                
              }
            });
            modalInstance.result.then(function(data) {
                $scope.sampleData = data;
            });
          }).
          error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            $scope.message = data;
          });


    };
    
    
    /** Start - Access functions **/
    
    $scope.getCollabGroups = function() {
        if ($scope.accessForm.theAccess.groupName === undefined || $scope.accessForm.theAccess.groupName === null) {
            $scope.accessForm.theAccess.groupName = '';
        }

        $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.accessForm.theAccess.groupName}).
            success(function(data, status, headers, config) {
                $scope.collabGroups = data;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
            });

        //$scope.collabGroups = ["curator group", "NCI", "NCIP"];
        $scope.showAccessSelection=true;

    };

    $scope.getAccessUsers = function() {
        if ($scope.accessForm.theAccess.userBean.loginName === undefined || $scope.accessForm.theAccess.userBean.loginName === null) {
            $scope.accessForm.theAccess.userBean.loginName = '';
        }

        $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.accessForm.theAccess.userBean.loginName}).
            success(function(data, status, headers, config) {
                $scope.accessUsers = data;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
            });

        //$scope.accessUsers = {"lethai":"Le Thai","Omelchen":"Omelchenko Marina","burnskd":"Burns Kevin","canano_guest":"Guest Guest","grodzinp":"Grodzinski Piotr","swand":"Swan Don","skoczens":"Skoczen Sarah","sternstephan":"Stern Stephan","zolnik":"Zolnik Banu","hunseckerk":"Hunsecker Kelly","lipkeyfg":"Lipkey Foster","marina":"Dobrovolskaia Marina","pottert":"Potter Tim","uckunf":"Uckun Fatih","michal":"Lijowski Michal","mcneils":"Mcneil Scott","neunb":"Neun Barry","cristr":"Crist Rachael","zhengji":"Zheng Jiwen","frittsmj":"Fritts Martin","SchaeferH":"Schaefer Henry","benhamm":"Benham Mick","masoods":"Masood Sana","mclelandc":"McLeland Chris","torresdh":"Torres David","KlemmJ":"Klemm Juli","patria":"Patri Anil","hughesbr":"Hughes Brian","clogstonj":"Clogston Jeff","hinkalgw":"Hinkal George","MorrisS2":"Morris Stephanie","sharon":"Gaheen Sharon"};
        $scope.showAccessSelection=true;

    };

    $scope.hideAccessSection = function() {
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
    }
    
    $scope.saveAccessSection = function() {
        $scope.loader = true;
        $scope.sampleData.theAccess = $scope.accessForm.theAccess;
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
        
        if( $scope.accessForm.theAccess.accessBy == 'public') {
            $scope.sampleData.isPublic = true;
        }

        $http({method: 'POST', url: '/caNanoLab/rest/sample/saveAccess',data: $scope.sampleData}).
            success(function(data, status, headers, config) {
                // $rootScope.sampleData = data;
                //$scope.sampleData.data = data;
                //$location.path("/sampleResults").replace();
            	
            	$scope.sampleData = data;
            	
            	$scope.groupAccesses = $scope.sampleData.groupAccesses;
                $scope.userAccesses = $scope.sampleData.userAccesses;
                
                $scope.loader = false;
                $scope.accessExists = true;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                // $rootScope.sampleData = data;
                $scope.loader = false;
                $scope.messages = data;
            });
        
    }; 
    
    $scope.editUserAccessSection = function(loginName, userAccess) {
        $scope.addAccess=true;
        $scope.accessForm.theAccess.accessBy='user';
        $scope.accessForm.theAccess.userBean.loginName=loginName;
        $scope.showCollaborationGroup=false;
        $scope.showAccessuser=true;
        $scope.showAccessSelection=false;

        for(var key in $scope.csmRoleNames){
            if($scope.csmRoleNames[key] == userAccess){
                $scope.accessForm.theAccess.roleName = key;
            }
        }
    }

    $scope.editGroupAccessSection = function(groupName, groupAccess) {
        $scope.addAccess=true;
        $scope.accessForm.theAccess.accessBy='group';
        $scope.accessForm.theAccess.groupName=groupName;
        $scope.showCollaborationGroup=true;
        $scope.showAccessuser=false;
        $scope.showAccessSelection=false;

        for(var key in $scope.csmRoleNames){
            if($scope.csmRoleNames[key] == groupAccess){
                $scope.accessForm.theAccess.roleName = key;
            }
        }
    }

    
    $scope.removeAccessSection = function() {
        $scope.sampleData.theAccess = $scope.accessForm.theAccess;
        $scope.addAccess=false;
        $scope.showAddAccessButton=true;
        
        console.log($scope.sampleData.theAccess);
        
        if (confirm("Are you sure you want to delete?")) {
            $scope.loader = true;
        
            $http({method: 'POST', url: '/caNanoLab/rest/sample/deleteAccess',data: $scope.sampleData}).
                success(function(data, status, headers, config) {
                    // $rootScope.sampleData = data;
                    //$scope.sampleData.data = data;
                    //$location.path("/sampleResults").replace();
                	
                	$scope.sampleData = data;
                	
                	$scope.groupAccesses = $scope.sampleData.groupAccesses;
                    $scope.userAccesses = $scope.sampleData.userAccesses;
                    
                    if( $scope.userAccesses != null && $scope.userAccesses.length > 0 ) {
	                	$scope.accessExists = true;
	                }
                    
	                if( $scope.groupAccesses != null && $scope.groupAccesses.length > 1 ) {
	                	$scope.accessExists = true;
	                }
                    
                    $scope.loader = false;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                });
        }
        
    };   
    
    /** End - Access Section **/        

    $scope.master = angular.copy($scope.sampleData);
    $scope.reset();
})

.controller('modalCtrl', function ($scope, $modalInstance,id) {
    $scope.closeModal = function(val) {
        if (val) {
            $modalInstance.close(val);            
        }
        else {
            $modalInstance.dismiss();
        }

    }
});