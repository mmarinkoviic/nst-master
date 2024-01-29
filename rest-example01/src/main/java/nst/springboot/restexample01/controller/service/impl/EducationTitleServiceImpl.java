package nst.springboot.restexample01.controller.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import nst.springboot.restexample01.controller.domain.EducationTitle;
import nst.springboot.restexample01.controller.domain.Member;
import nst.springboot.restexample01.controller.repository.EducationTitleRepository;
import nst.springboot.restexample01.controller.repository.MemberRepository;
import nst.springboot.restexample01.controller.service.EducationTitleService;
import nst.springboot.restexample01.converter.impl.EducationTitleConverter;
import nst.springboot.restexample01.dto.EducationTitleDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EducationTitleServiceImpl implements EducationTitleService {
    private EducationTitleRepository educationTitleRepository;
    private EducationTitleConverter educationTitleConverter;
    private MemberRepository memberRepository;

    public EducationTitleServiceImpl(EducationTitleRepository educationTitleRepository,
                                     EducationTitleConverter educationTitleConverter,
                                     MemberRepository memberRepository) {
        this.educationTitleRepository = educationTitleRepository;
        this.educationTitleConverter = educationTitleConverter;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public EducationTitleDto save(String name) throws Exception {

        Optional<EducationTitle> check = educationTitleRepository.findByTitleIgnoreCase(name);
        if(check.isPresent()){
            throw new EntityExistsException("Education title " + name + " already exist!");
        }
        EducationTitle educationTitle = new EducationTitle(educationTitleRepository.findMaxId()+1,name);
        educationTitle = educationTitleRepository.save(educationTitle);
        return educationTitleConverter.toDto(educationTitle);
    }

    @Override
    public List<EducationTitleDto> getAll() {
        return educationTitleRepository
                .findAll().stream().map(educationTitle -> educationTitleConverter.toDto(educationTitle))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        EducationTitle educationTitle = educationTitleRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Education title does not exist!"));
        List<Member> memberList = memberRepository.findByEducationTitleId(id);
        if(memberList.isEmpty()) {
            educationTitleRepository.delete(educationTitle);
        }else{
            throw new Exception("Education title can not be deleted, because there are members with that education title!");
        }
    }

    @Override
    public EducationTitleDto findById(Long id) throws Exception {
        EducationTitle educationTitle = educationTitleRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Education title does not exist!"));
        return educationTitleConverter.toDto(educationTitle);
    }

    @Override
    public void update(Long id, String newName) throws Exception {
        if(educationTitleRepository.findByTitleIgnoreCase(newName).isPresent()){throw new EntityExistsException("Education title " + newName + " already exist!");}
        EducationTitle educationTitle = educationTitleRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Education title with id " + id + " does not exist!"));
        educationTitle.setTitle(newName);
        educationTitleRepository.save(educationTitle);
    }
}
