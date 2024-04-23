package nst.springboot.restexample01.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.domain.Department;
import nst.springboot.restexample01.domain.Management;
import nst.springboot.restexample01.domain.Member;
import nst.springboot.restexample01.repository.DepartmentRepository;
import nst.springboot.restexample01.repository.ManagementRepository;
import nst.springboot.restexample01.repository.MemberRepository;
import nst.springboot.restexample01.service.ManagementService;
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
    public ManagementDto save(Long departmentId,Long memberId, String role, LocalDate startDate) throws Exception {

        if(!role.equals("handler") && !role.equals("secretary")) {throw new IllegalArgumentException("Role must be handler or secretary!");}

        Member member =memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member does not exist!" ));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new EntityNotFoundException("Department does not exist!"));

        if(!startDate.isEqual(LocalDate.now()) && !startDate.isBefore(LocalDate.now())){throw new IllegalArgumentException("Date is not valid!");}

        Long id = 0L;
        if(managementRepository.findMaxId() != null){
            id = managementRepository.findMaxId();
        }
        Management management = new Management(id + 1, department, member, role, startDate,null);

       if(role.equals("handler")){
           Optional<Management> secretary = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId, "secretary");
           if(secretary.isPresent()) {
               if (management.getMember() == secretary.get().getMember()) {
                   throw new EntityExistsException("Member is already secretary of department.");
               }
           }
       }else{
           Optional<Management> handler = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId, "handler");
           if(handler.isPresent()) {
               if (management.getMember() == handler.get().getMember()) {
                   throw new EntityExistsException("Member is already handler of department.");
               }
           }
       }

       Optional<Management> check = managementRepository.findByEndDateIsNullAndDepartmentIdAndRole(departmentId, role);
       if (check.isPresent()) {
           if (management.getMember() == check.get().getMember()) {
               throw new EntityExistsException("Member is already " + role + " of department.");
           }
           Management update = check.get();
           update.setEndDate(startDate);
       }

            Management saved = managementRepository.save(management);
            return managementConverter.toDto(saved);
    }


    @Override
    public ManagementDto findById(Long id) throws Exception {
        Management management = managementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Management with id " + id + " does not exist!"));
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
        departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department does not exist!" ));

        List<ManagementDto> list = managementRepository.findByDepartmentId(id)
                .stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(list.isEmpty()){
            throw new NullPointerException("There are no currently active members of management.");
        }
        return list;
    }

    @Override
    public List<ManagementDto> findCurrentDepartment(Long id) throws Exception {
        departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department with name does not exist!" ));

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
    public List<ManagementDto> getHandlers() throws Exception {
        List<Management> list = managementRepository.findByRole("handler");
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no records about handlers.");
        }
        return print;
    }

    @Override
    public List<ManagementDto> getSecretaries() throws Exception {
        List<Management> list = managementRepository.findByRole("secretary");
        List<ManagementDto> print = list.stream().map(management -> managementConverter.toDto(management)).collect(Collectors.toList());
        if(print.isEmpty()){
            throw new NullPointerException("There are no records about handlers.");
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
            throw new EntityNotFoundException("Management record with " + id + "does not exist!");
        }
    }


}
