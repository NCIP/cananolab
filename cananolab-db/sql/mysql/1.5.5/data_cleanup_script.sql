/* Amount Unit of Composing Element 

select * from canano.common_lookup where name = 'composing element' and value = 'microgram/microL';  */

update canano.common_lookup
set value = 'uCi/mg'
where name = 'composing element' and value = 'micro Ci/mg';

update canano.common_lookup
set value = 'ug'
where name = 'composing element' and value = 'micro g';

update canano.common_lookup
set value = 'uL'
where name = 'composing element' and value = 'micro L';

update canano.common_lookup
set value = 'uM'
where name = 'composing element' and value = 'micro M';

update canano.common_lookup
set value = 'ug/mL'
where name = 'composing element' and value = 'microg/mL';

update canano.common_lookup
set value = 'umol'
where name = 'composing element' and value = 'micromol';

update canano.common_lookup
set value = 'mL'
where name = 'composing element' and value = 'milli L';

update canano.common_lookup
set value = 'ug/uL'
where name = 'composing element' and value = 'microgram/microL';

/* Amount Unit of Functionalized Element

select * from canano.common_lookup where name = 'functionalizing entity' and value = 'microgram/microL';  */

update canano.common_lookup
set value = 'ug'
where name = 'functionalizing entity' and value = 'micro g';

update canano.common_lookup
set value = 'uL'
where name = 'functionalizing entity' and value = 'micro L';

update canano.common_lookup
set value = 'uM'
where name = 'functionalizing entity' and value = 'micro M';

update canano.common_lookup
set value = 'uCi/mg'
where name = 'functionalizing entity' and value = 'microCi/mg';

update canano.common_lookup
set value = 'ug/uL'
where name = 'functionalizing entity' and value = 'microgram/microL';

/* Update Associated_element */
update canano.associated_element
set value_unit = 'uCi/mg'
where value_unit = 'micro Ci/mg';

update canano.associated_element
set value_unit = 'ug'
where value_unit = 'micro g';

update canano.associated_element
set value_unit = 'uL'
where value_unit = 'micro L';

update canano.associated_element
set value_unit = 'uM'
where value_unit = 'micro M';

update canano.associated_element
set value_unit = 'ug/mL'
where value_unit = 'microg/mL';

update canano.associated_element
set value_unit = 'umol'
where value_unit = 'micromol';

update canano.associated_element
set value_unit = 'mL'
where value_unit = 'milli L';

update canano.associated_element
set value_unit = 'ug/uL'
where value_unit = 'microgram/microL';

/* Remove Dupes and Typos 

select * from canano.common_lookup where name = 'molecular weight'
and value = 'gel permeation chromotography';

select * from canano.common_lookup where name = 'size'
and value = 'gel permeation chromatograhy';

*/

delete from canano.common_lookup where name = 'molecular weight'
and value = 'gel permeation chromotography';

delete from canano.common_lookup where name = 'size'
and value = 'gel permeation chromatograhy';

delete from canano.common_lookup where value = 'uv-vis  absorbance spectroscopy';

UPDATE canano.characterization
set assay_type = 'uv-vis absorbance spectroscopy'
WHERE assay_type ='uv-vis  absorbance spectroscopy';

/* Inherent Function Imaging Modality Type 

Remove Bioluminiscnce

select * from canano.common_lookup where name = 'imaging function' and value = 'Bioluminiscnce'; */

delete from canano.common_lookup where name = 'imaging function' and value = 'Bioluminiscnce';

/* Physicochemical Characterizations

   Characterization Name  surface
   
   Move  all assay types except surface to Characterization Name other_pc

select * from canano.common_lookup where name = 'other' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
order by value;

select * from canano.common_lookup where name = 'surface'
and value IN ( 
'Raman spectroscopy',
'fluorescence  lifetime',
'FT-IR spectroscopy',
'FTIR spectroscopy',
'gel electrophoresis',
'photoluminescence spectrophotometry',
'protein assay',
'uv-vis absorbance spectroscopy',
'uv-vis absorbance spectrometry',
'uv-vis absorbance spectrophotometry ',
'uv-vis  absorbance spectroscopy',
'uv-vis absorption spectrometry',
'uv-vis absorption spectroscopy',
'uv-vis spectroscopy',
'x-ray photoelectron spectroscopy'
);  

select assay_type, discriminator, other_char_assay_category, other_char_name from canano.characterization where assay_type IN ( 
'Raman spectroscopy',
'fluorescence  lifetime',
'FT-IR spectroscopy',
'FTIR spectroscopy',
'gel electrophoresis',
'photoluminescence spectrophotometry',
'protein assay',
'uv-vis absorbance spectroscopy',
'uv-vis absorbance spectrometry',
'uv-vis absorbance spectrophotometry ',
'uv-vis  absorbance spectroscopy',
'uv-vis absorption spectrometry',
'uv-vis absorption spectroscopy',
'uv-vis spectroscopy',
'x-ray photoelectron spectroscopy'
)
and discriminator = 'Surface';

*/

update canano.common_lookup
set name = 'other_pc'
WHERE name = 'surface'
and value IN ( 
'Raman spectroscopy',
'fluorescence  lifetime',
'FT-IR spectroscopy',
'FTIR spectroscopy',
'gel electrophoresis',
'photoluminescence spectrophotometry',
'protein assay',
'uv-vis absorbance spectroscopy',
'uv-vis absorbance spectrometry',
'uv-vis absorbance spectrophotometry ',
'uv-vis  absorbance spectroscopy',
'uv-vis absorption spectrometry',
'uv-vis absorption spectroscopy',
'uv-vis spectroscopy',
'x-ray photoelectron spectroscopy'
);

UPDATE canano.characterization
set discriminator = 'OtherCharacterization',
other_char_name = 'other_pc',
other_char_assay_category = 'physico-chemical characterization',
surface_is_hydrophobic = null
WHERE assay_type IN ( 
'Raman spectroscopy',
'fluorescence  lifetime',
'FT-IR spectroscopy',
'FTIR spectroscopy',
'gel electrophoresis',
'photoluminescence spectrophotometry',
'protein assay',
'uv-vis absorbance spectroscopy',
'uv-vis absorbance spectrometry',
'uv-vis absorbance spectrophotometry ',
'uv-vis  absorbance spectroscopy',
'uv-vis absorption spectrometry',
'uv-vis absorption spectroscopy',
'uv-vis spectroscopy',
'x-ray photoelectron spectroscopy'
)
and discriminator = 'Surface';

/* Physicochemical Characterizations

   Characterization Name  other
   
   Move all assay types to Characterization Name other_pc
   
select * from canano.common_lookup where name = 'other' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and VALUE in (
'acoustic microscopy',
'controlled aggregation',
'cyclodextrin content',
'DNA quantification',
'dye content',
'dye release',
'elemental analysis',
'elemental composition',
'Gd2O3 content',
'ICP-AES Analysis of Gd2O3 content in Gd2O3@SWNHag',
'imaging',
'iron content',
'lipid-induced fluorescence enhancement',
'magnetic properties',
'magnetic property',
'Ninhydrin Assay',
'nuclear magnetic resonance',
'protein adsorption',
'Quantification of drug loading',
'quantification of drug payload',
'RBITC content',
'RBITC release',
'scanning transmission electron microscopy',
'signal contrast enhancement',
'stability',
'Thermogravimetric Analysis of quantity of Gd2O3 in Gd2O3@SWNHag',
'X-ray diffraction'
);

select * from canano.characterization where assay_type IN ( 
'acoustic microscopy',
'controlled aggregation',
'cyclodextrin content',
'DNA quantification',
'dye content',
'dye release',
'elemental analysis',
'elemental composition',
'Gd2O3 content',
'ICP-AES Analysis of Gd2O3 content in Gd2O3@SWNHag',
'imaging',
'iron content',
'lipid-induced fluorescence enhancement',
'magnetic properties',
'magnetic property',
'Ninhydrin Assay',
'nuclear magnetic resonance',
'protein adsorption',
'Quantification of drug loading',
'quantification of drug payload',
'RBITC content',
'RBITC release',
'scanning transmission electron microscopy',
'signal contrast enhancement',
'stability',
'Thermogravimetric Analysis of quantity of Gd2O3 in Gd2O3@SWNHag',
'X-ray diffraction'
)
AND other_char_name = 'other';

*/

update canano.common_lookup
set name = 'other_pc'
WHERE name = 'other'
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and value IN (
'acoustic microscopy',
'controlled aggregation',
'cyclodextrin content',
'DNA quantification',
'dye content',
'dye release',
'elemental analysis',
'elemental composition',
'Gd2O3 content',
'ICP-AES Analysis of Gd2O3 content in Gd2O3@SWNHag',
'imaging',
'iron content',
'lipid-induced fluorescence enhancement',
'magnetic properties',
'magnetic property',
'Ninhydrin Assay',
'nuclear magnetic resonance',
'protein adsorption',
'Quantification of drug loading',
'quantification of drug payload',
'RBITC content',
'RBITC release',
'scanning transmission electron microscopy',
'signal contrast enhancement',
'stability',
'Thermogravimetric Analysis of quantity of Gd2O3 in Gd2O3@SWNHag',
'X-ray diffraction'
);

UPDATE canano.characterization
set other_char_name = 'other_pc'
WHERE assay_type IN (
'acoustic microscopy',
'controlled aggregation',
'cyclodextrin content',
'DNA quantification',
'dye content',
'dye release',
'elemental analysis',
'elemental composition',
'Gd2O3 content',
'ICP-AES Analysis of Gd2O3 content in Gd2O3@SWNHag',
'imaging',
'iron content',
'lipid-induced fluorescence enhancement',
'magnetic properties',
'magnetic property',
'Ninhydrin Assay',
'nuclear magnetic resonance',
'protein adsorption',
'Quantification of drug loading',
'quantification of drug payload',
'RBITC content',
'RBITC release',
'scanning transmission electron microscopy',
'signal contrast enhancement',
'stability',
'Thermogravimetric Analysis of quantity of Gd2O3 in Gd2O3@SWNHag',
'X-ray diffraction'
)
AND other_char_name = 'other';


/* in vitro Characterizations

   Characterization Name  other
   
   Move all assay types to Characterization Name other_vt
   
select * from canano.common_lookup where name = 'other' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and VALUE in (
'actin cytoskeleton disruption',
'apoptosis',
'cholesterol impact on pore formation in plasma membrane',
'cytochrome c release',
'LDH release',
'Tubule Formation/Angiogenesis',
'VE-cadherin'
);

select * from canano.characterization where assay_type IN (
'acoustic microscopy',
'actin cytoskeleton disruption',
'apoptosis',
'cholesterol impact on pore formation in plasma membrane',
'cytochrome c release',
'LDH release',
'stability',
'Tubule Formation/Angiogenesis',
'VE-cadherin'
)
AND other_char_name = 'other';

*/

update canano.common_lookup
set name = 'other_vt'
WHERE name = 'other'
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and value IN (
'actin cytoskeleton disruption',
'apoptosis',
'cholesterol impact on pore formation in plasma membrane',
'cytochrome c release',
'LDH release',
'Tubule Formation/Angiogenesis',
'VE-cadherin'
);

UPDATE canano.characterization
set other_char_name = 'other_vt'
WHERE assay_type IN (
'acoustic microscopy',
'actin cytoskeleton disruption',
'apoptosis',
'cholesterol impact on pore formation in plasma membrane',
'cytochrome c release',
'LDH release',
'stability',
'Tubule Formation/Angiogenesis',
'VE-cadherin'
)
AND other_char_name = 'other';

/* remove SERS detection sensivity Assay Type (other_vt) 

select * from canano.common_lookup
where name = 'other_vt' and value = 'SERS detection sensivity';   */

delete from canano.common_lookup
where name = 'other_vt' and value = 'SERS detection sensivity';

UPDATE canano.characterization
set assay_type = 'SERS detection sensitivity'
where assay_type = 'SERS detection sensivity';


/* Replace in vivo imaging by imaging.

Remove in vivo multimodality imaging, in vivo multimodality imaging sensitivity, in vivo multimodality kinetics.

select * from canano.common_lookup where name = 'imaging' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and value = 'in vivo imaging';

select * from canano.common_lookup where name = 'imaging' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and value IN 
( 'in vivo multimodality imaging',
'in vivo multimodality imaging sensitivity',
'in vivo multimodality kinetics')


select assay_type, discriminator, other_char_assay_category, other_char_name from canano.characterization 
where assay_type = 'in vivo imaging'
and discriminator = 'OtherCharacterization'
and other_char_name = 'imaging'

*/

update canano.common_lookup
set value = 'imaging'
WHERE name = 'imaging'
and value = 'in vivo imaging';

update canano.characterization
set assay_type = 'imaging'
where assay_type = 'in vivo imaging'
and discriminator = 'OtherCharacterization'
and other_char_name = 'imaging';

delete from canano.common_lookup where name = 'imaging' 
and ( attribute = 'assayType' OR attribute='otherAssayType' )
and value IN 
( 'in vivo multimodality imaging',
'in vivo multimodality imaging sensitivity',
'in vivo multimodality kinetics');

