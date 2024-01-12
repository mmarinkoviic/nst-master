create table tbl_management(
    id bigint unsigned not null AUTO_INCREMENT,
    department_id bigint unsigned,
    member_id bigint unsigned,
    role varchar(10) not null,
    start_date date,
    end_date date,

    primary key (id),
    constraint department_mn_fk FOREIGN KEY (department_id) REFERENCES tbl_department(id),
    constraint member_mn_fk FOREIGN KEY (member_id) REFERENCES tbl_member(id)
)