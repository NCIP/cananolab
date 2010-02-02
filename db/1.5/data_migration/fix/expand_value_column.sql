use canano;

-- Expand datum.value from decimal(22,3) to decimal(30,10).
alter table canano.datum
change value value decimal(30,10) not null;

-- Expand associated_element.value from decimal(22,3) to decimal(30,10).
alter table canano.associated_element
change value value decimal(30,10);
