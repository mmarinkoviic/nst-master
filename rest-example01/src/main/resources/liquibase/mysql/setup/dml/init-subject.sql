insert into tbl_subject(name,espb,department_id)
values ("Algorithms and data structure", 10, 3);

insert into tbl_subject(name,espb,department_id)
values ("Programming", 10, (select (id) from tbl_department WHERE name="Computer science"));

insert into tbl_subject(name,espb,department_id)
values ("Artificial intelligence", 5, (select (id) from tbl_department WHERE name="Computer science"));

insert into tbl_subject(name,espb,department_id)
values ("Algebra", 5, (select (id) from tbl_department WHERE name="Mathematics"));

insert into tbl_subject(name,espb,department_id)
values ("Geometry", 5, (select (id) from tbl_department WHERE name="Mathematics"));
