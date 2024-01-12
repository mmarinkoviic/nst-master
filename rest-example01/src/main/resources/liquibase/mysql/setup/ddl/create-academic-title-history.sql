create table tbl_academic_title_history(
    id bigint unsigned not null AUTO_INCREMENT,
    member_id bigint unsigned,
    start_date date,
    end_date date,
    academic_title_id bigint unsigned,
    scientific_field_id bigint unsigned,

    primary key (id),
            constraint member_fk FOREIGN KEY (member_id) REFERENCES tbl_member(id),
            constraint academic_title_m_fk FOREIGN KEY (academic_title_id) REFERENCES tbl_academic_title(id),
            constraint scf_field_m_fk FOREIGN KEY (scientific_field_id) REFERENCES tbl_scientific_field(id)

)