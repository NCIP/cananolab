use cananolab;

--only available after 5.0.32
--drop trigger if exists set_history_nanoparticle_char;
--drop trigger if exists set_history_derived_biodata;
--drop trigger if exists set_history_datum;
--drop trigger if exists set_history_characterization;
--drop trigger if exists set_history_lab_file;
--drop trigger if exists set_hist_composing_element;
--drop trigger if exists set_history_surface;
--drop trigger if exists set_history_surface_chemistry;
--drop trigger if exists set_history_surface_group;
--drop trigger if exists set_history_solubility;
--drop trigger if exists set_history_shape;
--drop trigger if exists set_history_morphology;
--drop trigger if exists set_history_nanotube;
--drop trigger if exists set_history_polymer;
--drop trigger if exists set_history_fullerene;
--drop trigger if exists set_history_emulsion;
--drop trigger if exists set_history_dendrimer;
--drop trigger if exists set_history_cytotoxicity;

delimiter $
create trigger set_history_nanoparticle_char
before delete
    on nanoparticle_char
    for each row
begin
   -- insert record into audit table
    insert into history_nanoparticle_char
     ( characterization_pk_id, nanoparticle_pk_id, deleted_date )
    values
     ( old.characterization_pk_id, old.nanoparticle_pk_id, sysdate());

end$

create trigger set_history_derived_biodata
before update
    on derived_bioassay_data
    for each row
begin
   if new.characterization_pk_id is null then
       insert into history_derived_bioassay_data 
        ( derived_bioassay_data_pk_id, 
          characterization_pk_id, 
          list_index, 
          deleted_date )
       values 
        (old.derived_bioassay_data_pk_id, 
         old.characterization_pk_id, 
         old.list_index,
         sysdate());
   else 
       delete from history_derived_bioassay_data where derived_bioassay_data_pk_id=old.derived_bioassay_data_pk_id;
   end if;
end$


create trigger set_history_datum
before update
    on datum
    for each row
begin
   if new.derived_bioassay_data_pk_id is null then
       insert into history_datum 
        ( datum_pk_id, 
          name, 
          value, 
          value_unit, 
          derived_bioassay_data_pk_id, 
          control_name, 
          control_type, 
          list_index, 
          statistics_type, 
          bioassay_data_category, 
          deleted_date )
       values
        (old.datum_pk_id, 
         old.name, 
         old.value, 
         old.value_unit, 
         old.derived_bioassay_data_pk_id, 
         old.control_name, 
         old.control_type, 
         old.list_index, 
         old.statistics_type, 
         old.bioassay_data_category, 
         sysdate() );
   else 
       delete from history_datum where datum_pk_id=old.datum_pk_id;
   end if;
end$

create trigger set_history_characterization
before delete
    on characterization
    for each row
begin
   -- insert record into audit table
    insert into history_characterization
     ( characterization_pk_id,
       classification,
       source,description,
       identifier_name,
       name,
       discriminator,
       created_date,
       created_by,
       protocol_file_pk_id,
       instrument_config_pk_id, 
       deleted_date,
       table_source)
    values
     ( old.characterization_pk_id, 
       old.classification, 
       old.source, 
       old.description, 
       old.identifier_name, 
       old.name, 
       old.discriminator, 
       old.created_date, 
       old.created_by, 
       old.protocol_file_pk_id, 
       old.instrument_config_pk_id, 
       sysdate(),
       'characterization');

end$

create trigger set_hist_composing_element
before update
    on  composing_element
    for each row
begin
   if new.characterization_pk_id is null then
       insert into history_composing_element
        ( composing_element_pk_id, 
          element_type, 
          chemical_name, 
          description, 
          characterization_pk_id, 
          list_index, 
          deleted_date )
       values
        (old.composing_element_pk_id, 
         old.element_type, 
         old.chemical_name, 
         old.description, 
         old.characterization_pk_id, 
         old.list_index, 
         sysdate() );
   else 
      delete from history_composing_element where composing_element_pk_id=old.composing_element_pk_id;
   end if;
end$

create trigger set_history_surface_chemistry
before update
    on  surface_chemistry
    for each row
begin
   if new.surface_pk_id is null then
       insert into history_surface_chemistry
        ( surface_chemistry_pk_id, 
          molecule_name, 
          surface_pk_id, 
          number_molecule, 
          list_index, 
		  molecular_formula_type,
          deleted_date )
       values
        (old.surface_chemistry_pk_id, 
         old.molecule_name, 
         old.surface_pk_id, 
         old.number_molecule, 
         old.list_index, 
		 old.molecular_formula_type,
         sysdate() );
   else
      delete from history_surface_chemistry where surface_chemistry_pk_id=old.surface_chemistry_pk_id;
   end if;      
end$

create trigger set_history_surface_group
before update
    on  surface_group
    for each row
begin
   if new.d_composition_pk_id is null then
       insert into history_surface_group
        ( surface_group_pk_id, 
          name, 
          modifier,
          d_composition_pk_id, 
          list_index, 
          deleted_date )
       values
        (old.surface_group_pk_id, 
         old.name, 
         old.modifier, 
         old.d_composition_pk_id, 
         old.list_index, 
         sysdate() );
   else 
      delete from history_surface_group where surface_group_pk_id=old.surface_group_pk_id;
   end if;
end$

create trigger set_history_lab_file
before delete
    on lab_file
    for each row
begin
   -- insert record into audit table
    insert into history_lab_file
     ( file_pk_id,
       file_name,
       file_uri,
       file_type_extension,
       file_source_type,
       version,
       status,
       reason,
       created_by,
       created_date,
       sample_sop_pk_id,
       run_pk_id,
       data_status_pk_id,
       title,
       description,
       comments,
       type,
       deleted_date )   
    values
     ( old.file_pk_id,
       old.file_name,
       old.file_uri,
       old.file_type_extension,
       old.file_source_type,
       old.version,
       old.status,
       old.reason,
       old.created_by,
       old.created_date,
       old.sample_sop_pk_id,
       old.run_pk_id,
       old.data_status_pk_id,
       old.title,
       old.description,
       old.comments,
       old.type,
       sysdate());
end$

create trigger set_history_nanotube
before delete
    on carbon_nanotube_composition
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id, 
       chirality, 
       growth_diameter, 
       average_length, 
       wall_type, 
       deleted_date, 
       table_source)
    values
     ( old.cn_composition_pk_id, 
       old.chirality, 
       old.growth_diameter, 
       old.average_length, 
       old.wall_type, 
       sysdate(), 
       'CARBON_NANOTUBE_COMPOSITION');
      
end$

create trigger set_history_dendrimer
before delete
    on dendrimer_composition
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       generation, 
       molecular_formula, 
       repeat_unit, 
       branch,
       deleted_date, 
       table_source)
    values
     ( old.d_composition_pk_id,
       old.generation,
       old.molecular_formula,
       old.repeat_unit,
       old.branch,
       sysdate(),
       'DENDRIMER_COMPOSITION');
end$

create trigger set_history_emulsion
before delete
    on emulsion_composition
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       emulsion_type, 
       molecular_formula, 
       polymer_name, 
       is_polymerized,
       deleted_date, 
       table_source)
    values
     ( old.e_composition_pk_id,
       old.emulsion_type,
       old.molecular_formula,
       old.polymer_name,
       old.is_polymerized,
       sysdate(),
       'EMULSION_COMPOSITION');
end$

create trigger set_history_cytotoxicity
before delete
    on cytotoxicity
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       cell_line, 
       cell_death_method, 
       deleted_date, 
       table_source)
    values
     ( old.cytotoxicity_pk_id,
       old.cell_line,
       old.cell_death_method,
       sysdate(),
       'CYTOTOXICITY');
end$

create trigger set_history_fullerene
before delete
    on fullerene_composition
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       number_of_carbon,
       deleted_date, 
       table_source)
    values
     ( old.f_composition_pk_id,
       old.number_of_carbon,
       sysdate(),
       'FULLERENE_COMPOSITION');
end$

create trigger set_history_morphology
before delete
    on morphology
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       type,
       deleted_date, 
       table_source)
    values
     ( old.morphology_pk_id,
       old.type,
       sysdate(),
       'MORPHOLOGY');
end$

create trigger set_history_polymer
before delete
    on polymer_composition
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       is_cross_link,
       cross_link_degree,
       initiator,
       deleted_date, 
       table_source)
    values
     ( old.p_composition_pk_id,
       old.is_cross_link,
       old.cross_link_degree,
       old.initiator,
       sysdate(),
       'POLYMER_COMPOSITION');
end$

create trigger set_history_shape
before delete
    on shape
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       max_dimension,
       min_dimension,
       max_dimension_unit,
       min_dimension_unit,
       type,
       deleted_date, 
       table_source)
    values
     ( old.shape_pk_id,
       old.max_dimension,
       old.min_dimension,
       old.max_dimension_unit,
       old.min_dimension_unit,
       old.type,
       sysdate(),
       'SHAPE');
end$

create trigger set_history_solubility
before delete
    on solubility
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       solvent,
       critical_concentration,
       concentration_unit,
       is_soluble,
       deleted_date, 
       table_source)
    values
     ( old.solubility_pk_id,
       old.solvent,
       old.critical_concentration,
       old.concentration_unit,
       old.is_soluble,
       sysdate(),
       'SOLUBILITY');
end$

create trigger set_history_surface
before delete
    on surface
    for each row
begin     
    insert into history_characterization
     ( characterization_pk_id,
       surface_area,
       surface_area_unit,
       zeta_potential,
	   zeta_potential_unit,
       charge,
       charge_unit,
       is_hydrophobic,
       deleted_date, 
       table_source)
    values
     ( old.surface_pk_id,
       old.surface_area,
       old.surface_area_unit,
       old.zeta_potential,
	   old.zeta_potential_unit,
       old.charge,
       old.charge_unit,
       old.is_hydrophobic,
       sysdate(),
       'SURFACE');
end$

delimiter ;
