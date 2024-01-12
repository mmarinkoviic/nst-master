create table tbl_member(
	id bigint unsigned not null AUTO_INCREMENT,
	first_name varchar(20) not null,
    last_name varchar(40) not null,
    academic_title_id bigint unsigned,
    education_title_id bigint unsigned,
    scf_field_id bigint unsigned,
    department_id bigint unsigned,

	primary key (id),
        constraint academic_title_fk FOREIGN KEY (academic_title_id) REFERENCES tbl_academic_title(id),
        constraint education_title_fk FOREIGN KEY (education_title_id) REFERENCES tbl_education_title(id),
        constraint scf_field_fk FOREIGN KEY (scf_field_id) REFERENCES tbl_scientific_field(id),
        constraint department_m_fk FOREIGN KEY (department_id) REFERENCES tbl_department(id)
)