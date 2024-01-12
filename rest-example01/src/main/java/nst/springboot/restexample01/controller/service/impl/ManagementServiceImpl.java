package nst.springboot.restexample01.controller.service.impl;

import nst.springboot.restexample01.controller.domain.Management;
import nst.springboot.restexample01.controller.repository.DepartmentRepository;
import nst.springboot.restexample01.controller.repository.ManagementRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.service.ManagementService;
import nst.springboot.restexample01.converter.impl.ManagementConverter;
import nst.springboot.restexample01.dto.ManagementDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagementServiceImpl implements ManagementService {
    private ManagementRepository managementRepository;
    private ManagementConverter managementConverter;
    private DepartmentRepository departmentRepository;
    private MemberRepository memberRepository;

    public ManagementServiceImpl(ManagementRepository managementRepository, ManagementConverter managementConverter,
                                 DepartmentRepository departmentRepository, MemberRepository memberRepository) {
        this.managementRepository = managementRepository;
        this.managementConverter = managementConverter;
        this.departmentRepository = departmentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public ManagementDto save(String departmentName, String firstName, String lastName, String role, LocalDate startDate) throws Exception {
       if(!role.equals("handler") && !role.equals("secretary")) {throw new Exception("Role must be handler or secretary!");}
       memberRepository.findByFirstNameAndLastName(firstName,lastName).orElseThrow(() -> new Exception("Member " + firstName + " " + lastName + " does not exist!" ));
       departmentRepository.findByName(departmentName).orElseThrow(()->new Exception("Department " + departmentName + " does not exist!"));
       if(!startDate.isEqual(LocalDate.now()) && !startDate.isAfter(LocalDate.now())){throw new Exception("Date is not valid!");}

        Management management = new Management(managementRepository.count() + 1, departmentRepository.findByName(departmentName).get(),
               memberRepository.findByFirstNameAndLastName(firstName, lastName).get(), role, startDate,null);

       if(role.equals("handler")){
           Optional<Management> secretary = managementRepository.findByEndDateIsNullAndDepartmentNameAndRole(departmentName, "secretary");
           if(secretary.isPresent()) {
               if (management.getMember() == secretary.get().getMember()) {
                   throw new Exception("Member " + firstName + " " + lastName + " is already secretary of department " + departmentName + ".");
               }
           }
       }else{
           Optional<Management> handler = managementRepository.findByEndDateIsNullAndDepartmentNameAndRole(departmentName, "handler");
           if(handler.isPresent()) {
               if (management.getMember() == handler.get().getMember()) {
                   throw new Exception("Member " + firstName + " " + lastName + " is already handler of department " + departmentName + ".");
               }
           }
       }

       Optional<Management> check = managementRepository.findByEndDateIsNullAndDepartmentNameAndRole(departmentName, role);
       if (check.isPresent()) {
           if (management.getMember() == check.get().getMember()) {
               throw new Exception("Member " + firstName + " " + lastName + " is already " + role + " of department " + departmentName + ".");
           }
           Management update = check.get();
           update.setEndDate(startDate);
       }

            Management saved = managementRepository.save(management);
            return managementConverter.toDto(saved);
    }

    @Override
    public void update(String department, String role, LocalDate date) throws Exception{
        Optional<Management> check = managementRepository.findByEndDateIsNullAndDepartmentNameAndRole(department,role);
        if(check.isEmpty()){
            throw new Exception("There are no record about this department.");
        }
        Management management = check.get();
        management.setEndDate(date);
        managementRepository.save(management);
    }

    @Override
    public String findById(Long id) throws Exception {
        Management management = managementRepository.findById(id)
                .orElseThrow(() -> new Exception("Management with id " + id + " does not exist!"));
        return printing(managementConverter.toDto(management));

    }

    @Override
    public String findMemberByRoleAndDepartment(String departmentName, String role) throws Exception {
        departmentRepository.findByName(departmentName).orElseThrow(() -> new Exception("Department with name " + departmentName + " does not exist!" ));
        if(!role.equals("handler") && !role.equals("secretary")) {throw new Exception("Role must be handler or secretary!");}

        Management management = managementRepository.findByEndDateIsNullAndDepartmentNameAndRole(departmentName,role).
                orElseThrow(()-> new Exception("Department " + departmentName + " does not have current " + role + "."));
        return printing(managementConverter.toDto(management));
    }

    @Override
    public List<String> getAll() {
        return managementRepository
                .findAll()
                .stream().map(entity -> printing(managementConverter.toDto(entity))).sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findByDepartment(String nameDepartment) throws Exception {
        departmentRepository.findByName(nameDepartment).orElseThrow(() -> new Exception("Department with name " + nameDepartment + " does not exist!" ));

        List<Management> list = managementRepository.findByDepartmentName(nameDepartment);
        List<String> print = list.stream().map(management -> printing(managementConverter.toDto(management))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members of management.");
        }
        return print;
    }

    @Override
    public List<String> findCurrentDepartment(String nameDepartment) throws Exception {
        departmentRepository.findByName(nameDepartment).orElseThrow(() -> new Exception("Department with name " + nameDepartment + " does not exist!" ));

        List<Management> list = managementRepository.findByEndDateIsNullAndDepartmentName(nameDepartment);
        List<String> print = list.stream().map(management -> printing(managementConverter.toDto(management))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members of management of department " + nameDepartment + ".");
        }
        return print;
    }

    @Override
    public List<String> currentManagement() throws Exception {
        List<Management> list = managementRepository.findByEndDateIsNull();
        List<String> print = list.stream().map(management -> printing(managementConverter.toDto(management))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members of management.");
        }
        return print;
    }

    @Override
    public List<String> currentHandlers() throws Exception {
        List<Management> list = managementRepository.findByEndDateIsNullAndRole("handler");
        List<String> print = list.stream().map(management -> printing(managementConverter.toDto(management))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active handlers.");
        }
        return print;
    }

    @Override
    public List<String> currentSecretary() throws Exception {
        List<Management> list = managementRepository.findByEndDateIsNullAndRole("secretary");
        List<String> print = list.stream().map(management -> printing(managementConverter.toDto(management))).sorted().collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no current active secretaries.");
        }
        return print;
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Management> management = managementRepository.findById(id);
        if(management.isPresent()){
            Management mngmt = management.get();
            if(mngmt.getEndDate() == null){throw new Exception("It can not be deleted! This record is about active statement!");}

            managementRepository.delete(mngmt);
        }else {
            throw new Exception("Management record with " + id + "does not exist!");
        }
    }

    @Override
    public String printing(ManagementDto managementDto) {
        if(managementDto.getEndDate() == null) {
            return "Current " + managementDto.getRole() + " of department " + managementDto.getDepartmentDto() +
                    " is " + managementDto.getMemberDto().toString() + " from " + managementDto.getStartDate() + ".";
        }else{
            return "Former " + managementDto.getRole() + " of department " + managementDto.getDepartmentDto() +
                    " was " + managementDto.getMemberDto().toString() + " from " + managementDto.getStartDate() + " to " + managementDto.getEndDate() + ".";
        }
    }
}
