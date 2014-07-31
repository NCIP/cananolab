'use strict';

var app = angular.module('angularApp')
.controller('editSampleCtrl', function (sampleService,navigationService,groupService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal,$routeParams) {

//var app = angular.module('angularApp').app.controller('editSampleCtrl', function (sampleService,utilsService,navigationService, groupService, $rootScope,$scope,$http,$location,$filter,$routeParams) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.sampleId = sampleService.sampleId;
    $scope.dataChanaged = 0;
    $scope.newKeyword = "";

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
        $scope.sampleId.data = $routeParams.sampleId;
    } else {
        $scope.message = "Need to add a sampleId to the url";
    }

    //Print out the scope for this page
    console.log("scope");
    console.dir($scope);
    console.log("rootScope");
    console.dir($rootScope);

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
        var sampleData = [{"sampleName":"NCL-23-1","sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":20884736,"contactPerson":"Steve Johnson","address":null,"firstName":"Raghuraman","lastName":"Kannan","middleInitial":null,"phoneNumber":"207 345-8247","email":"steve@donotmail.org","role":"investigator","primaryContact":true,"organizationName":"DNT"}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"sampleId":20917507,"groupName":"Curator","roleDisplayName":"read update delete","loginName":null,"accessBy":null},{"sampleId":20917507,"groupName":"Public","roleDisplayName":"read","loginName":null,"accessBy":null}]},"dataAvailability":{"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"errors":[]}];
        var sampleData2 = [{"sampleName":"NCL-23-1","sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":20884736,"contactPerson":"Steve Johnson","address":null,"firstName":"Raghuraman","lastName":"Kannan","middleInitial":null,"phoneNumber":"207 345-8247","email":"steve@donotmail.org","role":"investigator","primaryContact":true,"organizationName":"DNT"}, {"id":20884737,"contactPerson":"Larry Miller","address":null,"firstName":"Larry","lastName":"Miller","middleInitial":null,"phoneNumber":"207 345-9982","email":"larry.miller@donotmail.org","role":"investigator","primaryContact":false,"organizationName":"DNT"}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"sampleId":20917507,"groupName":"Curator","roleDisplayName":"read update delete","loginName":null,"accessBy":null},{"sampleId":20917507,"groupName":"Public","roleDisplayName":"read","loginName":null,"accessBy":null}]},"dataAvailability":{"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"errors":[]}];
        /* Sample Data 3, July 25 */
        var sampleData3 = [{"sampleName":"NCL-23-1","sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":20884736,"contactPerson":null,"address":null,"firstName":"","lastName":"","middleInitial":null,"phoneNumber":null,"email":null,"role":"","primaryContact":true,"organizationName":"DNT"}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"sampleId":20917507,"groupName":"Curator","roleDisplayName":"read update delete","loginName":null,"accessBy":null,"accessRight":null},{"sampleId":20917507,"groupName":"Public","roleDisplayName":"read","loginName":null,"accessBy":null,"accessRight":null}]},"dataAvailability":{"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"errors":[]}];
        var sampleData4 = [{"sampleName":"NCL-23-1","sampleId":20917507,"userIsCurator":true,"pointOfContacts":[{"id":20884736,"contactPerson":null,"address":null,"firstName":"","lastName":"","middleInitial":null,"phoneNumber":null,"email":null,"role":"","primaryContact":true,"organizationName":"DNT"},{"id":43646976,"contactPerson":null,"address":null,"firstName":"Shan","lastName":"Yang","middleInitial":null,"phoneNumber":null,"email":null,"role":"Chris Contact Role","primaryContact":true,"organizationName":"BB_SH_DFCI_WCMC_BWH_MIT"}],"keywords":["MAGNEVIST","MRI","NCL-23"],"accessToSample":{"userAccesses":[],"groupAccesses":[{"sampleId":20917507,"groupName":"Curator","roleDisplayName":"read update delete","loginName":null,"accessBy":null,"accessRight":null},{"sampleId":20917507,"groupName":"Public","roleDisplayName":"read","loginName":null,"accessBy":null,"accessRight":null}]},"dataAvailability":{"caNanoLabScore":"50.0% (15 out of 30)","mincharScore":"33.0% (3 out of 9)","chemicalAssocs":["attachment","encapsulation","entrapment"],"physicoChars":["molecular weight","physical state","purity","relaxivity","shape","size","solubility","surface"],"invitroChars":["blood contact","cytotoxicity","enzyme induction","immune cell function","metabolic stability","oxidative stress","sterility","targeting","transfection"],"invivoChars":["pharmacokinetics","toxicology"],"caNano2MINChar":{"zeta potential":"surface charge","sample composition":"chemical composition","purity":"purity","surface charge":"surface charge","shape":"shape","attachment":"surface chemistry","surface area":"surface area","size":"particle size/size distribution"}},"organizationNamesForUser":["AIST_HTRC","BB_SH_DFCI_WCMC_BWH_MIT","BB_SH_KCI_DFCI_WCMC_BWH_MIT","BROWN_STANFORD","BWH_AnesthesiologyD","C-Sixty (CNI)","CAS_VT_VCU","CLM_UHA_CDS_INSERM","CP_UCLA_CalTech","CWRU","Childrens Hospital Los Angeles","Chris","DC","DNT","DWU_SNU_SU_US","GATECH","GATECH_EMORY","GATECH_UCSF","GIST_LifeScienceD","HarvardU_PhysD","Harvard_MIT_DHST","JHU_KSU","JSTA_FHU_NEC_MU_AIST_JAPAN","JSTA_JFCR_NEC_FHU_TUSM_NIAIST","JST_AIST_FHU_TU_NEC_MU_JAPAN","Joe Barchi","KI","KU_JSTC_JAPAN","LMRT","MIT_ChemD","MIT_ChemEngineeringD","MIT_LMRT","MIT_MGH","MIT_MGH_GIST","MIT_UC_BBIC_HST_CU","MSKCC_CU_UA","Mansoor Amiji","Mark Kester","Mark Kester PSU","NCL","NEU","NEU_DPS","NEU_MGH_UP_FCCC","NEU_MIT_MGH","NIEHS","NIOSH","NRCWE","NWU","NWU_ChemD_IIN","Nanotechnology Characterization Laboratory","Nanotechnology_Characterization_Laboratory","New One","PNNL_CBBG","PURDUE","PURDUEU_BN","RIT_KI_SU","SNL_UNM","SNU_CHINA","STANFORD","STANFORD_ChemD","STANFORD_MIPS","STANFORD_OM","SUNYB_ILPB","Sharon","TAM_UT","TTU","UAM_CSIC_IMDEA","UCSD_ChemBiochemD","UCSD_MIT_MGH","UC_HU_UEN_GERMANY","UI","UKY","UL_NL","UM","UM-C","UMC_DVP","UMC_RadiolD","UN","UNC","UNC_ChemD","UTHSCH_UMG_MDACC_RU_UTA","UT_UMG_MDACC_RU_UTA","UToronto","VCU_VT_EHC","WSU","WSU_DPS","WUSTL","WUSTL_DIM","WU_SU_CHINA","YALE"],"contactRoles":["Chris Contact Role","investigator","manufacturer"],"allGroupNames":["Carbon Tube Group","Demo University","Demo-Collaboration","NCI group","NCNHIR","Test Collaboration"],"filteredUsers":{"lethai":"Le, Thai","Omelchen":"Omelchenko, Marina","burnskd":"Burns, Kevin","canano_guest":"Guest, Guest","grodzinp":"Grodzinski, Piotr","swand":"Swan, Don","sternstephan":"Stern, Stephan","zolnik":"Zolnik, Banu","canano_res":"Researcher, CaNano","hunseckerk":"Hunsecker, Kelly","lipkeyfg":"Lipkey, Foster","marina":"Dobrovolskaia, Marina","pottert":"Potter, Tim","uckunf":"Uckun, Fatih","michal":"Lijowski, Michal","mcneils":"Mcneil, Scott","neunb":"Neun, Barry","cristr":"Crist, Rachael","zhengji":"Zheng, Jiwen","SchaeferH":"Schaefer, Henry","frittsmj":"Fritts, Martin","benhamm":"Benham, Mick","masoods":"Masood, Sana","mclelandc":"McLeland, Chris","torresdh":"Torres, David","KlemmJ":"Klemm, Juli","patria":"Patri, Anil","hughesbr":"Hughes, Brian","clogstonj":"Clogston, Jeff","hinkalgw":"Hinkal, George","MorrisS2":"Morris, Stephanie","sharon":"Gaheen, Sharon"},"showReviewButton":false,"errors":[]}];
        $scope.sampleData = sampleData3[0];
        $scope.sampleId = '20917507';
        $scope.loader = false;
        $scope.editSampleForm = true;

    } else {
        /* if hostname fetch the data */
        /* REST: http://localhost:8080/caNanoLab/rest/sample/edit?sampleId=20917507 */
        //$http({method: 'GET', url: '/caNanoLab/rest/sample/edit?sampleId=20917507'+$scope.sampleId.data}).
    	//alert("What is the sampleId?");
        $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/sample/edit?sampleId='+$scope.sampleId.data}).
            success(function(data, status, headers, config, statusText) {
                $scope.sampleData = data;
                $scope.loader = false;
                $scope.editSampleForm = true;
            }).
            error(function(data, status, headers, config, statusText) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                if(status != 200){
                    $scope.message = "Response code " + status.toString() + ":  " + statusText;
                    console.log($scope.message);
                    console.dir(data);
                } else {
                    $scope.message = data;
                }
                $scope.loader = false;
                $scope.submissionView = false;
        });
    }

/*
  	$.each( keyword, function( key, $scope.sampleData.keywords ) {
        $scope.scratchPad.keywords.push(keyword+"\n");
    }
    for(var keyword; t<temp.length; i++) {
        values[key].push(parseFloat(temp[i]));
    }
*/
    //Format keywords for textarea add watch if data changes
    $scope.getKeywords = function() {
        if( Object.prototype.toString.call( $scope.sampleData.keywords ) === '[object Array]' ) {
              $scope.keywords = $scope.sampleData.keywords.join("\n");
        } else {
              $scope.keywords = "";

        }
    };
    $scope.$watch(
        'sampleData',
        function() {
            console.log("dataChanged = "+$scope.dataChanged);
            $scope.getKeywords();
            $scope.dataChanged++;
        },
        true
    );

    $scope.getKeywords();

//Modal for Add keyword
    $scope.openAccessToSampleModal = function(sampleId) {

        var modalInstance = $modal.open({
            templateUrl: '/views/sample/edit/modal/accessToSampleModal.html',
            controller: 'keywordModalCtrl',
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
    };
/*
    $scope.updateSampleName = function() {
        $scope.sampleData.sampleName = what?;

    };
*/
//Add keyword
    $scope.addKeyword=function(){
        if( Object.prototype.toString.call( $scope.sampleData.keywords ) === '[object Array]' ) {
            console.log("$scope.sampleData.keywords is an array.")
            console.dir($scope.sampleData.keywords);
        }
        console.dir($scope.newKeyword);
        if($scope.newKeyword.length > 1) {
            $scope.sampleData.keywords.push($scope.newKeyword.toUpperCase());
            $scope.newKeyword = "";
        }
    };
//Remove keyword
    $scope.removeKeyword = function(item) {
      var index = $scope.sampleData.keywords.indexOf(item)
      $scope.sampleData.keywords.splice(index, 1);
    }
    $scope.delete = function() {

    };
    $scope.copy = function() {

    };
    $scope.reset = function() {
        $location.path("/editSample").replace();
        $location.search('sampleId', $scope.sampleData.sampleId);
    };
    $scope.update = function() {

    };

});