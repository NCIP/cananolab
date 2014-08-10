'use strict';

var app = angular.module('angularApp')
.controller('editSampleCtrl', function (sampleService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;
    $scope.pocData = sampleService.pocData;
    $scope.scratchPad = sampleService.scratchPad;
    $scope.master = {};
    $scope.newKeyword = "";
    
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
        /* if no hostname then populate with raw data */
    	console.info("No REST Service, loading hard coded sampleData")
        var hardCodedSampleData = [{"sampleName":"Gaheen_Test","sampleId":38404103,"userIsCurator":true,"pointOfContacts":[{"id":15695942,"contactPerson":"","organization":{"id":11796483,"name":"NCL"},"role":"","address":{"line1":"CBER, FDA, 1401 Rockville Pike","line2":"HFM 335","city":"Rockville","stateProvince":"MD","zip":"20852-1448","country":"USA"},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","sampleId":0,"primaryContact":true}],"keywords":[],"accessToSample":{"userAccesses":[],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","sampleId":38404103}]},"dataAvailability":null,"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","skoczens":"Skoczen, Sarah","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]}];
        hardCodedSampleData = [{"sampleName":"WUSTL-JMarshIEEEUS2000-01","sampleId":3735561,"userIsCurator":true,"pointOfContacts":[{"id":15695884,"contactPerson":"","organization":{"id":11796482,"name":"WUSTL"},"role":"investigator","address":{"line1":"","line2":"","city":"Saint Louis","stateProvince":"MO","zip":"","country":"USA"},"firstName":"Jon","lastName":"Marsh","middleInitial":"","phoneNumber":"","email":"","sampleId":0,"primaryContact":true},{"id":15695945,"contactPerson":"","organization":{"id":11796482,"name":"WUSTL"},"role":"investigator","address":{"line1":"","line2":"","city":"Saint Louis","stateProvince":"MO","zip":"","country":"USA"},"firstName":"Samuel","lastName":"Wickline","middleInitial":"","phoneNumber":"","email":"","sampleId":0,"primaryContact":true},{"id":15695949,"contactPerson":"","organization":{"id":11796482,"name":"WUSTL"},"role":"investigator","address":{"line1":"","line2":"","city":"Saint Louis","stateProvince":"MO","zip":"","country":"USA"},"firstName":"Gregory","lastName":"Lanza","middleInitial":"","phoneNumber":"","email":"","sampleId":0,"primaryContact":true}],"keywords":["ACOUSTIC MICROSCOPY","AVIDIN","BIOTIN","FIBRIN CLOTS","LIPID MONOLAYER","PERFLUOROCARBON","THROMBIN","ULTRASOUND"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","sampleId":3735561},{"groupName":"Public","roleName":"","roleDisplayName":"read","loginName":"","accessBy":"","sampleId":3735561}]},"dataAvailability":{"sampleName":"WUSTL-JMarshIEEEUS2000-01","dataAvailability":"caNanoLab: 30%; MINChar: 33%","caNanoLabScore":"30.0% (9 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"availableEntityNames":["sample function","functionalizing entities","general sample information","publications","attachment","sample composition","size","encapsulation","nanomaterial entities"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","skoczens":"Skoczen, Sarah","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]}];
        hardCodedSampleData = [{"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02","cloningSampleName":null,"sampleId":27131907,"userIsCurator":true,"pointOfContacts":[{"id":43548673,"contactPerson":"","organization":{"id":43581441,"name":"Chris"},"role":"Chris Contact Role","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"Chris","lastName":"Nice","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":true},{"id":27066369,"contactPerson":"","organization":{"id":27099137,"name":"UMC_RadiolD"},"role":"investigator","address":{"line1":"Department of Radiology","line2":"University of Missouri-Columbia","city":"Columbia","stateProvince":"MO","zip":"65212","country":"USA"},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":true}],"keywords":["GOLD","GUM ARABIC","IN VIVO","THERAPEUTIC EFFICACY","TUMOR"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","sampleId":27131907},{"groupName":"Public","roleName":"","roleDisplayName":"read","loginName":"","accessBy":"","sampleId":27131907}]},"dataAvailability":{"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02","dataAvailability":"caNanoLab: 16%; MINChar: 11%","caNanoLabScore":"16.0% (5 out of 30)","mincharScore":"11.0% (1 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"availableEntityNames":["publications","sample composition","sample function","general sample information","nanomaterial entities"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","Chris 2","Chris Org","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","skoczens":"Skoczen, Sarah","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]}];
        hardCodedSampleData = [{"sampleName":"NCL-23-1","cloningSampleName":null,"sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":20884736,"contactPerson":"","organization":{"id":20884736,"name":"DNT"},"role":"","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":true},{"id":43646976,"contactPerson":"","organization":{"id":35782656,"name":"BB_SH_DFCI_WCMC_BWH_MIT"},"role":"Chris Contact Role","address":{"line1":"","line2":"","city":"","stateProvince":"","zip":"","country":""},"firstName":"","lastName":"","middleInitial":"","phoneNumber":"","email":"","dirty":false,"primaryContact":false}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"groupName":"Curator","roleName":"","roleDisplayName":"read update delete","loginName":"","accessBy":"","dirty":false},{"groupName":"Public","roleName":"","roleDisplayName":"read","loginName":"","accessBy":"","dirty":false}]},"dataAvailability":{"sampleName":"NCL-23-1","dataAvailability":"caNanoLab: 53%; MINChar: 33%","caNanoLabScore":"53.0% (16 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"availableEntityNames":["general sample information","size","blood contact","cytotoxicity","immune cell function","functionalizing entities","sample composition","association","sample function","purity","encapsulation","publications","molecular weight","solubility","nanomaterial entities","physical state"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","Chris 2","Chris Org","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","SY Org","SY company","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE","my org","natcompany","other","shanyang","shanyangcomp"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"roleNames":{"R":"read","CURD":"read update delete"},"showReviewButton":false,"errors":[]}] ;

        $scope.sampleData = hardCodedSampleData[0];
        $scope.loader = false;
        $scope.editSampleForm = true;

    } else {

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



// * editSample Button Bar
    $scope.delete = function() {

    };
    $scope.copy = function() {
        //Submit a copy of $root.master with the samplName changed
        $modal.open({
            templateUrl: 'views/sample/edit/modal/copySample.html',
            backdrop: true,
            windowClass: 'modal',
            controller: function ($scope, $modalInstance, sampleName) {
                $scope.copyOfSampleData = angular.copy($scope.master);
                $scope.copyOfSampleData.sampleName = sampleName;
                $scope.submit = function () {
                    console.log('Submiting user info.');
                    console.log(user);
                    $modalInstance.dismiss('cancel');

                }
                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            },
            resolve: {
                copyOfSampleData: function () {
                    return $scope.copyOfSampleData;
                }
            }
        });
    };
    $scope.reset = function() {
         $scope.sampleData = angular.copy($scope.master);
    };
    $scope.update = function() {
        console.info("Rest call here.  /caNanoLab/rest/sample/updateSample");
        $http({method: 'POST', url: '/caNanoLab/rest/sample/updateSample',data: $scope.sampleData}).
        success(function(data, status, headers, config) {
            //alert(data);
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
            alert(data);
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
            }
          }
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
                        alert(data);
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
                sampleData: function() {
                  return data;
                }
              }
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
});