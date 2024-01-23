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
    public ManagementDto findById(Long id) throws Exception {
        Management management = managementRepository.findById(id)
                .orElseThrow(() -> new Exception("Management with id " + id + " does not exist!"));
        return managementConverter.toDto(management);

    }

    @Override
    public List<ManagementDto> getAll() {
        return managementRepository
                .findAll()
                .stream().map(entity -> managementConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public List<ManagementDto> findByDepartment(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(() -> new Exception("Department does not exist!" ));

        List<Management> list = managementRepository.findByDepartmentId(id);
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members of management.");
        }
        return print;
    }

    @Override
    public List<ManagementDto> findCurrentDepartment(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(() -> new Exception("Department with name does not exist!" ));

        List<Management> list = managementRepository.findByEndDateIsNullAndDepartmentId(id);
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active members of management of this department.");
        }
        return print;
    }


    @Override
    public List<ManagementDto> currentHandlers() throws Exception {
        List<Management> list = managementRepository.findByEndDateIsNullAndRole("handler");
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no currently active handlers.");
        }
        return print;
    }

    @Override
    public List<ManagementDto> currentSecretary() throws Exception {
        List<Management> list = managementRepository.findByEndDateIsNullAndRole("secretary");
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
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


}
