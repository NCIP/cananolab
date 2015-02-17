'use strict';
var app = angular.module('angularApp')
  .controller('PublicationCtrl', function (sampleService, navigationService, groupService, $rootScope,$scope,$http,$location,$filter,$routeParams) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   
    $scope.sampleData = sampleService.sampleData;
    //$scope.sampleData = {"data":[{"sampleId":27131907,"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02","pointOfContact":"UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA","composition":["MetalParticle"],"functions":["ImagingFunction"],"characterizations":[],"dataAvailability":"N/A","createdDate":1275683278000},{"sampleId":27131906,"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","pointOfContact":"UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA","composition":["MetalParticle"],"functions":[],"characterizations":["BloodContact","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1275683278000},{"sampleId":12451840,"sampleName":"GATECH_UCSF-EDickersonCL2008-01","pointOfContact":"GATECH_UCSF<br>Georgia Institute of Technology<br>Atlanta GA 30332","composition":["OtherNanomaterialEntity"],"functions":["ImagingFunction","TherapeuticFunction"],"characterizations":["Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":1240200000000},{"sampleId":11337795,"sampleName":"NCL-MGelderman-IJN2008-02","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","composition":["Fullerene"],"functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1238731200000},{"sampleId":11337794,"sampleName":"NCL-MGelderman-IJN2008-01","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","composition":["Fullerene"],"functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1238731200000},{"sampleId":24063238,"sampleName":"NCL-49","pointOfContact":"Mark Kester PSU","composition":["Liposome"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":1181275200000},{"sampleId":24063237,"sampleName":"NCL-48","pointOfContact":"Mark Kester PSU","composition":["Liposome"],"functions":[],"characterizations":["Cytotoxicity","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1181275200000},{"sampleId":24063236,"sampleName":"NCL-45","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 26%; MINChar: 33%","createdDate":1179374400000},{"sampleId":24063235,"sampleName":"NCL-42","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","OxidativeStress","Purity","Size","Surface"],"dataAvailability":"caNanoLab: 40%; MINChar: 44%","createdDate":1179374400000},{"sampleId":24063234,"sampleName":"NCL-19","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000},{"sampleId":24063233,"sampleName":"NCL-17","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity"],"dataAvailability":"caNanoLab: 20%; MINChar: 11%","createdDate":1179374400000},{"sampleId":24063232,"sampleName":"NCL-16","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000},{"sampleId":20917514,"sampleName":"NCL-51-3","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917513,"sampleName":"NCL-50-1","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917512,"sampleName":"NCL-49-2","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917511,"sampleName":"NCL-48-4","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917510,"sampleName":"NCL-26-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917509,"sampleName":"NCL-25-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917508,"sampleName":"NCL-24-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction"],"dataAvailability":"caNanoLab: 30%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":"DNT","composition":["OtherNanomaterialEntity","Dendrimer"],"functions":["ImagingFunction","endosomolysis"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 50%; MINChar: 33%","createdDate":1152504000000},{"sampleId":20917506,"sampleName":"NCL-22-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 40%; MINChar: 33%","createdDate":1152504000000},{"sampleId":20917505,"sampleName":"NCL-21-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity"],"dataAvailability":"caNanoLab: 20%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917504,"sampleName":"NCL-20-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1152504000000}]}
    $scope.sampleId = sampleService.sampleId;
    $scope.publicationCategories=[];
    $scope.masterPublicationCategories = ["book chapter","editorial","peer review article","proceeding","report","review"];

    if ($routeParams.sampleId) {
      $scope.sampleId.data = $routeParams.sampleId;
    };

    if ($routeParams.isAdvancedSearch) {
      $scope.isAdvancedSearch = 1;
    };      
    

    $scope.goBack = function() {
        if ($scope.isAdvancedSearch) {
          $location.path("/advancedSampleSearch").replace();           
        }
        else {
          $location.path("/sampleResults").replace();           
        }        
      $location.search('sampleId', null);      };

    $scope.select = function(tab) {
        var size = 0, key;
        for (key in $scope.masterPublicationCategories) {
          size+=1
        };

        for (var x=0; x<size;x++) {
            if (tab>=0) {
                if (x==tab){
                    $scope['show'+x]=false;                
                }
                else {
                    $scope['show'+x]=true;
                } 
            }    
            else {
                $scope['show'+x]=false;
            }      
        }
    };    
    

    // $scope.data = {"publicationCategories":["report","review"],"category2Publications":{"report":[{"displayName":"DENDRITIC NANOTECHNOLOGIES 122006. <a href=rest/publication/download?fileId=23178496 target='_self'>View</a>.","researchAreas":null,"description":"Dendrimer-Based MRI Contrast Agents prepared for Dendritic Nanotechnologies, December, 2006","keywordsDisplayName":"","keywordsStr":"","status":"published"}],"review":[{"displayName":"Hall, JB, Dobrovolskaia, MA, Patri, AK, McNeil, SE. Characterization of nanoparticles for therapeutics. Nanomedicine (London, England). 2007; 2:789-803. <a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/18095846>PMID: 18095846</a>.","researchAreas":["characterization","in vitro","in vivo"],"description":"Nanotechnology offers many advantages to traditional drug design, delivery and medical diagnostics; however, nanomedicines present considerable challenges for preclinical development. Nanoparticle constructs intended for medical applications consist of a wide variety of materials, and their small size, unique physicochemical properties and biological activity often require modification of standard characterization techniques. A rational characterization strategy for nanomedicines includes physicochemical characterization, sterility and pyrogenicity assessment, biodistribution (absorption, distribution, metabolism and excretion [ADME]) and toxicity characterization, which includes both in vitro tests and in vivo animal studies. Here, we highlight progress for a few methods that are uniquely useful for nanoparticles or are indicative of their toxicity or efficacy.","keywordsDisplayName":"CHARACTERIZATION<br>CHARGE<br>NANOPARTICLE<br>SIZE<br>TOXICITY","keywordsStr":"CHARACTERIZATION\r\nCHARGE\r\nNANOPARTICLE\r\nSIZE\r\nTOXICITY","status":"published"},{"displayName":"Dobrovolskaia, MA, McNeil, SE. Immunological properties of engineered nanomaterials. Nature nanotechnology. 2007; 2:469-478. <a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/18654343>PMID: 18654343</a>.","researchAreas":["characterization","in vitro","in vivo"],"description":"Most research on the toxicology of nanomaterials has focused on the effects of nanoparticles that enter the body accidentally. There has been much less research on the toxicology of nanoparticles that are used for biomedical applications, such as drug delivery or imaging, in which the nanoparticles are deliberately placed in the body. Moreover, there are no harmonized standards for assessing the toxicity of nanoparticles to the immune system (immunotoxicity). Here we review recent research on immunotoxicity, along with data on a range of nanotechnology-based drugs that are at different stages in the approval process. Research shows that nanoparticles can stimulate and/or suppress the immune responses, and that their compatibility with the immune system is largely determined by their surface chemistry. Modifying these factors can significantly reduce the immunotoxicity of nanoparticles and make them useful platforms for drug delivery.","keywordsDisplayName":"IMMUNOTOXICITY<br>SURFACE CHEMISTRY","keywordsStr":"IMMUNOTOXICITY\r\nSURFACE CHEMISTRY","status":"published"}]}};    
    //             $scope.publicationCategories = $scope.data.publicationCategories;
    //             $scope.publicationBean = $scope.data.publicationBean;
    //             $scope.category2Publications = $scope.data.category2Publications;

    //$scope.$on('$viewContentLoaded', function(){
        $scope.loader = true;
        
        $http({method: 'GET', url: '/caNanoLab/rest/publication/summaryView?sampleId=' + $scope.sampleId.data}).
            success(function(data, status, headers, config) {
                $scope.publicationData = data;
                $scope.publicationCategories = data.publicationCategories;
                $scope.publicationBean = data.publicationBean;
                $scope.category2Publications = data.category2Publications;
                $scope.sampleName = sampleService.sampleName($scope.sampleId.data);
                
                $scope.loader = false;
                
                // Create a list of categories to display
                if( $rootScope.tabs.userLoggedIn ) {
        	        for (var i=0; i<$scope.publicationCategories.length; i++) {
        	            var index = $scope.masterPublicationCategories.indexOf($scope.publicationCategories[i]);
        	            if (index > -1) {
        	                $scope.masterPublicationCategories.splice(index, 1);
        	            }
        	        }
        	        $scope.masterPublicationCategories =  $scope.masterPublicationCategories.concat($scope.publicationCategories);
                }
                else {
                	$scope.masterPublicationCategories = $scope.publicationCategories;
                }
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
                $scope.loader = false;

            });
    //});
        

        
        $scope.print = function() {
        	window.open('views/sample/view/printPublication.html?sampleId=' + $scope.sampleId.data+'&sampleName='+$scope.sampleName.name);
        };

  });
